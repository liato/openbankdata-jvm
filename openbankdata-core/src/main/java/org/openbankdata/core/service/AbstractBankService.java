package org.openbankdata.core.service;

import org.openbankdata.core.client.BankClient;

public abstract class AbstractBankService {

    private BankClient mBankClient;

    protected AbstractBankService(BankClient pBankClient) {
        mBankClient = pBankClient;
    }

    protected BankClient getBankClient() {
        return mBankClient;
    }
}
