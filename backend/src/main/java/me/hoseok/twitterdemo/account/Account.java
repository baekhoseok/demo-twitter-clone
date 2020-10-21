package me.hoseok.twitterdemo.account;

import lombok.*;
import me.hoseok.twitterdemo.account.payload.AccountSignupReq;
import me.hoseok.twitterdemo.account.payload.AccountUpdateReq;
import me.hoseok.twitterdemo.common.BaseEntity;
import me.hoseok.twitterdemo.follow.Follow;
import me.hoseok.twitterdemo.post.Post;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "username", "fullName", "email"})
public class Account extends BaseEntity {

    @Id @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String fullName;
    @Column(unique = true, nullable = false)
    private String email;
//    @Enumerated(value = EnumType.STRING)
    private String role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "from")
    private Set<Follow> followings = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "to")
    private Set<Follow> followers = new HashSet<>();


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    public Account(Long id, String username, String fullName, String email) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    public Account(String username, String fullName, String email, String password) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void addPost(Post post) {
        this.posts.add(post);
        post.setOwner(this);
    }

    public void update(AccountUpdateReq req) {
        this.username = req.getUsername();
        this.email = req.getEmail();
    }
}
