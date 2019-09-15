package com.revolut.money.transfer.service;

import com.revolut.money.transfer.bean.Account;
import com.revolut.money.transfer.bean.Transaction;
import com.revolut.money.transfer.enums.ErrorCodes;
import com.revolut.money.transfer.response.GenericResponse;
import com.revolut.money.transfer.dao.TransactionDaoImpl;
import com.revolut.money.transfer.exception.CustomException;
import com.revolut.money.transfer.util.BankUtil;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

public class TransactionService {
    private static Logger logger = Logger.getLogger(TransactionService.class);

    TransactionDaoImpl transactionImpl = new TransactionDaoImpl();

    public Transaction transfer(String from, String to, BigDecimal amount) throws CustomException {

        Account fromAccount = BankUtil.getAccount(from);
        Account toAccount = BankUtil.getAccount(to);
        if (null != fromAccount && null != toAccount && fromAccount.equals(toAccount)) {
            logger.error("Both source and destined accounts are same..");
            throw new CustomException(ErrorCodes.INVALID_DATA, "Source and destination accounts can't be same");
        }
        if (fromAccount == null || toAccount == null) {
            logger.error("Source or destined accounts can't be null..");
            throw new CustomException(ErrorCodes.INVALID_DATA, fromAccount == null ? "Source account not found" : "Destination account not found");
        }
        return transactionImpl.transferAmount(fromAccount, toAccount, amount);
    }

    public Transaction deposit(String toAccount, BigDecimal amount) throws CustomException {
        if (null != toAccount && !toAccount.isEmpty() && null != BankUtil.getAccount(toAccount)) {
            return transactionImpl.deposit(toAccount, amount);
        } else {
            logger.error("Source account not found..");
            throw new CustomException(ErrorCodes.INVALID_DATA, "Source account not found");
        }
    }

    public Transaction withdraw(String fromAccount, BigDecimal amount) throws CustomException {
        GenericResponse genericResponse = new GenericResponse();
        if (null != fromAccount && !fromAccount.isEmpty() && null != BankUtil.getAccount(fromAccount)) {
            return transactionImpl.withDraw(fromAccount, amount);
        } else {
            logger.error("Destination account not found..");
            throw new CustomException(ErrorCodes.INVALID_DATA, "Destination account not found");

        }

    }

}
