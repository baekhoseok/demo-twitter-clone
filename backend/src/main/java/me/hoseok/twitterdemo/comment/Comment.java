package me.hoseok.twitterdemo.comment;

import lombok.*;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.common.BaseEntity;
import me.hoseok.twitterdemo.post.Post;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "content"})
public class Comment extends BaseEntity {

    @Id @GeneratedValue
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public Comment(String content) {
        this.content = content;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
