package hu.elte.bm.transactionservice.web.common;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class RequestModelContext {

    private TransactionType transactionType;

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(final TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
