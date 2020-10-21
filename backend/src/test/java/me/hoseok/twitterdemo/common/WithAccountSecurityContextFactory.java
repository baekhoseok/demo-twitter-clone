package me.hoseok.twitterdemo.common;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.AccountService;
import me.hoseok.twitterdemo.account.payload.AccountSignupReq;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountService accountService;


    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {
        String username = withAccount.value();

        AccountSignupReq accountDto = new AccountSignupReq();
        accountDto.setUsername(username);
        accountDto.setEmail(username + "@naver.com");
        accountDto.setPassword("12345678");
        accountService.processNewAccount(accountDto);

        UserDetails principal = accountService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
