package org.openbankdata.core.client;

import com.github.kevinsawicki.http.HttpRequest;

public class BankResponse {

    private HttpRequest mResponse;

    private String mBody;

    public BankResponse(HttpRequest pResponse) {
        mResponse = pResponse;
    }

    public String getBody() {
        if (mResponse != null && mBody == null) {
            mBody = mResponse.body();
        }
        return mBody;
    }

    public int code() {
        return mResponse.code();
    }

}
