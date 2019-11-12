package hu.elte.bm.calculationservice.service.statistics;

import java.util.List;

import org.springframework.stereotype.Service;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.calculationservice.service.statistics.budgetdetails.BudgetCalculatorProxy;
import hu.elte.bm.transactionservice.Transaction;

@Service
public class CalculationService {

    private final BudgetCalculatorProxy budgetCalculatorProxy;
    // private final ChartDataCalculator chartDataCalculator;

    public CalculationService(final BudgetCalculatorProxy budgetCalculatorProxy) {
        this.budgetCalculatorProxy = budgetCalculatorProxy;
    }

    public BudgetDetails calculateDetails(final List<Transaction> incomes, final List<Transaction> outcomes, final StatisticsSchema schema) {
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
}
