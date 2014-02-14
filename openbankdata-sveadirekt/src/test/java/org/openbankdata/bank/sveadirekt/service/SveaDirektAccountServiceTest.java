package org.openbankdata.bank.sveadirekt.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbankdata.bank.sveadirekt.client.SveaDirektBankClient;
import org.openbankdata.core.Account;
import org.openbankdata.core.AccountType;
import org.openbankdata.core.client.BankRequest;
import org.openbankdata.core.client.BankResponse;
import org.openbankdata.core.client.Cache;
import org.openbankdata.core.client.DefaultBankResponse;
import org.openbankdata.test.util.TestUtils;

public class SveaDirektAccountServiceTest {

    private static final String ACCOUNT_LIST = "sveadirekt-list-accounts.htm";
    private static final String ACCOUNTS_URL = "https://services.sveadirekt.se/faces/WEB-INF/britney_jsp_s/home.jsp";

    private Cache mockedCache;

    @Mock
    private SveaDirektBankClient mockedBankClient;

    @Mock
    private SveaDirektTransactionService mockedTransactionService;

    @TestSubject
    private SveaDirektAccountService mAccountService;

    @Before
    public void setUp() {
        mockedCache = new Cache();
        mockedBankClient = EasyMock.createMock(SveaDirektBankClient.class);
        mockedTransactionService = EasyMock.createMock(SveaDirektTransactionService.class);
        mAccountService = new SveaDirektAccountService(mockedBankClient, mockedTransactionService);
    }

    @After
    public void tearDown() {
        EasyMock.reset(mockedBankClient, mockedTransactionService);
    }

    @Test
    public void testGetSingleSavingsAccounts() {
        // Given
        // A user that has a single savings account
        setUpMocks();

        // When
        // Fetching the account details from the website
        List<Account> actual = mAccountService.getAccounts();

        // Then
        EasyMock.verify(mockedBankClient, mockedTransactionService);

        assertEquals("The returned list should only contain the expected account", 1, actual.size());
        assertEquals("The accounts details should have been fetched.", getExpectedAccount(), actual.get(0));
        assertTrue("First account's transactions should have been added to the cache",
                mockedCache.exists(getTransactionsRequest()));
    }

    private Account getExpectedAccount() {
        Account account = new Account();
        account.setAccountNumber("1234567");
        account.setCurrency(Currency.getInstance("SEK"));
        account.setName("Sparkonto");
        BigDecimal balance = new BigDecimal(12345);
        account.setAvailable(balance);
        account.setBalance(balance);
        account.setAccountType(AccountType.SAVINGS);
        account.setId("_idcl:0:_id15");
        return account;
    }

    private BankRequest getAccountsRequest() {
        BankRequest bankRequest = new BankRequest(ACCOUNTS_URL);
        bankRequest.addParam("homeForm:balance", "Saldo");
        bankRequest.addParam("homeForm", "homeForm");
        return bankRequest;
    }

    private BankResponse getExpectedAccountsResponse() {
        DefaultBankResponse bankResponse = new DefaultBankResponse();
        bankResponse.setBody(TestUtils.getFileContentAsString(ACCOUNT_LIST));
        bankResponse.setCode(200);
        return bankResponse;
    }

    private BankRequest getTransactionsRequest() {
        return new BankRequest("http://foo.bar");
    }

    private void setUpMocks() {
        EasyMock.expect(mockedBankClient.post(getAccountsRequest())).andReturn(getExpectedAccountsResponse());
        EasyMock.expect(mockedBankClient.getCache()).andReturn(mockedCache);
        EasyMock.expect(mockedTransactionService.createTransactionRequest(getExpectedAccount())).andReturn(
                getTransactionsRequest());
        EasyMock.replay(mockedBankClient, mockedTransactionService);
    }
}
