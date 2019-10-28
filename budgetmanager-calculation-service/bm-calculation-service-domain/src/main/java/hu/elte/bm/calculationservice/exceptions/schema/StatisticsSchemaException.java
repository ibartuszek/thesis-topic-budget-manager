package hu.elte.bm.calculationservice.exceptions.schema;

import hu.elte.bm.calculationservice.exceptions.CustomException;
import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;

public interface StatisticsSchemaException extends CustomException {

    StatisticsSchema getSchema();

}
