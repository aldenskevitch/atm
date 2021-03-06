package com.solvd.atm.service.impl;

import com.solvd.atm.domain.Account;
import com.solvd.atm.domain.Bank;
import com.solvd.atm.domain.Card;
import com.solvd.atm.domain.exception.ReadDataBaseException;
import com.solvd.atm.persistence.AccountRepository;
import com.solvd.atm.persistence.impl.AccountRepositoryImpl;
import com.solvd.atm.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LogManager.getLogger();

    private final AccountRepository accountRepository;

    public AccountServiceImpl() {
        this.accountRepository = new AccountRepositoryImpl();
    }

    @Override
    public void lockAccount(Account account) {
        if (account != null) {
            account.setLockStatus(true);
            accountRepository.blockAccount(account);
        }
    }

    @Override
    public Account getAccountInfo(Card card) {
        try {
            if (accountRepository.getAccountInfo(card) != null) {
                Account account = accountRepository.getAccountInfo(card);
                lockAccount(account);
                return account;
            } else throw new ReadDataBaseException("Account not found!");
        } catch (ReadDataBaseException e) {
            LOGGER.info(e);
            return null;
        }
    }

    @Override
    public void decrementMoney(Account account, BigDecimal money) {
        accountRepository.changeAccountMoney(account, account.getMoney().subtract(money));
    }

    @Override
    public void incrementMoney(Account account, BigDecimal money) {
        accountRepository.changeAccountMoney(account, account.getMoney().add(money));
    }

    @Override
    public void unlockAccount(Account account) {
        account.setLockStatus(false);
        accountRepository.unblockAccount(account);
    }

    @Override
    public BigDecimal getBalance(Card card) {
        Account account = accountRepository.getAccountInfo(card);
        return account.getMoney();
    }

    @Override
    public Bank getBank(Account account){
        return accountRepository.getBankInfo(account);
    }
}
