package hu.elte.bm.calculationservice.service.statistics.chartdata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.chartdata.ChartData;
import hu.elte.bm.calculationservice.chartdata.QuadraticChartData;
import hu.elte.bm.calculationservice.chartdata.QuadraticPoint;
import hu.elte.bm.calculationservice.service.statistics.RounderUtil;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class ScaleChartDataCalculator {

    private final RounderUtil rounderUtil;

    public ScaleChartDataCalculator(final RounderUtil rounderUtil) {
        this.rounderUtil = rounderUtil;
    }

    ChartData calculateScaleChartData(final List<Transaction> transactionList, final String title) {
        return QuadraticChartData.builder()
            .withLegend(title)
            .withDataPoints(createDataPoints(transactionList))
            .build();
    }

    private List<QuadraticPoint> createDataPoints(final List<Transaction> transactionList) {
        int index = 0;
        double amount = 0;
        List<QuadraticPoint> dataPoints = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            amount = transaction.getTransactionType().equals(TransactionType.INCOME)
                ? amount + transaction.getAmount() : amount - transaction.getAmount();
            dataPoints.add(QuadraticPoint.builder()
                .withX(index++)
                .withY(rounderUtil.round(amount))
                .withDate(transaction.getDate())
                .withLabel(transaction.getTitle())
                .build());
        }
        return dataPoints;
    }
}
