package me.hoseok.twitterdemo.account;

import me.hoseok.twitterdemo.account.payload.AccountFullDto;

public interface AccountReposirotyCustom {
    AccountFullDto findExtensionAccount(String username);
}

