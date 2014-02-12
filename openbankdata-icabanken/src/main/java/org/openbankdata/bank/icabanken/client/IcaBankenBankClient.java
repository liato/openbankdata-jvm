package org.openbankdata.bank.icabanken.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.openbankdata.bank.icabanken.model.response.LoginResponse;
import org.openbankdata.core.client.AbstractBankClient;
import org.openbankdata.core.client.BankClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

public class IcaBankenBankClient extends AbstractBankClient {

    private static final String API_KEY = "9A75DD46-82EE-449B-9F78-F11F636BB7BE";
    private static final String API_VERSION = "1.0";
    private static final String API_URL = "https://appserver.icabanken.se";

    private String mUsername;
    private String mPassword;

    LoginResponse mLoginCache;

    @Override
    protected HttpRequest configureConnection(HttpRequest pRequest) {
        return pRequest.header("ApiVersion", API_VERSION)
                .header("ApiKey", API_KEY)
                .header("Accept", "application/json");
    }

    @Override
    public BankClient setCredentials(String pUsername, String pPassword) {
        mUsername = pUsername;
        mPassword = pPassword;
        return this;
    }

    @Override
    public void clearCache() {
        super.clearCache();
        mLoginCache = null;
    }

    @Override
    protected boolean activateSession() {
        String loginUri = API_URL + "/login?customerId=" + mUsername + "&password=" + mPassword;
        HttpRequest request = configureConnection(HttpRequest.get(loginUri));

        if (request.ok()) {
            ObjectMapper vObjectMapper = new ObjectMapper();
            vObjectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("sv", "SE")));
            try {
                mLoginCache = vObjectMapper.readValue(request.body(), LoginResponse.class);
            } catch (JsonParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (HttpRequestException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mLoginCache != null;
    }

    public LoginResponse getLoginCache() {
        if (mLoginCache == null) {
            if (!activateSession()) {
                throw new RuntimeException("Not logged in");
            }
        }
        return mLoginCache;
    }
}
