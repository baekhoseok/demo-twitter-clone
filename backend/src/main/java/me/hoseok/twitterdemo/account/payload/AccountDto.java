package me.hoseok.twitterdemo.account.payload;

import lombok.*;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.account.AccountRole;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long id;
    private String nickname;
    private String email;

    public AccountDto(Account account) {
        this.id = account.getId();
        this.nickname = account.getUsername();
        this.email = account.getEmail();
    }
}
