package me.hoseok.twitterdemo.comment.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.payload.AccountViewDto;

@Data
public class CommentViewDto {

    private Long id;
    @JsonIgnore
    private Long postId;
    private String content;
    private AccountViewDto account;

    @QueryProjection
    public CommentViewDto(Long id, Long postId, String content, Account account) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.account = new AccountViewDto(account.getId(), account.getUsername());
    }
}
