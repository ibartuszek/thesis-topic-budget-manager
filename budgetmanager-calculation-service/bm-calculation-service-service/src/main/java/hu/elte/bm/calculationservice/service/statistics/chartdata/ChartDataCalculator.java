package hu.elte.bm.calculationservice.service.statistics.chartdata;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.budgetdetails.TransactionData;
import hu.elte.bm.calculationservice.chartdata.ChartData;
import hu.elte.bm.transactionservice.Transaction;

@Component
public class ChartDataCalculator {

    private final StandardChartDataCalculator standardCalculator;
    private final ScaleChartDataCalculator scaleCalculator;
    private final SumChartDataCalculator sumCalculator;

    public ChartDataCalculator(final StandardChartDataCalculator standardCalculator,
        final ScaleChartDataCalculator scaleCalculator, final SumChartDataCalculator sumCalculator) {
        this.standardCalculator = standardCalculator;
        this.scaleCalculator = scaleCalculator;
        this.sumCalculator = sumCalculator;
    }

    public ChartData calculateStandardChartData(final List<TransactionData> outcomes) {
        return standardCalculator.calculateStandardChartData(outcomes);
    }

    public ChartData calculateScaleChartData(final List<Transaction> incomes, final List<Transaction> outcomes, final String title) {
        return scaleCalculator.calculateScaleChartData(createSortedTransactionList(incomes, outcomes), title);
    }

    public ChartData calculateSumChartData(final List<Transaction> outcomes, final String title) {
        return sumCalculator.calculateSumChartData(outcomes, title);
    }

    private List<Transaction> createSortedTransactionList(final List<Transaction> incomes, final List<Transaction> outcomes) {
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.addAll(incomes);
        transactionList.addAll(outcomes);
        transactionList.sort(this::sortTransactions);
        return transactionList;
    }

    private int sortTransactions(final Transaction first, final Transaction second) {
        int result;
        if (first.isMonthly() || second.isMonthly()) {
            int firstDateDays = first.getDate().getDayOfMonth();
            int secondDateDays = second.getDate().getDayOfMonth();
            result = Integer.compare(firstDateDays, secondDateDays);
        } else {
            LocalDate firstDate = first.getDate();
            LocalDate secondDate = second.getDate();
            result = firstDate.isAfter(secondDate) ? 1
                : secondDate.isAfter(firstDate) ? -1
                : 0;
        }
        return result;
    }

}
