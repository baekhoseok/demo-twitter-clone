package me.hoseok.twitterdemo.Account;

import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private AccountRole role;
}
