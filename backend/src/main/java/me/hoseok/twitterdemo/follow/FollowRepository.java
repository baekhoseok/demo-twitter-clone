package me.hoseok.twitterdemo.follow;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFromIdAndToId(Long fromId, Long toId);

    Follow findByFromIdAndToId(Long fromId, Long toId);
}
