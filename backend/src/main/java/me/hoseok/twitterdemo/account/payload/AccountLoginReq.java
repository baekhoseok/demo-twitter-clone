package me.hoseok.twitterdemo.account.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountLoginReq {
    private String username;
    private String password;
}
