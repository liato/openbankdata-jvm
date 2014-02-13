package org.openbankdata.bank.americanexpress.client;

import org.openbankdata.bank.americanexpress.service.AmericanExpressAccountService;
import org.openbankdata.core.client.AbstractBankClient;
import org.openbankdata.core.client.BankClient;
import org.openbankdata.core.client.BankRequest;
import org.openbankdata.core.client.BankResponse;

import com.github.kevinsawicki.http.HttpRequest;

public class AmericanExpressBankClient extends AbstractBankClient {

    private static final String BASE_URI = "https://www.americanexpress.com/home/se/home_c.shtml";
    private static final String LOGIN_URL = "https://global.americanexpress.com/myca/logon/emea/action";

    private String mUsername;
    private String mPassword;

    @Override
    public BankClient setCredentials(String pUsername, String pPassword) {
        mUsername = pUsername;
        mPassword = pPassword;
        return this;
    }

    @Override
    protected boolean activateSession() {
        HttpRequest.get(BASE_URI).code();
        HttpRequest loginRequest = HttpRequest.post(LOGIN_URL)
                .form("request_type", "LogLogonHandler")
                .form("Face", "sv_SE")
                .form("DestPage", AmericanExpressAccountService.ACCOUNTS_URL)
                .form("Logon", "Continue...")
                .form("UserID", mUsername)
                .form("Password", mPassword);
        loginRequest.code();

        BankResponse loginResponse = new BankResponse(loginRequest);
        if (isLoginSuccessful(loginResponse)) {
            this.setActiveSession(true);
            getCache()
                    .put(new BankRequest(AmericanExpressAccountService.ACCOUNTS_URL),
                            loginResponse);
            return true;
        }
        return false;
    }

    private boolean isLoginSuccessful(BankResponse pRequest) {
        boolean ok = pRequest.code() == 200;
        int index = pRequest.getBody().indexOf("Your Personal Cards");
        return ok && index != -1;
    }
}
