package hu.elte.bm.transactionservice.web.transaction;

import java.util.List;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.web.common.ResponseModel;

public final class TransactionListResponse extends ResponseModel {

    private List<Transaction> transactionList;

    private TransactionListResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    static TransactionListResponse createSuccessfulSubCategoryResponse(final List<Transaction> transactionList) {
        return createSuccessfulSubCategoryResponse(transactionList, null);
    }

    static TransactionListResponse createSuccessfulSubCategoryResponse(final List<Transaction> transactionList, final String message) {
        TransactionListResponse response = new TransactionListResponse(message, true);
        response.setTransactionList(transactionList);
        return response;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(final List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public String toString() {
        return "TransactionListResponse{"
            + "message='" + getMessage() + '\''
            + ", successful=" + isSuccessful()
            + ", transactionList=" + transactionList
            + '}';
    }
}
