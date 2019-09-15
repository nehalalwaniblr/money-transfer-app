package com.revolut.money.transfer;

import com.google.gson.Gson;
import com.revolut.money.transfer.controllerServlet.AccountServlet;
import com.revolut.money.transfer.controllerServlet.TransactionServlet;
import com.revolut.money.transfer.util.BankUtil;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import java.io.File;

public class MainApp {

    private static Gson gson = new Gson();
    private static Logger logger = Logger.getLogger(MainApp.class);

    public static void main(String[] args) throws LifecycleException {
        logger.info("Creating some default accounts..");
        BankUtil.createDefaultAccounts();
        logger.info("Default accounts created..");

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        tomcat.setPort(8080);

        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();
        Context context = tomcat.addContext(contextPath, docBase);

        HttpServlet transactionServlet = new TransactionServlet();
        tomcat.addServlet(contextPath, "TransactionServlet", transactionServlet);
        context.addServletMappingDecoded("/transact/*", "TransactionServlet");

        HttpServlet accountServlet = new AccountServlet();
        tomcat.addServlet(contextPath, "AccountServlet", accountServlet);
        context.addServletMappingDecoded("/account/*", "AccountServlet");
        tomcat.start();
        tomcat.getServer().await();
    }


}
