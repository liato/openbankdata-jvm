package org.openbankdata.bank.americanexpress;

import org.openbankdata.bank.americanexpress.client.AmericanExpressBankClient;
import org.openbankdata.bank.americanexpress.service.AmericanExpressAccountService;
import org.openbankdata.bank.americanexpress.service.AmericanExpressTransactionService;
import org.openbankdata.core.bank.AbstractBankDataProvider;

public class AmericanExpress extends AbstractBankDataProvider {

    public AmericanExpress(String pUsername, String pPassword) {
        AmericanExpressBankClient bankClient = new AmericanExpressBankClient();
        bankClient.setCredentials(pUsername, pPassword);
        setBankClient(bankClient);

        setAccountService(new AmericanExpressAccountService(bankClient));
        setTransactionService(new AmericanExpressTransactionService(bankClient));
    }

}
