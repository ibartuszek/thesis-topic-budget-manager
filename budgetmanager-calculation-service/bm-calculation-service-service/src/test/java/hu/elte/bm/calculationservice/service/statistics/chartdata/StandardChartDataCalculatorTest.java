package hu.elte.bm.calculationservice.service.statistics.chartdata;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import hu.elte.bm.calculationservice.budgetdetails.TransactionData;
import hu.elte.bm.calculationservice.chartdata.RadialChartData;
import hu.elte.bm.calculationservice.chartdata.SectorPoint;
import hu.elte.bm.calculationservice.service.statistics.AbstractCalculatorTest;
import hu.elte.bm.calculationservice.service.statistics.RounderUtil;

public class StandardChartDataCalculatorTest extends AbstractCalculatorTest {

    private static final String CHART_LABEL = "Expenses";

    private final StandardChartDataCalculator underTest = new StandardChartDataCalculator(new RounderUtil());

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(underTest, "standardChartDataLabel", CHART_LABEL);
    }

    @Test
    public void testCalculateStandardChartData() {
        // GIVEN
        TransactionData first = TransactionData.builder()
            .withMainCategoryName(DEFAULT_MAIN_CATEGORY_NAME)
            .withAmount(OTHER_TRANSACTION_AMOUNT)
            .build();
        TransactionData second = TransactionData.builder()
            .withMainCategoryName(DEFAULT_MAIN_CATEGORY_NAME)
            .withSubCategoryName(DEFAULT_SUB_CATEGORY_NAME)
            .withAmount(DEFAULT_TRANSACTION_AMOUNT)
            .build();
        TransactionData third = TransactionData.builder()
            .withMainCategoryName(OTHER_MAIN_CATEGORY_NAME)
            .withAmount(ANOTHER_TRANSACTION_AMOUNT)
            .build();
        List<TransactionData> outcomes = List.of(first, second, third);
        SectorPoint firstSectorPoint = SectorPoint.builder()
            .withAngle(OTHER_TRANSACTION_AMOUNT)
            .withLabel(DEFAULT_MAIN_CATEGORY_NAME)
            .build();
        SectorPoint secondSectorPoint = SectorPoint.builder()
            .withAngle(ANOTHER_TRANSACTION_AMOUNT)
            .withLabel(OTHER_MAIN_CATEGORY_NAME)
            .build();
        RadialChartData expected = RadialChartData.builder()
            .withLegend(CHART_LABEL)
            .withSectorPoints(List.of(firstSectorPoint, secondSectorPoint))
            .build();

        // WHEN
        var result = underTest.calculateStandardChartData(outcomes);

        // THEN
        Assertions.assertEquals(expected, result);
    }

}
