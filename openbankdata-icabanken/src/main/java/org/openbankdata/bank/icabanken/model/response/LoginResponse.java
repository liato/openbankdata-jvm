package org.openbankdata.bank.icabanken.model.response;

import org.openbankdata.bank.icabanken.model.IcaBankenAccountList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {

    private String mABCustomerId;

    private IcaBankenAccountList mAccountList;

    private String mSessionId;

    @JsonProperty("ABCustomerId")
    public String getABCustomerId() {
        return mABCustomerId;
    }

    public void setABCustomerId(String pABCustomerId) {
        this.mABCustomerId = pABCustomerId;
    }

    @JsonProperty("AccountList")
    public IcaBankenAccountList getAccountList() {
        return mAccountList;
    }

    public void setAccountList(IcaBankenAccountList pAccountList) {
        this.mAccountList = pAccountList;
    }

    @JsonProperty("SessionId")
    public String getSessionId() {
        return mSessionId;
    }

    public void setSessionId(String pSessionId) {
        this.mSessionId = pSessionId;
    }
}
