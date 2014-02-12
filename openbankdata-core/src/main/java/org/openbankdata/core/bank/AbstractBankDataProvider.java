package org.openbankdata.core.bank;

import java.util.ArrayList;
import java.util.List;

import org.openbankdata.core.Account;
import org.openbankdata.core.Transaction;
import org.openbankdata.core.client.BankClient;
import org.openbankdata.core.service.AccountService;
import org.openbankdata.core.service.TransactionService;

/**
 *
 */
public abstract class AbstractBankDataProvider implements BankDataProvider {

    private TransactionService mTransactionService;

    private AccountService mAccountService;

    private BankClient mBankClient;

    private boolean mFetchedAccounts;

    private List<String> mFetchedTransactions;

    protected AbstractBankDataProvider() {
        mFetchedTransactions = new ArrayList<String>();
    }

    @Override
    public List<Account> getAccounts() {
        if (mFetchedAccounts) {
            cleanCache();
        }
        mFetchedAccounts = true;
        return mAccountService.getAccounts();
    }

    @Override
    public List<Transaction> getTransactions(Account pAccount) {
        if (mFetchedTransactions.contains(pAccount.getId())) {
            cleanCache();
        }
        mFetchedTransactions.add(pAccount.getId());
        return mTransactionService.getTransactions(pAccount);
    }

    private void cleanCache() {
        mBankClient.clearCache();
        mFetchedAccounts = false;
        mFetchedTransactions.clear();
    }

    protected void setTransactionService(TransactionService pTransactionService) {
        mTransactionService = pTransactionService;
    }

    protected void setAccountService(AccountService pAccountService) {
        mAccountService = pAccountService;
    }

    protected void setBankClient(BankClient pBankClient) {
        mBankClient = pBankClient;
    }
}
