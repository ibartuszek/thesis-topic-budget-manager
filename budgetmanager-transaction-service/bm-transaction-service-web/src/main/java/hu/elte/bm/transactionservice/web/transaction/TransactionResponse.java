package hu.elte.bm.transactionservice.web.transaction;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.web.common.ResponseModel;

public final class TransactionResponse extends ResponseModel {

    private Transaction transaction;

    private TransactionResponse(final String message, final boolean successful) {
        super(message, successful);
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

    @Override
    public String toString() {
        return "TransactionResponse{"
            + "message='" + getMessage() + '\''
            + ", successful=" + isSuccessful()
            + ", transaction=" + transaction
            + '}';
    }
}
