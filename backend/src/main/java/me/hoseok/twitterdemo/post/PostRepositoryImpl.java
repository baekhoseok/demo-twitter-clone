package me.hoseok.twitterdemo.post;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.hoseok.twitterdemo.comment.payload.CommentFullDto;
import me.hoseok.twitterdemo.comment.payload.CommentViewDto;
import me.hoseok.twitterdemo.comment.payload.QCommentFullDto;
import me.hoseok.twitterdemo.comment.payload.QCommentViewDto;
import me.hoseok.twitterdemo.hashtag.QHashTag;
import me.hoseok.twitterdemo.hashtag.QPostHashTag;
import me.hoseok.twitterdemo.image.payload.ImageSimpleDto;
import me.hoseok.twitterdemo.image.payload.ImageViewDto;
import me.hoseok.twitterdemo.image.payload.QImageSimpleDto;
import me.hoseok.twitterdemo.image.payload.QImageViewDto;
import me.hoseok.twitterdemo.like.QLike;
import me.hoseok.twitterdemo.like.payload.LikeSimpleDto;
import me.hoseok.twitterdemo.like.payload.QLikeSimpleDto;
import me.hoseok.twitterdemo.post.payload.PostFullDto;
import me.hoseok.twitterdemo.post.payload.PostViewDto;
import me.hoseok.twitterdemo.post.payload.QPostFullDto;
import me.hoseok.twitterdemo.post.payload.QPostViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.hoseok.twitterdemo.account.QAccount.*;
import static me.hoseok.twitterdemo.comment.QComment.*;
import static me.hoseok.twitterdemo.hashtag.QHashTag.*;
import static me.hoseok.twitterdemo.hashtag.QPostHashTag.*;
import static me.hoseok.twitterdemo.image.QImage.*;
import static me.hoseok.twitterdemo.like.QLike.*;
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
                .select( new QImageViewDto( image.id, post.id, image.src ) )
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

    @Override
    public Page<PostFullDto> findFullPosts(Pageable pageable) {
        Page<PostFullDto> postFullDtos =  findPostFullDtos(pageable);

        List<Long> postIds = getFullPostIds(postFullDtos.getContent());

        Map<Long, List<CommentFullDto>> commentFullDtoMap = findCommentFullDtos( postIds );

        Map<Long, List<ImageSimpleDto>> imageSimpleDtoMap = findImageSimpleDtos( postIds );

        Map<Long, List<LikeSimpleDto>> likeSimpleDtoMap = findLikeSimpleDtos( postIds );

        postFullDtos.forEach( p -> {
            List<CommentFullDto> fullDtos = commentFullDtoMap.get(p.getId());
            if(fullDtos != null) {
                p.setComments(fullDtos);
            }
        });
        postFullDtos.forEach( p -> {
            List<ImageSimpleDto> simpleDtos = imageSimpleDtoMap.get(p.getId());
            if (simpleDtos != null) {
                p.setImages(simpleDtos);
            }
        } );
        postFullDtos.forEach( p -> {
            List<LikeSimpleDto> simpleDtos = likeSimpleDtoMap.get(p.getId());
            if (simpleDtos != null) {
                p.setLikes(simpleDtos);
            }
        } );
        return postFullDtos;
    }

    @Override
    public Page<PostFullDto> findFullPostsByAccount(Long accountId,  String value,Pageable pageable) {
        Page<PostFullDto> postFullDtos =  findPostFullDtosByAccount(accountId, value, pageable);

        List<Long> postIds = getFullPostIds(postFullDtos.getContent());

        Map<Long, List<CommentFullDto>> commentFullDtoMap = findCommentFullDtos( postIds );

        Map<Long, List<ImageSimpleDto>> imageSimpleDtoMap = findImageSimpleDtos( postIds );

        Map<Long, List<LikeSimpleDto>> likeSimpleDtoMap = findLikeSimpleDtos( postIds );

        postFullDtos.forEach( p -> {
            List<CommentFullDto> fullDtos = commentFullDtoMap.get(p.getId());
            if(fullDtos != null) {
                p.setComments(fullDtos);
            }
        });
        postFullDtos.forEach( p -> {
            List<ImageSimpleDto> simpleDtos = imageSimpleDtoMap.get(p.getId());
            if (simpleDtos != null) {
                p.setImages(simpleDtos);
            }
        } );
        postFullDtos.forEach( p -> {
            List<LikeSimpleDto> simpleDtos = likeSimpleDtoMap.get(p.getId());
            if (simpleDtos != null) {
                p.setLikes(simpleDtos);
            }
        } );
        return postFullDtos;
    }

    @Override
    public Page<PostFullDto> findFullPostsByHashtag(String value, Pageable pageable) {
        Page<PostFullDto> postFullDtos =  findPostFullDtosHashtag(value, pageable);

        List<Long> postIds = getFullPostIds(postFullDtos.getContent());

        Map<Long, List<CommentFullDto>> commentFullDtoMap = findCommentFullDtos( postIds );

        Map<Long, List<ImageSimpleDto>> imageSimpleDtoMap = findImageSimpleDtos( postIds );

        Map<Long, List<LikeSimpleDto>> likeSimpleDtoMap = findLikeSimpleDtos( postIds );

        postFullDtos.forEach( p -> {
            List<CommentFullDto> fullDtos = commentFullDtoMap.get(p.getId());
            if(fullDtos != null) {
                p.setComments(fullDtos);
            }
        });
        postFullDtos.forEach( p -> {
            List<ImageSimpleDto> simpleDtos = imageSimpleDtoMap.get(p.getId());
            if (simpleDtos != null) {
                p.setImages(simpleDtos);
            }
        } );
        postFullDtos.forEach( p -> {
            List<LikeSimpleDto> simpleDtos = likeSimpleDtoMap.get(p.getId());
            if (simpleDtos != null) {
                p.setLikes(simpleDtos);
            }
        } );
        return postFullDtos;
    }

    private Page<PostFullDto> findPostFullDtosHashtag(String value, Pageable pageable) {

        JPAQuery<PostFullDto> query = queryFactory
                .select(new QPostFullDto(post.id, post.content, post.createdAt, post.account))
                .from(post)
                .leftJoin(post.postHashTags, postHashTag)
                .where(
                        hashtagEq(value)
                );
        JPQLQuery<PostFullDto> jpqlQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<PostFullDto> fetchResult = jpqlQuery.fetchResults();
        return new PageImpl<>(fetchResult.getResults(), pageable, fetchResult.getTotal());
    }




    private Page<PostFullDto> findPostFullDtosByAccount(Long accountId, String value, Pageable pageable) {
        JPAQuery<PostFullDto> query = queryFactory
                .select(new QPostFullDto(post.id, post.content, post.createdAt, post.account))
                .from(post)
                .where(
                        accountIdEq(accountId),
                        containgsValue(value)
                );
        JPQLQuery<PostFullDto> jpqlQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<PostFullDto> fetchResult = jpqlQuery.fetchResults();
        return new PageImpl<>(fetchResult.getResults(), pageable, fetchResult.getTotal());
    }


    private Page<PostFullDto> findPostFullDtos(Pageable pageable) {
        JPAQuery<PostFullDto> query = queryFactory
                .select(new QPostFullDto(post.id, post.content, post.createdAt, post.account))
                .from(post);
        JPQLQuery<PostFullDto> jpqlQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<PostFullDto> fetchResult = jpqlQuery.fetchResults();
        return new PageImpl<>(fetchResult.getResults(), pageable, fetchResult.getTotal());
    }

    private List<Long> getFullPostIds(List<PostFullDto> posts) {
        return posts.stream().map(p -> p.getId()).collect(Collectors.toList());
    }

    private Map<Long, List<CommentFullDto>> findCommentFullDtos(List<Long> postIds) {
        List<CommentFullDto> commentViewDtos = queryFactory
                .select( new QCommentFullDto( comment.id, post.id, comment.content, comment.account ) )
                .from( comment )
                .where( comment.post.id.in( postIds ) )
                .fetch();

        Map<Long, List<CommentFullDto>> collect = commentViewDtos.stream().collect( Collectors.groupingBy( dto -> dto.getPostId() ) );
        return collect;
    }

    private Map<Long, List<ImageSimpleDto>> findImageSimpleDtos(List<Long> postIds) {
        List<ImageSimpleDto> viewDtos = queryFactory
                .select( new QImageSimpleDto( image.id, post.id, image.src ) )
                .from( image )
                .where( image.post.id.in( postIds ) )
                .fetch();
        Map<Long, List<ImageSimpleDto>> collect = viewDtos.stream().collect( Collectors.groupingBy( dto -> dto.getPostId() ) );
        return collect;
    }

    private Map<Long, List<LikeSimpleDto>> findLikeSimpleDtos(List<Long> postIds) {
        List<LikeSimpleDto> viewDtos = queryFactory
                .select( new QLikeSimpleDto( like.post.id, like.account.id  ) )
                .from(like )
                .where( like.post.id.in( postIds ) )
                .fetch();
        Map<Long, List<LikeSimpleDto>> collect = viewDtos.stream().collect( Collectors.groupingBy( dto -> dto.getPostId() ) );
        return collect;
    }

    private BooleanExpression containgsValue(String value) {
        return value!=null ? post.content.contains(value) : null;
    }

    private BooleanExpression accountIdEq(Long accountId) {
        return accountId != null ? post.account.id.eq(accountId) : null;
    }

    private BooleanExpression hashtagEq(String value) {
        return value != null ? postHashTag.hashTag.name.eq(value) : null;
    }

}
