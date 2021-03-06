package hu.elte.bm.calculationservice.exceptions.schema;

import hu.elte.bm.calculationservice.schema.StatisticsSchema;

public class IllegalStatisticsSchemaException extends RuntimeException implements StatisticsSchemaException {

    private final StatisticsSchema schema;

    public IllegalStatisticsSchemaException(final StatisticsSchema schema, final String message) {
        super(message);
        this.schema = schema;
    }

    @Override
    public StatisticsSchema getSchema() {
        return schema;
    }
}
