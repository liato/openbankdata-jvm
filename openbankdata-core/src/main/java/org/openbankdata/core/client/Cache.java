package org.openbankdata.core.client;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Cache {

    private Map<BankRequest, BankResponse> mCache;

    public Cache() {
        mCache = new HashMap<BankRequest, BankResponse>();
    }

    public BankResponse get(BankRequest pBankRequest) {
        return mCache.get(pBankRequest);
    }

    public void put(BankRequest pBankRequest, BankResponse pBankResponse) {
        mCache.put(pBankRequest, pBankResponse);
    }

    public void clear() {
        mCache.clear();
    }

    public boolean exists(BankRequest pBankRequest) {
        return mCache.containsKey(pBankRequest);
    }
}
