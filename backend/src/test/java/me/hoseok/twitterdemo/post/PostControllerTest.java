package me.hoseok.twitterdemo.post;

import me.hoseok.twitterdemo.common.BaseTest;
import me.hoseok.twitterdemo.account.Account;
import me.hoseok.twitterdemo.comment.Comment;
import me.hoseok.twitterdemo.common.WithAccount;
import me.hoseok.twitterdemo.image.Image;
import me.hoseok.twitterdemo.like.Like;
import me.hoseok.twitterdemo.post.payload.PostReq;
import me.hoseok.twitterdemo.post.payload.PostViewDto;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerTest extends BaseTest {


    @BeforeEach
    public void beforeEach() {
    }

    @AfterEach
    public void afterEach() {
        em.flush();
        em.clear();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Post Creation")
    @WithAccount("user")
    public void createPostTest() throws Exception {
        PostReq post = createPostDto(  "hahahah", "here" );

        mockMvc.perform( post("/api/posts")
                            .content( objectMapper.writeValueAsString( post ) )
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType( MediaType.APPLICATION_JSON ))
                        .andExpect( status().isOk() )
                        .andDo(document("create-post",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("content").description(" content of post"),
                                fieldWithPath("location").description(" location of post")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of new post"),
                                fieldWithPath("content").description("content of new post"),
                                fieldWithPath("location").description("location of new post")
                        )

                ));

        Account account = accountRepository.findByUsername( "user" );
        Post findPost = postRepository.findByAccountId( account.getId() );
        assertEquals( post.getContent(), findPost.getContent() );
        assertEquals( post.getLocation(), findPost.getLocation() );

    }

    @Test
    @DisplayName("Post Creation Validation Error")
    @WithAccount("user")
    public void createPostTest_validation_error() throws Exception {
        PostReq post = createPostDto(  "a", "here" );

        mockMvc.perform( post("/api/posts")
                            .content( objectMapper.writeValueAsString( post ) )
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType( MediaType.APPLICATION_JSON ))
                        .andExpect( status().isBadRequest() )
                        .andExpect(jsonPath("content").exists());

    }

    @Test
    @DisplayName("Post Update")
    @WithAccount("user")
    public void updatePostTest() throws Exception {
        Post post = createPostInDb( );
        PostReq postReq = createPostDto(  "gagaga", "there" );
        mockMvc.perform( put("/api/posts/"+post.getId())
                            .content( objectMapper.writeValueAsString(postReq) )
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType( MediaType.APPLICATION_JSON ) )
                        .andDo( print() )
                        .andExpect( status().isOk() )
                        .andDo(document("update-post",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("content").description(" content of post"),
                                fieldWithPath("location").description(" location of post")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of updated post"),
                                fieldWithPath("content").description("content of updated post"),
                                fieldWithPath("location").description("location of updated post")
                        )

                ));

        Post findPost = postRepository.findById( post.getId() ).get();
        assertEquals( postReq.getContent(), findPost.getContent() );
        assertEquals( postReq.getLocation(), findPost.getLocation() );

    }

    @Test
    @DisplayName("Post Update Fail")
    @WithAccount("user")
    public void updatePostTest_fail() throws Exception {
        Post post = createPostInDb(  );
        PostReq postReq = createPostDto(  "gagaga", "there" );
        mockMvc.perform( put("/api/posts/222222")
                            .content( objectMapper.writeValueAsString(postReq) )
                            .contentType( MediaType.APPLICATION_JSON ) )
                        .andDo( print() )
                        .andExpect( status().isBadRequest() );

    }

    @Test
    @DisplayName("Post Delete")
    @WithAccount("user")
    public void deletePostTest() throws Exception {
        Post post = createPostInDb();
        mockMvc.perform( delete("/api/posts/"+post.getId())
                .contentType( MediaType.APPLICATION_JSON ) )
                .andDo( print() )
                .andExpect( status().isOk() );

        assertFalse( postRepository.existsById( post.getId() ) );
    }

    @Test
    @DisplayName("Post Delete Fail")
    @WithAccount("user")
    public void deletePostTest_fail() throws Exception {
        Post post = createPostInDb( );
        mockMvc.perform( delete("/api/posts/222222")
                .contentType( MediaType.APPLICATION_JSON ) )
                .andDo( print() )
                .andExpect( status().isBadRequest() );
    }

    @Test
    @DisplayName("Post Get")
    @WithAccount("user")
    public void getPostTest() throws Exception {
        Post post = createPostInDb( );
        mockMvc.perform( get("/api/posts/"+post.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType( MediaType.APPLICATION_JSON ) )
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "id" ).value( post.getId()) )
                .andExpect( jsonPath( "content" ).value( post.getContent()) )
                .andExpect( jsonPath( "location" ).value( post.getLocation()) )
                .andDo(document("get-post",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of updated post"),
                                fieldWithPath("content").description("content of updated post"),
                                fieldWithPath("location").description("location of updated post")
                        )

                ));
    }


    @Test
    @DisplayName("Post Like")
    @WithAccount("user")
    public void likePostTest() throws Exception {
        Post post = createPostInDb();
        mockMvc.perform( post("/api/posts/"+post.getId()+"/like")
                .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );


        Account account = accountRepository.findByUsername("user");
        Like like = likeRepository.findByPostIdAndAccountId(post.getId(), account.getId());
        assertNotNull(like);
    }

    @Test
    @DisplayName("Post Like Fail")
    @WithAccount("user")
    public void likePostTest_fail() throws Exception {
        Post post = createPostInDb();
        mockMvc.perform( post("/api/posts/12312313/like")
                .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() )
                .andExpect(jsonPath("post").value("Post not found"));


        Account account = accountRepository.findByUsername("user");
        Like like = likeRepository.findByPostIdAndAccountId(post.getId(), account.getId());
        assertNull(like);
    }

    @Test
    @DisplayName("Post unLike")
    @WithAccount("user")
    @Rollback(value = false)
    public void unLikePostTest() throws Exception {
        Like like = likePost();

        mockMvc.perform( delete("/api/posts/"+like.getPost().getId()+"/unLike")
                .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );


        Account account = like.getAccount();
        Post post = like.getPost();
        Like findLike = likeRepository.findByPostIdAndAccountId(post.getId(), account.getId());
        assertNull(findLike);
    }


    private void createDummy() {
        for (int i = 0; i < 10; i++) {
            Account account = new Account("hoseok" + i, "baekhoseok" + i, "hoseok" + i + "@naver.com", "1234");
            Post post = new Post("content" + i, "location" + i);
            for (int j = 0; j < 10; j++) {
                Comment comment = new Comment("comment" + j);
                Image image = new Image("image-src" + j);
                comment.setAccount( account );
                post.addComment(comment);
                post.addImage(image);
            }

            account.addPost(post);
            accountRepository.save(account);
        }

    }


    @Test
    @DisplayName("post load test")
    public void loadPosts() throws Exception {
        System.out.println("[LOAD]=========================================");
        List<PostViewDto> posts = postService.search();
        posts.forEach(p -> System.out.println("[LOAD] "+p.toString()));
    }
}