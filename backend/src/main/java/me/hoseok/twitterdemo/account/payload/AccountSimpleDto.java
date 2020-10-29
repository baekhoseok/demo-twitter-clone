package me.hoseok.twitterdemo.account.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.account.Account;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountSimpleDto {
    private Long id;
    private String username;

    public AccountSimpleDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
    }
}
