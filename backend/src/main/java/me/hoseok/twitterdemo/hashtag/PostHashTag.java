package me.hoseok.twitterdemo.hashtag;

import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.post.Post;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class PostHashTag {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Post post;

    @ManyToOne
    private HashTag hashTag;

    public PostHashTag(Post post, HashTag hashTag) {
        this.post = post;
        this.hashTag = hashTag;
    }

    @CreatedDate
    private LocalDateTime createdAt;
}
