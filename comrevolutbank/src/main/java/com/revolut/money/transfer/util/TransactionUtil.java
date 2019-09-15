package com.revolut.money.transfer.util;

import com.revolut.money.transfer.bean.Transaction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionUtil {
    static Map<String, Transaction> transactions = new ConcurrentHashMap<>();

    public static Map<String, Transaction> getTransactions() {
        return transactions;
    }

    public static void setTransaction(Transaction transaction) {
        transactions.putIfAbsent(transaction.getTransactionId(), transaction);
    }
}
