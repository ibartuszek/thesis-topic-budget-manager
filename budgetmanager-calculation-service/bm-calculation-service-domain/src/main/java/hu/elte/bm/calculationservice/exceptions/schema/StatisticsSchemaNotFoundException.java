package hu.elte.bm.calculationservice.exceptions.schema;

import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;

public class StatisticsSchemaNotFoundException extends RuntimeException implements StatisticsSchemaException {

    private final StatisticsSchema schema;
    private final Long schemaId;
    private final String schemaTitle;

    public StatisticsSchemaNotFoundException(final StatisticsSchema schema, final String message) {
        super(message);
        this.schema = schema;
        this.schemaId = null;
        this.schemaTitle = null;
    }

    public StatisticsSchemaNotFoundException(final Long schemaId, final String message) {
        super(message);
        this.schema = null;
        this.schemaId = schemaId;
        this.schemaTitle = null;
    }

    public StatisticsSchemaNotFoundException(final String schemaTitle, final String message) {
        super(message);
        this.schema = null;
        this.schemaId = null;
        this.schemaTitle = schemaTitle;
    }

    @Override
    public StatisticsSchema getSchema() {
        return schema;
    }

    public Long getSchemaId() {
        return schemaId;
    }

    public String getSchemaTitle() {
        return schemaTitle;
    }
}
