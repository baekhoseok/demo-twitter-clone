package me.hoseok.twitterdemo.image;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.hoseok.twitterdemo.common.BaseEntity;
import me.hoseok.twitterdemo.post.Post;

import javax.persistence.*;
import java.security.Principal;
import java.time.LocalDateTime;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "url"})
@NoArgsConstructor
public class Image extends BaseEntity {

    @Id @GeneratedValue
    private Long id;
    private String src;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public Image(String src, Post post) {
        this.src = src;
        this.post = post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
