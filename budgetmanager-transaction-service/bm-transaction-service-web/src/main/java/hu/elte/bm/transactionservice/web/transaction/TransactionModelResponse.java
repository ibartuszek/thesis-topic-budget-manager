package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;

import hu.elte.bm.transactionservice.web.common.ResponseModel;

public class TransactionModelResponse extends ResponseModel {

    private TransactionModel transactionModel;

    private LocalDate firstPossibleDay;

    public TransactionModel getTransactionModel() {
        return transactionModel;
    }

    public void setTransactionModel(final TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
    }

    public LocalDate getFirstPossibleDay() {
        return firstPossibleDay;
    }

    public void setFirstPossibleDay(final LocalDate firstPossibleDay) {
        this.firstPossibleDay = firstPossibleDay;
    }
}
