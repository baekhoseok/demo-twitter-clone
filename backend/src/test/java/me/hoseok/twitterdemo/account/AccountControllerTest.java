package me.hoseok.twitterdemo.account;

import me.hoseok.twitterdemo.account.payload.AccountLoginReq;
import me.hoseok.twitterdemo.account.payload.AccountSignupReq;
import me.hoseok.twitterdemo.account.payload.AccountUpdateReq;
import me.hoseok.twitterdemo.common.BaseTest;
import me.hoseok.twitterdemo.common.WithAccount;
import me.hoseok.twitterdemo.follow.Follow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AccountControllerTest extends BaseTest {


    @Test
    @DisplayName("Login")
    @WithAccount("user")
    public void login() throws Exception {
        AccountLoginReq req = new AccountLoginReq("user", "12345678");
        mockMvc.perform(post("/api/accounts/login")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("create-account",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("username").description("username of new account"),
                                fieldWithPath("password").description("password of new account")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of my account"),
                                fieldWithPath("username").description("username of my account"),
                                fieldWithPath("email").description("email of my account"),
                                fieldWithPath("followers").description("follower list of my account"),
                                fieldWithPath("followings").description("following list of my account"),
                                fieldWithPath("posts").description("post list of my account"),
                                fieldWithPath("token").description("jwttoken of my account")
                        )

                ));



    }

    @Test
    @DisplayName("Signup")
    public void signup() throws Exception {
        AccountSignupReq accountDto = AccountSignupReq.builder()
                .username("nobody")
                .password("123456")
                .passwordConfirm("123456")
                .email("nobody@naver.com")
                .build();

        mockMvc.perform(post("/api/accounts/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto))

        )
                .andExpect(status().isCreated())
                .andDo(document("create-account",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("username").description("username of new account"),
                                fieldWithPath("email").description("email of new account"),
                                fieldWithPath("password").description("password of new account"),
                                fieldWithPath("passwordConfirm").description("passwordConfirm of new account")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of new account"),
                                fieldWithPath("username").description("username of new account"),
                                fieldWithPath("email").description("email of new account")
                        )

                ));
    }

    @Test
    @DisplayName("Account Update")
    @WithAccount("user")
    public void updateAccount() throws Exception {

        mockMvc.perform(put("/api/accounts/tommy")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andDo(document("update-account",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of new account"),
                                fieldWithPath("username").description("username of new account"),
                                fieldWithPath("email").description("email of new account")
                        )

                ));
    }

    @Test
    @DisplayName("Account Update Fail")
    @WithAccount("user")
    public void updateAccount_fail() throws Exception {

        mockMvc.perform(put("/api/accounts/us")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").exists())
        ;
    }

    @Test
    @DisplayName("Get Account")
    @WithAccount("user")
    public void getAccount() throws Exception {

        mockMvc.perform(get("/api/accounts/user")
                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-account",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of the account"),
                                fieldWithPath("username").description("username of the account"),
                                fieldWithPath("email").description("email of the account"),
                                fieldWithPath("postsCount").description("postsCount of the account"),
                                fieldWithPath("followingsCount").description("followingsCount of the account"),
                                fieldWithPath("followersCount").description("followersCount of the account")
                        )

                ));
    }

    @Test
    @DisplayName("Signup Fail - empty value")
    public void signup_fail() throws Exception {
        AccountSignupReq signupReq = AccountSignupReq.builder()
                .username("")
                .password("")
                .passwordConfirm("")
                .email("")
                .build();

        mockMvc.perform(post("/api/accounts/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupReq))

        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("username").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("password").exists())
                .andExpect(jsonPath("passwordConfirm").exists())
        ;
    }

    @Test
    @DisplayName("Signup Fail - Password not matched")
    public void signup_fail_not_matched_password() throws Exception {
        AccountSignupReq signupReq = AccountSignupReq.builder()
                .username("hoseok")
                .password("12345678")
                .passwordConfirm("12341234")
                .email("hoseok@naver.com")
                .build();

        mockMvc.perform(post("/api/accounts/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupReq))

        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("password").value("Password is not matched"))
        ;
    }

    @Test
    @DisplayName("Signup Fail - username or email is duplicated")
    public void signup_fail_username_or_email_is_duplicated() throws Exception {
        createAccountInDb("user");
        AccountSignupReq signupReq = AccountSignupReq.builder()
                .username("user")
                .password("12345678")
                .passwordConfirm("12345678")
                .email("user@naver.com")
                .build();

        mockMvc.perform(post("/api/accounts/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupReq))

        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("username").value("Username is duplicated"))
                .andExpect(jsonPath("email").value("Email is duplicated"))
        ;
    }



    @Test
    @DisplayName("Follow")
    @WithAccount("user")
    public void followTest() throws Exception {
        Account targetAccount = createAccountInDb("admin");
        mockMvc.perform(post("/api/accounts/follow/"+targetAccount.getId()))
                .andExpect(status().isOk());

        em.flush();
        em.clear();
        Account me = accountRepository.findByUsername("user");
        Follow follow = followRepository.findByFromIdAndToId(me.getId(), targetAccount.getId());


        assertEquals(follow.getFrom(), me);
        assertEquals(follow.getTo(), targetAccount);
    }

    @Test
    @DisplayName("Follow fail - follow self")
    @WithAccount("user")
    public void followTest_fail_follow_self() throws Exception {
        Account me = accountRepository.findByUsername("user");
        Account targetAccount = createAccountInDb("admin");
        mockMvc.perform(post("/api/accounts/follow/"+me.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("arguments").value("Can not follow your self"));

        em.flush();
        em.clear();
        Follow follow = followRepository.findByFromIdAndToId(me.getId(), targetAccount.getId());
        assertNull(follow);
    }

    @Test
    @DisplayName("Follow fail - follow nobody")
    @WithAccount("user")
    public void followTest_fail_follow_nobody() throws Exception {
        Account me = accountRepository.findByUsername("user");
        Account targetAccount = createAccountInDb("admin");
        mockMvc.perform(post("/api/accounts/follow/123123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("account").value("Target Account not found"));

        em.flush();
        em.clear();
        Follow follow = followRepository.findByFromIdAndToId(me.getId(), targetAccount.getId());
        assertNull(follow);
    }

    @Test
    @DisplayName("UnFollow")
    @WithAccount("user")
    public void unFollowTest() throws Exception {
        Account targetAccount = createAccountInDb("admin");
        Account me = follow(targetAccount);
        mockMvc.perform(post("/api/accounts/unFollow/"+targetAccount.getId()))
                .andExpect(status().isOk());
        em.flush();
        em.clear();
        Account me2 = accountRepository.findByUsername("user");
        Account target = accountRepository.findById(targetAccount.getId()).get();
        assertFalse(me2.getFollowings().contains(target));
        assertFalse(target.getFollowers().contains(me2));
    }
}