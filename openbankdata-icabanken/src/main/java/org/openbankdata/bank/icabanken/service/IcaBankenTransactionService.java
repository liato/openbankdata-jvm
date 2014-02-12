package org.openbankdata.bank.icabanken.service;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.openbankdata.bank.icabanken.client.IcaBankenBankClient;
import org.openbankdata.bank.icabanken.model.IcaBankenAccount;
import org.openbankdata.bank.icabanken.model.IcaBankenTransaction;
import org.openbankdata.bank.icabanken.model.response.LoginResponse;
import org.openbankdata.core.Account;
import org.openbankdata.core.Transaction;
import org.openbankdata.core.service.AbstractBankService;
import org.openbankdata.core.service.TransactionService;

/**
 *
 *
 */
public class IcaBankenTransactionService extends AbstractBankService implements TransactionService {

    /**
     *
     */
    public IcaBankenTransactionService(IcaBankenBankClient pBankClient) {
        super(pBankClient);

    }

    @Override
    public List<Transaction> getTransactions(Account pAccount) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        LoginResponse response = getBankClient().getLoginCache();

        for (IcaBankenAccount icaAccount : response.getAccountList().getAccounts()) {
            if (icaAccount.getAccountId().equals(pAccount.getId())) {
                transactions.addAll(mapTransactions(icaAccount));
            }
        }

        return transactions;
    }

    @Override
    public IcaBankenBankClient getBankClient() {
        return (IcaBankenBankClient) super.getBankClient();
    }

    private ArrayList<Transaction> mapTransactions(IcaBankenAccount pAccount) {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        for (IcaBankenTransaction icaTransaction : pAccount.getTransactions()) {
            Transaction transaction = new Transaction();
            transaction.setAmount(icaTransaction.getAmount());
            transaction.setCurrency(Currency.getInstance("SEK"));
            transaction.setDescription(icaTransaction.getMemoText());
            transaction.setTransactionDate(icaTransaction.getPostedDate());
            transactions.add(transaction);
        }
        return transactions;
    }
}
