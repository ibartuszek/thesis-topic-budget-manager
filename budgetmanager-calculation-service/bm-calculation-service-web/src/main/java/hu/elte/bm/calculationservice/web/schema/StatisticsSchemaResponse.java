package hu.elte.bm.calculationservice.web.schema;

import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.web.common.ResponseModel;

public final class StatisticsSchemaResponse extends ResponseModel {

    private StatisticsSchema schema;

    private StatisticsSchemaResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    static StatisticsSchemaResponse createSuccessfulResponse(final StatisticsSchema schema, final String message) {
        StatisticsSchemaResponse response = new StatisticsSchemaResponse(message, true);
        response.schema = schema;
        return response;
    }

    public StatisticsSchema getSchema() {
        return schema;
    }

    public void setSchema(final StatisticsSchema schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "StatisticsSchemaResponse{"
            + "message='" + getMessage() + '\''
            + ", successful=" + isSuccessful()
            + ", schema=" + schema
            + '}';
    }
}
