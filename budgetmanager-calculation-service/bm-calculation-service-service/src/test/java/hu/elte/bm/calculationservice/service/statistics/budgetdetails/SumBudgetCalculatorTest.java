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
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.transactionservice.Currency;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@ExtendWith(MockitoExtension.class)
public class SumBudgetCalculatorTest extends AbstractBudgetCalculatorTest {

    private static final String TOTAL_INCOMES_LABEL = "Total income of Main category";
    private static final String TOTAL_EXPENSES_LABEL = "Total cost of Main category";

    @InjectMocks
    private SumBudgetCalculator underTest;

    @Mock
    private BudgetCalculatorUtils utils;

    @Test
    public void testCalculateSumDetailsWhenTransactionTypeIsIncome() {
        // GIVEN
        List<Transaction> transactionList = createExampleTransactionList(TransactionType.INCOME, Currency.EUR);
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SUM, DEFAULT_CURRENCY)
            .withMainCategory(transactionList.get(0).getMainCategory())
            .build();
        BudgetDetailsElement totalIncomes = createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_INCOMES_LABEL);
        BudgetDetails expected = BudgetDetails.builder()
            .withTotalIncomes(totalIncomes)
            .build();
        Mockito.when(utils.getAmountFromTransactionList(transactionList)).thenReturn(EXPECTED_SUM_AMOUNT);

        // WHEN
        var result = underTest.calculateSumDetails(transactionList, Collections.emptyList(), schema);

        // THEN
        Mockito.verify(utils).getAmountFromTransactionList(transactionList);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateSumDetailsWhenTransactionTypeIsOutcome() {
        // GIVEN
        List<Transaction> transactionList = createExampleTransactionList(TransactionType.OUTCOME, Currency.EUR);
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SUM, DEFAULT_CURRENCY)
            .withMainCategory(transactionList.get(0).getMainCategory())
            .build();
        BudgetDetailsElement totalExpenses = createBudgetDetailsElement(EXPECTED_SUM_AMOUNT, TOTAL_EXPENSES_LABEL);
        BudgetDetails expected = BudgetDetails.builder()
            .withTotalExpenses(totalExpenses)
            .build();
        Mockito.when(utils.getAmountFromTransactionList(transactionList)).thenReturn(EXPECTED_SUM_AMOUNT);

        // WHEN
        var result = underTest.calculateSumDetails(Collections.emptyList(), transactionList, schema);

        // THEN
        Mockito.verify(utils).getAmountFromTransactionList(transactionList);
        Assertions.assertEquals(expected, result);
    }

}
