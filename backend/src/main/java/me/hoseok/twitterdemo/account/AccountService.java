package me.hoseok.twitterdemo.account;

import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.account.payload.AccountSignupReq;
import me.hoseok.twitterdemo.account.payload.AccountUpdateReq;
import me.hoseok.twitterdemo.account.payload.Me;
import me.hoseok.twitterdemo.exception.AccountNotFoundException;
import me.hoseok.twitterdemo.exception.InvalidArgumentsException;
import me.hoseok.twitterdemo.exception.UsernameAlreadyExistsException;
import me.hoseok.twitterdemo.follow.Follow;
import me.hoseok.twitterdemo.follow.FollowRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Transient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transient
@Transactional
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        return new Me(account);
    }

    public Account signup(Account account) {
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

    public Account processNewAccount(AccountSignupReq accountDto) {
        Account account = modelMapper.map( accountDto, Account.class );
        account.encodePassword(passwordEncoder);
        return accountRepository.save( account );
    }

    public Account follow(Me me, Long targetId) {
        if(me.getId().equals(targetId)) {
            throw new InvalidArgumentsException("Can not follow your self");
        }
        Account myAccount = accountRepository.findById( me.getId() ).get();
        Optional<Account> targetAccountOptional = accountRepository.findById( targetId );
        targetAccountOptional.orElseThrow(() -> new AccountNotFoundException( "Target Account not found" ));

        Account targetAccount = targetAccountOptional.get();
//        myAccount.getFollowings().add( new Follow(myAccount, targetAccount));
//        targetAccount.getFollowers().add( myAccount );
        followRepository.save(new Follow(myAccount, targetAccount));
        return targetAccount;
    }

    public Account unFollow(Me me, Long targetId) {
        if(me.getId() == targetId) {
            throw new InvalidArgumentsException("Can not unFollow your self");
        }
        Account myAccount = accountRepository.findById( me.getId() ).get();
        Optional<Account> targetAccountOptional = accountRepository.findById( targetId );
        targetAccountOptional.orElseThrow(() -> new AccountNotFoundException( "Target Account not found" ));

        Account targetAccount = targetAccountOptional.get();
//        myAccount.getFollowings().remove( targetAccount );
//        targetAccount.getFollowers().remove( myAccount );
        followRepository.deleteByFromIdAndToId(myAccount.getId(), targetAccount.getId());
        return targetAccount;
    }

    public Account update(AccountUpdateReq req, String username) {
        Account account = accountRepository.findByUsername(username);
        account.update(req);
        return account;
    }
}
