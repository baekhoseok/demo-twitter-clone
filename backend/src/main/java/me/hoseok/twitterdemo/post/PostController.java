package me.hoseok.twitterdemo.post;

import me.hoseok.twitterdemo.Account.payload.SimpleAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @GetMapping("/{postId}")
    public ResponseEntity getPost(@AuthenticationPrincipal SimpleAccount simpleAccount, @PathVariable Long postId) {
        return ResponseEntity.ok(simpleAccount);
    }
}
