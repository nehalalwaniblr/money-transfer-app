package com.revolut.money.transfer.service;

import com.revolut.money.transfer.bean.Account;
import com.revolut.money.transfer.dao.AccountDaoImpl;
import com.revolut.money.transfer.util.BankUtil;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;

public class AccountServiceTest {
    static Map<String, Account> accounts = null;
    @InjectMocks
    AccountService accountService;

    @Mock
    AccountDaoImpl accountDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeClass
    public static void initializeData() {
        accounts = BankUtil.createDefaultAccounts();

    }

    @Test
    public void testAddAccount() {
        Account expected = new Account("101", "ABC", BigDecimal.valueOf(1000));
        Mockito.when(accountDao.addAccount(Matchers.any(Account.class))).thenReturn(expected);
        Account account = accountService.addAccount(expected);
        Assert.assertNotNull(account);
        Assert.assertThat(account.getAccountId(), CoreMatchers.is("101"));
        Assert.assertThat(account.getUserName(), CoreMatchers.is("ABC"));
        Assert.assertThat(account.getAccountBalance(), CoreMatchers.is(BigDecimal.valueOf(1000)));

    }

    @Test
    public void testDeleteAccount() {
        Account expected = new Account("1", "ABC", BigDecimal.valueOf(1000));
        Assert.assertTrue(accountService.deleteAccount("1"));
    }

    @AfterClass
    public static void cleanup() {
        accounts.clear();
    }
}
