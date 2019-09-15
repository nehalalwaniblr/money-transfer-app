package com.revolut.money.transfer.bean;

import com.google.gson.annotations.Expose;
import com.revolut.money.transfer.enums.ErrorCodes;
import com.revolut.money.transfer.exception.CustomException;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    @Expose
    String accountId;
    @Expose
    String userName;
    @Expose
    BigDecimal accountBalance;
    Lock accountLock;
    private static Logger logger = Logger.getLogger(Account.class);

    public Account(String accountId, String userName, BigDecimal accountBalance) {
        this.accountId = accountId;
        this.userName = userName;
        this.accountBalance = accountBalance;
        this.accountLock = new ReentrantLock();
    }

    public Lock getAccountLock() {
        return accountLock;
    }

    public void setAccountLock(Lock accountLock) {
        this.accountLock = accountLock;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public boolean debit(BigDecimal amount) {
        try {
            if (accountLock.tryLock(10L, TimeUnit.MILLISECONDS)) {
                if (this.accountBalance.compareTo(amount) > 0) {
                    this.accountBalance = this.accountBalance.subtract(amount);
                    return true;
                } else {
                    //throw exception
                    throw new CustomException(ErrorCodes.INSUFFICIENT_FUNDS, "Insufficient funds in source account");

                }
            }
        } catch (InterruptedException e) {
            logger.error("Not able to get lock on source account..",e);
            throw new CustomException(ErrorCodes.CONCURRENCY_ERROR, "Account not accessible, please try again");
        } finally {
            accountLock.unlock();
        }
        return false;
    }

    public boolean credit(BigDecimal amount) {
        try {
            if (accountLock.tryLock(10L, TimeUnit.MILLISECONDS)) {
                this.accountBalance = this.accountBalance.add(amount);
                return true;
            }
        } catch (Exception e) {
            logger.error("Not able to get lock on destination account..",e);
            throw new CustomException(ErrorCodes.CONCURRENCY_ERROR, "Account not accessible, please try again");
        } finally {
            accountLock.unlock();
        }
        return false;
    }

    @Override
    public String toString() {
        return this.accountId + " " + this.accountBalance + " " + this.userName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Account that = (Account) obj;
        if (this.accountId != that.accountId) return false;
        if (!this.userName.equals(that.userName)) return false;
        if (!this.accountBalance.equals(that.accountBalance)) return false;
        return true;
    }

}

