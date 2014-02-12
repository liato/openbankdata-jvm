package org.openbankdata.core.client;

public interface BankClient {

    BankClient setCredentials(final String pUsername, final String pPassword);

    BankResponse post(BankRequest pBankRequest);

    BankResponse get(BankRequest pBankRequest);

    Cache getCache();

    void clearCache();
}
