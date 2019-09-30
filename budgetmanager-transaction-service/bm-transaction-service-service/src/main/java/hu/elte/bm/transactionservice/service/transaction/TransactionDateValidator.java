package hu.elte.bm.transactionservice.service.transaction;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.service.database.TransactionDaoProxy;

@Component
public class TransactionDateValidator {

    private final TransactionDaoProxy transactionDaoProxy;

    @Value("${transaction.days_to_subtract_to_calculate_first_day_of_new_period:30}")
    private Long daysToSubtract;

    @Value("${transaction.date_before_the_beginning}")
    private String dateBeforeThePeriodExceptionMessage;

    @Value("${transaction.end_date_before_date}")
    private String endDateBeforeStart;

    @Value("${transaction.not_monthly_transaction}")
    private String notMonthlyTransaction;

    public TransactionDateValidator(final TransactionDaoProxy transactionDaoProxy) {
        this.transactionDaoProxy = transactionDaoProxy;
    }

    LocalDate getTheFirstDateOfTheNewPeriod(final TransactionContext context) {
        LocalDate start = LocalDate.now().minusDays(daysToSubtract);
        List<Transaction> transactionList = transactionDaoProxy.getTransactionListBothTypes(start, LocalDate.now(), context.getUserId());
        return transactionList.stream()
            .filter(Transaction::isLocked)
            .max(Comparator.comparing(Transaction::getDate))
            .map(Transaction::getDate)
            .map(localDate -> localDate.plusDays(1L))
            .orElseGet(() -> LocalDate.now().minusDays(daysToSubtract));
    }

    void validate(final Transaction transaction, final TransactionContext context) {
        LocalDate possibleFirstDate = getTheFirstDateOfTheNewPeriod(context);
        if (transaction.getDate().isBefore(possibleFirstDate)) {
            throw new IllegalArgumentException(dateBeforeThePeriodExceptionMessage);
        } else if (transaction.getEndDate() != null) {
            if (!transaction.isMonthly()) {
                throw new IllegalArgumentException(notMonthlyTransaction);
            } else if (transaction.getDate().isAfter(transaction.getEndDate()) || transaction.getDate().equals(transaction.getEndDate())) {
                throw new IllegalArgumentException(endDateBeforeStart);
            }
        }

    }
}
