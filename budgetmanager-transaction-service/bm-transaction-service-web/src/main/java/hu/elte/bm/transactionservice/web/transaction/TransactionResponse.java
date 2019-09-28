package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.web.common.ResponseModel;

public final class TransactionResponse extends ResponseModel {

    private Transaction transaction;

    private LocalDate firstPossibleDay;

    private TransactionResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    static TransactionResponse createSuccessfulTransactionResponse(final Transaction transaction, final LocalDate firstPossibleDay, final String message) {
        TransactionResponse response = createSuccessfulTransactionResponse(transaction, message);
        response.setFirstPossibleDay(firstPossibleDay);
        return response;
    }

    static TransactionResponse createSuccessfulTransactionResponse(final Transaction transaction, final String message) {
        TransactionResponse response = new TransactionResponse(message, true);
        response.setTransaction(transaction);
        return response;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(final Transaction transaction) {
        this.transaction = transaction;
    }

    public LocalDate getFirstPossibleDay() {
        return firstPossibleDay;
    }

    public void setFirstPossibleDay(final LocalDate firstPossibleDay) {
        this.firstPossibleDay = firstPossibleDay;
    }
}
