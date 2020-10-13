package me.hoseok.twitterdemo.security;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.Account.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static me.hoseok.twitterdemo.security.SecurityConstants.H2_URL;
import static me.hoseok.twitterdemo.security.SecurityConstants.SIGN_UP_URLS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .anyRequest().permitAll();
//        http.cors().and().csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests()
//                .antMatchers(
//                        "/",
//                        "/favicon.ico",
//                        "/**/*.png",
//                        "/**/*.gif",
//                        "/**/*.svg",
//                        "/**/*.jpg",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js"
//                ).permitAll()
//                .antMatchers(SIGN_UP_URLS).permitAll()
//                .antMatchers(H2_URL).permitAll()
//                .anyRequest().authenticated();

//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
