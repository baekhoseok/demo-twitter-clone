package me.hoseok.twitterdemo.post.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.account.payload.AccountSimpleDto;
import me.hoseok.twitterdemo.post.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSimpleDto {
    private Long id;
    private String content;
    private AccountSimpleDto account;

    public PostSimpleDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.account = new AccountSimpleDto(post.getAccount());
    }
}
