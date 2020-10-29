package me.hoseok.twitterdemo.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.payload.*;
import me.hoseok.twitterdemo.account.validator.AccountSignupValidator;
import me.hoseok.twitterdemo.common.MapValidationErrorsService;
import me.hoseok.twitterdemo.exception.AccountNotFoundException;
import me.hoseok.twitterdemo.exception.ErrorResponse;
import me.hoseok.twitterdemo.security.CurrentMe;
import me.hoseok.twitterdemo.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static me.hoseok.twitterdemo.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MapValidationErrorsService mapValidationErrorsService;
    private final AccountSignupValidator accountSignupValidator;
    private final AccountRepository accountRepository;

    @InitBinder("accountSignupReq")
    public void signupInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountSignupValidator);
    }

    @PostMapping("/signup")
    public ResponseEntity singup(@RequestBody @Valid AccountSignupReq accountSignupReq, Errors errors) {

        ResponseEntity errorMap = mapValidationErrorsService.MapValidationErrorsService(errors);
        if(errorMap != null) return errorMap;

        Account account = modelMapper.map(accountSignupReq, Account.class);
        Account newAccount = accountService.signup(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccountRes(newAccount));
    }

    @PutMapping("/{username}")
    public ResponseEntity updateAccount(@CurrentMe Me me, @PathVariable String username) {

        if (username == null || username.length() < 4 || username.length() > 30) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username must not null and has between 4 and 30 lengh"));
        }

        if (accountRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username is already exists"));
        }
//        ResponseEntity errorMap = mapValidationErrorsService.MapValidationErrorsService(errors);
//        if(errorMap != null) return errorMap;

        Account newAccount = accountService.update(me.getUsername(), username);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccountRes(newAccount));
    }

    @GetMapping("/{username}")
    public ResponseEntity getAccount(@PathVariable String username) {

        AccountFullDto extensionAccount = accountRepository.findExtensionAccountByUsername(username);
        return ResponseEntity.ok(extensionAccount);
    }

    @GetMapping("/id/{accountId}")
    public ResponseEntity getAccount(@PathVariable Long accountId) {

        AccountFullDto extensionAccount = accountRepository.findExtensionAccountById(accountId);
        return ResponseEntity.ok(extensionAccount);
    }

    @GetMapping("/me")
    public ResponseEntity getMe(@CurrentMe Me me, HttpServletRequest request) {
        if(me == null) {
            throw new AccountNotFoundException("Account not found");
        }
        ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
        String token = wrappingRequest.getHeader("Authorization");
        AccountMeDto accountMe = accountRepository.findAccountMe(me.getUsername());
        accountMe.setToken(token);
        return ResponseEntity.ok(accountMe);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AccountLoginReq req, Errors errors, HttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        AccountMeDto accountMe = accountRepository.findAccountMe(req.getUsername());
        accountMe.setToken(jwt);
        Cookie myCookie = new Cookie("jwtToken", URLEncoder.encode( jwt, "UTF-8" ));
        myCookie.setPath("/");
//        myCookie.setSecure(true);
//        myCookie.set
        myCookie.setHttpOnly(true);
//        response.setHeader("Access-Control-Allow-Headers",
//                "Date, Content-Type, Accept, X-Requested-With, Authorization, From, X-Auth-Token, Request-Id");
//        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        myCookie.setMaxAge(쿠키 expiration 타임 (int));
//        myCookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(myCookie);
        return ResponseEntity.ok(accountMe);
//        return ResponseEntity.ok(new JwtLoginSuccessResponse(jwt));
    }

    @PostMapping("/follow/{targetId}")
    public ResponseEntity follow(@AuthenticationPrincipal Me me, @PathVariable Long targetId) {
        Account account = accountService.follow(me, targetId);
        return ResponseEntity.ok(new AccountSimpleDto(account.getId(), account.getUsername()));
    }

    @PostMapping("/unFollow/{targetId}")
    public ResponseEntity unFollow(@AuthenticationPrincipal Me me, @PathVariable Long targetId) {
        Account account = accountService.unFollow(me, targetId);
        return ResponseEntity.ok(new AccountSimpleDto(account.getId(), account.getUsername()));
    }

    @DeleteMapping("/follower/{targetId}")
    public ResponseEntity deleteFollower(@AuthenticationPrincipal Me me, @PathVariable Long targetId) {
        Account account = accountService.removeFollower(me, targetId);
        return ResponseEntity.ok(new AccountSimpleDto(account.getId(), account.getUsername()));
    }
}
