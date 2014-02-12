package org.openbankdata.core.client;

import java.net.CookieHandler;
import java.net.CookieManager;

import com.github.kevinsawicki.http.HttpRequest;

/**
 *
 */
public abstract class AbstractBankClient implements BankClient {

    protected boolean mActiveSession;

    protected static final String USER_AGENT = "OpenBankDataJava/1.0";

    private Cache mCache;

    /**
     *
     */
    protected AbstractBankClient() {
        CookieHandler.setDefault(new CookieManager());
        mCache = new Cache();

    }

    /**
     * @param uri
     * @param method
     * @return
     */
    protected HttpRequest createConnection(String uri, String method) {
        HttpRequest vHttpRequest = new HttpRequest(uri, method);
        vHttpRequest.userAgent(USER_AGENT);
        return configureConnection(vHttpRequest);
    }

    public BankResponse post(BankRequest pBankRequest) {
        if (!isSessionActive()) {
            getCache().clear();
            setActiveSession(activateSession());
            if (!isSessionActive()) {
                throw new RuntimeException("Not logged in");
            }
        }
        if (getCache().exists(pBankRequest)) {
            return getCache().get(pBankRequest);
        }
        HttpRequest request = createConnection(pBankRequest.getUri(), HttpRequest.METHOD_POST);
        request.form(pBankRequest.getParams());
        request.code();
        BankResponse response = new BankResponse(request);
        getCache().put(pBankRequest, response);
        return response;
    }

    public BankResponse get(BankRequest pBankRequest) {
        if (!isSessionActive()) {
            setActiveSession(activateSession());
            if (!isSessionActive()) {
                throw new RuntimeException("Not logged in");
            }
        }
        if (getCache().exists(pBankRequest)) {
            return getCache().get(pBankRequest);
        }
        HttpRequest request = createConnection(pBankRequest.generateUri(), HttpRequest.METHOD_GET);
        request.code();
        BankResponse response = new BankResponse(request);
        getCache().put(pBankRequest, response);
        return response;
    }

    public Cache getCache() {
        return mCache;
    }

    public void clearCache() {
        mCache.clear();
    }

    protected HttpRequest configureConnection(HttpRequest pHttpRequest) {
        return pHttpRequest;
    }

    protected void setActiveSession(boolean pActiveSession) {
        mActiveSession = pActiveSession;
    }

    protected boolean isSessionActive() {
        return mActiveSession;
    }

    protected abstract boolean activateSession();
}
