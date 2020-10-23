package me.hoseok.twitterdemo.comment.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.payload.AccountSimpleDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentFullDto {
    private Long id;
    private Long postId;
    private String content;
    private AccountSimpleDto account;

    @QueryProjection
    public CommentFullDto(Long id, Long postId, String content, Account account) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.account = new AccountSimpleDto(account.getId(), account.getUsername());
    }
}
