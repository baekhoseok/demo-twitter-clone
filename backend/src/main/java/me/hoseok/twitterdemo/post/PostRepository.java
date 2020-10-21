package me.hoseok.twitterdemo.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long>, PostReposirotyCustom {
    List<Post> findAll();

    Post findByAccountId(long accountId);


//    @EntityGraph(attributePaths = {"account", "comments", "images"}, type = EntityGraph.EntityGraphType.LOAD)
//    List<Post> findAllWithComment();
}
