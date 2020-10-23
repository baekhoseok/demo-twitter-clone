package me.hoseok.twitterdemo.account.payload;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.post.payload.PostSimpleDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountMeDto {
    private Long id;
    private String username;
    private String email;
    private List<PostSimpleDto> posts = new ArrayList<>();
    private List<AccountSimpleDto> followings = new ArrayList<>();
    private List<AccountSimpleDto> followers = new ArrayList<>();
    private String token;

    @QueryProjection
    public AccountMeDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.email = account.getEmail();
        this.posts = account.getPosts().stream()
                .map( p -> new PostSimpleDto(p.getId(), p.getContent()))
                .collect(Collectors.toList());
        this.followings = account.getFollowings().stream()
                .map(f -> new AccountSimpleDto(f.getTo().getId(), f.getTo().getUsername()))
                .collect(Collectors.toList());
        this.followers = account.getFollowers().stream()
                .map(f -> new AccountSimpleDto(f.getFrom().getId(), f.getTo().getUsername()))
                .collect(Collectors.toList());
    }
}
