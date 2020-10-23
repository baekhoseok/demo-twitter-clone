package me.hoseok.twitterdemo.post.payload;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.payload.AccountSimpleDto;
import me.hoseok.twitterdemo.account.payload.AccountViewDto;
import me.hoseok.twitterdemo.comment.Comment;
import me.hoseok.twitterdemo.comment.payload.CommentFullDto;
import me.hoseok.twitterdemo.comment.payload.CommentViewDto;
import me.hoseok.twitterdemo.image.Image;
import me.hoseok.twitterdemo.image.payload.ImageSimpleDto;
import me.hoseok.twitterdemo.image.payload.ImageViewDto;
import me.hoseok.twitterdemo.like.payload.LikeDto;
import me.hoseok.twitterdemo.like.payload.LikeSimpleDto;
import me.hoseok.twitterdemo.post.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostFullDto {
    private Long id;
    private String content;
    private String location;
    private AccountSimpleDto account;
    private List<CommentFullDto> comments = new ArrayList<>();
    private List<ImageSimpleDto> images = new ArrayList<>();
    private List<LikeSimpleDto> likes = new ArrayList<>();

    @QueryProjection
    public PostFullDto(Long id, String content, Account account) {
        this.id = id;
        this.content = content;
        this.account = new AccountSimpleDto(account.getId(), account.getUsername());
    }

    public PostFullDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.location = post.getLocation();
        this.account = new AccountSimpleDto(post.getAccount().getId(), post.getAccount().getUsername());
        this.comments = post.getComments().stream()
                        .map(c -> new CommentFullDto(c.getId(), null, c.getContent(),
                                new AccountSimpleDto(c.getAccount().getId(), c.getAccount().getUsername())
                                )).collect(Collectors.toList());
        this.images = post.getImages().stream()
                        .map(i -> new ImageSimpleDto(i.getId(),null, i.getUrl()))
                        .collect(Collectors.toList());
        this.likes = post.getLikes().stream()
                        .map(l -> new LikeSimpleDto( null, l.getAccount().getId()))
                        .collect(Collectors.toList());
    }
}
