package com.revolut.money.transfer.controllerServlet;


import com.google.gson.Gson;
import com.revolut.money.transfer.enums.ErrorCodes;
import com.revolut.money.transfer.response.GenericResponse;
import com.revolut.money.transfer.bean.Transaction;
import com.revolut.money.transfer.exception.CustomException;
import com.revolut.money.transfer.service.TransactionService;
import com.revolut.money.transfer.util.ResponseUtil;
import com.sun.tools.javac.util.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TransactionServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(TransactionServlet.class);

    /**
     * Deposit, Withdraw or transfer in accounts
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        GenericResponse genericResponse = new GenericResponse();
        Gson gson = new Gson();

        try {
            Transaction transaction = getTransactionRequest(req);
            Transaction transactionResponse=null;
            if (StringUtils.toUpperCase(req.getRequestURI()).contains("TRANSFER")) {
                logger.info("Transfer request..");
                transactionResponse = new TransactionService().transfer(transaction.getFromAccount(), transaction.getToAccount(), transaction.getAmount());
            } else if (StringUtils.toUpperCase(req.getRequestURI()).contains("DEPOSIT")) {
                logger.info("Deposit request..");
                transactionResponse = new TransactionService().deposit(transaction.getToAccount(), transaction.getAmount());
            } else if (StringUtils.toUpperCase(req.getRequestURI()).contains("WITHDRAW")) {
                logger.info("Withdrawal request..");
                transactionResponse = new TransactionService().withdraw(transaction.getFromAccount(), transaction.getAmount());
            }else{
                throw new CustomException(ErrorCodes.NOT_FOUND,"Invalid url...");
            }
            genericResponse.setSuccess(true);
            genericResponse.setData((transactionResponse));
            resp.getOutputStream().print(gson.toJson(genericResponse));
            resp.getOutputStream().flush();
        }
        catch (CustomException ex) {
            logger.error("Transaction failed..");
            genericResponse = ResponseUtil.createExceptionResponse(ex.getDescription(), ex.getErrorCode());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getOutputStream().print(gson.toJson(genericResponse));
            resp.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("Error occurred during json conversion");
            genericResponse = ResponseUtil.createExceptionResponse(e.getMessage(), null);
            resp.getOutputStream().print(gson.toJson(genericResponse));
            resp.getOutputStream().flush();
        }
    }

    private Transaction getTransactionRequest(HttpServletRequest req) throws IOException {
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = req.getReader().readLine()) != null) {
            sb.append(s);
        }
        Transaction transaction = (Transaction) gson.fromJson(sb.toString(), Transaction.class);
        return transaction;

    }
}
