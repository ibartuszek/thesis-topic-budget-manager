package hu.elte.bm.calculationservice.service.statistics;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.chartdata.RadialChartData;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.calculationservice.service.schema.StatisticsSchemaService;
import hu.elte.bm.calculationservice.service.statistics.transactionprovider.TransactionProvider;
import hu.elte.bm.calculationservice.service.statistics.transactionprovider.TransactionProviderContext;
import hu.elte.bm.calculationservice.statistics.Statistics;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@ExtendWith(MockitoExtension.class)
public class StatisticsFactoryTest extends AbstractCalculatorTest {

    private static final Long USER_ID = 1L;
    private static final Long SCHEMA_ID = 1L;
    private static final LocalDate START = LocalDate.now().minusDays(30);
    private static final LocalDate END = LocalDate.now();

    @InjectMocks
    private StatisticsFactory underTest;

    @Mock
    private StatisticsSchemaService schemaService;

    @Mock
    private CalculationService calculationService;

    @Mock
    private TransactionProvider transactionProvider;

    @Test
    public void testCreateStandardStatistics() {
        // GIVEN
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.STANDARD)
            .build();
        TransactionProviderContext context = createContext();
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        BudgetDetails details = BudgetDetails.builder()
            .build();
        RadialChartData chartData = RadialChartData.builder()
            .build();
        Statistics expected = createStatisticsBuilder(details, chartData, schema);
        Mockito.when(schemaService.getStandardSchema(USER_ID)).thenReturn(schema);
        Mockito.when(transactionProvider.getTransactions(TransactionType.INCOME, context)).thenReturn(incomes);
        Mockito.when(transactionProvider.getTransactions(TransactionType.OUTCOME, context)).thenReturn(outcomes);
        Mockito.when(calculationService.calculateDetails(incomes, outcomes, schema)).thenReturn(details);
        Mockito.when(calculationService.createChartData(incomes, outcomes, schema, details)).thenReturn(chartData);
        // WHEN
        var result = underTest.createStandardStatistics(USER_ID, START, END);

        // THEN
        Mockito.verify(schemaService).getStandardSchema(USER_ID);
        Mockito.verify(transactionProvider).getTransactions(TransactionType.INCOME, context);
        Mockito.verify(transactionProvider).getTransactions(TransactionType.OUTCOME, context);
        Mockito.verify(calculationService).calculateDetails(incomes, outcomes, schema);
        Mockito.verify(calculationService).createChartData(incomes, outcomes, schema, details);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCreateCustomStatistics() {
        // GIVEN
        StatisticsSchema schema = createExampleSchemaBuilder(StatisticsType.SCALE)
            .build();
        TransactionProviderContext context = createContext();
        List<Transaction> incomes = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomes = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        BudgetDetails details = BudgetDetails.builder()
            .build();
        RadialChartData chartData = RadialChartData.builder()
            .build();
        Statistics expected = createStatisticsBuilder(details, chartData, schema);
        Mockito.when(schemaService.getCustomSchemaById(SCHEMA_ID, USER_ID)).thenReturn(schema);
        Mockito.when(transactionProvider.getTransactions(TransactionType.INCOME, context)).thenReturn(incomes);
        Mockito.when(transactionProvider.getTransactions(TransactionType.OUTCOME, context)).thenReturn(outcomes);
        Mockito.when(calculationService.calculateDetails(incomes, outcomes, schema)).thenReturn(details);
        Mockito.when(calculationService.createChartData(incomes, outcomes, schema, details)).thenReturn(chartData);
        // WHEN
        var result = underTest.createCustomStatistics(USER_ID, SCHEMA_ID, START, END);

        // THEN
        Mockito.verify(schemaService).getCustomSchemaById(SCHEMA_ID, USER_ID);
        Mockito.verify(transactionProvider).getTransactions(TransactionType.INCOME, context);
        Mockito.verify(transactionProvider).getTransactions(TransactionType.OUTCOME, context);
        Mockito.verify(calculationService).calculateDetails(incomes, outcomes, schema);
        Mockito.verify(calculationService).createChartData(incomes, outcomes, schema, details);
        Assertions.assertEquals(expected, result);
    }

    private Statistics createStatisticsBuilder(final BudgetDetails details, final RadialChartData chartData,
        final StatisticsSchema schema) {
        return Statistics.builder()
            .withBudgetDetails(details)
            .withSchema(schema)
            .withChartData(chartData)
            .withStartDate(START)
            .withEndDate(END)
            .build();
    }

    private TransactionProviderContext createContext() {
        return TransactionProviderContext.builder()
            .withUserId(USER_ID)
            .withCurrency(DEFAULT_CURRENCY)
            .withStart(START)
            .withEnd(END)
            .build();
    }
}
