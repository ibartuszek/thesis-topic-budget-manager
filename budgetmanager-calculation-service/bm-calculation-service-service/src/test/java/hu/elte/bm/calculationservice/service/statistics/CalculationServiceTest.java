package hu.elte.bm.calculationservice.service.statistics;

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
import hu.elte.bm.calculationservice.chartdata.ChartData;
import hu.elte.bm.calculationservice.chartdata.RadialChartData;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.calculationservice.service.statistics.budgetdetails.BudgetCalculatorProxy;
import hu.elte.bm.calculationservice.service.statistics.chartdata.ChartDataCalculator;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@ExtendWith(MockitoExtension.class)
public class CalculationServiceTest extends AbstractCalculatorTest {

    @InjectMocks
    private CalculationService underTest;

    @Mock
    private BudgetCalculatorProxy budgetCalculatorProxy;

    @Mock
    private ChartDataCalculator chartDataCalculator;

    @Test
    public void testCalculateDetailsWhenTypeIsStandard() {
        // GIVEN
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.STANDARD)
            .build();
        BudgetDetails expected = BudgetDetails.builder()
            .build();
        Mockito.when(budgetCalculatorProxy.calculateStandardDetails(incomes, outcomes)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(budgetCalculatorProxy).calculateStandardDetails(incomes, outcomes);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateDetailsWhenTypeIsScale() {
        // GIVEN
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE)
            .build();
        BudgetDetails expected = BudgetDetails.builder()
            .build();
        Mockito.when(budgetCalculatorProxy.calculateScaleDetails(incomes, outcomes, schema)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(budgetCalculatorProxy).calculateScaleDetails(incomes, outcomes, schema);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateDetailsWhenTypeIsSum() {
        // GIVEN
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SUM)
            .build();
        BudgetDetails expected = BudgetDetails.builder()
            .build();
        Mockito.when(budgetCalculatorProxy.calculateSumDetails(incomes, outcomes, schema)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateDetails(incomes, outcomes, schema);

        // THEN
        Mockito.verify(budgetCalculatorProxy).calculateSumDetails(incomes, outcomes, schema);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCreateChartDataWhenTypeIsStandard() {
        // GIVEN
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.STANDARD)
            .build();
        BudgetDetails details = BudgetDetails.builder()
            .withOutcomes(new ArrayList<>())
            .build();
        ChartData expected = RadialChartData.builder()
            .build();
        Mockito.when(chartDataCalculator.calculateStandardChartData(details.getOutcomes())).thenReturn(expected);

        // WHEN
        var result = underTest.createChartData(incomes, outcomes, schema, details);

        // THEN
        Mockito.verify(chartDataCalculator).calculateStandardChartData(details.getOutcomes());
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCreateChartDataWhenTypeIsScale() {
        // GIVEN
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        List<Transaction> filteredOutcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE)
            .build();
        BudgetDetails details = BudgetDetails.builder()
            .build();
        ChartData expected = RadialChartData.builder()
            .build();
        Mockito.when(budgetCalculatorProxy.createFilteredTransactionList(outcomes, schema)).thenReturn(filteredOutcomes);
        Mockito.when(chartDataCalculator.calculateScaleChartData(incomes, filteredOutcomes, schema.getTitle())).thenReturn(expected);

        // WHEN
        var result = underTest.createChartData(incomes, outcomes, schema, details);

        // THEN
        Mockito.verify(budgetCalculatorProxy).createFilteredTransactionList(outcomes, schema);
        Mockito.verify(chartDataCalculator).calculateScaleChartData(incomes, filteredOutcomes, schema.getTitle());
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCreateChartDataWhenTypeIsSum() {
        // GIVEN
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        List<Transaction> filteredOutcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SUM)
            .build();
        BudgetDetails details = BudgetDetails.builder()
            .build();
        ChartData expected = RadialChartData.builder()
            .build();
        Mockito.when(budgetCalculatorProxy.createFilteredTransactionList(outcomes, schema)).thenReturn(filteredOutcomes);
        Mockito.when(chartDataCalculator.calculateSumChartData(filteredOutcomes, schema.getTitle())).thenReturn(expected);

        // WHEN
        var result = underTest.createChartData(incomes, outcomes, schema, details);

        // THEN
        Mockito.verify(budgetCalculatorProxy).createFilteredTransactionList(outcomes, schema);
        Mockito.verify(chartDataCalculator).calculateSumChartData(filteredOutcomes, schema.getTitle());
        Assertions.assertEquals(expected, result);
    }

}
