package hu.elte.bm.calculationservice.service.statistics;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.service.schema.StatisticsSchemaService;
import hu.elte.bm.calculationservice.service.statistics.transactionprovider.TransactionProvider;
import hu.elte.bm.calculationservice.service.statistics.transactionprovider.TransactionProviderContext;
import hu.elte.bm.calculationservice.statistics.Statistics;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class StatisticsFactory {

    private final StatisticsSchemaService schemaService;
    private final CalculationService calculationService;
    private final TransactionProvider transactionProvider;

    public StatisticsFactory(final StatisticsSchemaService schemaService, final CalculationService calculationService,
        final TransactionProvider transactionProvider) {
        this.schemaService = schemaService;
        this.calculationService = calculationService;
        this.transactionProvider = transactionProvider;
    }

    public Statistics createStandardStatistics(final Long userId, final LocalDate start, final LocalDate end) {
        StatisticsSchema schema = schemaService.getStandardSchema(userId);
        TransactionProviderContext context = createContext(userId, start, end, schema);
        return createStatistics(schema, context);
    }

    public Statistics createCustomStatistics(final Long userId, final Long schemaId, final LocalDate start, final LocalDate end) {
        StatisticsSchema schema = schemaService.getCustomSchemaById(schemaId, userId);
        TransactionProviderContext context = createContext(userId, start, end, schema);
        return createStatistics(schema, context);
    }

    private Statistics createStatistics(final StatisticsSchema schema, final TransactionProviderContext context) {
        List<Transaction> incomes = transactionProvider.getTransactions(TransactionType.INCOME, context);
        List<Transaction> outcomes = transactionProvider.getTransactions(TransactionType.OUTCOME, context);
        BudgetDetails details = calculationService.calculateDetails(incomes, outcomes, schema);
        return Statistics.builder()
            .withSchema(schema)
            .withStartDate(context.getStart())
            .withEndDate(context.getEnd())
            .withChartData(calculationService.createChartData(incomes, outcomes, schema, details))
            .withBudgetDetails(details)
            .build();
    }

    private TransactionProviderContext createContext(final Long userId, final LocalDate start, final LocalDate end, final StatisticsSchema schema) {
        return TransactionProviderContext.builder()
            .withUserId(userId)
            .withCurrency(schema.getCurrency())
            .withStart(start)
            .withEnd(end)
            .build();
    }

}
