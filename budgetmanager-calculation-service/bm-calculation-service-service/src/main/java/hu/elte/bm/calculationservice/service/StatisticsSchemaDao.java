package hu.elte.bm.calculationservice.service;

import java.util.List;
import java.util.Optional;

import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;

public interface StatisticsSchemaDao {

    StatisticsSchema getStandardSchema(Long userId);

    List<StatisticsSchema> getCustomSchemas(Long userId);

    StatisticsSchema findById(Long id, Long userId);

    Optional<StatisticsSchema> findByTitle(String title, Long userId);

    StatisticsSchema save(StatisticsSchema schema, Long userId);

    StatisticsSchema update(StatisticsSchema schema, Long userId);

    StatisticsSchema delete(StatisticsSchema schema, Long userId);

}
