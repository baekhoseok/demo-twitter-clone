package me.hoseok.twitterdemo.common;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.AccountRepository;
import me.hoseok.twitterdemo.comment.CommentRepository;
import me.hoseok.twitterdemo.image.ImageRepository;
import me.hoseok.twitterdemo.post.Post;
import me.hoseok.twitterdemo.post.PostRepository;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AppStartedEvent implements ApplicationListener<ApplicationStartedEvent> {

//    private final AccountRepository accountRepository;
//    private final PostRepository postRepository;
//    private final CommentRepository commentRepository;
//    private final ImageRepository imageRepository;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        System.out.println("onApplicationEvent");
    }

//    @Transactional
//    public void makeFakeData() {
//        for (int i = 0; i < 10; i++) {
//
//            Account account = new Account("hoseok" + i, "baekhoseok" + i, "hoseok" + i + "@naver.com");
//            Post post = new Post("content" + i, "location" + i);
//
//
//            accountRepository.save(account);
//
//
//        }
//    }
}
