package me.hoseok.twitterdemo.post.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.account.payload.AccountSimpleDto;
import me.hoseok.twitterdemo.comment.payload.CommentFullDto;
import me.hoseok.twitterdemo.image.payload.ImageSimpleDto;
import me.hoseok.twitterdemo.like.payload.LikeSimpleDto;
import me.hoseok.twitterdemo.post.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PostRetweetDto {
    private Long id;
    private String content;
    private String location;
    private AccountSimpleDto account;
    private List<CommentFullDto> comments = new ArrayList<>();
    private List<ImageSimpleDto> images = new ArrayList<>();
    private List<LikeSimpleDto> likes = new ArrayList<>();
    private PostSimpleRetweetDto retweet;
    public PostRetweetDto(Post post) {
        this.id = post.getId();
        this.retweet = new PostSimpleRetweetDto(post.getRetweet());
        this.id = post.getId();
        this.content = post.getContent();
        this.location = post.getLocation();
        this.account = new AccountSimpleDto(post.getAccount().getId(), post.getAccount().getUsername());
        this.comments = post.getComments().stream()
                .map(c -> new CommentFullDto(c.getId(), null, c.getContent(),
                        new AccountSimpleDto(c.getAccount().getId(), c.getAccount().getUsername())
                )).collect(Collectors.toList());

        this.likes = post.getLikes().stream()
                .map(l -> new LikeSimpleDto( null, l.getAccount().getId()))
                .collect(Collectors.toList());
    }
}
