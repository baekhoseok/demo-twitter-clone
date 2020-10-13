package me.hoseok.twitterdemo.Account.payload;

import lombok.*;
import me.hoseok.twitterdemo.Account.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class SimpleAccount extends User {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String role;

    public SimpleAccount(Account account) {
        super(account.getUsername(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_"+account.getRole())));
        this.id = account.getId();
        this.username = account.getUsername();
        this.fullName = account.getFullName();
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.role = account.getRole();
    }

    public SimpleAccount(Long id, String username, String fullName, String email) {
        super(username, "dummy", Collections.EMPTY_LIST);
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }
}
