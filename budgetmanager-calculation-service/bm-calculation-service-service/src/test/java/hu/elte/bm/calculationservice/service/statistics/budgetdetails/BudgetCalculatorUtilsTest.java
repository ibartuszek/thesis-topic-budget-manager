package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetailsElement;
import hu.elte.bm.calculationservice.service.statistics.AbstractCalculatorTest;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.Transaction;

class BudgetCalculatorUtilsTest extends AbstractCalculatorTest {

    private static final double TOTAL_INCOMES_AMOUNT = 35.0d;
    private static final double TOTAL_EXPENSES_AMOUNT = 30.0d;
    private static final double SAVINGS_AMOUNT = 5.0d;
    private static final String TOTAL_INCOMES_LABEL = "Total incomes";
    private static final String TOTAL_EXPENSES_LABEL = "Total expenses";

    private final BudgetCalculatorUtils underTest = new BudgetCalculatorUtils();

    @Test
    public void testFilterTransactionListOnMainCategoryWhenMainCategoryIsNull() {
        // GIVEN
        MainCategory mainCategory = null;
        List<Transaction> transactionList = createExampleTransactionList(DEFAULT_TRANSACTION_TYPE, DEFAULT_CURRENCY);
        // WHEN
        var result = underTest.filterTransactionListOnMainCategory(mainCategory, transactionList);
        // THEN
        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFilterTransactionListOnMainCategoryWhenGotEmptyList() {
        // GIVEN
        MainCategory mainCategory = createExampleMainCategoryBuilder(DEFAULT_TRANSACTION_TYPE)
            .build();
        List<Transaction> transactionList = Collections.emptyList();
        // WHEN
        var result = underTest.filterTransactionListOnMainCategory(mainCategory, transactionList);
        // THEN
        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFilterTransactionListOnMainCategory() {
        // GIVEN
        List<Transaction> transactionList = createExampleTransactionList(DEFAULT_TRANSACTION_TYPE, DEFAULT_CURRENCY);
        MainCategory mainCategory = transactionList.get(0).getMainCategory();
        List<Transaction> expected = List.of(transactionList.get(0), transactionList.get(1));
        // WHEN
        var result = underTest.filterTransactionListOnMainCategory(mainCategory, transactionList);
        // THEN
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testFilterTransactionListOnSubCategoryWhenSubCategoryIsNull() {
        // GIVEN
        SubCategory subCategory = null;
        List<Transaction> transactionList = createExampleTransactionList(DEFAULT_TRANSACTION_TYPE, DEFAULT_CURRENCY);
        // WHEN
        var result = underTest.filterTransactionListOnSubCategory(subCategory, transactionList);
        // THEN
        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFilterTransactionListOnSubCategoryWhenGotEmptyList() {
        // GIVEN
        SubCategory subCategory = createExampleSubCategoryBuilder(DEFAULT_TRANSACTION_TYPE)
            .build();
        List<Transaction> transactionList = Collections.emptyList();
        // WHEN
        var result = underTest.filterTransactionListOnSubCategory(subCategory, transactionList);
        // THEN
        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFilterTransactionListOnSubCategory() {
        // GIVEN
        List<Transaction> transactionList = createExampleTransactionList(DEFAULT_TRANSACTION_TYPE, DEFAULT_CURRENCY);
        SubCategory subCategory = transactionList.get(1).getSubCategory();
        List<Transaction> expected = List.of(transactionList.get(1));
        // WHEN
        var result = underTest.filterTransactionListOnSubCategory(subCategory, transactionList);
        // THEN
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testGetAmountFromTransactionListWhenGetEmptyList() {
        // GIVEN
        // WHEN
        var result = underTest.getAmountFromTransactionList(Collections.emptyList());
        // THEN
        Assertions.assertEquals(ZERO, result);
    }

    @Test
    public void testGetAmountFromTransactionList() {
        // GIVEN
        List<Transaction> transactionList = createExampleTransactionList(DEFAULT_TRANSACTION_TYPE, DEFAULT_CURRENCY);
        // WHEN
        var result = underTest.getAmountFromTransactionList(transactionList);
        // THEN
        Assertions.assertEquals(EXPECTED_SUM_AMOUNT, result);
    }

    @Test
    public void testCreateSavings() {
        // GIVEN
        BudgetDetailsElement totalIncomes = createBudgetDetailsElement(TOTAL_INCOMES_AMOUNT, TOTAL_INCOMES_LABEL);
        BudgetDetailsElement totalExpenses = createBudgetDetailsElement(TOTAL_EXPENSES_AMOUNT, TOTAL_EXPENSES_LABEL);
        BudgetDetailsElement expected = createBudgetDetailsElement(SAVINGS_AMOUNT, SAVINGS_LABEL);
        // WHEN
        var result = underTest.createSavings(totalIncomes, totalExpenses);
        // THEN
        Assertions.assertEquals(expected, result);
    }

}
