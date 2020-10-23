package me.hoseok.twitterdemo.post;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.AccountRepository;
import me.hoseok.twitterdemo.account.payload.Me;
import me.hoseok.twitterdemo.common.MapValidationErrorsService;
import me.hoseok.twitterdemo.like.Like;
import me.hoseok.twitterdemo.like.payload.LikeDto;
import me.hoseok.twitterdemo.post.payload.PostFullDto;
import me.hoseok.twitterdemo.post.payload.PostReq;
import me.hoseok.twitterdemo.post.payload.PostRes;
import me.hoseok.twitterdemo.post.payload.PostSimpleDto;
import me.hoseok.twitterdemo.security.CurrentMe;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final MapValidationErrorsService mapValidationErrorsService;

    @PostConstruct
    public void init() {
//        for (int i = 0; i < 10; i++) {
//            Account account = new Account("hoseok" + i, "baekhoseok" + i, "hoseok" + i + "@naver.com", "1234");
//            Post post = new Post("content" + i, "location" + i);
//            for (int j = 0; j < 10; j++) {
//                Comment comment = new Comment("comment" + j);
//                Image image = new Image("image-src" + j);
//                comment.setAccount( account );
//                post.addComment(comment);
//                post.addImage(image);
//            }
//
//            account.addPost(post);
//            accountRepository.save(account);
//        }
    }

    @GetMapping("/search")
    public ResponseEntity searchPost(String keyword, Model model,
                                     @PageableDefault(size = 10, page = 1, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostFullDto> all = postService.search(pageable);
        return ResponseEntity.ok( all );
    }

    @GetMapping("/{postId}")
    public ResponseEntity getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        PostRes res = new PostRes(post.getId(), post.getContent(), post.getLocation());
        return ResponseEntity.ok(res);
    }

    @PostMapping()
    public ResponseEntity createPost(@CurrentMe Me me, @RequestBody @Valid PostReq postReq, Errors errors) {
        ResponseEntity errorMap = mapValidationErrorsService.MapValidationErrorsService(errors);
        if(errorMap != null) return errorMap;

        Post post = postService.createPost(postReq, me.getId());
        PostFullDto res = new PostFullDto(post);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{postId}")
    public ResponseEntity patchPost(@CurrentMe Me me, @RequestBody PostReq postReq, @PathVariable Long postId) {
        Post post = postService.patchPost(postReq, postId);
        PostRes res = new PostRes(post.getId(), post.getContent(), post.getLocation());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@CurrentMe Me me, @PathVariable Long postId) {
        Post post = postService.deletePost(postId);
        return ResponseEntity.ok(new PostSimpleDto(post.getId(), post.getContent()));
    }



    @GetMapping("/makeTestPosts")
    public ResponseEntity make(@CurrentMe  Me me) {
        postService.makeTestPosts(me);
        return ResponseEntity.ok().build();
    }

}
