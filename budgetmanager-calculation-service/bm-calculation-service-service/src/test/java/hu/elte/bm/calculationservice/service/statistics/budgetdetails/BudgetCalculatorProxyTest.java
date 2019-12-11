package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.calculationservice.service.statistics.AbstractCalculatorTest;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@ExtendWith(MockitoExtension.class)
class BudgetCalculatorProxyTest extends AbstractCalculatorTest {

    @InjectMocks
    private BudgetCalculatorProxy underTest;
    @Mock
    private BudgetCalculatorUtils utils;
    @Mock
    private StandardBudgetDetailsCalculator standardCalculator;
    @Mock
    private ScaleBudgetCalculator scaleCalculator;
    @Mock
    private SumBudgetCalculator sumCalculator;

    @Test
    public void testCalculateStandardDetails() {
        // GIVEN
        List<Transaction> incomes = createOtherExampleList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        BudgetDetails expected = BudgetDetails.builder().build();
        Mockito.when(standardCalculator.calculateStandardDetails(incomes, outcomes)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateStandardDetails(incomes, outcomes);

        // THEN
        Mockito.verify(standardCalculator).calculateStandardDetails(incomes, outcomes);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateScaleDetailsWhenSchemaHasHasNotGotAnyCategory() {
        // GIVEN
        List<Transaction> incomes = createOtherExampleList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE, DEFAULT_CURRENCY)
            .build();
        BudgetDetails expected = BudgetDetails.builder().build();
        Mockito.when(scaleCalculator.calculateScaleDetails(incomes, outcomes)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateScaleDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(scaleCalculator).calculateScaleDetails(incomes, outcomes);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateScaleDetailsWhenSchemaHasMainCategory() {
        // GIVEN
        List<Transaction> incomes = createOtherExampleList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        MainCategory mainCategory = createExampleMainCategoryBuilder(TransactionType.OUTCOME)
            .build();
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE, DEFAULT_CURRENCY)
            .withMainCategory(mainCategory)
            .build();
        List<Transaction> filteredOutcomes = List.of(outcomes.get(0), outcomes.get(1));
        BudgetDetails expected = BudgetDetails.builder().build();
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, outcomes)).thenReturn(filteredOutcomes);
        Mockito.when(scaleCalculator.calculateScaleDetails(incomes, filteredOutcomes)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateScaleDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(scaleCalculator).calculateScaleDetails(incomes, filteredOutcomes);
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, outcomes);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateScaleDetailsWhenSchemaHasSubCategory() {
        // GIVEN
        List<Transaction> incomes = createOtherExampleList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        MainCategory mainCategory = outcomes.get(1).getMainCategory();
        SubCategory subCategory = outcomes.get(1).getSubCategory();
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE, DEFAULT_CURRENCY)
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory)
            .build();
        List<Transaction> filteredOutcomesOnMainCategory = List.of(outcomes.get(0), outcomes.get(1));
        List<Transaction> filteredOutcomesOnSubCategory = List.of(outcomes.get(1));
        BudgetDetails expected = BudgetDetails.builder().build();
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, outcomes)).thenReturn(filteredOutcomesOnMainCategory);
        Mockito.when(utils.filterTransactionListOnSubCategory(subCategory, filteredOutcomesOnMainCategory)).thenReturn(filteredOutcomesOnSubCategory);
        Mockito.when(scaleCalculator.calculateScaleDetails(incomes, filteredOutcomesOnSubCategory)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateScaleDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, outcomes);
        Mockito.verify(utils).filterTransactionListOnSubCategory(subCategory, filteredOutcomesOnMainCategory);
        Mockito.verify(scaleCalculator).calculateScaleDetails(incomes, filteredOutcomesOnSubCategory);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateSUmDetailsWhenSchemaHasHasNotGotAnyCategory() {
        // GIVEN
        List<Transaction> incomes = createOtherExampleList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE, DEFAULT_CURRENCY)
            .build();
        BudgetDetails expected = BudgetDetails.builder().build();
        Mockito.when(sumCalculator.calculateSumDetails(incomes, outcomes, schema)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateSumDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(sumCalculator).calculateSumDetails(incomes, outcomes, schema);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateSumDetailsWhenSchemaHasMainCategory() {
        // GIVEN
        List<Transaction> incomes = createOtherExampleList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        MainCategory mainCategory = createExampleMainCategoryBuilder(TransactionType.OUTCOME)
            .build();
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE, DEFAULT_CURRENCY)
            .withMainCategory(mainCategory)
            .build();
        List<Transaction> filteredOutcomes = List.of(incomes.get(0), incomes.get(1));
        BudgetDetails expected = BudgetDetails.builder().build();
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, outcomes)).thenReturn(filteredOutcomes);
        Mockito.when(sumCalculator.calculateSumDetails(incomes, filteredOutcomes, schema)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateSumDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(sumCalculator).calculateSumDetails(incomes, filteredOutcomes, schema);
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, outcomes);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateSumDetailsWhenSchemaHasSubCategory() {
        // GIVEN
        List<Transaction> incomes = createOtherExampleList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        MainCategory mainCategory = outcomes.get(1).getMainCategory();
        SubCategory subCategory = outcomes.get(1).getSubCategory();
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE, DEFAULT_CURRENCY)
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory)
            .build();
        List<Transaction> filteredOutcomesOnMainCategory = List.of(outcomes.get(0), outcomes.get(1));
        List<Transaction> filteredOutcomesOnSubCategory = List.of(outcomes.get(1));
        BudgetDetails expected = BudgetDetails.builder().build();
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, outcomes)).thenReturn(filteredOutcomesOnMainCategory);
        Mockito.when(utils.filterTransactionListOnSubCategory(subCategory, filteredOutcomesOnMainCategory)).thenReturn(filteredOutcomesOnSubCategory);
        Mockito.when(sumCalculator.calculateSumDetails(incomes, filteredOutcomesOnSubCategory, schema)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateSumDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, outcomes);
        Mockito.verify(utils).filterTransactionListOnSubCategory(subCategory, filteredOutcomesOnMainCategory);
        Mockito.verify(sumCalculator).calculateSumDetails(incomes, filteredOutcomesOnSubCategory, schema);
        Assertions.assertEquals(expected, result);
    }

}
