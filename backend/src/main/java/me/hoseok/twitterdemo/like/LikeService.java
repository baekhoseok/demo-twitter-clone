package me.hoseok.twitterdemo.like;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.AccountRepository;
import me.hoseok.twitterdemo.account.payload.Me;
import me.hoseok.twitterdemo.exception.PostNotFoundException;
import me.hoseok.twitterdemo.post.Post;
import me.hoseok.twitterdemo.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;


    public Like likePost(Long postId, Me me) {
        Account account = accountRepository.findById(me.getId()).get();
        Optional<Post> byId = postRepository.findById( postId );
        Post post = byId.orElseThrow(() -> new PostNotFoundException( "Post not found" ) );
        Like like = new Like(post, account);
        post.getLikes().add(like);
        return like;
    }

    public Like unLikePost(Long postId, Me me) {
        Optional<Post> byId = postRepository.findById( postId );
        Post post = byId.orElseThrow(() -> new PostNotFoundException( "Post not found" ) );
        Like like = likeRepository.findByPostIdAndAccountId(post.getId(), me.getId());
        post.getLikes().remove(like);
        return like;
    }
}
