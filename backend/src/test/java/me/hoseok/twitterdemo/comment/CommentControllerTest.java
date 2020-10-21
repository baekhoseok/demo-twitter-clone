package me.hoseok.twitterdemo.comment;

import me.hoseok.twitterdemo.comment.payload.CommentReq;
import me.hoseok.twitterdemo.comment.payload.CommentDto;
import me.hoseok.twitterdemo.common.BaseTest;
import me.hoseok.twitterdemo.common.WithAccount;
import me.hoseok.twitterdemo.post.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CommentControllerTest extends BaseTest {

    @Test
    @DisplayName("Comment Create")
    @WithAccount( "user" )
    public void createCommentTest() throws Exception {
        Post post = createPostInDb();

        CommentReq req = new CommentReq( "hello" );
        mockMvc.perform( post("/api/posts/"+post.getId()+"/comment")
                            .content( objectMapper.writeValueAsString(req) )
                            .contentType( MediaType.APPLICATION_JSON )
                            .accept(MediaType.APPLICATION_JSON))
                        .andExpect( status().isOk() )
                        .andExpect( jsonPath( "id" ).exists() )
                        .andExpect( jsonPath( "content" ).value( req.getContent() ) )
                        .andDo(document("create-comment",
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                                ),
                                relaxedRequestFields(
                                        fieldWithPath("content").description(" content of comment")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                                ),
                                relaxedResponseFields(
                                        fieldWithPath("id").description("identifier of new comment"),
                                        fieldWithPath("content").description("content of new comment")
                                )

                        ));

        em.flush();
        em.clear();
        System.out.println("====================================");
        Comment comment = commentRepository.findByPostId(post.getId());
        assertEquals( comment.getContent(), req.getContent() );
        assertEquals( comment.getAccount().getUsername(), "user");
    }

    @Test
    @DisplayName("Comment Update")
    @WithAccount( "user" )
    public void updateCommentTest() throws Exception {
//        Post post = createPostInDb();
        Comment comment = createCommentInDb("user");
        CommentReq req = new CommentReq( "hello" );
        mockMvc.perform( put("/api/posts/"+comment.getPost().getId()+"/comment/"+comment.getId())
                            .content( objectMapper.writeValueAsString(req) )
                            .contentType( MediaType.APPLICATION_JSON )
                            .accept(MediaType.APPLICATION_JSON))
                        .andExpect( status().isOk() )
                        .andExpect( jsonPath( "id" ).exists() )
                        .andExpect( jsonPath( "content" ).value( req.getContent() ) )
                        .andDo(document("update-comment",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("content").description(" content of comment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of new comment"),
                                fieldWithPath("content").description("content of new comment")
                        )

                ));

        em.flush();
        em.clear();
        System.out.println("====================================");
        Comment findComment = commentRepository.findById(comment.getId()).get();
        assertEquals( findComment.getContent(), req.getContent() );
        assertEquals( findComment.getAccount().getUsername(), "user");
    }

    @Test
    @DisplayName("Comment Delete")
    @WithAccount( "user" )
    public void deleteCommentTest() throws Exception {
        Comment comment = createCommentInDb("user");
        mockMvc.perform( delete("/api/posts/"+comment.getPost().getId()+"/comment/"+comment.getId())
                            .contentType( MediaType.APPLICATION_JSON )
                            .accept(MediaType.APPLICATION_JSON))
                        .andExpect( status().isOk() );


        Optional<Comment> byId = commentRepository.findById(comment.getId());
        assertTrue( byId.isEmpty());
    }
}