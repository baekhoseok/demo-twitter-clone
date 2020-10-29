package me.hoseok.twitterdemo.post;

import me.hoseok.twitterdemo.post.payload.PostFullDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long>, PostReposirotyCustom {
    List<Post> findAll();

    Post findByAccountId(long accountId);

    boolean existsByRetweetId(Long id);

    Post findByRetweetId(Long id);


//    @EntityGraph(attributePaths = {"account", "comments", "images"}, type = EntityGraph.EntityGraphType.LOAD)
//    List<Post> findAllWithComment();
}
