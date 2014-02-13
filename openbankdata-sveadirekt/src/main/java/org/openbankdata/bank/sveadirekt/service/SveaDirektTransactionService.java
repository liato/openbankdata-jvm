package org.openbankdata.bank.sveadirekt.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openbankdata.bank.sveadirekt.client.SveaDirektBankClient;
import org.openbankdata.core.Account;
import org.openbankdata.core.Transaction;
import org.openbankdata.core.client.BankRequest;
import org.openbankdata.core.client.BankResponse;
import org.openbankdata.core.service.AbstractBankService;
import org.openbankdata.core.service.TransactionService;

public class SveaDirektTransactionService extends AbstractBankService implements TransactionService {

    private static final String TRANSACTIONS_URL =
            "https://services.sveadirekt.se/faces/WEB-INF/britney_jsp_s/balance.jsp";

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyy-MM-dd");

    public SveaDirektTransactionService(SveaDirektBankClient pBankClient) {
        super(pBankClient);

    }

    @Override
    public List<Transaction> getTransactions(Account pAccount) {
        List<Transaction> vTransactions = new ArrayList<Transaction>();
        BankResponse response = fetchTransactionData(pAccount);

        vTransactions.addAll(parseTransactions(response.getBody()));
        return vTransactions;
    }

    BankResponse fetchTransactionData(Account pAccount) {

        return getBankClient().post(createTransactionRequest(pAccount));
    }

    private String createAccountFormInput(Account pAccount) {
        String vAccountId = pAccount.getId();
        return "balanceForm:accountsList:" + vAccountId.substring(vAccountId.indexOf(':') + 1);
    }

    private String createAccountFormName(Account pAccount) {
        String vAccountId = pAccount.getId();
        String vId = vAccountId.substring(0, vAccountId.indexOf(':'));
        return "balanceForm:" + vId;
    }

    private List<Transaction> parseTransactions(String pResponse) {
        List<Transaction> vTransactions = new ArrayList<Transaction>();
        Document doc = Jsoup.parse(pResponse);
        Elements vTransactionElements = doc.getElementById("balanceForm:transactionPostList").select("tbody tr");

        for (Element element : vTransactionElements) {
            Transaction vTransaction = new Transaction();
            Elements vTransactionElement = element.select("td");

            vTransaction.setAmount(new BigDecimal(vTransactionElement.get(1).text().replaceAll("[^\\d-]", "")));
            vTransaction.setDescription(vTransactionElement.get(2).text());
            if (!vTransaction.hasDescription()) {
                vTransaction.setDescription(vTransaction.getAmount().compareTo(BigDecimal.ZERO) > 0 ? "Insättning"
                        : "Uttag");
            }
            vTransaction.setCurrency(Currency.getInstance("SEK"));

            try {
                vTransaction.setTransactionDate(DATE_FORMATTER.parse(vTransactionElement.first().text()));
            } catch (ParseException e) {
                // Ignore Exception
            }
            vTransactions.add(vTransaction);
        }
        return vTransactions;
    }

    BankRequest createTransactionRequest(Account pAccount) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("balanceForm", "balanceForm");
        params.put(createAccountFormName(pAccount), createAccountFormInput(pAccount));
        return new BankRequest().setUri(TRANSACTIONS_URL).setParams(params);
    }
}
