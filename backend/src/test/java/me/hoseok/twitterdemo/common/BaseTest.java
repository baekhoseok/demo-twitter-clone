package me.hoseok.twitterdemo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.AccountRepository;
import me.hoseok.twitterdemo.account.AccountService;
import me.hoseok.twitterdemo.account.payload.AccountSignupReq;
import me.hoseok.twitterdemo.comment.Comment;
import me.hoseok.twitterdemo.comment.CommentRepository;
import me.hoseok.twitterdemo.comment.payload.CommentDto;
import me.hoseok.twitterdemo.follow.Follow;
import me.hoseok.twitterdemo.follow.FollowRepository;
import me.hoseok.twitterdemo.image.ImageRepository;
import me.hoseok.twitterdemo.like.Like;
import me.hoseok.twitterdemo.like.LikeRepository;
import me.hoseok.twitterdemo.post.Post;
import me.hoseok.twitterdemo.post.PostRepository;
import me.hoseok.twitterdemo.post.PostService;
import me.hoseok.twitterdemo.post.payload.PostReq;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@Transactional
public class BaseTest {
    @Autowired protected MockMvc mockMvc;
    @Autowired protected AccountRepository accountRepository;
    @Autowired protected PostRepository postRepository;
    @Autowired protected CommentRepository commentRepository;
    @Autowired protected ImageRepository imageRepository;
    @Autowired protected PostService postService;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected ModelMapper modelMapper;
    @Autowired protected EntityManager em;
    @Autowired protected AccountService accountService;
    @Autowired protected LikeRepository likeRepository;
    @Autowired protected FollowRepository followRepository;

    public PostReq createPostDto(String content, String location) {

        return new PostReq( content, location, List.of(
            "https://image.kpopmap.com/2019/08/ITZY_Yuna_ITz_Icy_teaser_image_11.png",
                "https://i.pinimg.com/originals/88/a0/d4/88a0d45d471c0977037f1049bd4a4536.jpg",
                "https://pbs.twimg.com/media/DzSuG1cV4AASBjk?format=jpg&name=large"
        ) );
    }

    public Post createPostInDb() {
        Account account = accountRepository.findByUsername( "user" );
        PostReq postReq = createPostDto( "hahaha", "here" );
        Post post = modelMapper.map(postReq, Post.class );
        post.setAccount( account );
        postRepository.save( post );
        return post;
    }

    @Rollback(value = false)
    public Post createPostInDbByAnotherAccount(String username) {
        Account account = createAccountInDb(username);
        PostReq postReq = createPostDto( "hahaha", "here" );
        Post post = modelMapper.map(postReq, Post.class );
        post.setAccount( account );
        postRepository.save( post );
        return post;
    }

    public Comment createCommentInDb(String username) {
        Account account = null;
        if(username.equals("user")) {
            account = accountRepository.findByUsername( "user" );
        } else {
            account = createAccountInDb( username );
        }
        Post post = createPostInDb();
        CommentDto commentDto = new CommentDto( "hello" );
        Comment comment = modelMapper.map( commentDto, Comment.class );
        comment.setPost( post );
        comment.setAccount( account );
        return commentRepository.save( comment );

    }

    public Account createAccountInDb(String username) {
        AccountSignupReq accountDto = new AccountSignupReq();
        accountDto.setUsername(username);
        accountDto.setEmail(username + "@naver.com");
        accountDto.setPassword("12345678");
        return accountService.processNewAccount(accountDto);
    }

    public Account follow(Account target) {
        Account account = accountRepository.findByUsername( "user" );

//        account.getFollowings().add(new Follow(account, target));
//        target.getFollowers().add(account);
        followRepository.save(new Follow(account, target));
        return account;
    }

    public Like likePost() {
        Post post = createPostInDb();
        Account account = accountRepository.findByUsername("user");
        Like like = likeRepository.save(new Like(post, account));
        post.getLikes().add(like);
        return like;
    }
}
