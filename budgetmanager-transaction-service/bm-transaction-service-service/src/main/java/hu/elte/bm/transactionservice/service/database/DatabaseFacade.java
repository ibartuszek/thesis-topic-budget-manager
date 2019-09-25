package hu.elte.bm.transactionservice.service.database;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

public interface DatabaseFacade {

    List<Transaction> findAllTransaction(LocalDate start, LocalDate end, TransactionContext context);

    Optional<Transaction> findTransactionById(Long id, TransactionContext context);

    List<Transaction> findTransactionByTitle(String title, TransactionContext context);

    Optional<Transaction> saveTransaction(Transaction transaction, TransactionContext context);

    Optional<Transaction> updateTransaction(Transaction transaction, TransactionContext context);

    void deleteTransaction(Transaction transaction, TransactionContext context);

}
