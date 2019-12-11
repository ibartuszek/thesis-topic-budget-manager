package hu.elte.bm.transactionservice.domain.exceptions.transaction;

import hu.elte.bm.transactionservice.domain.exceptions.CustomException;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;

public interface TransactionException extends CustomException {

    Transaction getTransaction();
}
