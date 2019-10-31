package hu.elte.bm.transactionservice.service.transaction;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.exceptions.transaction.IllegalTransactionException;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.service.database.TransactionDaoProxy;

@Component
public class TransactionDateValidator {

    private final TransactionDaoProxy transactionDaoProxy;

    @Value("${transaction.date_before_the_beginning}")
    private String dateBeforeThePeriodExceptionMessage;

    @Value("${transaction.end_date_before_date}")
    private String endDateBeforeStart;

    @Value("${transaction.not_monthly_transaction}")
    private String notMonthlyTransaction;

    public TransactionDateValidator(final TransactionDaoProxy transactionDaoProxy) {
        this.transactionDaoProxy = transactionDaoProxy;
    }

    LocalDate getTheFirstDateOfTheNewPeriod(final Long userId) {
        List<Transaction> transactionList = transactionDaoProxy.getTransactionListBothTypes(userId);
        return transactionList.stream()
            .filter(Transaction::isLocked)
            .max(Comparator.comparing(Transaction::getDate))
            .map(Transaction::getDate)
            .map(localDate -> localDate.plusDays(1L))
            .orElse(null);
    }

    void validate(final Transaction transaction, final TransactionContext context) {
        LocalDate possibleFirstDate = getTheFirstDateOfTheNewPeriod(context.getUserId());
        if (possibleFirstDate != null && transaction.getDate().isBefore(possibleFirstDate)) {
            throw new IllegalTransactionException(transaction, dateBeforeThePeriodExceptionMessage);
        }
        if (transaction.getEndDate() != null) {
            if (!transaction.isMonthly()) {
                throw new IllegalTransactionException(transaction, notMonthlyTransaction);
            } else if (transaction.getDate().isAfter(transaction.getEndDate()) || transaction.getDate().equals(transaction.getEndDate())) {
                throw new IllegalTransactionException(transaction, endDateBeforeStart);
            }
        }

    }
}
