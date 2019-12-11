package hu.elte.bm.calculationservice.transactionserviceclient.transactions;

import java.util.List;

import hu.elte.bm.calculationservice.transactionserviceclient.BaseResponse;
import hu.elte.bm.transactionservice.Transaction;

public final class TransactionListResponse extends BaseResponse {

    private List<Transaction> transactionList;

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
