package hu.elte.bm.transactionservice.web.common;

import javax.validation.constraints.NotNull;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class RequestContext {

    @NotNull(message = "Category type cannot be null!")
    private TransactionType transactionType;

    @NotNull(message = "User id cannot be null!")
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

    @Override
    public String toString() {
        return "RequestContext{"
                + "transactionType=" + transactionType
                + ", userId=" + userId
                + '}';
    }
}
