package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.util.ArrayList;
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
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@ExtendWith(MockitoExtension.class)
class BudgetCalculatorProxyTest extends AbstractBudgetCalculatorTest {

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
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleOutcomeList();
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
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleOutcomeList();
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
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleOutcomeList();
        MainCategory mainCategory = createExampleMainCategoryBuilder(TransactionType.INCOME)
                .build();
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE, DEFAULT_CURRENCY)
                .withMainCategory(mainCategory)
                .build();
        List<Transaction> filteredIncomes = List.of(incomes.get(0), incomes.get(1));
        List<Transaction> filteredOutcomes = new ArrayList<>();
        BudgetDetails expected = BudgetDetails.builder().build();
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, incomes)).thenReturn(filteredIncomes);
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, outcomes)).thenReturn(filteredOutcomes);
        Mockito.when(scaleCalculator.calculateScaleDetails(filteredIncomes, filteredOutcomes)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateScaleDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(scaleCalculator).calculateScaleDetails(filteredIncomes, filteredOutcomes);
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, incomes);
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, outcomes);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateScaleDetailsWhenSchemaHasSubCategory() {
        // GIVEN
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleOutcomeList();
        MainCategory mainCategory = incomes.get(1).getMainCategory();
        SubCategory subCategory = incomes.get(1).getSubCategory();
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE, DEFAULT_CURRENCY)
                .withMainCategory(mainCategory)
                .withSubCategory(subCategory)
                .build();
        List<Transaction> filteredIncomesOnMainCategory = List.of(incomes.get(0), incomes.get(1));
        List<Transaction> filteredIncomesOnSubCategory = List.of(incomes.get(1));
        List<Transaction> filteredOutcomes = new ArrayList<>();
        BudgetDetails expected = BudgetDetails.builder().build();
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, incomes)).thenReturn(filteredIncomesOnMainCategory);
        Mockito.when(utils.filterTransactionListOnSubCategory(subCategory, filteredIncomesOnMainCategory)).thenReturn(filteredIncomesOnSubCategory);
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, outcomes)).thenReturn(filteredOutcomes);
        Mockito.when(utils.filterTransactionListOnSubCategory(subCategory, filteredOutcomes)).thenReturn(filteredOutcomes);
        Mockito.when(scaleCalculator.calculateScaleDetails(filteredIncomesOnSubCategory, filteredOutcomes)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateScaleDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, incomes);
        Mockito.verify(utils).filterTransactionListOnSubCategory(subCategory, filteredIncomesOnMainCategory);
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, outcomes);
        Mockito.verify(utils).filterTransactionListOnSubCategory(subCategory, filteredOutcomes);
        Mockito.verify(scaleCalculator).calculateScaleDetails(filteredIncomesOnSubCategory, filteredOutcomes);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateSUmDetailsWhenSchemaHasHasNotGotAnyCategory() {
        // GIVEN
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleOutcomeList();
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
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleOutcomeList();
        MainCategory mainCategory = createExampleMainCategoryBuilder(TransactionType.INCOME)
                .build();
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE, DEFAULT_CURRENCY)
                .withMainCategory(mainCategory)
                .build();
        List<Transaction> filteredIncomes = List.of(incomes.get(0), incomes.get(1));
        List<Transaction> filteredOutcomes = new ArrayList<>();
        BudgetDetails expected = BudgetDetails.builder().build();
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, incomes)).thenReturn(filteredIncomes);
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, outcomes)).thenReturn(filteredOutcomes);
        Mockito.when(sumCalculator.calculateSumDetails(filteredIncomes, filteredOutcomes, schema)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateSumDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(sumCalculator).calculateSumDetails(filteredIncomes, filteredOutcomes, schema);
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, incomes);
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, outcomes);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateSumDetailsWhenSchemaHasSubCategory() {
        // GIVEN
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleOutcomeList();
        MainCategory mainCategory = incomes.get(1).getMainCategory();
        SubCategory subCategory = incomes.get(1).getSubCategory();
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE, DEFAULT_CURRENCY)
                .withMainCategory(mainCategory)
                .withSubCategory(subCategory)
                .build();
        List<Transaction> filteredIncomesOnMainCategory = List.of(incomes.get(0), incomes.get(1));
        List<Transaction> filteredIncomesOnSubCategory = List.of(incomes.get(1));
        List<Transaction> filteredOutcomes = new ArrayList<>();
        BudgetDetails expected = BudgetDetails.builder().build();
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, incomes)).thenReturn(filteredIncomesOnMainCategory);
        Mockito.when(utils.filterTransactionListOnSubCategory(subCategory, filteredIncomesOnMainCategory)).thenReturn(filteredIncomesOnSubCategory);
        Mockito.when(utils.filterTransactionListOnMainCategory(mainCategory, outcomes)).thenReturn(filteredOutcomes);
        Mockito.when(utils.filterTransactionListOnSubCategory(subCategory, filteredOutcomes)).thenReturn(filteredOutcomes);
        Mockito.when(sumCalculator.calculateSumDetails(filteredIncomesOnSubCategory, filteredOutcomes, schema)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateSumDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, incomes);
        Mockito.verify(utils).filterTransactionListOnSubCategory(subCategory, filteredIncomesOnMainCategory);
        Mockito.verify(utils).filterTransactionListOnMainCategory(mainCategory, outcomes);
        Mockito.verify(utils).filterTransactionListOnSubCategory(subCategory, filteredOutcomes);
        Mockito.verify(sumCalculator).calculateSumDetails(filteredIncomesOnSubCategory, filteredOutcomes, schema);
        Assertions.assertEquals(expected, result);
    }

}
