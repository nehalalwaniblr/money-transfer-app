package com.revolut.money.transfer.dao;

import com.revolut.money.transfer.bean.Account;
import com.revolut.money.transfer.bean.Status;
import com.revolut.money.transfer.util.BankUtil;

import java.util.UUID;

public class AccountDaoTest {
    public Status addAccount(Account account) {
        Status status = new Status();
        Account account1=null;
        String accountId=null;
        if (null != account && null == account.getAccountId()) {
            accountId = BankUtil.generateAccountId(0, Integer.MAX_VALUE);
        }else{
            accountId=account.getAccountId();
        }
        account1=new Account(accountId,account.getUserName(),account.getAccountBalance());
        BankUtil.setAccounts(account1);
        status.setSuccess(true);
        status.setDescription("Account " + account1.getAccountId() + " added successfully");
        return status;
    }
}
