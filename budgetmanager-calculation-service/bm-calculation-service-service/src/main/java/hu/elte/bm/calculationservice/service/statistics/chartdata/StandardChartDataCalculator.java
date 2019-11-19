package hu.elte.bm.calculationservice.service.statistics.chartdata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.budgetdetails.TransactionData;
import hu.elte.bm.calculationservice.chartdata.ChartData;
import hu.elte.bm.calculationservice.chartdata.RadialChartData;
import hu.elte.bm.calculationservice.chartdata.SectorPoint;
import hu.elte.bm.calculationservice.service.statistics.RounderUtil;

@Component
public class StandardChartDataCalculator {

    private final RounderUtil rounderUtil;

    @Value("${statistics.standard_chart_data_label:Expenses}")
    private String standardChartDataLabel;

    public StandardChartDataCalculator(final RounderUtil rounderUtil) {
        this.rounderUtil = rounderUtil;
    }

    ChartData calculateStandardChartData(final List<TransactionData> outcomes) {
        return RadialChartData.builder()
            .withLegend(standardChartDataLabel)
            .withSectorPoints(createStandardSectorPoints(outcomes))
            .build();
    }

    private List<SectorPoint> createStandardSectorPoints(final List<TransactionData> outcomes) {
        List<SectorPoint> sectorPoints = new ArrayList<>();
        outcomes.stream().filter(transactionData -> transactionData.getSubCategoryName() == null)
            .forEach(outcome -> sectorPoints.add(SectorPoint.builder()
                .withLabel(outcome.getMainCategoryName())
                .withAngle(rounderUtil.round(outcome.getAmount()))
                .build()));
        return sectorPoints;
    }

}
