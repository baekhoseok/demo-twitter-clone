package me.hoseok.twitterdemo.account.payload;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.account.Account;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class AccountFullDto {
    private Long id;
    private String username;
    private String email;
    private Integer followingsCount;
    private Integer followersCount;
    private Integer postsCount;
    private String token;

    @QueryProjection
    public AccountFullDto(Long id, String username, String email, Integer followingsCount, Integer followersCount, Integer postsCount) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.followingsCount = followingsCount;
        this.followersCount = followersCount;
        this.postsCount = postsCount;
    }
}
