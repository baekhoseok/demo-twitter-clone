package me.hoseok.twitterdemo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter @Getter
public class AccountNotFoundResponse {
    private String account;
}
