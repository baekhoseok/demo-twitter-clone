package me.hoseok.twitterdemo.Account;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.Account.payload.SimpleAccount;
import me.hoseok.twitterdemo.exception.UsernameAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Transient;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transient
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        return new SimpleAccount(account);
    }

    public Account register(Account account) {
        try {
            account.encodePassword(passwordEncoder);
            //transaction 완료시 commit 발생되고 transaction 은 함수가 리턴된후 완료된다.
            //따라서 try catch 구문에서 account 중복 exception 을 잡을 수 없다.
            //그러므로 save 동시에 flush 를 해주어 transaction 완료 전 중복 exception 을 발생시킬 수 있다.
            return accountRepository.saveAndFlush(account);
        } catch (Exception e) {
            throw new UsernameAlreadyExistsException("Username or Email already exists");
        }
    }

    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
