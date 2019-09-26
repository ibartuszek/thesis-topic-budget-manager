package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.web.common.RequestContext;

public class TransactionRequestContext extends RequestContext {

    private LocalDate start;
    private LocalDate end;
    private Transaction transaction;

    public LocalDate getStart() {
        return start;
    }

    public void setStart(final LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(final LocalDate end) {
        this.end = end;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(final Transaction transaction) {
        this.transaction = transaction;
    }
}
