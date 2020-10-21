package me.hoseok.twitterdemo.post;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.AccountRepository;
import me.hoseok.twitterdemo.account.payload.Me;
import me.hoseok.twitterdemo.exception.PostNotFoundException;
import me.hoseok.twitterdemo.like.Like;
import me.hoseok.twitterdemo.like.LikeRepository;
import me.hoseok.twitterdemo.post.payload.PostReq;
import me.hoseok.twitterdemo.post.payload.PostViewDto;
import org.modelmapper.ModelMapper;
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
    public List<PostViewDto> search() {
        return postRepository.findExtensionPosts();
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

    public void deletePost(Long postId) {
        Optional<Post> byId = postRepository.findById( postId );
        byId.orElseThrow(() -> new PostNotFoundException( "Post not found" ) );
        Post post = byId.get();
        postRepository.delete( post );
    }

    @Transactional(readOnly = true)
    public Post getPost(Long postId) {
        return postRepository.findById( postId ).get();
    }

    public void likePost(Long postId, Me me) {
        Account account = accountRepository.findById(me.getId()).get();
        Optional<Post> byId = postRepository.findById( postId );
        Post post = byId.orElseThrow(() -> new PostNotFoundException( "Post not found" ) );
        Like like = new Like(post, account);
        post.getLikes().add(like);
    }

    public void unLikePost(Long postId, Me me) {
        Optional<Post> byId = postRepository.findById( postId );
        Post post = byId.orElseThrow(() -> new PostNotFoundException( "Post not found" ) );
        Like like = likeRepository.findByPostIdAndAccountId(post.getId(), me.getId());
        post.getLikes().remove(like);
    }
}
