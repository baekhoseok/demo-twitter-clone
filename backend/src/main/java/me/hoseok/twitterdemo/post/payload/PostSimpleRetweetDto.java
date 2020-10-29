package me.hoseok.twitterdemo.post.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.account.payload.AccountSimpleDto;
import me.hoseok.twitterdemo.image.payload.ImageSimpleDto;
import me.hoseok.twitterdemo.post.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PostSimpleRetweetDto {
    private Long id;
    private String content;
    private AccountSimpleDto account;
    private List<ImageSimpleDto> images = new ArrayList<>();

    public PostSimpleRetweetDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.account = new AccountSimpleDto(post.getAccount());
        this.images = post.getImages().stream()
                .map(i -> new ImageSimpleDto(i.getId(),null, i.getSrc()))
                .collect(Collectors.toList());
    }
}
