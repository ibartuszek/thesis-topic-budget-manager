package hu.elte.bm.calculationservice.app.test.statistics;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import hu.elte.bm.calculationservice.app.test.AbstractCalculationServiceApplicationTest;
import hu.elte.bm.calculationservice.app.test.utils.TransactionServiceContext;
import hu.elte.bm.transactionservice.TransactionType;

public abstract class AbstractStatisticsTest extends AbstractCalculationServiceApplicationTest {

    protected static final Long CUSTOM_SCHEMA_ID = 2L;
    protected static final String FIND_ALL_INCOME_FILE = "findAllIncomeTransactionWithResponseOk.json";
    protected static final String FIND_ALL_OUTCOME_FILE = "findAllOutcomeTransactionWithResponseOk.json";
    protected static final String FIND_ALL_TRANSACTION_WITH_EMPTY_BODY = "findAllTransactionWithEmptyList.json";
    protected static final String GET_EXCHANGE_RATES_FILE = "getExchangeRatesWithResponseOk.json";

    protected static Stream<Arguments> getStandardStatisticsParametersForValidation() {
        return Stream.of(
            Arguments.of(null, START.toString(), END.toString(), "Required Long parameter 'userId' is not present"),
            Arguments.of(USER_ID.toString(), null, END.toString(), "Required LocalDate parameter 'startDate' is not present"),
            Arguments.of(USER_ID.toString(), START.toString(), null, "Required LocalDate parameter 'endDate' is not present")
        );
    }

    protected static Stream<Arguments> getCustomStatisticsParametersForValidation() {
        return Stream.of(
            Arguments.of(null, CUSTOM_SCHEMA_ID.toString(), START.toString(), END.toString(), "Required Long parameter 'userId' is not present"),
            Arguments.of(USER_ID.toString(), null, START.toString(), END.toString(), "Required Long parameter 'schemaId' is not present"),
            Arguments.of(USER_ID.toString(), CUSTOM_SCHEMA_ID.toString(), null, END.toString(), "Required LocalDate parameter 'startDate' is not present"),
            Arguments.of(USER_ID.toString(), CUSTOM_SCHEMA_ID.toString(), START.toString(), null, "Required LocalDate parameter 'endDate' is not present")
        );
    }

    protected TransactionServiceContext.Builder createDefaultContextBuilder() {
        return TransactionServiceContext.builder()
            .withType(TRANSACTION_TYPE)
            .withUserId(USER_ID)
            .withStart(START)
            .withEnd(END);
    }

    protected TransactionServiceContext createDefaultContext(final TransactionType type) {
        return createDefaultContextBuilder()
            .withType(type)
            .build();
    }

}
