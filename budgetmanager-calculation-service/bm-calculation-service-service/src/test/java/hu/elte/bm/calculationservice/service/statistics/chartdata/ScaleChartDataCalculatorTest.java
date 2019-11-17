package hu.elte.bm.calculationservice.service.statistics.chartdata;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.elte.bm.calculationservice.chartdata.ChartData;
import hu.elte.bm.calculationservice.chartdata.QuadraticChartData;
import hu.elte.bm.calculationservice.chartdata.QuadraticPoint;
import hu.elte.bm.calculationservice.service.statistics.AbstractCalculatorTest;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

public class ScaleChartDataCalculatorTest extends AbstractCalculatorTest {

    private static final String CHART_LABEL = "Scale";

    private final ScaleChartDataCalculator underTest = new ScaleChartDataCalculator();

    @Test
    public void testCalculateScaleChartData() {
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
        QuadraticPoint expectedThirdPoint = QuadraticPoint.builder()
            .withX(2)
            .withY(DEFAULT_TRANSACTION_AMOUNT + OTHER_TRANSACTION_AMOUNT - ANOTHER_TRANSACTION_AMOUNT)
            .withDate(transactions.get(2).getDate())
            .withLabel(transactions.get(2).getTitle())
            .build();
        ChartData expected = QuadraticChartData.builder()
            .withLegend(CHART_LABEL)
            .withDataPoints(List.of(expectedFirstPoint, expectedSecondPoint, expectedThirdPoint))
            .build();

        // WHEN
        var result = underTest.calculateScaleChartData(transactions, CHART_LABEL);

        // THEN
        Assertions.assertEquals(expected, result);
    }

    private List<Transaction> createExampleTransactionList() {
        Transaction firstIncome = createExampleTransactionBuilder(TransactionType.INCOME, DEFAULT_CURRENCY)
            .build();
        Transaction secondIncome = createOtherExampleTransactionBuilder(TransactionType.INCOME, DEFAULT_CURRENCY)
            .build();
        Transaction thirdOutcome = createExampleTransactionBuilder(TransactionType.OUTCOME, DEFAULT_CURRENCY)
            .withAmount(ANOTHER_TRANSACTION_AMOUNT)
            .build();
        return List.of(firstIncome, secondIncome, thirdOutcome);
    }

}
