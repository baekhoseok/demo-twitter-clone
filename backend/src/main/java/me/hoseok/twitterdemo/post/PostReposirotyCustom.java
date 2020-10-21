package me.hoseok.twitterdemo.post;

import me.hoseok.twitterdemo.post.payload.PostViewDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostReposirotyCustom {

    List<PostViewDto> findExtensionPosts();
    List<Post> findPosts();
}
