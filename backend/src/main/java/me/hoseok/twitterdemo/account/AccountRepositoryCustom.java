package me.hoseok.twitterdemo.account;

import me.hoseok.twitterdemo.account.payload.AccountFullDto;
import me.hoseok.twitterdemo.account.payload.AccountMeDto;

public interface AccountRepositoryCustom {
    AccountFullDto findExtensionAccountByUsername(String username);
    AccountMeDto findAccountMe(String username);

    AccountFullDto findExtensionAccountById(Long accountId);
}

