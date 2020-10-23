package me.hoseok.twitterdemo.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
    Account findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
