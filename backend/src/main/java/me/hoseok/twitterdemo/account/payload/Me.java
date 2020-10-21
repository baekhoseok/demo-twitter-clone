package me.hoseok.twitterdemo.account.payload;

import lombok.*;
import me.hoseok.twitterdemo.account.Account;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.List;

@Getter
public class Me extends User {
    private Long id;
    private String username;
    private String fullName;
    private String email;
//    private String password;
//    private String role;

    public Me(Account account) {
        super(account.getUsername(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_"+account.getRole())));
        this.id = account.getId();
        this.username = account.getUsername();
        this.fullName = account.getFullName();
        this.email = account.getEmail();
//        this.password = account.getPassword();
//        this.role = account.getRole();
    }

    public Me(Long id, String username, String fullName, String email) {
        super(username, "dummy", Collections.EMPTY_LIST);
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }
}
