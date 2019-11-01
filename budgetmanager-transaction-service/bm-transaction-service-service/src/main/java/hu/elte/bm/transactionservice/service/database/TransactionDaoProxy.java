package hu.elte.bm.transactionservice.service.database;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@Component
public class TransactionDaoProxy {

    private final IncomeDao incomeDao;
    private final OutcomeDao outcomeDao;

    public TransactionDaoProxy(final IncomeDao incomeDao, final OutcomeDao outcomeDao) {
        this.incomeDao = incomeDao;
        this.outcomeDao = outcomeDao;
    }

    public List<Transaction> getTransactionList(final LocalDate end, final TransactionContext context) {
        return isIncome(context) ? incomeDao.findAll(end, context.getUserId()) : outcomeDao.findAll(end, context.getUserId());
    }

    public List<Transaction> getTransactionList(final LocalDate start, final LocalDate end, final TransactionContext context) {
        return isIncome(context) ? incomeDao.findAll(start, end, context.getUserId()) : outcomeDao.findAll(start, end, context.getUserId());
    }

    public List<Transaction> getTransactionListBothTypes(final Long userId) {
        List<Transaction> transactionList = incomeDao.findAll(userId);
        transactionList.addAll(outcomeDao.findAll(userId));
        return transactionList;
    }

    public Optional<Transaction> findById(final Long id, final TransactionContext context) {
        return isIncome(context) ? incomeDao.findById(id, context.getUserId()) : outcomeDao.findById(id, context.getUserId());
    }

    public List<Transaction> findByTitle(final String title, final TransactionContext context) {
        return isIncome(context) ? incomeDao.findByTitle(title, context.getUserId()) : outcomeDao.findByTitle(title, context.getUserId());
    }

    public Transaction save(final Transaction transaction, final TransactionContext context) {
        return isIncome(context) ? incomeDao.save(transaction, context.getUserId()) : outcomeDao.save(transaction, context.getUserId());
    }

    public Transaction update(final Transaction transaction, final TransactionContext context) {
        return isIncome(context) ? incomeDao.update(transaction, context.getUserId()) : outcomeDao.update(transaction, context.getUserId());
    }

    public void update(final List<Transaction> transactionList, final TransactionContext context) {
        if (isIncome(context)) {
            incomeDao.update(transactionList, context.getUserId());
        } else {
            outcomeDao.update(transactionList, context.getUserId());
        }
    }

    public Transaction delete(final Transaction transaction, final TransactionContext context) {
        return isIncome(context) ? incomeDao.delete(transaction, context.getUserId()) : outcomeDao.delete(transaction, context.getUserId());
    }

    private boolean isIncome(final TransactionContext context) {
        return context.getTransactionType().equals(TransactionType.INCOME);
    }
}
