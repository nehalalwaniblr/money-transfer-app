package com.revolut.money.transfer.dao;


import com.revolut.money.transfer.bean.Account;
import com.revolut.money.transfer.util.BankUtil;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class AccountDaoTest {
    private static Logger log = Logger.getLogger(AccountDaoTest.class);
    @InjectMocks
    AccountDaoImpl accountDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddAccount() {
        Account expected = new Account("101", "ABC", BigDecimal.valueOf(1000));
        accountDao.addAccount(expected);
        Account actual = BankUtil.getAccount("101");
        Assert.assertEquals(expected, actual);
    }

}
