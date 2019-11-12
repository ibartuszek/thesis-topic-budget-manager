package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.budgetdetails.BudgetDetailsElement;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class SumBudgetCalculator {

    private static final String SUM_INCOME_BASE_LABEL = "Total income of {0}";
    private static final String SUM_OUTCOME_BASE_LABEL = "Total cost of {0}";

    private final BudgetCalculatorUtils utils;

    public SumBudgetCalculator(final BudgetCalculatorUtils utils) {
        this.utils = utils;
    }

    public BudgetDetails calculateSumDetails(final List<Transaction> incomeList, final List<Transaction> outcomeList, final StatisticsSchema schema) {
        BudgetDetails.Builder builder = BudgetDetails.builder();
        if (isIncome(schema)) {
            BudgetDetailsElement totalIncomes = BudgetDetailsElement.builder()
                .withAmount(utils.getAmountFromTransactionList(incomeList))
                .withLabel(createLabelForSum(schema, SUM_INCOME_BASE_LABEL))
                .build();
            builder.withTotalIncomes(totalIncomes);
        } else {
            BudgetDetailsElement totalExpenses = BudgetDetailsElement.builder()
                .withAmount(utils.getAmountFromTransactionList(outcomeList))
                .withLabel(createLabelForSum(schema, SUM_OUTCOME_BASE_LABEL))
                .build();
            builder.withTotalExpenses(totalExpenses);
        }
        return builder.build();
    }

    private String createLabelForSum(final StatisticsSchema schema, final String baseLabel) {
        String result;
        if (schema.getSubCategory() != null) {
            result = MessageFormat.format(baseLabel, schema.getSubCategory().getName());
        } else {
            result = MessageFormat.format(baseLabel, schema.getMainCategory().getName());
        }
        return result;
    }

    private boolean isIncome(final StatisticsSchema schema) {
        return schema.getMainCategory().getTransactionType().equals(TransactionType.INCOME);
    }

}
