package com.revolut.money.transfer.util;

import com.revolut.money.transfer.bean.Account;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class BankUtil {
    public static Map<String, Account> getAccounts() {
        return accounts;
    }

    static Map<String, Account> accounts = new ConcurrentHashMap<String, Account>();

    public static Map<String, Account> createDefaultAccounts() {
        Account account = null;
        String accountId = null;
        for (int i = 1; i <= 10; i++) {
            accountId = UUID.randomUUID().toString();
//            account = new Account(accountId, "ABC", generateAmount(0, Integer.MAX_VALUE));
            account = new Account(i + "", "ABC", generateAmount(0, Integer.MAX_VALUE));

            accounts.putIfAbsent(i + "", account);

        }
        return accounts;
    }


    public static String generateAccountId(int min, int max) {
       return String.valueOf(getRandom().nextInt(max-min));
    }

    private static Random getRandom() {
        return ThreadLocalRandom.current();
    }

    public static BigDecimal generateAmount(int min, int max) {
        final BigDecimal amount = BigDecimal.valueOf(min + getRandom().nextInt(max - min), 2);
        return amount;
    }

    public static Account getAccount(String accountId) {
        if (accountId != null && !accountId.isEmpty())
            return accounts.get(accountId);
        else
            return null;
    }

    public static void setAccounts(Account account) {
       accounts.putIfAbsent(account.getAccountId(), account);
    }
}
