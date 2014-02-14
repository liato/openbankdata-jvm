package org.openbankdata.core;

import java.math.BigDecimal;
import java.util.Currency;

import org.openbankdata.core.utils.ObjectUtils;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Account other = (Account) obj;
        return ObjectUtils.equal(this.mId, other.mId)
                && ObjectUtils.equal(this.mAccountNumber, other.mAccountNumber)
                && ObjectUtils.equal(this.mAccountType, other.mAccountType)
                && ObjectUtils.equal(this.mAvailable, other.mAvailable)
                && ObjectUtils.equal(this.mBalance, other.mBalance)
                && ObjectUtils.equal(this.mCurrency, other.mCurrency)
                && ObjectUtils.equal(this.mName, other.mName);

    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.mAccountNumber,
                this.mId,
                this.mAccountType,
                this.mAvailable,
                this.mBalance,
                this.mCurrency,
                this.mName);
    }
}
