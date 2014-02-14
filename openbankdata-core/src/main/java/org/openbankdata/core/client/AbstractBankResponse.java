package org.openbankdata.core.client;

import org.openbankdata.core.utils.ObjectUtils;

abstract class AbstractBankResponse implements BankResponse {

    private int mCode;

    private String mBody;

    @Override
    public int code() {
        return mCode;
    }

    @Override
    public String getBody() {
        return mBody;
    }

    void setCode(int pCode) {

    }

    void setBody(String pBody) {
        mBody = pBody;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractBankResponse other = (AbstractBankResponse) obj;
        return ObjectUtils.equal(this.mCode, other.mCode) && ObjectUtils.equal(this.mBody, other.mBody);

    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.mCode, this.mBody);
    }
}
