package hu.elte.bm.transactionservice.dal;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.elte.bm.transactionservice.dal.transaction.IncomeDao;
import hu.elte.bm.transactionservice.dal.transaction.OutcomeDao;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.service.database.DatabaseFacade;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@Service("databaseFacade")
public class DefaultDatabaseFacade implements DatabaseFacade {

    @Autowired
    private IncomeDao incomeDao;

    @Autowired
    private OutcomeDao outcomeDao;

    @Override
    public List<Transaction> findAllTransaction(final LocalDate start, final LocalDate end, final TransactionContext context) {
        return context.getTransactionType() == INCOME ? incomeDao.findAll(start, end, context) : outcomeDao.findAll(start, end, context);
    }

    @Override
    public Optional<Transaction> findTransactionById(final Long id, final TransactionContext context) {
        return context.getTransactionType() == INCOME ? incomeDao.findById(id, context) : outcomeDao.findById(id, context);
    }

    @Override
    public List<Transaction> findTransactionByTitle(final String title, final TransactionContext context) {
        return context.getTransactionType() == INCOME ? incomeDao.findByTitle(title, context) : outcomeDao.findByTitle(title, context);
    }

    @Override
    public Optional<Transaction> saveTransaction(final Transaction transaction, final TransactionContext context) {
        return context.getTransactionType() == INCOME ? incomeDao.save(transaction, context) : outcomeDao.save(transaction, context);
    }

    @Override
    public Optional<Transaction> updateTransaction(final Transaction transaction, final TransactionContext context) {
        return context.getTransactionType() == INCOME ? incomeDao.update(transaction, context) : outcomeDao.update(transaction, context);
    }

    @Override
    public void deleteTransaction(final Transaction transaction, final TransactionContext context) {
        if (context.getTransactionType() == INCOME) {
            incomeDao.delete(transaction, context);
        } else {
            outcomeDao.delete(transaction, context);
        }
    }

}
