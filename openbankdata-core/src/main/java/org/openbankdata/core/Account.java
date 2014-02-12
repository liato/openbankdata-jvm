package org.openbankdata.core;

import java.math.BigDecimal;
import java.util.Currency;

/**
 *
 */
public class Account {

    private String mId;
    private String mAccountNumber;
    private BigDecimal mBalance;
    private BigDecimal mAvailable;
    private String mName;
    private AccountType mAccountType;
    private Currency mCurrency;

    public String getId() {
        return mId;
    }

    public void setId(String pId) {
        this.mId = pId;
    }

    public String getAccountNumber() {
        return mAccountNumber;
    }

    public void setAccountNumber(String pAccountNumber) {
        this.mAccountNumber = pAccountNumber;
    }

    public BigDecimal getBalance() {
        return mBalance;
    }

    public void setBalance(BigDecimal pBalance) {
        this.mBalance = pBalance;
    }

    public BigDecimal getAvailable() {
        return mAvailable;
    }

    public void setAvailable(BigDecimal pAvailable) {
        this.mAvailable = pAvailable;
    }

    public String getName() {
        return mName;
    }

    public void setName(String pName) {
        this.mName = pName;
    }

    public AccountType getAccountType() {
        return mAccountType;
    }

    public void setAccountType(AccountType pAccountType) {
        this.mAccountType = pAccountType;
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public void setCurrency(Currency pCurrency) {
        this.mCurrency = pCurrency;
    }
}
