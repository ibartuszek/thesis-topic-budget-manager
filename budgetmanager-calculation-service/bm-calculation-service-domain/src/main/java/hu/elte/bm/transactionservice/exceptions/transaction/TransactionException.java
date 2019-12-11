package hu.elte.bm.transactionservice.exceptions.transaction;

import hu.elte.bm.calculationservice.exceptions.CustomException;
import hu.elte.bm.transactionservice.Transaction;

public interface TransactionException extends CustomException {

    Transaction getTransaction();
}
