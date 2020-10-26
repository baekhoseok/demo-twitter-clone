package me.hoseok.twitterdemo.hashtag;

import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.common.BaseEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class HashTag {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    public HashTag(String name) {
        this.name = name;
    }

    @CreatedDate
    private LocalDateTime createdAt;
}
