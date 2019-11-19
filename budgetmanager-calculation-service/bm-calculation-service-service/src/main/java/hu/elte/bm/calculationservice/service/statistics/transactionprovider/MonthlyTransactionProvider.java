package hu.elte.bm.calculationservice.service.statistics.transactionprovider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.Transaction;

@Component
public class MonthlyTransactionProvider {

    protected List<Transaction> provide(final List<Transaction> transactionList, final LocalDate start, final LocalDate end) {
        List<Transaction> result = new ArrayList<>();
        List<Transaction> filteredTransactions = transactionList.stream()
            .filter(Transaction::isMonthly)
            .collect(Collectors.toList());
        for (Transaction transaction : filteredTransactions) {
            for (LocalDate index = getFirstDate(transaction.getDate(), start); isFinished(index, transaction.getEndDate(), end); index = index.plusMonths(1L)) {
                result.add(createNewTransaction(transaction, index));
            }
        }
        return result;
    }

    private LocalDate getFirstDate(final LocalDate date, final LocalDate start) {
        return date.isBefore(start) ? LocalDate.of(start.getYear(), start.getMonth(), date.getDayOfMonth()) : date;
    }

    private boolean isFinished(final LocalDate transactionDate, final LocalDate transactionEndDate, final LocalDate endDate) {
        return transactionDate.isBefore(endDate) && (transactionEndDate == null || transactionDate.isBefore(transactionEndDate));
    }

    private Transaction createNewTransaction(final Transaction transaction, final LocalDate date) {
        return Transaction.builder()
            .withId(transaction.getId())
            .withTitle(transaction.getTitle())
            .withTransactionType(transaction.getTransactionType())
            .withAmount(transaction.getAmount())
            .withCurrency(transaction.getCurrency())
            .withMainCategory(transaction.getMainCategory())
            .withSubCategory(transaction.getSubCategory())
            .withDate(date)
            .withMonthly(transaction.isMonthly())
            .withEndDate(transaction.getEndDate())
            .withLocked(transaction.isLocked())
            .withDescription(transaction.getDescription())
            .withCoordinate(transaction.getCoordinate())
            .withPictureId(transaction.getPictureId())
            .build();
    }
}
