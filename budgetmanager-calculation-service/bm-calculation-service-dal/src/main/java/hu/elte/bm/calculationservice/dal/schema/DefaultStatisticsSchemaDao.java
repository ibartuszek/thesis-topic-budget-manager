package hu.elte.bm.calculationservice.dal.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaNotFoundException;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.calculationservice.service.schema.StatisticsSchemaDao;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;

@Component(value = "statisticsSchemaDao")
public class DefaultStatisticsSchemaDao implements StatisticsSchemaDao {

    private final CategoryProvider categoryProvider;
    private final StatisticsSchemaRepository repository;
    private final StatisticsSchemaTransformer transformer;

    @Value("${schema.schema_standard_title:Standard schema}")
    private String standardSchemaTitle;

    @Value("${schema.standard_schema_not_found:Standard schema cannot be found!}")
    private String standardSchemaNotFound;

    @Value("${schema.schema_not_found:Schema cannot be found!!}")
    private String schemaNotFound;

    public DefaultStatisticsSchemaDao(final CategoryProvider categoryProvider,
        final StatisticsSchemaRepository repository, final StatisticsSchemaTransformer transformer) {
        this.categoryProvider = categoryProvider;
        this.repository = repository;
        this.transformer = transformer;
    }

    @Override
    public StatisticsSchema getStandardSchema(final Long userId) {
        return repository.findByTitleAndUserId(standardSchemaTitle, userId)
            .map(entity -> transformer.transformToStatistcisSchema(entity, null, null))
            .orElseThrow(() -> new StatisticsSchemaNotFoundException(standardSchemaTitle, standardSchemaNotFound));
    }

    @Override
    public List<StatisticsSchema> getCustomSchemas(final Long userId) {
        List<StatisticsSchemaEntity> schemaEntityList = getSchemaEntityList(userId);
        Set<Long> mainCategoryIdList = getMainCategoryIdSet(schemaEntityList);
        List<MainCategory> mainCategoryList = categoryProvider.provideMainCategoryList(mainCategoryIdList, userId, TransactionType.OUTCOME);
        return transformList(schemaEntityList, mainCategoryList);
    }

    @Override
    public StatisticsSchema findById(final Long id, final Long userId) {
        StatisticsSchemaEntity entity = findSchemaEntityById(id, userId);
        return createStatisticsSchema(userId, entity);
    }

    @Override
    public Optional<StatisticsSchema> findByTitle(final String title, final Long userId) {
        Optional<StatisticsSchemaEntity> entity = repository.findByTitleAndUserId(title, userId);
        return entity.map(statisticsSchemaEntity -> createStatisticsSchema(userId, statisticsSchemaEntity));
    }

    @Override
    @Transactional
    public StatisticsSchema save(final StatisticsSchema schema, final Long userId) {
        StatisticsSchemaEntity entity = repository.save(transformer.transformToStatisticsSchemaEntity(schema, userId));
        return transformer.transformToStatistcisSchema(entity, schema.getMainCategory(), schema.getSubCategory());
    }

    @Override
    @Transactional
    public StatisticsSchema update(final StatisticsSchema schema, final Long userId) {
        return save(schema, userId);
    }

    @Override
    @Transactional
    public StatisticsSchema delete(final StatisticsSchema schema, final Long userId) {
        repository.delete(transformer.transformToStatisticsSchemaEntity(schema, userId));
        return schema;
    }

    private List<StatisticsSchemaEntity> getSchemaEntityList(final Long userId) {
        return repository.findAllSchema(userId).stream()
            .filter(entity -> !entity.getType().equals(StatisticsType.STANDARD))
            .collect(Collectors.toList());
    }

    private Set<Long> getMainCategoryIdSet(final List<StatisticsSchemaEntity> schemaEntityList) {
        return schemaEntityList.stream()
            .filter(entity -> entity.getMainCategoryId() != null)
            .map(StatisticsSchemaEntity::getMainCategoryId)
            .collect(Collectors.toSet());
    }

    private MainCategory getMainCategory(final Long mainCategoryId, final List<MainCategory> mainCategoryList) {
        return mainCategoryList.stream()
            .filter(mainCategory -> mainCategory.getId().equals(mainCategoryId))
            .findAny()
            .orElse(null);
    }

    private List<StatisticsSchema> transformList(final List<StatisticsSchemaEntity> schemaEntityList, final List<MainCategory> mainCategoryList) {
        List<StatisticsSchema> result = new ArrayList<>();
        for (StatisticsSchemaEntity entity : schemaEntityList) {
            MainCategory mainCategory = getMainCategory(entity.getMainCategoryId(), mainCategoryList);
            SubCategory subCategory = entity.getSubCategoryId() != null
                ? categoryProvider.provideSubCategory(entity.getSubCategoryId(), mainCategory) : null;
            result.add(transformer.transformToStatistcisSchema(entity, mainCategory, subCategory));
        }
        return result;
    }

    private StatisticsSchemaEntity findSchemaEntityById(final Long id, final Long userId) {
        return repository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new StatisticsSchemaNotFoundException(id, schemaNotFound));
    }

    private StatisticsSchema createStatisticsSchema(final Long userId, final StatisticsSchemaEntity entity) {
        MainCategory mainCategory = entity.getMainCategoryId() == null ? null
            : categoryProvider.provideMainCategory(entity.getMainCategoryId(), userId);
        SubCategory subCategory = mainCategory == null || entity.getSubCategoryId() == null ? null
            : categoryProvider.provideSubCategory(entity.getSubCategoryId(), mainCategory);
        return transformer.transformToStatistcisSchema(entity, mainCategory, subCategory);
    }

}
