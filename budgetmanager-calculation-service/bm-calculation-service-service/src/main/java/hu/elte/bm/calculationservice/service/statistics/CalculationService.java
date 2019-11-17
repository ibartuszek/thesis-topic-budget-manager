package hu.elte.bm.calculationservice.service.statistics;

import java.util.List;

import org.springframework.stereotype.Service;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.chartdata.ChartData;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.calculationservice.service.statistics.budgetdetails.BudgetCalculatorProxy;
import hu.elte.bm.calculationservice.service.statistics.chartdata.ChartDataCalculator;
import hu.elte.bm.transactionservice.Transaction;

@Service
public class CalculationService {

    private final BudgetCalculatorProxy budgetCalculatorProxy;
    private final ChartDataCalculator chartDataCalculator;

    public CalculationService(final BudgetCalculatorProxy budgetCalculatorProxy,
        final ChartDataCalculator chartDataCalculator) {
        this.budgetCalculatorProxy = budgetCalculatorProxy;
        this.chartDataCalculator = chartDataCalculator;
    }

    BudgetDetails calculateDetails(final List<Transaction> incomes, final List<Transaction> outcomes, final StatisticsSchema schema) {
        BudgetDetails result;
        if (schema.getType().equals(StatisticsType.STANDARD)) {
            result = budgetCalculatorProxy.calculateStandardDetails(incomes, outcomes);
        } else if (schema.getType().equals(StatisticsType.SCALE)) {
            result = budgetCalculatorProxy.calculateScaleDetails(incomes, outcomes, schema);
        } else {
            result = budgetCalculatorProxy.calculateSumDetails(incomes, outcomes, schema);
        }
        return result;
    }

    ChartData createChartData(final List<Transaction> incomes, final List<Transaction> outcomes, final StatisticsSchema schema,
        final BudgetDetails details) {
        ChartData result;
        if (schema.getType().equals(StatisticsType.STANDARD)) {
            result = chartDataCalculator.calculateStandardChartData(details.getOutcomes());
        } else if (schema.getType().equals(StatisticsType.SCALE)) {
            List<Transaction> filteredOutcomeList = budgetCalculatorProxy.createFilteredTransactionList(outcomes, schema);
            result = chartDataCalculator.calculateScaleChartData(incomes, filteredOutcomeList, schema.getTitle());
        } else {
            List<Transaction> filteredOutcomeList = budgetCalculatorProxy.createFilteredTransactionList(outcomes, schema);
            result = chartDataCalculator.calculateSumChartData(filteredOutcomeList, schema.getTitle());
        }
        return result;
    }

}
