package org.openbankdata.bank.sveadirekt;

import org.openbankdata.bank.sveadirekt.client.SveaDirektBankClient;
import org.openbankdata.bank.sveadirekt.service.SveaDirektAccountService;
import org.openbankdata.bank.sveadirekt.service.SveaDirektTransactionService;
import org.openbankdata.core.bank.AbstractBankDataProvider;

public class SveaDirekt extends AbstractBankDataProvider {

    public SveaDirekt(String pUsername, String pPassword) {
        SveaDirektBankClient bankClient = new SveaDirektBankClient();
        bankClient.setCredentials(pUsername, pPassword);
        setBankClient(bankClient);

        SveaDirektTransactionService transactionService = new SveaDirektTransactionService(bankClient);
        setTransactionService(transactionService);

        SveaDirektAccountService accountService = new SveaDirektAccountService(bankClient, transactionService);
        setAccountService(accountService);
    }

}
