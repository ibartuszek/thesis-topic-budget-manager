package hu.elte.bm.calculationservice.service.statistics.chartdata;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.elte.bm.calculationservice.budgetdetails.TransactionData;
import hu.elte.bm.calculationservice.chartdata.QuadraticChartData;
import hu.elte.bm.calculationservice.chartdata.RadialChartData;
import hu.elte.bm.calculationservice.service.statistics.AbstractCalculatorTest;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@ExtendWith(MockitoExtension.class)
public class ChartDataCalculatorTest extends AbstractCalculatorTest {

    private static final String STANDARD_CHART_LEGEND = "Standard";
    private static final String SCALE_CHART_LEGEND = "Scale";
    private static final String SUM_CHART_LEGEND = "Sum";
    private static final boolean MONTHLY = true;
    private static final LocalDate MONTHLY_TRANSACTION_START_DATE = DEFAULT_TRANSACTION_DATE.minusDays(30);

    @InjectMocks
    private ChartDataCalculator underTest;

    @Mock
    private StandardChartDataCalculator standardCalculator;

    @Mock
    private ScaleChartDataCalculator scaleCalculator;

    @Mock
    private SumChartDataCalculator sumCalculator;

    @Test
    public void testCalculateStandardChartData() {
        // GIVEN
        List<TransactionData> outcomes = new ArrayList<>();
        RadialChartData expected = RadialChartData.builder()
            .withSectorPoints(new ArrayList<>())
            .withLegend(STANDARD_CHART_LEGEND)
            .build();
        Mockito.when(standardCalculator.calculateStandardChartData(outcomes)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateStandardChartData(outcomes);

        // THEN
        Mockito.verify(standardCalculator).calculateStandardChartData(outcomes);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateScaleChartData() {
        // GIVEN
        List<Transaction> incomes = new ArrayList<>(createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY));
        Transaction monthlyIncome = createExampleTransactionBuilder(TransactionType.INCOME, DEFAULT_CURRENCY)
            .withMainCategory(incomes.get(0).getMainCategory())
            .withMonthly(MONTHLY)
            .withDate(MONTHLY_TRANSACTION_START_DATE)
            .build();
        incomes.add(monthlyIncome);
        List<Transaction> outcomes = createOtherExampleList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        List<Transaction> orderedList = List.of(incomes.get(0), outcomes.get(0), incomes.get(1), incomes.get(3), outcomes.get(1), incomes.get(2));
        QuadraticChartData expected = QuadraticChartData.builder()
            .withLegend(SCALE_CHART_LEGEND)
            .withDataPoints(new ArrayList<>())
            .build();
        Mockito.when(scaleCalculator.calculateScaleChartData(orderedList, SCALE_CHART_LEGEND)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateScaleChartData(incomes, outcomes, SCALE_CHART_LEGEND);

        // THEN
        Mockito.verify(scaleCalculator).calculateScaleChartData(orderedList, SCALE_CHART_LEGEND);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCalculateSumChartData() {
        // GIVEN
        List<Transaction> outcomes = createOtherExampleList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        QuadraticChartData expected = QuadraticChartData.builder()
            .withLegend(SUM_CHART_LEGEND)
            .withDataPoints(new ArrayList<>())
            .build();
        Mockito.when(sumCalculator.calculateSumChartData(outcomes, SUM_CHART_LEGEND)).thenReturn(expected);

        // WHEN
        var result = underTest.calculateSumChartData(outcomes, SUM_CHART_LEGEND);

        // THEN
        Mockito.verify(sumCalculator).calculateSumChartData(outcomes, SUM_CHART_LEGEND);
        Assertions.assertEquals(expected, result);
    }

}
