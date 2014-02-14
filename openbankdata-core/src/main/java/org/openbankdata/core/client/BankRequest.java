package org.openbankdata.core.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openbankdata.core.utils.ObjectUtils;

/**
 *
 */
public class BankRequest {

    private Map<String, String> mParams;

    private String mUri;

    public BankRequest() {
    }

    public BankRequest(String pUri) {
        setUri(pUri);
    }

    public String getUri() {
        return mUri;
    }

    public BankRequest setUri(String pUri) {
        mUri = pUri;
        return this;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public BankRequest addParam(String pKey, String pValue) {
        if (mParams == null) {
            mParams = new HashMap<String, String>();
        }
        mParams.put(pKey, pValue);
        return this;
    }

    public BankRequest setParams(Map<String, String> pParams) {
        mParams = pParams;
        return this;
    }

    public String generateUri() {
        if (mUri == null) {
            return "";
        }
        if (mParams == null || mParams.isEmpty()) {
            return mUri;
        }
        Map<String, String> params = new HashMap<String, String>();

        StringBuilder uriBuilder = new StringBuilder();

        if (mUri.contains("?")) {
            String paramString = mUri.substring(mUri.indexOf('?') + 1);
            String[] keyValues = paramString.split("&");

            for (String parameter : keyValues) {
                String[] param = parameter.split("=");
                if (!mParams.containsKey(param[0])) {
                    params.put(param[0], param[1]);
                }
            }
            uriBuilder.append(mUri.substring(0, mUri.indexOf('?') + 1));

            params.putAll(mParams);
            for (Entry<String, String> entry : params.entrySet()) {
                uriBuilder.append(entry.getKey()).append("=")
                .append(entry.getValue()).append("&");
            }
        }
        String uri = uriBuilder.toString();
        return uri.endsWith("&") ? uri.substring(0, uri.length() - 1) : uri;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BankRequest other = (BankRequest) obj;
        return ObjectUtils.equal(this.generateUri(), other.generateUri());

    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.generateUri());
    }

}
