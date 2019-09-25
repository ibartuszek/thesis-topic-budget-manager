package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;

import hu.elte.bm.transactionservice.web.common.RequestContext;

public class TransactionModelRequestContext extends RequestContext {

    private LocalDate start;
    private LocalDate end;
    private TransactionModel transactionModel;

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

    public TransactionModel getTransactionModel() {
        return transactionModel;
    }

    public void setTransactionModel(final TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
    }
}
