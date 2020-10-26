package me.hoseok.twitterdemo.post;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.AccountRepository;
import me.hoseok.twitterdemo.account.payload.Me;
import me.hoseok.twitterdemo.exception.InvalidArgumentsException;
import me.hoseok.twitterdemo.exception.PostNotFoundException;
import me.hoseok.twitterdemo.hashtag.HashTag;
import me.hoseok.twitterdemo.hashtag.HashTagRepository;
import me.hoseok.twitterdemo.hashtag.PostHashTag;
import me.hoseok.twitterdemo.hashtag.PostHashTagRepository;
import me.hoseok.twitterdemo.image.Image;
import me.hoseok.twitterdemo.image.payload.ImageSimpleDto;
import me.hoseok.twitterdemo.like.Like;
import me.hoseok.twitterdemo.like.LikeRepository;
import me.hoseok.twitterdemo.post.payload.PostFullDto;
import me.hoseok.twitterdemo.post.payload.PostReq;
import me.hoseok.twitterdemo.post.payload.PostViewDto;
import net.bytebuddy.utility.RandomString;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final LikeRepository likeRepository;
    private final PostHashTagRepository postHashTagRepository;
    private final HashTagRepository hashTagRepository;

    public static String hashtagRegex = "(#[^\\s#]+)";
    public static Pattern hashtagPattern = Pattern.compile(hashtagRegex);

    @Transactional(readOnly = true)
    public Page<PostFullDto> search(Pageable pageable) {
        return postRepository.findFullPosts(pageable);
    }

    public Post createPost(PostReq postReq, Long accountId) {
        //get account
        Optional<Account> accountOptional = accountRepository.findById( accountId );
        accountOptional.orElseThrow( () ->  new UsernameNotFoundException( "User id not found" ));

        //create post
        Post post = new Post(postReq.getContent(), postReq.getLocation(), accountOptional.get());

        //create hashtag
        Set<String> tags = new HashSet<>();
        Matcher matcher = hashtagPattern.matcher(postReq.getContent());
        while ( matcher.find() ) {
            String tag = matcher.group().replace(" ", "").replace("#", "");
            tags.add(tag);
        }

        tags.forEach( tag -> {
            HashTag hashTag = new HashTag(tag);
            HashTag findHashTag = hashTagRepository.findByName(tag);
            if(findHashTag == null) {
                findHashTag = hashTagRepository.save(hashTag);
            }
            PostHashTag postHashTag = new PostHashTag(post, findHashTag);
            post.getPostHashTags().add(postHashTag);
        });


        //create images
        if(postReq.getImages() != null && postReq.getImages().size() > 0) {
            List<Image> collect = postReq.getImages().stream()
                    .map(i -> new Image(i, post))
                    .collect(Collectors.toList());
            post.setImages(collect);
        }


        return postRepository.save( post );
    }

    public Post updatePost(PostReq postReq, Long postId, Long accountId) {
        Optional<Post> byId = postRepository.findById( postId );
        byId.orElseThrow(() -> new PostNotFoundException( "Post not found" ) );

        Post post = byId.get();
        if (!post.getAccount().getId().equals(accountId)) {
            throw new InvalidArgumentsException("Can not update this post");
        }

        post.setContent( postReq.getContent() );
        post.setLocation( postReq.getLocation() );
        return post;
    }

    public Post deletePost(Long postId, Long accountId) {
        Optional<Post> byId = postRepository.findById( postId );
        byId.orElseThrow(() -> new PostNotFoundException( "Post not found" ) );
        Post post = byId.get();
        if (!post.getAccount().getId().equals(accountId)) {
            throw new InvalidArgumentsException("Can not delete this post");
        }
        postRepository.delete( post );
        return post;
    }

    @Transactional(readOnly = true)
    public Post getPost(Long postId) {
        return postRepository.findById( postId ).get();
    }

    public void makeTestPosts(Me me) {
        Account account = accountRepository.findByUsername(me.getUsername());
        Post post = null;
        for (int i = 0; i < 30; i++) {
            String value = "["+i+"]"+RandomString.make(30);
            post = new Post(value, "seoul", account);
            postRepository.save(post);
        }
    }
}
