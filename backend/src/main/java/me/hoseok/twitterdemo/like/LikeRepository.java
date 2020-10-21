package me.hoseok.twitterdemo.like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByPostIdAndAccountId(Long id, Long id1);
}
