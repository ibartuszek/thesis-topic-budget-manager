package hu.elte.bm.calculationservice.web;

import java.util.List;

import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.web.common.ResponseModel;

public final class StatisticsSchemaListResponse extends ResponseModel  {

    private List<StatisticsSchema> schemaList;

    private StatisticsSchemaListResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    static StatisticsSchemaListResponse createSuccessfulResponse(final List<StatisticsSchema> schemaList) {
        return createSuccessfulResponse(schemaList, null);
    }

    static StatisticsSchemaListResponse createSuccessfulResponse(final List<StatisticsSchema> schemaList, final String message) {
        StatisticsSchemaListResponse response = new StatisticsSchemaListResponse(message, true);
        response.schemaList = schemaList;
        return response;
    }

    public List<StatisticsSchema> getSchemaList() {
        return schemaList;
    }

    public void setSchemaList(final List<StatisticsSchema> schemaList) {
        this.schemaList = schemaList;
    }

    @Override
    public String toString() {
        return "StatisticsSchemaListResponse{"
                + "message='" + getMessage() + '\''
                + ", successful=" + isSuccessful()
                + ", schemaList=" + schemaList
                + '}';
    }
}
