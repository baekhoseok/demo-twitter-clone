package me.hoseok.twitterdemo.Account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

    @Autowired AccountService accountService;
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("어드민 권한 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void admin() throws Exception {

        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("어드민 권한 실패")
    @WithUser
    public void admin_fail() throws Exception {

        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("USER 권한 테스트")
    @WithUser
    public void user() throws Exception {

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입")
    public void register() throws Exception {
        AccountDto accountDto = AccountDto.builder()
                .username("hoseok")
                .password("123456")
                .passwordConfirm("123456")
                .email("hoseok@naver.com")
                .build();

        mockMvc.perform(post("/api/accounts/add")
                .content(objectMapper.writeValueAsString(accountDto))
        )
                .andExpect(status().isOk())
        ;
    }
}