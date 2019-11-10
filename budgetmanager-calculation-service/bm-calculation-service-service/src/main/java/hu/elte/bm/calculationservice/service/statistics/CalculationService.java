package hu.elte.bm.calculationservice.service.statistics;

import java.util.List;

import org.springframework.stereotype.Service;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.calculationservice.service.statistics.budgetdetails.StandardBudgetDetailsCalculator;
import hu.elte.bm.transactionservice.Transaction;

@Service
public class CalculationService {

    private final StandardBudgetDetailsCalculator detailsCalculator;
    // private final ChartDataCalculator chartDataCalculator;

    public CalculationService(final StandardBudgetDetailsCalculator detailsCalculator) {
        this.detailsCalculator = detailsCalculator;
        // this.chartDataCalculator = chartDataCalculator;
    }

    public BudgetDetails calculateDetails(final List<Transaction> incomes, final List<Transaction> outcomes, final StatisticsSchema schema) {
        BudgetDetails result;
        if (schema.getType().equals(StatisticsType.STANDARD)) {
            result = detailsCalculator.calculateStandardDetails(incomes, outcomes);
        } else {
            List<Transaction> filteredIncomeList = createFilteredTransactionList(incomes, schema);
            List<Transaction> filteredOutcomeList = createFilteredTransactionList(outcomes, schema);
            if (schema.getType().equals(StatisticsType.SCALE)) {
                result = detailsCalculator.calculateScaleDetails(filteredIncomeList, filteredOutcomeList);
            } else {
                result = detailsCalculator.calculateSumDetails(filteredIncomeList, filteredOutcomeList, schema);
            }
        }
        return result;
    }

    private List<Transaction> createFilteredTransactionList(final List<Transaction> transactionList, final StatisticsSchema schema) {
        List<Transaction> filteredTransactionList;
        if (schema.getMainCategory() != null) {
            filteredTransactionList = detailsCalculator.filterTransactionListOnMainCategory(schema.getMainCategory(), transactionList);
            if (schema.getSubCategory() != null) {
                filteredTransactionList = detailsCalculator.filterTransactionListOnSubCategory(schema.getSubCategory(), filteredTransactionList);
            }
        } else {
            filteredTransactionList = transactionList;
        }
        return filteredTransactionList;
    }
}
