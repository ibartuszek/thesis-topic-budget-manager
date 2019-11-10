package hu.elte.bm.calculationservice.web.statistics;

import hu.elte.bm.calculationservice.statistics.Statistics;
import hu.elte.bm.calculationservice.web.common.ResponseModel;

public final class StatisticsResponse extends ResponseModel {

    private Statistics statistics;

    private StatisticsResponse() {
        super();
    }

    private StatisticsResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    static StatisticsResponse createSuccessfulResponse(final Statistics statistics, final String message) {
        StatisticsResponse response = new StatisticsResponse(message, true);
        response.statistics = statistics;
        return response;
    }

    @Override
    public String toString() {
        return "StatisticsSchemaListResponse{"
            + "message='" + getMessage() + '\''
            + ", successful=" + isSuccessful()
            + ", statistics=" + statistics
            + '}';
    }
}
