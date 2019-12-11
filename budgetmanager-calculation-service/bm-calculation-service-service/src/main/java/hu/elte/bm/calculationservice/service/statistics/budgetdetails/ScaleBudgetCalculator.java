package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.util.List;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.budgetdetails.BudgetDetailsElement;
import hu.elte.bm.transactionservice.Transaction;

@Component
public class ScaleBudgetCalculator {

    private final BudgetCalculatorUtils utils;

    public ScaleBudgetCalculator(final BudgetCalculatorUtils utils) {
        this.utils = utils;
    }

    public BudgetDetails calculateScaleDetails(final List<Transaction> incomeList, final List<Transaction> outcomeList) {
        BudgetDetailsElement totalIncomes = incomeList.isEmpty() ? null
            : utils.createBudgetDetailsElement(utils.getAmountFromTransactionList(incomeList), utils.getTotalIncomesLabel());
        BudgetDetailsElement totalExpenses = outcomeList.isEmpty() ? null
            : utils.createBudgetDetailsElement(utils.getAmountFromTransactionList(outcomeList), utils.getTotalExpensesLabel());
        BudgetDetailsElement savings = totalIncomes == null || totalExpenses == null ? null
            : utils.createSavings(totalIncomes, totalExpenses);
        return BudgetDetails.builder()
            .withTotalIncomes(totalIncomes)
            .withTotalExpenses(totalExpenses)
            .withSavings(savings)
            .build();
    }

}
