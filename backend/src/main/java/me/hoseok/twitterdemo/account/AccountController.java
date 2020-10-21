package me.hoseok.twitterdemo.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.payload.*;
import me.hoseok.twitterdemo.account.validator.AccountSignupValidator;
import me.hoseok.twitterdemo.account.validator.AccountUpdateValidator;
import me.hoseok.twitterdemo.common.MapValidationErrorsService;
import me.hoseok.twitterdemo.security.JwtLoginSuccessResponse;
import me.hoseok.twitterdemo.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    private final AccountUpdateValidator accountUpdateValidator;
    private final AccountRepository accountRepository;

    @InitBinder("accountSignupReq")
    public void signupInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountSignupValidator);
    }

    @InitBinder("accountUpdateReq")
    public void updateInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountUpdateValidator);
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
    public ResponseEntity updateAccount(@RequestBody @Valid AccountUpdateReq req, Errors errors, @PathVariable String username) {

        ResponseEntity errorMap = mapValidationErrorsService.MapValidationErrorsService(errors);
        if(errorMap != null) return errorMap;

        Account newAccount = accountService.update(req, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccountRes(newAccount));
    }

    @GetMapping("/{username}")
    public ResponseEntity getAccount(@PathVariable String username) {

        AccountFullDto extensionAccount = accountRepository.findExtensionAccount(username);
        return ResponseEntity.ok(extensionAccount);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AccountLoginReq req, Errors errors) throws JsonProcessingException {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtLoginSuccessResponse(jwt));
    }

    @PostMapping("/{targetId}/follow")
    public ResponseEntity follow(@AuthenticationPrincipal Me me, @PathVariable Long targetId) {
        accountService.follow( me, targetId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{targetId}/unFollow")
    public ResponseEntity unFollow(@AuthenticationPrincipal Me me, @PathVariable Long targetId) {
        accountService.unFollow( me, targetId);
        return ResponseEntity.ok().build();
    }
}
