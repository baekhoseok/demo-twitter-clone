package me.hoseok.twitterdemo.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class JwtLoginSuccessResponse {
    private String token;
}
