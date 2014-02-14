package org.openbankdata.core.client;

import com.github.kevinsawicki.http.HttpRequest;

class HttpRequestBankResponse extends AbstractBankResponse {

    private final HttpRequest mResponse;

    public HttpRequestBankResponse(HttpRequest pResponse) {
        mResponse = pResponse;
    }

    @Override
    public String getBody() {
        if (super.getBody() == null && mResponse != null) {
            super.setBody(mResponse.body());
        }
        return super.getBody();
    }

    @Override
    public int code() {
        if (super.code() == 0 && mResponse != null) {
            super.setCode(mResponse.code());
        }
        return super.code();
    }
}
