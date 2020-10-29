package me.hoseok.twitterdemo.post;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.AccountRepository;
import me.hoseok.twitterdemo.account.payload.Me;
import me.hoseok.twitterdemo.common.MapValidationErrorsService;
import me.hoseok.twitterdemo.config.FileStorageProperties;
import me.hoseok.twitterdemo.post.payload.*;
import me.hoseok.twitterdemo.security.CurrentMe;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final MapValidationErrorsService mapValidationErrorsService;
    private final FileStorageProperties fileStorageProperties;
    private Path fileStorageLocation;

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

    @GetMapping("/posts/search")
    public ResponseEntity searchPost(String keyword, Model model,
                                     @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostFullDto> all = postService.search(pageable);
        return ResponseEntity.ok( all );
    }

    @GetMapping("/posts/hashtag/search")
    public ResponseEntity searchPost(String keyword, Model model, SearchDto searchDto,
                                     @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostFullDto> all = postService.searchHashtag(searchDto, pageable);
        return ResponseEntity.ok( all );
    }

    @GetMapping("/posts/{accountId}/search")
    public ResponseEntity searchPost(String keyword, Model model, @PathVariable Long accountId, SearchDto searchDto,
                                     @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostFullDto> all = postService.searchPostsByAccountId(accountId, searchDto, pageable);
        return ResponseEntity.ok( all );
    }
//
//    @GetMapping("/posts/{postId}")
//    public ResponseEntity getPost(@PathVariable Long postId) {
//        Post post = postService.getPost(postId);
//        PostRes res = new PostRes(post.getId(), post.getContent(), post.getLocation());
//        return ResponseEntity.ok(res);
//    }

    @PostMapping("/posts")
    public ResponseEntity createPost(@CurrentMe Me me, @Valid PostReq postReq, Errors errors) {
        ResponseEntity errorMap = mapValidationErrorsService.MapValidationErrorsService(errors);
        if(errorMap != null) return errorMap;

        Post post = postService.createPost(postReq, me.getId());
        PostFullDto res = new PostFullDto(post);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity updatePost(@CurrentMe Me me, @Valid @RequestBody PostReq postReq, Errors errors, @PathVariable Long postId) {
        ResponseEntity errorMap = mapValidationErrorsService.MapValidationErrorsService(errors);
        if(errorMap != null) return errorMap;

        Post post = postService.updatePost(postReq, postId, me.getId());
        PostRes res = new PostRes(post.getId(), post.getContent(), post.getLocation());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity deletePost(@CurrentMe Me me, @PathVariable Long postId) {
        Post post = postService.deletePost(postId, me.getId());
        return ResponseEntity.ok(new PostSimpleDto(post));
    }



    @GetMapping("/posts/makeTestPosts")
    public ResponseEntity make(@CurrentMe  Me me) {
        postService.makeTestPosts(me);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/posts/images")
    public ResponseEntity uploadFile(@RequestParam("image") MultipartFile[] files) throws Exception{

        List<String> imageUris = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
             String fileName = StringUtils.cleanPath(files[i].getOriginalFilename());
            String extension = "";

            int idx = fileName.lastIndexOf('.');
            if (idx > 0) {
                extension = fileName.substring(idx+1);
                fileName = fileName.substring(0, idx) + System.currentTimeMillis()+"."+extension;
            System.out.println("extension = " + extension + " / fileName = " + fileName);
            }
            this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                    .toAbsolutePath().normalize();

            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
            }

            try {

                // Check if the file's name contains invalid characters
                if(fileName.contains("..")) {
                    throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
                }

                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(files[i].getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException ex) {
                throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
            }

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(fileName)
                    .toUriString();

            imageUris.add(fileDownloadUri);
        }

        return ResponseEntity.ok(imageUris);
    }

    @PostMapping("/posts/{postId}/retweet")
    public ResponseEntity retweet(@CurrentMe Me me, @PathVariable Long postId) {
        Post post = postService.retweet(me, postId);
        PostRetweetDto res = new PostRetweetDto(post);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        return ResponseEntity.ok(new PostFullDto(post));
    }

}
