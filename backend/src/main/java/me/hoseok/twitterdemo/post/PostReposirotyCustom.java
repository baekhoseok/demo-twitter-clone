package me.hoseok.twitterdemo.post;

import me.hoseok.twitterdemo.post.payload.PostFullDto;
import me.hoseok.twitterdemo.post.payload.PostViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostReposirotyCustom {

    List<PostViewDto> findExtensionPosts();
    List<Post> findPosts();
    Page<PostFullDto> findFullPosts(Pageable pageable);
    Page<PostFullDto> findFullPostsByAccount(Long accountId, String value, Pageable pageable);

    Page<PostFullDto> findFullPostsByHashtag(String value, Pageable pageable);
}
