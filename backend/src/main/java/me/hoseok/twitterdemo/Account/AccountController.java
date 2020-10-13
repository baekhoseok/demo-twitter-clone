package me.hoseok.twitterdemo.Account;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.security.JwtLoginSuccessResponse;
import me.hoseok.twitterdemo.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import static me.hoseok.twitterdemo.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AccountDto accountDto, Errors errors) {

        Account account = modelMapper.map(accountDto, Account.class);
        Account newAccount = accountService.register(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(newAccount, AccountDto.class));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AccountDto accountDto, Errors errors) throws JsonProcessingException {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        accountDto.getUsername(),
                        accountDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtLoginSuccessResponse(jwt));
    }
}
