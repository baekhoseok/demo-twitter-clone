package me.hoseok.twitterdemo.account.payload;

import lombok.Data;

@Data
public class AccountViewDto {
    private Long id;
    private String username;

    public AccountViewDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
