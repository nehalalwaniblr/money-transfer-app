package com.revolut.money.transfer.util;

import com.revolut.money.transfer.service.TransactionService;

import java.math.BigDecimal;

public class TransferMoneyJob implements Runnable {
    TransactionService transactionService = null;
    String from;
    String to;
    BigDecimal amount;

    public TransferMoneyJob(String from, String to, BigDecimal amount) {
        this.transactionService = new TransactionService();
        this.from=from;
        this.to=to;
        this.amount=amount;
    }

    @Override
    public void run() {
//        System.out.println(Thread.currentThread()+" "+ from+" -->  "+to);
        transactionService.transfer(from, to, amount);
    }
}
