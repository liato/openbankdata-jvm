package org.openbankdata.bank.americanexpress.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openbankdata.bank.americanexpress.client.AmericanExpressBankClient;
import org.openbankdata.core.Account;
import org.openbankdata.core.AccountType;
import org.openbankdata.core.client.BankRequest;
import org.openbankdata.core.client.BankResponse;
import org.openbankdata.core.service.AbstractBankService;
import org.openbankdata.core.service.AccountService;

public class AmericanExpressAccountService extends AbstractBankService implements AccountService {

    public static final String ACCOUNTS_URL =
            "https://global.americanexpress.com/myca/intl/acctsumm/emea/accountSummary.do?request_type=&Face=sv_SE";

    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("sorted_index=([0-9]*)");

    public AmericanExpressAccountService(AmericanExpressBankClient pBankClient) {
        super(pBankClient);
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> vAccounts = new ArrayList<Account>();
        BankResponse vAccountsRequest = getBankClient().get(new BankRequest(ACCOUNTS_URL));
        String response = vAccountsRequest.getBody();
        Document doc = Jsoup.parse(response);
        Element el = doc.getElementById("summaryImageHeaderRow");
        Account account = new Account();
        account.setName(el.getElementById("headerSectionLeft").select("span.cardTitle a").text());
        account.setAccountType(AccountType.LIABILITY);
        Element link = el.select("div.summaryTitles a").first();
        account.setAccountNumber(link.text());
        account.setCurrency(Currency.getInstance("SEK"));
        account.setId(parseAccountId(link));

        String balance = doc.getElementById("colOSBalance").select("div.summaryValues").text();
        account.setBalance(new BigDecimal(balance.replaceAll("[^\\d-]", "")).divide(new BigDecimal(100)));
        vAccounts.add(account);
        return vAccounts;
    }

    private String parseAccountId(Element elem) {
        Matcher matcher = ACCOUNT_PATTERN.matcher(elem.toString());
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
