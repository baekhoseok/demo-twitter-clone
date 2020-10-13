package me.hoseok.twitterdemo.Account;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
@WithMockUser(username = "hoseok", roles = "USER")
public @interface WithUser {
}
