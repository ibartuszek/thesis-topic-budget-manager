package hu.elte.bm.calculationservice.service.statistics;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.service.schema.StatisticsSchemaService;
import hu.elte.bm.calculationservice.statistics.Statistics;
import hu.elte.bm.calculationservice.transactionserviceclient.TransactionServiceFacade;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class StatisticsFactory {

    private final StatisticsSchemaService schemaService;
    private final CalculationService calculationService;
    private final TransactionServiceFacade transactionServiceFacade;

    public StatisticsFactory(final StatisticsSchemaService schemaService, final CalculationService calculationService,
        final TransactionServiceFacade transactionServiceFacade) {
        this.schemaService = schemaService;
        this.calculationService = calculationService;
        this.transactionServiceFacade = transactionServiceFacade;
    }

    public Statistics createStandardStatistics(final Long userId, final LocalDate start, final LocalDate end) {
        StatisticsSchema schema = schemaService.getStandardSchema(userId);
        List<Transaction> incomes = transactionServiceFacade.getTransactions(TransactionType.INCOME, userId, start, end);
        List<Transaction> outcomes = transactionServiceFacade.getTransactions(TransactionType.OUTCOME, userId, start, end);
        BudgetDetails details = calculationService.calculateDetails(incomes, outcomes, schema);
        return Statistics.builder()
            .withSchema(schema)
            .withStartDate(start)
            .withEndDate(end)
            //            .withChartData(calculationService.createChartData(schema, userId, ))
            .withBudgetDetails(details)
            .build();
    }

    public Statistics createCustomStatistics(final Long userId, final Long schemaId, final LocalDate start, final LocalDate end) {
        // TODO:
        return null;
    }
}
