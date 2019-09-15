package com.revolut.money.transfer.dao;

import com.revolut.money.transfer.bean.Account;
import com.revolut.money.transfer.util.BankUtil;
import com.revolut.money.transfer.util.TransferMoneyJob;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertThat;

public class TransactionDaoTest {

    @Test
    public void testTransferForMultipleAccounts() {
        Map<String, Account> accounts=BankUtil.createDefaultAccounts();
        BigDecimal expected1=BankUtil.getAccount("1").getAccountBalance();
        BigDecimal expected5=BankUtil.getAccount("5").getAccountBalance();
        BigDecimal expected10=BankUtil.getAccount("10").getAccountBalance();

        for (int i = 1; i <= 9; i++) {
            Runnable r = new TransferMoneyJob(i + "", (i + 1) + "", BigDecimal.valueOf(1));
            Thread t = new Thread(r);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertThat(accounts.get("1").getAccountBalance(), CoreMatchers.is(expected1.subtract(BigDecimal.valueOf(1))));
        assertThat(accounts.get("5").getAccountBalance(), CoreMatchers.is(expected5));
        assertThat(accounts.get("10").getAccountBalance(), CoreMatchers.is(expected10.add(BigDecimal.valueOf(1))));


    }

//    @Test
//    public void testTransferForMultipleAccounts1() {
//        Account a=new Account("123","ac",BigDecimal.valueOf(100));
//        Account b=a;
//        Runnable r = new TransferMoneyJob("123","123", BigDecimal.valueOf(1));
//        Thread t = new Thread(r);
//        t.start();
//        try {
//            t.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
}
