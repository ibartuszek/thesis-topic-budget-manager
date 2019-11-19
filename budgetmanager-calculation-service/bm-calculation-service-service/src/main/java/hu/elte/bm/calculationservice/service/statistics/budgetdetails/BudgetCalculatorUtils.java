package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetailsElement;
import hu.elte.bm.calculationservice.service.statistics.RounderUtil;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.Transaction;

@Component
public class BudgetCalculatorUtils {

    private static final double ZERO = 0.0d;
    private static final String SAVINGS_LABEL = "Savings";
    private static final String TOTAL_INCOMES_LABEL = "Total incomes";
    private static final String TOTAL_EXPENSES_LABEL = "Total expenses";

    private final RounderUtil rounderUtil;

    public BudgetCalculatorUtils(final RounderUtil rounderUtil) {
        this.rounderUtil = rounderUtil;
    }

    double getZero() {
        return ZERO;
    }

    String getTotalIncomesLabel() {
        return TOTAL_INCOMES_LABEL;
    }

    String getTotalExpensesLabel() {
        return TOTAL_EXPENSES_LABEL;
    }

    List<Transaction> filterTransactionListOnMainCategory(final MainCategory mainCategory, final List<Transaction> transactionList) {
        return transactionList.stream()
            .filter(transaction -> transaction.getMainCategory().equals(mainCategory))
            .collect(Collectors.toList());
    }

    List<Transaction> filterTransactionListOnSubCategory(final SubCategory subCategory, final List<Transaction> transactionList) {
        return transactionList.stream()
            .filter(transaction -> transaction.getSubCategory() != null && transaction.getSubCategory().equals(subCategory))
            .collect(Collectors.toList());
    }

    Double getAmountFromTransactionList(final List<Transaction> transactionList) {
        double sum = transactionList.stream()
            .map(Transaction::getAmount)
            .reduce(Double::sum)
            .orElse(ZERO);
        return rounderUtil.round(sum);
    }

    BudgetDetailsElement createBudgetDetailsElement(final double amount, final String label) {
        return BudgetDetailsElement.builder()
            .withAmount(rounderUtil.round(amount))
            .withLabel(label)
            .build();
    }

    BudgetDetailsElement createSavings(final BudgetDetailsElement totalIncomes, final BudgetDetailsElement totalExpenses) {
        double amount = totalIncomes.getAmount() - totalExpenses.getAmount();
        return BudgetDetailsElement.builder()
            .withAmount(rounderUtil.round(amount))
            .withLabel(SAVINGS_LABEL)
            .build();
    }

}
