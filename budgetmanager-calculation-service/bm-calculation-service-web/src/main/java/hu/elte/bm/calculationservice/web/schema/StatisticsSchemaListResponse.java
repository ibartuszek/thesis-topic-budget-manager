package hu.elte.bm.calculationservice.web.schema;

import java.util.List;

import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.web.common.ResponseModel;

public final class StatisticsSchemaListResponse extends ResponseModel {

    private StatisticsSchema standardSchema;
    private List<StatisticsSchema> customSchemas;

    private StatisticsSchemaListResponse() {
        super();
    }

    private StatisticsSchemaListResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    static StatisticsSchemaListResponse createSuccessfulResponse(final StatisticsSchema standardSchema, final List<StatisticsSchema> customSchemas) {
        return createSuccessfulResponse(standardSchema, customSchemas, null);
    }

    static StatisticsSchemaListResponse createSuccessfulResponse(final StatisticsSchema standardSchema, final List<StatisticsSchema> customSchemas,
        final String message) {
        StatisticsSchemaListResponse response = new StatisticsSchemaListResponse(message, true);
        response.standardSchema = standardSchema;
        response.customSchemas = customSchemas;
        return response;
    }

    public StatisticsSchema getStandardSchema() {
        return standardSchema;
    }

    public void setStandardSchema(final StatisticsSchema standardSchema) {
        this.standardSchema = standardSchema;
    }

    public List<StatisticsSchema> getCustomSchemas() {
        return customSchemas;
    }

    public void setCustomSchemas(final List<StatisticsSchema> customSchemas) {
        this.customSchemas = customSchemas;
    }

    @Override
    public String toString() {
        return "StatisticsSchemaListResponse{"
            + "message='" + getMessage() + '\''
            + ", successful=" + isSuccessful()
            + ", customSchemas=" + customSchemas
            + '}';
    }
}
