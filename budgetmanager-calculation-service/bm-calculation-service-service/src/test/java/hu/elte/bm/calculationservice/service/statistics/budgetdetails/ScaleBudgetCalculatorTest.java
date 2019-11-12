package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.budgetdetails.BudgetDetailsElement;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@ExtendWith(MockitoExtension.class)
class ScaleBudgetCalculatorTest extends AbstractBudgetCalculatorTest {

    private static final double EXPECTED_EXPENSE_SUM_AMOUNT = 45.0d;
    private static final double EXPECTED_SAVINGS_AMOUNT = 10.0d;

    @InjectMocks
    private ScaleBudgetCalculator underTest;

    @Mock
    private BudgetCalculatorUtils utils;

    @Test
    public void testCalculateScaleDetailsWhenOutcomeListIsEmpty() {
        // GIVEN
        List<Transaction> incomeList = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomeList = Collections.emptyList();
        BudgetDetailsElement totalIncomes = createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_INCOMES_LABEL);
        BudgetDetails expected = BudgetDetails.builder()
            .withTotalIncomes(totalIncomes)
            .build();
        Mockito.when(utils.getAmountFromTransactionList(incomeList)).thenReturn(EXPECTED_SUM_AMOUNT);
        Mockito.when(utils.createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_INCOMES_LABEL)).thenReturn(totalIncomes);
        Mockito.when(utils.getTotalIncomesLabel()).thenReturn(TOTAL_INCOMES_LABEL);

        // WHEN
        var result = underTest.calculateScaleDetails(incomeList, outcomeList);

        // THEN
        Mockito.verify(utils).getAmountFromTransactionList(incomeList);
        Mockito.verify(utils).createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_INCOMES_LABEL);
        Mockito.verify(utils).getTotalIncomesLabel();
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateScaleDetailsWhenIncomeListIsEmpty() {
        // GIVEN
        List<Transaction> incomeList = Collections.emptyList();
        List<Transaction> outcomeList = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        BudgetDetailsElement totalExpenses = createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_EXPENSES_LABEL);
        BudgetDetails expected = BudgetDetails.builder()
            .withTotalExpenses(totalExpenses)
            .build();
        Mockito.when(utils.getAmountFromTransactionList(outcomeList)).thenReturn(EXPECTED_SUM_AMOUNT);
        Mockito.when(utils.createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_EXPENSES_LABEL)).thenReturn(totalExpenses);
        Mockito.when(utils.getTotalExpensesLabel()).thenReturn(TOTAL_EXPENSES_LABEL);

        // WHEN
        var result = underTest.calculateScaleDetails(incomeList, outcomeList);

        // THEN
        Mockito.verify(utils).getAmountFromTransactionList(outcomeList);
        Mockito.verify(utils).createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_EXPENSES_LABEL);
        Mockito.verify(utils).getTotalExpensesLabel();
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateScaleDetails() {
        // GIVEN
        List<Transaction> incomeList = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomeList = createExampleOutcomeList();
        BudgetDetailsElement totalIncomes = createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_INCOMES_LABEL);
        BudgetDetailsElement totalExpenses = createBudgetDetailsElement(EXPECTED_EXPENSE_SUM_AMOUNT, TOTAL_EXPENSES_LABEL);
        BudgetDetailsElement savings = createBudgetDetailsElement(EXPECTED_SAVINGS_AMOUNT, SAVINGS_LABEL);
        BudgetDetails expected = BudgetDetails.builder()
            .withTotalIncomes(totalIncomes)
            .withTotalExpenses(totalExpenses)
            .withSavings(savings)
            .build();
        Mockito.when(utils.getAmountFromTransactionList(incomeList)).thenReturn(EXPECTED_SUM_AMOUNT);
        Mockito.when(utils.createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_INCOMES_LABEL)).thenReturn(totalIncomes);
        Mockito.when(utils.getTotalIncomesLabel()).thenReturn(TOTAL_INCOMES_LABEL);
        Mockito.when(utils.getAmountFromTransactionList(outcomeList)).thenReturn(EXPECTED_SUM_AMOUNT);
        Mockito.when(utils.createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_EXPENSES_LABEL)).thenReturn(totalExpenses);
        Mockito.when(utils.getTotalExpensesLabel()).thenReturn(TOTAL_EXPENSES_LABEL);
        Mockito.when(utils.createSavings(totalIncomes, totalExpenses)).thenReturn(savings);
        // WHEN
        var result = underTest.calculateScaleDetails(incomeList, outcomeList);

        // THEN
        Mockito.verify(utils).getAmountFromTransactionList(incomeList);
        Mockito.verify(utils).createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_INCOMES_LABEL);
        Mockito.verify(utils).getTotalIncomesLabel();
        Mockito.verify(utils).getAmountFromTransactionList(outcomeList);
        Mockito.verify(utils).createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_EXPENSES_LABEL);
        Mockito.verify(utils).getTotalExpensesLabel();
        Mockito.verify(utils).createSavings(totalIncomes, totalExpenses);
        Assertions.assertEquals(expected, result);
    }

}
