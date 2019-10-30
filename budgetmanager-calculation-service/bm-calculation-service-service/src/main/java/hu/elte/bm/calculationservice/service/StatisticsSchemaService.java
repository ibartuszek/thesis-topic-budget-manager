package hu.elte.bm.calculationservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.calculationservice.exceptions.schema.IllegalStatisticsSchemaException;
import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaConflictException;
import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaNotFoundException;
import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.transactionserviceclient.TransactionServiceFacade;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;

@Service
public class StatisticsSchemaService {

    private final StatisticsSchemaDao schemaDao;
    private final TransactionServiceFacade transactionServiceFacade;

    @Value("${schema.schema_id_cannot_be_null:Schema's id cannot be null!}")
    private String schemaIdCannotBeNull;

    @Value("${schema.schema_id_must_be_null:Schema's id must be null!}")
    private String schemaIdMustBeNull;

    @Value("${schema.schema_not_changed:Schema has no changes!}")
    private String schemaNotChanged;

    @Value("${schema.schema_not_found:Schema cannot be found!}")
    private String schemaNotFound;

    @Value("${schema.schema_main_category_cannot_be_found:Schema's main category cannot be found!}")
    private String schemaMainCategoryCannotBeFound;

    @Value("${schema.schema_sub_category_cannot_be_found:Schema's supplementary category cannot be found!}")
    private String schemaSubCategoryCannotBeFound;

    @Value("${schema.schema_title_is_reserved:Schema's title is reserved!}")
    private String schemaTitleIsReserved;

    public StatisticsSchemaService(final StatisticsSchemaDao schemaDao, final TransactionServiceFacade transactionServiceFacade) {
        this.schemaDao = schemaDao;
        this.transactionServiceFacade = transactionServiceFacade;
    }

    public StatisticsSchema getStandardSchema() {
        return schemaDao.getStandardSchema();
    }

    public List<StatisticsSchema> getCustomSchemas(final Long userId) {
        return schemaDao.getCustomSchemas(userId);
    }

    public StatisticsSchema save(final StatisticsSchema schema, final Long userId) {
        validateForSave(schema, userId);
        validateCategories(schema, userId);
        return schemaDao.save(schema, userId);
    }

    public StatisticsSchema update(final StatisticsSchema schema, final Long userId) {
        validateForUpdate(schema, userId);
        validateCategories(schema, userId);
        return schemaDao.update(schema, userId);
    }

    public StatisticsSchema delete(final StatisticsSchema schema, final Long userId) {
        validateForDelete(schema, userId);
        return schemaDao.delete(schema, userId);
    }

    private void validateForSave(final StatisticsSchema schema, final Long userId) {
        Assert.isNull(schema.getId(), schemaIdMustBeNull);
        List<StatisticsSchema> schemaListWithSameTitle = schemaDao.findByTitle(schema, userId);
        if (!schemaListWithSameTitle.isEmpty()) {
            throw new StatisticsSchemaConflictException(schema, schemaTitleIsReserved);
        }
    }

    private void validateCategories(final StatisticsSchema schema, final Long userId) {
        if (schema.getMainCategory() != null) {
            List<MainCategory> mainCategoryList = transactionServiceFacade.getMainCategories(TransactionType.OUTCOME, userId);
            if (!mainCategoryList.contains(schema.getMainCategory())) {
                throw new IllegalStatisticsSchemaException(schema, schemaMainCategoryCannotBeFound);
            }
        }
        if (schema.getSubCategory() != null) {
            List<SubCategory> subCategoryList = transactionServiceFacade.getSubCategories(TransactionType.OUTCOME, userId);
            if (!subCategoryList.contains(schema.getSubCategory())) {
                throw new IllegalStatisticsSchemaException(schema, schemaSubCategoryCannotBeFound);
            }
        }
    }

    private void validateForUpdate(final StatisticsSchema schema, final Long userId) {
        Optional<StatisticsSchema> optionalOriginalSchema = validateForDelete(schema, userId);
        StatisticsSchema originalSchema = optionalOriginalSchema.get();
        if (schema.equals(originalSchema)) {
            throw new IllegalStatisticsSchemaException(schema, schemaNotChanged);
        }
        List<StatisticsSchema> schemaListWithSameTitle = schemaDao.findByTitle(schema, userId);
        if (!schemaListWithSameTitle.isEmpty() && !schemaListWithSameTitle.contains(originalSchema)) {
            throw new StatisticsSchemaConflictException(schema, schemaTitleIsReserved);
        }
    }

    private Optional<StatisticsSchema> validateForDelete(final StatisticsSchema schema, final Long userId) {
        Assert.notNull(schema.getId(), schemaIdCannotBeNull);
        Optional<StatisticsSchema> optionalOriginalSchema = schemaDao.findById(schema.getId(), userId);
        if (optionalOriginalSchema.isEmpty()) {
            throw new StatisticsSchemaNotFoundException(schema, schemaNotFound);
        }
        return optionalOriginalSchema;
    }

}
