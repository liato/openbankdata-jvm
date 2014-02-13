package org.openbankdata.bank.sveadirekt.client;

import org.openbankdata.core.client.AbstractBankClient;
import org.openbankdata.core.client.BankClient;

import com.github.kevinsawicki.http.HttpRequest;

public class SveaDirektBankClient extends AbstractBankClient {

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
        HttpRequest.get("https://services.sveadirekt.se/mypages/sv/").code();
        HttpRequest loginRequest = HttpRequest
                .post("https://services.sveadirekt.se/mypages/sv/j_security_check");
        loginRequest.form("j_username", mUsername);
        loginRequest.form("j_password", mPassword);
        loginRequest.code();
        return isLoginSuccessful(loginRequest);
    }

    private boolean isLoginSuccessful(HttpRequest pRequest) {
        boolean ok = pRequest.ok();
        String body = pRequest.body();
        int index = body.indexOf("SveaDirekt - Mina Sidor");
        return ok && index != -1;
    }
}
