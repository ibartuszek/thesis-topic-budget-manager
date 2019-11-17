package hu.elte.bm.calculationservice.service.statistics.chartdata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.chartdata.ChartData;
import hu.elte.bm.calculationservice.chartdata.QuadraticChartData;
import hu.elte.bm.calculationservice.chartdata.QuadraticPoint;
import hu.elte.bm.transactionservice.Transaction;

@Component
public class SumChartDataCalculator {

    ChartData calculateSumChartData(final List<Transaction> outcomes, final String title) {
        return QuadraticChartData.builder()
            .withLegend(title)
            .withDataPoints(createDataPoints(outcomes))
            .build();
    }

    private List<QuadraticPoint> createDataPoints(final List<Transaction> outcomes) {
        int index = 0;
        int amount = 0;
        List<QuadraticPoint> dataPoints = new ArrayList<>();
        for (Transaction transaction : outcomes) {
            amount += transaction.getAmount();
            dataPoints.add(QuadraticPoint.builder()
                .withX(index++)
                .withY(amount)
                .withLabel(transaction.getTitle())
                .withDate(transaction.getDate())
                .build());
        }
        return dataPoints;
    }
}
