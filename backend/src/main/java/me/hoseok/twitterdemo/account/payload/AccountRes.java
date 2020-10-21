package me.hoseok.twitterdemo.account.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.account.Account;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRes {
    private Long id;
    private String username;
    private String email;

    public AccountRes(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.email = account.getEmail();
    }
}
