package me.hoseok.twitterdemo.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    boolean existsByName(String tag);

    HashTag findByName(String tag);
}
