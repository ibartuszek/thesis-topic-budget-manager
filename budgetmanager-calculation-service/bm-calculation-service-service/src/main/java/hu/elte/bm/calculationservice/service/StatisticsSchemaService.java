package hu.elte.bm.calculationservice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;

@Service
public class StatisticsSchemaService {

    public List<StatisticsSchema> getSchemaList(final LocalDate start, final LocalDate end, final Long userId) {
        // TODO:
        return null;
    }

    public StatisticsSchema save(final StatisticsSchema schema, final Long userId) {
        // TODO:
        return null;
    }

    public StatisticsSchema update(final StatisticsSchema schema, final Long userId) {
        // TODO:
        return null;
    }

    public StatisticsSchema delete(final StatisticsSchema schema, final Long userId) {
        // TODO:
        return null;
    }
}
