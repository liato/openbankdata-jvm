package org.openbankdata.core.service;

import java.util.List;

import org.openbankdata.core.Account;
import org.openbankdata.core.Transaction;

/**
 *
 */
public interface TransactionService {

    List<Transaction> getTransactions(Account pAccount);

}
