package hu.elte.bm.calculationservice.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.dal.schema.StatisticsSchemaEntity;
import hu.elte.bm.calculationservice.dal.schema.StatisticsSchemaRepository;
import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaNotFoundException;
import hu.elte.bm.calculationservice.service.StatisticsSchemaDao;
import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.transactionserviceclient.TransactionServiceFacade;
import hu.elte.bm.transactionservice.MainCategory;

@Component(value = "statisticsSchemaDao")
public class DefaultStatisticsSchemaDao implements StatisticsSchemaDao {

    @Value("${schema.schema_standard_title:Standard schema}")
    private String standardSchemaTitle;

    @Value("${schema.standard_schema_not_found:Standard schema cannot be found!}")
    private String standardSchemaNotFound;

    private final StatisticsSchemaRepository repository;
    private final TransactionServiceFacade transactionServiceFacade;

    public DefaultStatisticsSchemaDao(final TransactionServiceFacade transactionServiceFacade, final StatisticsSchemaRepository repository) {
        this.transactionServiceFacade = transactionServiceFacade;
        this.repository = repository;
    }

    @Override
    public StatisticsSchema getStandardSchema(final Long userId) {
        StatisticsSchemaEntity standardSchema = repository.findByTitle(standardSchemaTitle, userId)
                .orElseThrow(() -> new StatisticsSchemaNotFoundException(null, standardSchemaNotFound));

        return null;
    }

    @Override
    public List<StatisticsSchema> getCustomSchemas(final Long userId) {
        return null;
    }

    @Override
    public Optional<StatisticsSchema> findById(final Long id, final Long userId) {
        return Optional.empty();
    }

    @Override
    public List<StatisticsSchema> findByTitle(final StatisticsSchema schema, final Long userId) {
        return null;
    }

    @Override
    public StatisticsSchema save(final StatisticsSchema schema, final Long userId) {
        return null;
    }

    @Override
    public StatisticsSchema update(final StatisticsSchema schema, final Long userId) {
        return null;
    }

    @Override
    public StatisticsSchema delete(final StatisticsSchema schema, final Long userId) {
        return null;
    }
}
