package com.revolut.money.transfer.controllerServlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revolut.money.transfer.bean.Account;
import com.revolut.money.transfer.enums.ErrorCodes;
import com.revolut.money.transfer.exception.CustomException;
import com.revolut.money.transfer.response.GenericResponse;
import com.revolut.money.transfer.service.AccountService;
import com.revolut.money.transfer.util.BankUtil;
import com.revolut.money.transfer.util.ResponseUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccountServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(AccountServlet.class);


    /**
     * Get account by account id
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = null;
        GenericResponse genericResponse = new GenericResponse();
        try {
            String accountId = req.getPathInfo() != null ? (req.getPathInfo().split("/").length > 1 ? req.getPathInfo().split("/")[1] : null) : null;
            Account account = BankUtil.getAccount(accountId);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            if (account != null) {
                genericResponse.setSuccess(true);
                genericResponse.setData((account));
            } else {
                genericResponse.setSuccess(false);
                genericResponse.setDescription("Account does not exists");
                genericResponse.setErrorCodes(ErrorCodes.NOT_FOUND);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            resp.getOutputStream().print(gson.toJson(genericResponse));
            resp.getOutputStream().flush();
        } catch (CustomException ex) {
            logger.error("Account not found..");
            genericResponse = ResponseUtil.createExceptionResponse(ex.getDescription(), ex.getErrorCode());
            resp.getOutputStream().print(gson.toJson(genericResponse));
            resp.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("Error occurred during json conversion");
            genericResponse = ResponseUtil.createExceptionResponse(e.getMessage(), null);
            resp.getOutputStream().print(gson.toJson(genericResponse));
            resp.getOutputStream().flush();
        }

    }

    /**
     * Add an account
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        GenericResponse genericResponse = new GenericResponse();
        try {
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = req.getReader().readLine()) != null) {
                sb.append(s);
            }

            Account account = (Account) gson.fromJson(sb.toString(), Account.class);
            Account account1 = new AccountService().addAccount(account);
            if (account1 != null) {
                genericResponse.setSuccess(true);
                genericResponse.setData((account1));
                genericResponse.setDescription("Account " + account1.getAccountId() + " added successfully");
                resp.getOutputStream().print(gson.toJson(genericResponse));
                resp.getOutputStream().flush();
            }

        } catch (CustomException ex) {
            logger.error("Account not created..");
            genericResponse = ResponseUtil.createExceptionResponse(ex.getDescription(), ex.getErrorCode());
            resp.getOutputStream().print(gson.toJson(genericResponse));
            resp.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("Error occurred during json conversion");
            genericResponse = ResponseUtil.createExceptionResponse(e.getMessage(), null);
            resp.getOutputStream().print(gson.toJson(genericResponse));
            resp.getOutputStream().flush();
        }

    }


    /**
     * Delete an account
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String accountId = req.getPathInfo() != null ? req.getPathInfo().split("/")[1] : null;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        GenericResponse genericResponse = null;
        try {
            boolean isSuccess = new AccountService().deleteAccount(accountId);
            genericResponse = new GenericResponse();
            if (isSuccess) {
                genericResponse.setSuccess(true);
                genericResponse.setDescription("Account " + accountId + " successfully deleted");
            } else {
                genericResponse.setSuccess(false);
                genericResponse.setDescription("Account doesn't exist");
            }
            resp.getOutputStream().print(gson.toJson(genericResponse));
            resp.getOutputStream().flush();
        } catch (CustomException ex) {
            logger.error("Account not found..");
            genericResponse = ResponseUtil.createExceptionResponse(ex.getDescription(), ex.getErrorCode());
            resp.getOutputStream().print(gson.toJson(genericResponse));
            resp.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("Error occurred during json conversion");
            genericResponse = ResponseUtil.createExceptionResponse(e.getMessage(), null);
            resp.getOutputStream().print(gson.toJson(genericResponse));
            resp.getOutputStream().flush();
        }
    }
}
