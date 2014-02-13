package org.openbankdata.bank.americanexpress.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openbankdata.bank.americanexpress.client.AmericanExpressBankClient;
import org.openbankdata.core.Account;
import org.openbankdata.core.Transaction;
import org.openbankdata.core.client.BankRequest;
import org.openbankdata.core.client.BankResponse;
import org.openbankdata.core.service.AbstractBankService;
import org.openbankdata.core.service.TransactionService;

public class AmericanExpressTransactionService extends AbstractBankService
        implements TransactionService {

    private static final String TRANSACTIONS_URL =
            "https://global.americanexpress.com/myca/intl/estatement/emea/statement.do";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "d MMM yyyy", new Locale("sv-SE"));

    public AmericanExpressTransactionService(
            AmericanExpressBankClient pBankClient) {
        super(pBankClient);
    }

    @Override
    public List<Transaction> getTransactions(Account pAccount) {

        List<Transaction> transactions = new ArrayList<Transaction>();

        for (int i = 0; i < 4; i++) {
            BankResponse response = getBankClient().post(
                    generateUri(pAccount, i));
            transactions.addAll(parseTransactions(response));
        }

        return transactions;
    }

    private List<Transaction> parseTransactions(BankResponse pResponse) {
        List<Transaction> transactions = new ArrayList<Transaction>();

        Document doc = Jsoup.parse(pResponse.getBody());
        Element transactionTable = doc.getElementById("table-txnsCard0");
        if (transactionTable != null) {
            for (Element transaction : transactionTable.select("tbody tr")) {
                transactions.add(parseTransaction(transaction));
            }
        }
        return transactions;
    }

    private Transaction parseTransaction(Element elem) {
        Transaction transaction = new Transaction();
        String date = elem.child(0).html();
        try {
            transaction.setTransactionDate(DATE_FORMAT.parse(date));
        } catch (ParseException e) {
            // Ignore exception
        }
        transaction.setDescription(elem.child(1).ownText().trim());
        transaction.setAmount(parseAmount(elem.child(2), elem.child(3)));
        transaction.setCurrency(Currency.getInstance("SEK"));

        return transaction;
    }

    private BigDecimal parseAmount(Element pIncoming, Element pOutgoing) {
        String nonNumeric = "[^\\d]";
        String amount = pIncoming.ownText().replaceAll(nonNumeric, "").trim();
        if (amount.isEmpty()) {
            amount = "-"
                    + pOutgoing.ownText().replaceAll(nonNumeric, "").trim();
        }

        return new BigDecimal(amount).divide(new BigDecimal(100));
    }

    private BankRequest generateUri(Account pAccount, int pPage) {
        return new BankRequest(TRANSACTIONS_URL)
                .addParam("BPIndex", Integer.toString(pPage))
                .addParam("sorted_index", pAccount.getId())
                .addParam("Face", "sv_SE").addParam("request_type", "");
    }

}
