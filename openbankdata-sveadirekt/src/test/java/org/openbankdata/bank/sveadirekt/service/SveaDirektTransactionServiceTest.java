package org.openbankdata.bank.sveadirekt.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Currency;
import java.util.List;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.openbankdata.bank.sveadirekt.client.SveaDirektBankClient;
import org.openbankdata.core.Account;
import org.openbankdata.core.Transaction;
import org.openbankdata.core.client.BankRequest;
import org.openbankdata.core.client.BankResponse;
import org.openbankdata.core.client.DefaultBankResponse;
import org.openbankdata.test.util.TestUtils;

public class SveaDirektTransactionServiceTest {

    private SveaDirektBankClient mockedBankClient;

    private SveaDirektTransactionService mTransactionService;

    @Before
    public void setUp() {
        mockedBankClient = EasyMock.createMock(SveaDirektBankClient.class);
        mTransactionService = new SveaDirektTransactionService(mockedBankClient);
    }

    @Test
    public void testGetTransactions() throws ParseException {
        // Given
        // An account with three different transactions
        Account account = new Account();
        account.setId("_idcl:0:_id15");

        EasyMock.expect(mockedBankClient.post(getTransactionRequest())).andReturn(getTransactionResponse());
        EasyMock.replay(mockedBankClient);
        // When
        // Fetching the transactions from the website
        List<Transaction> actual = mTransactionService.getTransactions(account);

        // Then
        EasyMock.verify(mockedBankClient);
        assertEquals("The returned list should only contained the expected transactions", 2, actual.size());
        assertEquals("The incoming transaction should have been fetched", getExpectedIncomingTransaction(),
                actual.get(0));
        assertEquals("The outgoing transaction should have been fetched", getExpectedOutgoingTransaction(),
                actual.get(1));
    }

    private BankRequest getTransactionRequest() {
        return new BankRequest();
    }

    private BankResponse getTransactionResponse() {
        DefaultBankResponse response = new DefaultBankResponse();
        response.setBody(TestUtils.getFileContentAsString("sveadirekt-list-transactions.htm"));
        response.setCode(200);
        return response;
    }

    private Transaction getExpectedIncomingTransaction() throws ParseException {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(1234));
        transaction.setDescription("Insättning");
        transaction.setPending(false);
        transaction.setCurrency(Currency.getInstance("SEK"));
        transaction.setTransactionDate(new DateTime("2014-01-27"));
        return transaction;
    }

    private Transaction getExpectedOutgoingTransaction() throws ParseException {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(-123));
        transaction.setCurrency(Currency.getInstance("SEK"));
        transaction.setDescription("Innehållen kapitalskatt 2013");
        transaction.setPending(false);
        transaction.setTransactionDate(new DateTime("2013-12-31"));
        return transaction;

    }

}
