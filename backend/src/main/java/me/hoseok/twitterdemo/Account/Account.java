package me.hoseok.twitterdemo.Account;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String fullName;
    @Column(unique = true)
    private String email;

//    @Enumerated(value = EnumType.STRING)
    private String role;

    public Account(Long id, String username, String fullName, String email) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
