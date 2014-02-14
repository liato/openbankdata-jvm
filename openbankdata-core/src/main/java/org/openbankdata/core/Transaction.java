package org.openbankdata.core;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class Transaction {

    private Date mTransactionDate;
    private String mDescription;
    private BigDecimal mAmount;
    private Currency mCurrency;
    private boolean mPending;

    public Date getTransactionDate() {
        return mTransactionDate;
    }

    public void setTransactionDate(Date pTransactionDate) {
        this.mTransactionDate = pTransactionDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String pDescription) {
        this.mDescription = pDescription;
    }

    public boolean hasDescription() {
        return mDescription != null && !mDescription.isEmpty();
    }

    public BigDecimal getAmount() {
        return mAmount;
    }

    public void setAmount(BigDecimal pAmount) {
        this.mAmount = pAmount;
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public void setCurrency(Currency pCurrency) {
        this.mCurrency = pCurrency;
    }

    public void setPending(boolean pPending) {
        mPending = pPending;
    }

    public boolean isPending() {
        return mPending;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Transaction other = (Transaction) obj;
        return ObjectUtils.equal(this.mAmount, other.mAmount)
                && ObjectUtils.equal(this.mCurrency, other.mCurrency)
                && ObjectUtils.equal(this.mDescription, other.mDescription)
                && ObjectUtils.equal(this.mPending, other.mPending)
                && ObjectUtils.equal(this.mTransactionDate, other.mTransactionDate);

    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.mAmount,
                this.mCurrency,
                this.mDescription,
                this.mPending,
                this.mTransactionDate);
    }
}
