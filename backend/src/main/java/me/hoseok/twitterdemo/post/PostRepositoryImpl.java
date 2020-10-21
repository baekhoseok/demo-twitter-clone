package me.hoseok.twitterdemo.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.hoseok.twitterdemo.comment.payload.CommentViewDto;
import me.hoseok.twitterdemo.comment.payload.QCommentViewDto;
import me.hoseok.twitterdemo.image.payload.ImageViewDto;
import me.hoseok.twitterdemo.image.payload.QImageViewDto;
import me.hoseok.twitterdemo.post.payload.PostViewDto;
import me.hoseok.twitterdemo.post.payload.QPostViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.hoseok.twitterdemo.account.QAccount.*;
import static me.hoseok.twitterdemo.comment.QComment.*;
import static me.hoseok.twitterdemo.image.QImage.*;
import static me.hoseok.twitterdemo.post.QPost.*;

//@RequiredArgsConstructor
public class PostRepositoryImpl extends QuerydslRepositorySupport implements PostReposirotyCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public PostRepositoryImpl(EntityManager em) {
        super(Post.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PostViewDto> findExtensionPosts() {

        List<PostViewDto> postViewDtos = findPostViewDtos ();

        List<Long> postIds = getPostIds(postViewDtos);

        Map<Long, List<CommentViewDto>> commentViewDtoMap = findCommentViewDtos( postIds );

        Map<Long, List<ImageViewDto>> imageViewDtoMap = findImageViewDtos( postIds );

        postViewDtos.forEach( p -> p.setComments( commentViewDtoMap.get( p.getId() ) ));
        postViewDtos.forEach( p -> p.setImages( imageViewDtoMap.get( p.getId() ) ) );
        return postViewDtos;
    }

    private List<PostViewDto> findPostViewDtos() {
        return queryFactory
                .select ( new QPostViewDto ( post.id, post.content, post.account) )
                .from ( post )
                .fetch ();
    }

    private List<Long> getPostIds(List<PostViewDto> posts) {
        return posts.stream().map(p -> p.getId()).collect(Collectors.toList());
    }

    private Map<Long, List<CommentViewDto>> findCommentViewDtos(List<Long> postIds) {
        List<CommentViewDto> commentViewDtos = queryFactory
                .select( new QCommentViewDto( comment.id, post.id, comment.content, comment.account ) )
                .from( comment )
//                .leftJoin( comment.post, post )
                .where( comment.post.id.in( postIds ) )
                .fetch();
        Map<Long, List<CommentViewDto>> collect = commentViewDtos.stream().collect( Collectors.groupingBy( dto -> dto.getPostId() ) );
        return collect;
    }

    private Map<Long, List<ImageViewDto>> findImageViewDtos(List<Long> postIds) {
        List<ImageViewDto> viewDtos = queryFactory
                .select( new QImageViewDto( image.id, post.id, image.url ) )
                .from( image )
//                .leftJoin( image.post, post )
                .where( image.post.id.in( postIds ) )
                .fetch();
        Map<Long, List<ImageViewDto>> collect = viewDtos.stream().collect( Collectors.groupingBy( dto -> dto.getPostId() ) );
        return collect;
    }

    @Override
    public List<Post> findPosts() {

        return queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.account, account)
                .fetch ();
    }
}
