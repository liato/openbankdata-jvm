package org.openbankdata.bank.sveadirekt.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openbankdata.bank.sveadirekt.client.SveaDirektBankClient;
import org.openbankdata.core.Account;
import org.openbankdata.core.AccountType;
import org.openbankdata.core.client.BankRequest;
import org.openbankdata.core.client.BankResponse;
import org.openbankdata.core.service.AbstractBankService;
import org.openbankdata.core.service.AccountService;

public class SveaDirektAccountService extends AbstractBankService implements
        AccountService {

    private SveaDirektTransactionService mTransactionService;

    private static final String ACCOUNTS_URL = "https://services.sveadirekt.se/faces/WEB-INF/britney_jsp_s/home.jsp";
    private static final Pattern PATTERN_ACCOUNT_ID = Pattern
            .compile("balanceForm:(.*?)'.*?accountsList:(.*?)'");

    public SveaDirektAccountService(SveaDirektBankClient pBankClient,
            SveaDirektTransactionService pTransactionService) {
        super(pBankClient);
        mTransactionService = pTransactionService;
    }

    @Override
    public List<Account> getAccounts() {
        BankRequest pRequest = new BankRequest().setUri(ACCOUNTS_URL)
                .setParams(getAccountsRequestParams());
        BankResponse response = getBankClient().post(pRequest);

        Document doc = Jsoup.parse(response.getBody());
        List<Account> accounts = parseAccounts(doc);

        if (!accounts.isEmpty()) {
            Account firstAccount = accounts.get(0);
            // Get account details for first account
            addAccountDetails(firstAccount, doc);

            getBankClient().getCache().put(mTransactionService.createTransactionRequest(firstAccount), response);
        }
        if (accounts.size() > 1) {
            for (Account account : accounts) {
                // Fetch remaining transaction pages and fetch details
                String transactionResponse = mTransactionService
                        .fetchTransactionData(account).getBody();
                addAccountDetails(account, Jsoup.parse(transactionResponse));
            }
        }

        return accounts;
    }

    private Map<String, String> getAccountsRequestParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("homeForm:balance", "Saldo");
        params.put("homeForm", "homeForm");
        return params;
    }

    private List<Account> parseAccounts(Document pDocument) {
        List<Account> accountList = new ArrayList<Account>();
        Element element = pDocument.getElementById("balanceForm:accountsList");
        Elements accounts = element.select("td a[href=#]");
        Matcher vMatcher;
        for (Element rawAccount : accounts) {
            Account account = new Account();
            account.setAccountNumber(rawAccount.text());
            vMatcher = PATTERN_ACCOUNT_ID.matcher(rawAccount.attr("onclick"));
            if (vMatcher.find()) {
                account.setId(vMatcher.group(1) + ":" + vMatcher.group(2));
            }
            accountList.add(account);
        }
        return accountList;
    }

    private Account addAccountDetails(Account pAccount, Document pDocument) {
        Elements vAccountDetails = pDocument
                .select("strong:contains(Saldo och transaktioner) ~ table")
                .first().select("tr td:last-child");
        String vAccountType = vAccountDetails.first().text();
        String vBalance = vAccountDetails.last().text();
        pAccount.setName(vAccountType);
        pAccount.setCurrency(Currency.getInstance("SEK"));
        pAccount.setBalance(new BigDecimal(vBalance.replaceAll("[^\\d]", "")));
        pAccount.setAccountType(mapAccountType(vAccountType));
        return pAccount;
    }

    private AccountType mapAccountType(String pAccountType) {
        AccountType vAccountType;
        if ("Sparkonto".equals(pAccountType)) {
            vAccountType = AccountType.SAVINGS;
        } else {
            vAccountType = AccountType.OTHER;
        }
        return vAccountType;
    }
}
