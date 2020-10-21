package me.hoseok.twitterdemo.account.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountSimpleDto {
    private Long id;
    private String username;
}
