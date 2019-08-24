package hu.elte.bm.transactionservice.web.common;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class RequestModelContext {

    private TransactionType transactionType;

    private Long userId;

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(final TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}
