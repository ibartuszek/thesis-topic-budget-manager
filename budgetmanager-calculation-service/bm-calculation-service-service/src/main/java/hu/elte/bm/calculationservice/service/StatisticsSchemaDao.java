package hu.elte.bm.calculationservice.service;

import java.util.List;
import java.util.Optional;

import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;

public interface StatisticsSchemaDao  {

    StatisticsSchema getStandardSchema();

    List<StatisticsSchema> getCustomSchemas(Long userId);

    Optional<StatisticsSchema> findById(Long id, Long userId);

    List<StatisticsSchema> findByTitle(StatisticsSchema schema, Long userId);

    StatisticsSchema save(StatisticsSchema schema, Long userId);

    StatisticsSchema update(StatisticsSchema schema, Long userId);

    StatisticsSchema delete(StatisticsSchema schema, Long userId);

}
