package com.revolut.money.transfer.dao;

import com.revolut.money.transfer.bean.Account;
import com.revolut.money.transfer.util.BankUtil;

public class AccountDaoImpl {
    public Account addAccount(Account account) {
        Account account1=new Account(account.getAccountId(),account.getUserName(),account.getAccountBalance());
        BankUtil.setAccounts(account1);
        return account1;

    }
}
