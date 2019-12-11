package hu.elte.bm.calculationservice.service.statistics.chartdata;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.elte.bm.calculationservice.chartdata.ChartData;
import hu.elte.bm.calculationservice.chartdata.QuadraticChartData;
import hu.elte.bm.calculationservice.chartdata.QuadraticPoint;
import hu.elte.bm.calculationservice.service.statistics.AbstractCalculatorTest;
import hu.elte.bm.calculationservice.service.statistics.RounderUtil;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.Transaction;

public class SumChartDataCalculatorTest extends AbstractCalculatorTest {

    private static final String CHART_LABEL = "Sum";

    private final SumChartDataCalculator underTest = new SumChartDataCalculator(new RounderUtil());

    @Test
    public void testCalculateSumChartData() {
        // GIVEN
        List<Transaction> transactions = createExampleTransactionList();
        QuadraticPoint expectedFirstPoint = QuadraticPoint.builder()
            .withX(0)
            .withY(DEFAULT_TRANSACTION_AMOUNT)
            .withDate(transactions.get(0).getDate())
            .withLabel(transactions.get(0).getTitle())
            .build();
        QuadraticPoint expectedSecondPoint = QuadraticPoint.builder()
            .withX(1)
            .withY(DEFAULT_TRANSACTION_AMOUNT + OTHER_TRANSACTION_AMOUNT)
            .withDate(transactions.get(1).getDate())
            .withLabel(transactions.get(1).getTitle())
            .build();
        ChartData expected = QuadraticChartData.builder()
            .withLegend(CHART_LABEL)
            .withDataPoints(List.of(expectedFirstPoint, expectedSecondPoint))
            .build();

        // WHEN
        var result = underTest.calculateSumChartData(transactions, CHART_LABEL);

        // THEN
        Assertions.assertEquals(expected, result);
    }

    private List<Transaction> createExampleTransactionList() {
        MainCategory mainCategory = createExampleMainCategoryBuilder(DEFAULT_TRANSACTION_TYPE)
            .build();
        Transaction first = createExampleTransactionBuilder(DEFAULT_TRANSACTION_TYPE, DEFAULT_CURRENCY)
            .withMainCategory(mainCategory)
            .build();
        Transaction second = createOtherExampleTransactionBuilder(DEFAULT_TRANSACTION_TYPE, DEFAULT_CURRENCY)
            .withMainCategory(mainCategory)
            .build();
        return List.of(first, second);
    }

}
