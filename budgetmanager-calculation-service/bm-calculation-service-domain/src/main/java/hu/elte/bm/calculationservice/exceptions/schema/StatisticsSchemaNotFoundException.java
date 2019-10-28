package hu.elte.bm.calculationservice.exceptions.schema;

import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;

public class StatisticsSchemaNotFoundException extends RuntimeException implements StatisticsSchemaException {

    private final StatisticsSchema schema;

    public StatisticsSchemaNotFoundException(final StatisticsSchema schema, final String message) {
        super(message);
        this.schema = schema;
    }

    @Override
    public StatisticsSchema getSchema() {
        return schema;
    }
}
