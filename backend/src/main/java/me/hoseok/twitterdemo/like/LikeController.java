package me.hoseok.twitterdemo.like;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.payload.Me;
import me.hoseok.twitterdemo.like.payload.LikeDto;
import me.hoseok.twitterdemo.security.CurrentMe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity likePost(@CurrentMe Me me, @PathVariable Long postId) {
        Like like = likeService.likePost(postId, me);
        return ResponseEntity.ok(new LikeDto(like.getId(), like.getPost().getId(), like.getAccount().getId()));
    }

    @DeleteMapping("/like")
    public ResponseEntity unLikePost(@CurrentMe Me me, @PathVariable Long postId) {
        Like like = likeService.unLikePost(postId, me);
        return ResponseEntity.ok(new LikeDto(like.getId(), like.getPost().getId(), like.getAccount().getId()));

    }
}
