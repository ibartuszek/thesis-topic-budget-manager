package hu.elte.bm.transactionservice.web.transaction;

import javax.validation.Valid;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.web.common.RequestContext;

public class TransactionRequestContext extends RequestContext {

    @Valid
    private Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(final Transaction transaction) {
        this.transaction = transaction;
    }
}
