package me.hoseok.twitterdemo.post.payload;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.ToString;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.payload.AccountViewDto;
import me.hoseok.twitterdemo.comment.Comment;
import me.hoseok.twitterdemo.comment.payload.CommentViewDto;
import me.hoseok.twitterdemo.image.Image;
import me.hoseok.twitterdemo.image.payload.ImageViewDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
public class PostViewDto {
    private Long id;
    private String content;
    private AccountViewDto account;
    private List<CommentViewDto> comments = new ArrayList<>();
    private List<ImageViewDto> images = new ArrayList<>();

    @QueryProjection
    public PostViewDto(Long id, String content, Account account) {
        this.id = id;
        this.content = content;
        this.account = new AccountViewDto( account.getId(), account.getUsername());

    }

//    @QueryProjection
//    public PostViewDto(Long id, String content, Account account, List<Comment> comments, List<Image> images) {
//        this.id = id;
//        this.content = content;
//        this.account = new AccountViewDto ( account.getId (), account.getUsername () );
//        this.comments = comments.stream().map(c -> {
//            return new CommentViewDto(c.getId(), c.getContent(), c.getAccount().getId(), c.getAccount().getUsername());
//        }).collect(Collectors.toList());
//        this.images = images.stream().map(i-> {
//            return new ImageViewDto(i.getId(), i.getUrl());
//        }).collect(Collectors.toList());
//    }
}
