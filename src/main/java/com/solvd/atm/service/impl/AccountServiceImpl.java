package com.solvd.atm.service.impl;

import com.solvd.atm.domain.Account;
import com.solvd.atm.domain.Card;
import com.solvd.atm.persistence.AccountRepository;
import com.solvd.atm.persistence.impl.AccountRepositoryImpl;
import com.solvd.atm.service.AccountService;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl() {

        this.accountRepository = new AccountRepositoryImpl();
    }

    @Override
    public void lockAccount(Account account) {
        account.setLockStatus(true);
        accountRepository.blockAccount(account);
    }

    @Override
    public Account getAccountInfo(Card card) {
        Account account = accountRepository.getAccountInfo(card);
        lockAccount(account);
        return account;
    }

    @Override
    public void decrementMoney(Account account, Integer money) {
        accountRepository.changeAccountMoney(account, account.getMoney() - money);
    }

    @Override
    public void incrementMoney(Account account, Integer money){
        accountRepository.changeAccountMoney(account,account.getMoney() + money);
    }

    @Override
    public void unlockAccount(Account account) {
        account.setLockStatus(false);
        accountRepository.unblockAccount(account);
    }

    @Override
    public Integer getBalance(Card card){
        Account account = accountRepository.getAccountInfo(card);
        return account.getMoney();
    }

}
