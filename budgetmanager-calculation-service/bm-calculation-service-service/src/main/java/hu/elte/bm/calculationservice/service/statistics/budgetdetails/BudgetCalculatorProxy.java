package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.util.List;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.transactionservice.Transaction;

@Component
public class BudgetCalculatorProxy {

    private final BudgetCalculatorUtils utils;
    private final StandardBudgetDetailsCalculator standardCalculator;
    private final ScaleBudgetCalculator scaleCalculator;
    private final SumBudgetCalculator sumCalculator;

    public BudgetCalculatorProxy(final BudgetCalculatorUtils utils, final StandardBudgetDetailsCalculator standardCalculator,
            final ScaleBudgetCalculator scaleCalculator, final SumBudgetCalculator sumCalculator) {
        this.utils = utils;
        this.standardCalculator = standardCalculator;
        this.scaleCalculator = scaleCalculator;
        this.sumCalculator = sumCalculator;
    }

    public BudgetDetails calculateStandardDetails(final List<Transaction> incomes, final List<Transaction> outcomes) {
        return standardCalculator.calculateStandardDetails(incomes, outcomes);
    }

    public BudgetDetails calculateScaleDetails(final List<Transaction> incomes, final List<Transaction> outcomes, final StatisticsSchema schema) {
        List<Transaction> filteredIncomeList = createFilteredTransactionList(incomes, schema);
        List<Transaction> filteredOutcomeList = createFilteredTransactionList(outcomes, schema);
        return scaleCalculator.calculateScaleDetails(filteredIncomeList, filteredOutcomeList);
    }

    public BudgetDetails calculateSumDetails(final List<Transaction> incomes, final List<Transaction> outcomes, final StatisticsSchema schema) {
        List<Transaction> filteredIncomeList = createFilteredTransactionList(incomes, schema);
        List<Transaction> filteredOutcomeList = createFilteredTransactionList(outcomes, schema);
        return sumCalculator.calculateSumDetails(filteredIncomeList, filteredOutcomeList, schema);
    }

    private List<Transaction> createFilteredTransactionList(final List<Transaction> transactionList, final StatisticsSchema schema) {
        List<Transaction> filteredTransactionList;
        if (schema.getMainCategory() != null) {
            filteredTransactionList = utils.filterTransactionListOnMainCategory(schema.getMainCategory(), transactionList);
            if (schema.getSubCategory() != null) {
                filteredTransactionList = utils.filterTransactionListOnSubCategory(schema.getSubCategory(), filteredTransactionList);
            }
        } else {
            filteredTransactionList = transactionList;
        }
        return filteredTransactionList;
    }

}
