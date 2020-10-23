package me.hoseok.twitterdemo.account;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.hoseok.twitterdemo.account.payload.AccountFullDto;
import me.hoseok.twitterdemo.account.payload.AccountMeDto;
import me.hoseok.twitterdemo.account.payload.QAccountFullDto;
import me.hoseok.twitterdemo.account.payload.QAccountMeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;

import static me.hoseok.twitterdemo.account.QAccount.*;

public class AccountRepositoryImpl extends QuerydslRepositorySupport implements AccountRepositoryCustom {


    private final JPAQueryFactory queryFactory;

    @Autowired
    public AccountRepositoryImpl(EntityManager em) {
        super(Account.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public AccountFullDto findExtensionAccount(String username) {
        return queryFactory.select(new QAccountFullDto(
                account.id,
                account.username,
                account.email,
                account.posts.size(),
                account.followings.size(),
                account.followers.size()
                ))
                .from(account)
                .where(account.username.eq(username))
                .fetchOne();
    }

    @Override
    public AccountMeDto findAccountMe(String username) {
        return queryFactory.select(new QAccountMeDto( account))
                .from(account)
                .where(account.username.eq(username))
                .fetchOne();
    }
}
