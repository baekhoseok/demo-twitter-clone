package me.hoseok.twitterdemo.comment;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.payload.Me;
import me.hoseok.twitterdemo.comment.payload.CommentReq;
import me.hoseok.twitterdemo.comment.payload.CommentRes;
import me.hoseok.twitterdemo.comment.payload.CommentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity createComment(@AuthenticationPrincipal Me me, @RequestBody CommentReq req,
                                        @PathVariable Long postId) {
        Comment comment = commentService.createComment( me.getId(), postId, req );
        CommentRes res = new CommentRes(comment.getId(), comment.getContent());
        return ResponseEntity.ok(res);
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity patchComment(@AuthenticationPrincipal Me me, @RequestBody CommentReq req,
                                       @PathVariable Long postId, @PathVariable Long commentId) {
        Comment comment = commentService.patchComment(me.getId(), postId, commentId, req);
        CommentRes res = new CommentRes(comment.getId(), comment.getContent());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity patchComment(@AuthenticationPrincipal Me me,
                                       @PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(me.getId(), postId, commentId);
        return ResponseEntity.ok().build();
    }

}
