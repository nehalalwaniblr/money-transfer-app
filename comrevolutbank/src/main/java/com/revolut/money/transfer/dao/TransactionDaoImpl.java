package com.revolut.money.transfer.dao;

import com.revolut.money.transfer.bean.Account;
import com.revolut.money.transfer.bean.Transaction;
import com.revolut.money.transfer.enums.ErrorCodes;
import com.revolut.money.transfer.enums.TransactionType;
import com.revolut.money.transfer.exception.CustomException;
import com.revolut.money.transfer.util.BankUtil;
import com.revolut.money.transfer.util.TransactionUtil;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class TransactionDaoImpl {
    private static Logger logger = Logger.getLogger(TransactionDaoImpl.class);

    private Account fromAccount;
    private Account toAccount;


    public Transaction transferAmount(Account fromAccount, Account toAccount, BigDecimal amount) {
        Transaction transaction = null;
        Lock lockFrom = fromAccount.getAccountLock();
        try {
            if (lockFrom.tryLock(10L, TimeUnit.MILLISECONDS)) {
                Lock toLock = toAccount.getAccountLock();
                try {
                    if (toLock.tryLock(10L, TimeUnit.MILLISECONDS)) {
                        if (fromAccount.debit(amount)) {
                            if (toAccount.credit(amount)) {
                                logger.info("transfer done..");
                                transaction = addNewTransaction(fromAccount.getAccountId(), toAccount.getAccountId(), amount, TransactionType.TRANSFER);
                                return transaction;
                            }
                        } else {
                            //Exception: Insufficient funds
                            logger.error("Insufficient funds..");
                            throw new CustomException(ErrorCodes.INSUFFICIENT_FUNDS, "Insufficient funds in source account");
                        }
                    } else {
                        //Exception-concurrency
                        logger.error("Couldn't acquire a lock on destined account ");
                        throw new CustomException(ErrorCodes.CONCURRENCY_ERROR, "Account not accessible, please try again");

                    }
                } finally {
                    toLock.unlock();
                }


            } else {
                //Exception-concurrency
                logger.error("Couldn't acquire a lock on source account ");
                throw new CustomException(ErrorCodes.CONCURRENCY_ERROR, "Account not accessible, please try again");

            }
        } catch (InterruptedException e) {
            throw new CustomException(ErrorCodes.CONCURRENCY_ERROR, e.getMessage());
        } finally {
            lockFrom.unlock();
        }

        return transaction;
    }

    private Transaction addNewTransaction(String fromAccount, String toAccount, BigDecimal amount, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        TransactionUtil.setTransaction(transaction);
        return transaction;
    }

    public Transaction withDraw(String from, BigDecimal amount) {
        Account fromAccount = BankUtil.getAccount(from);
        Transaction transaction = new Transaction();
        Lock lockFrom = fromAccount.getAccountLock();
        try {
            if (lockFrom.tryLock(10L, TimeUnit.MILLISECONDS)) {
                if (fromAccount.debit(amount)) {
                    logger.info("withdraw done..");
                    transaction = addNewTransaction(from, "", amount, TransactionType.WITHDRAW);
                    return transaction;
                }
            } else {
                logger.error("Couldn't acquire a lock on source account ");
                throw new CustomException(ErrorCodes.CONCURRENCY_ERROR, "Account not accessible, please try again");
            }

        } catch (InterruptedException e) {
            throw new CustomException(ErrorCodes.CONCURRENCY_ERROR, e.getMessage());
        } finally {
            lockFrom.unlock();
        }
        return transaction;
    }

    public Transaction deposit(String to, BigDecimal amount) {
        Account toAccount = BankUtil.getAccount(to);
        Transaction transaction = null;
        Lock lockTo = toAccount.getAccountLock();
        try {
            if (lockTo.tryLock(10L, TimeUnit.MILLISECONDS)) {
                if (toAccount.credit(amount)) {
                    logger.info("deposit done..");
                    transaction = addNewTransaction("", to, amount, TransactionType.DEPOSIT);
                    return transaction;
                }
            } else {
                logger.error("Couldn't acquire a lock on destined account ");
                throw new CustomException(ErrorCodes.CONCURRENCY_ERROR, "Account not accessible, please try again");
            }

        } catch (Exception e) {
            throw new CustomException(ErrorCodes.CONCURRENCY_ERROR, e.getMessage());
        } finally {
            lockTo.unlock();
        }
        return transaction;
    }
}
