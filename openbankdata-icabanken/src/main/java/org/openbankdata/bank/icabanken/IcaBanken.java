package org.openbankdata.bank.icabanken;

import org.openbankdata.bank.icabanken.client.IcaBankenBankClient;
import org.openbankdata.bank.icabanken.service.IcaBankenAccountService;
import org.openbankdata.bank.icabanken.service.IcaBankenTransactionService;
import org.openbankdata.core.bank.AbstractBankDataProvider;

public class IcaBanken extends AbstractBankDataProvider {

    public IcaBanken(String pUsername, String pPassword) {
        IcaBankenBankClient bankClient = new IcaBankenBankClient();
        bankClient.setCredentials(pUsername, pPassword);
        setBankClient(bankClient);

        setTransactionService(new IcaBankenTransactionService(bankClient));
        setAccountService(new IcaBankenAccountService(bankClient));

    }

}
