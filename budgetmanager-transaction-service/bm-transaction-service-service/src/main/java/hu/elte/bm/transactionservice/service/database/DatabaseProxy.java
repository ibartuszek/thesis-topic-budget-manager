package hu.elte.bm.transactionservice.service.database;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionException;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@Component("databaseProxy")
public class DatabaseProxy {

    private final DatabaseFacade databaseFacade;

    @Value("${database_proxy.unexpected_error_message}")
    private String exceptionMessage;

    public DatabaseProxy(final DatabaseFacade databaseFacade) {
        this.databaseFacade = databaseFacade;
    }

    public List<Transaction> findAllTransaction(final LocalDate start, final LocalDate end, final TransactionContext context) {
        try {
            return databaseFacade.findAllTransaction(start, end, context);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public Optional<Transaction> findTransactionById(final Long id, final TransactionContext context) {
        try {
            return databaseFacade.findTransactionById(id, context);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public List<Transaction> findTransactionByTitle(final String title, final TransactionContext context) {
        try {
            return databaseFacade.findTransactionByTitle(title, context);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public Optional<Transaction> saveTransaction(final Transaction transaction, final TransactionContext context) {
        try {
            return databaseFacade.saveTransaction(transaction, context);
        } catch (DataAccessException exception) {
            throw new TransactionException(transaction, exceptionMessage, exception);
        }
    }

    public Optional<Transaction> updateTransaction(final Transaction transaction, final TransactionContext context) {
        try {
            return databaseFacade.updateTransaction(transaction, context);
        } catch (DataAccessException exception) {
            throw new TransactionException(transaction, exceptionMessage, exception);
        }
    }

    public void deleteTransaction(final Transaction transaction, final TransactionContext context) {
        try {
            databaseFacade.deleteTransaction(transaction, context);
        } catch (DataAccessException exception) {
            throw new TransactionException(transaction, exceptionMessage, exception);
        }
    }

}
