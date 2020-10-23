package me.hoseok.twitterdemo.account;

import me.hoseok.twitterdemo.account.payload.AccountFullDto;
import me.hoseok.twitterdemo.account.payload.AccountMeDto;

public interface AccountRepositoryCustom {
    AccountFullDto findExtensionAccount(String username);
    AccountMeDto findAccountMe(String username);
}

