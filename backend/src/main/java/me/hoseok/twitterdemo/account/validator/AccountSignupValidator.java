package me.hoseok.twitterdemo.account.validator;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.AccountRepository;
import me.hoseok.twitterdemo.account.payload.AccountSignupReq;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AccountSignupValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(AccountSignupReq.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccountSignupReq req = (AccountSignupReq) o;

        if (!req.getPassword().equals(req.getPasswordConfirm())) {
            errors.rejectValue("password", "invalid.password", "Password is not matched");
        }

        if (accountRepository.existsByUsername(req.getUsername())) {
            errors.rejectValue("username", "invalid.username", "Username is duplicated");
        }

        if (accountRepository.existsByEmail(req.getEmail())) {
            errors.rejectValue("email", "invalid.email", "Email is duplicated");
        }
    }
}
