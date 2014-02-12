package org.openbankdata.bank.icabanken.service;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.openbankdata.bank.icabanken.client.IcaBankenBankClient;
import org.openbankdata.bank.icabanken.model.IcaBankenAccount;
import org.openbankdata.bank.icabanken.model.response.LoginResponse;
import org.openbankdata.core.Account;
import org.openbankdata.core.service.AbstractBankService;
import org.openbankdata.core.service.AccountService;

public class IcaBankenAccountService extends AbstractBankService implements AccountService {

    public IcaBankenAccountService(IcaBankenBankClient pBankClient) {
        super(pBankClient);
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<Account>();
        LoginResponse response = getBankClient().getLoginCache();
        if (response != null) {
            accounts.addAll(mapAccounts(response.getAccountList().getAccounts()));
        }
        return accounts;
    }

    @Override
    public IcaBankenBankClient getBankClient() {
        return (IcaBankenBankClient) super.getBankClient();
    }

    private List<Account> mapAccounts(List<IcaBankenAccount> pIcaBankenAccounts) {
        List<Account> accounts = new ArrayList<Account>();
        for (IcaBankenAccount icaAccount : pIcaBankenAccounts) {
            Account account = new Account();
            account.setAccountNumber(icaAccount.getAccountNumber());
            account.setId(icaAccount.getAccountId());
            account.setCurrency(Currency.getInstance("SEK"));
            account.setName(icaAccount.getName());
            account.setAvailable(icaAccount.getAvailableAmount());
            account.setBalance(icaAccount.getCurrentAmount());
            accounts.add(account);
        }
        return accounts;
    }
}
