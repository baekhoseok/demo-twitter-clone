package me.hoseok.twitterdemo.like;

import lombok.*;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.post.Post;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "id")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
public class  Like {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @CreatedDate
    private LocalDateTime createdAt;

    public Like(Post post, Account account) {
        this.post = post;
        this.account = account;
    }
}
