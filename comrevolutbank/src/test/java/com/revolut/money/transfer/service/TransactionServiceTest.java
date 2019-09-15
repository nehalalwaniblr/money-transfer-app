package com.revolut.money.transfer.service;

import com.revolut.money.transfer.bean.Account;
import com.revolut.money.transfer.bean.Transaction;
import com.revolut.money.transfer.enums.TransactionType;
import com.revolut.money.transfer.exception.CustomException;
import com.revolut.money.transfer.dao.TransactionDaoImpl;
import com.revolut.money.transfer.util.BankUtil;
import org.hamcrest.CoreMatchers;
import org.junit.After;
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
import java.util.UUID;


public class TransactionServiceTest {
    static Map<String, Account> accounts = null;
    static Transaction mockData = null;
    @InjectMocks
    TransactionService transactionService;

    @Mock
    TransactionDaoImpl transactionDao;

    @BeforeClass
    public static void initializeData() {
        accounts = BankUtil.createDefaultAccounts();

    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockData = mockTransactionData("1", "2", BigDecimal.valueOf(1000),null);
    }


    @Test
    public void testTransferService() {
        Mockito.when(transactionDao.transferAmount(Matchers.any(Account.class), Matchers.any(Account.class), Matchers.any(BigDecimal.class))).thenReturn(mockData);
        Transaction transaction = transactionService.transfer("1", "2", BigDecimal.valueOf(1000));
        Assert.assertNotNull(transaction);
        Assert.assertThat(transaction.getFromAccount(), CoreMatchers.is("1"));
        Assert.assertThat(transaction.getToAccount(), CoreMatchers.is("2"));
        Assert.assertThat(transaction.getAmount(), CoreMatchers.is(BigDecimal.valueOf(1000)));

    }

    @Test
    public void testTransferServiceFailure() {
        Mockito.when(transactionDao.transferAmount(Matchers.any(Account.class), Matchers.any(Account.class), Matchers.any(BigDecimal.class))).thenReturn(null);
        Transaction transaction = transactionService.transfer("1", "2", BigDecimal.valueOf(1000));
        Assert.assertNull(transaction);
    }

    @Test(expected = CustomException.class)
    public void testTransferServiceNonExistingAccount() {
        Mockito.when(transactionDao.transferAmount(Matchers.any(Account.class), Matchers.any(Account.class), Matchers.any(BigDecimal.class))).thenReturn(null);
        transactionService.transfer("134", "2", BigDecimal.valueOf(1000));
            }

    @Test
    public void testWithdrawService() {
        Mockito.when(transactionDao.withDraw(Matchers.any(String.class), Matchers.any(BigDecimal.class))).thenReturn(mockData);
        Transaction transaction =  transactionService.withdraw("1", BigDecimal.valueOf(1000));
        Assert.assertThat(transaction.getFromAccount(), CoreMatchers.is("1"));
        Assert.assertThat(transaction.getAmount(), CoreMatchers.is(BigDecimal.valueOf(1000)));
    }

    @Test(expected = CustomException.class)
    public void testWithdrawServiceFailure() {
        Mockito.when(transactionDao.withDraw(Matchers.any(String.class), Matchers.any(BigDecimal.class))).thenReturn(null);
        transactionService.withdraw("112", BigDecimal.valueOf(1000));
    }

    @Test
    public void testDepositService() {
        Mockito.when(transactionDao.deposit(Matchers.any(String.class), Matchers.any(BigDecimal.class))).thenReturn(mockData);
        Transaction transaction =transactionService.deposit("2", BigDecimal.valueOf(1000));
        Assert.assertThat(transaction.getToAccount(), CoreMatchers.is("2"));
        Assert.assertThat(transaction.getAmount(), CoreMatchers.is(BigDecimal.valueOf(1000)));

    }

    @Test(expected = CustomException.class)
    public void testDepositServiceFailure() {
        Mockito.when(transactionDao.deposit(Matchers.any(String.class), Matchers.any(BigDecimal.class))).thenReturn(null);
        transactionService.deposit("112", BigDecimal.valueOf(1000));

    }

    private Transaction mockTransactionData(String fromAccount, String toAccount, BigDecimal amount, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        return transaction;
    }

    @After
    public  void clean() {
        mockData = null;
    }

    @AfterClass
    public static void cleanup() {
        accounts.clear();
    }

}
