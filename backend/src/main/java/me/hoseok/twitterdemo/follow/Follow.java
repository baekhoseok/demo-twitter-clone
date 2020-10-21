package me.hoseok.twitterdemo.follow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hoseok.twitterdemo.account.Account;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Follow {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account from;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account to;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Follow(Account from, Account to) {
        this.from = from;
        this.to = to;
    }
}
