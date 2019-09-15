package com.revolut.money.transfer.service;

import com.revolut.money.transfer.bean.Account;
import com.revolut.money.transfer.dao.AccountDaoImpl;
import com.revolut.money.transfer.util.BankUtil;

public class AccountService {
    AccountDaoImpl accountDao = new AccountDaoImpl();

    public Account addAccount(Account account) {
        if (null != account && null == account.getAccountId()) {
            account.setAccountId(BankUtil.generateAccountId(0, Integer.MAX_VALUE));
        }
        return accountDao.addAccount(account);
    }

    public boolean deleteAccount(String accountId) {
        if (BankUtil.getAccounts().remove(accountId) != null) {
            return true;
        }
        return false;
    }

}
