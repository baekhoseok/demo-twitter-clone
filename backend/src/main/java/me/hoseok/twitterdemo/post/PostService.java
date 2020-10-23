package me.hoseok.twitterdemo.post;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.AccountRepository;
import me.hoseok.twitterdemo.account.payload.Me;
import me.hoseok.twitterdemo.exception.PostNotFoundException;
import me.hoseok.twitterdemo.like.Like;
import me.hoseok.twitterdemo.like.LikeRepository;
import me.hoseok.twitterdemo.post.payload.PostFullDto;
import me.hoseok.twitterdemo.post.payload.PostReq;
import me.hoseok.twitterdemo.post.payload.PostViewDto;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final LikeRepository likeRepository;

    @Transactional(readOnly = true)
    public Page<PostFullDto> search(Pageable pageable) {
        return postRepository.findFullPosts(pageable);
    }

    public Post createPost(PostReq postReq, Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById( accountId );
        accountOptional.orElseThrow( () ->  new UsernameNotFoundException( "User id not found" ));
        Post post = modelMapper.map(postReq, Post.class );
        post.setOwner( accountOptional.get() );
        return postRepository.save( post );
    }

    public Post patchPost(PostReq postReq, Long postId) {
        Optional<Post> byId = postRepository.findById( postId );
        byId.orElseThrow(() -> new PostNotFoundException( "Post not found" ) );

        Post post = byId.get();
        post.setContent( postReq.getContent() );
        post.setLocation( postReq.getLocation() );
        return post;
    }

    public Post deletePost(Long postId) {
        Optional<Post> byId = postRepository.findById( postId );
        byId.orElseThrow(() -> new PostNotFoundException( "Post not found" ) );
        Post post = byId.get();
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
