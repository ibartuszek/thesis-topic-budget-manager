package hu.elte.bm.calculationservice.service.schema;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.calculationservice.exceptions.schema.IllegalStatisticsSchemaException;
import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaConflictException;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
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

    @Value("${schema.schema_main_category_cannot_be_found:Schema's main category cannot be found!}")
    private String schemaMainCategoryCannotBeFound;

    @Value("${schema.schema_sub_category_cannot_be_found:Schema's supplementary category cannot be found!}")
    private String schemaSubCategoryCannotBeFound;

    @Value("${schema.schema_title_is_reserved:Schema's title is reserved!}")
    private String schemaTitleIsReserved;

    @Value("${schema.schema_cannot_be_changed:Schema cannot be changed during deleting!}")
    private String schemaCannotBeChangeBeforeDelete;

    @Value("${schema.standard_schema_cannot_be_created:Standard schema cannot be created!}")
    private String standardSchemaCannotBeCreated;

    @Value("${schema.standard_schema_cannot_be_modified:Standard schema cannot be modified!}")
    private String standardSchemaCannotBeModified;

    @Value("${schema.standard_schema_cannot_be_deleted:Standard schema cannot be deleted!}")
    private String standardSchemaCannotBeDeleted;

    @Value("${schema.sum_must_have_category:Sum schema must have a category!}")
    private String sumStatisticsMustHaveCategory;

    @Value("${schema.scale_can_have_only_outcome_type_category:Scale schema can have only categories from expenses!}")
    private String scaleStatisticsCanHaveOutcomeCategory;

    public StatisticsSchemaService(final StatisticsSchemaDao schemaDao, final TransactionServiceFacade transactionServiceFacade) {
        this.schemaDao = schemaDao;
        this.transactionServiceFacade = transactionServiceFacade;
    }

    public StatisticsSchema getStandardSchema(final Long userId) {
        return schemaDao.getStandardSchema(userId);
    }

    public List<StatisticsSchema> getCustomSchemas(final Long userId) {
        return schemaDao.getCustomSchemas(userId);
    }

    public StatisticsSchema getCustomSchemaById(final Long schemaId, final Long userId) {
        return schemaDao.findById(schemaId, userId);
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
        validateNotStandardSchema(schema, standardSchemaCannotBeCreated);
        validateSumHasCategory(schema);
        validateScaleHasOnlyOutcomeTypeCategory(schema);
        Optional<StatisticsSchema> schemaWithSameTitle = schemaDao.findByTitle(schema.getTitle(), userId);
        if (schemaWithSameTitle.isPresent()) {
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
        Assert.notNull(schema.getId(), schemaIdCannotBeNull);
        validateNotStandardSchema(schema, standardSchemaCannotBeModified);
        validateSumHasCategory(schema);
        validateScaleHasOnlyOutcomeTypeCategory(schema);
        StatisticsSchema originalSchema = schemaDao.findById(schema.getId(), userId);
        if (schema.equals(originalSchema)) {
            throw new IllegalStatisticsSchemaException(schema, schemaNotChanged);
        }
        Optional<StatisticsSchema> schemaWithSameTitle = schemaDao.findByTitle(schema.getTitle(), userId);
        if (schemaWithSameTitle.isPresent() && !schemaWithSameTitle.get().equals(originalSchema)) {
            throw new StatisticsSchemaConflictException(schema, schemaTitleIsReserved);
        }
    }

    private void validateForDelete(final StatisticsSchema schema, final Long userId) {
        Assert.notNull(schema.getId(), schemaIdCannotBeNull);
        validateNotStandardSchema(schema, standardSchemaCannotBeDeleted);
        StatisticsSchema originalSchema = schemaDao.findById(schema.getId(), userId);
        if (!originalSchema.equals(schema)) {
            throw new IllegalStatisticsSchemaException(schema, schemaCannotBeChangeBeforeDelete);
        }
    }

    private void validateNotStandardSchema(final StatisticsSchema schema, final String errorMessage) {
        if (schema.getType().equals(StatisticsType.STANDARD)) {
            throw new IllegalStatisticsSchemaException(schema, errorMessage);
        }
    }

    private void validateSumHasCategory(final StatisticsSchema schema) {
        if (schema.getType().equals(StatisticsType.SUM)
            && schema.getMainCategory() == null) {
            throw new IllegalStatisticsSchemaException(schema, sumStatisticsMustHaveCategory);
        }
    }

    private void validateScaleHasOnlyOutcomeTypeCategory(final StatisticsSchema schema) {
        if (schema.getType().equals(StatisticsType.SCALE)
            && schema.getMainCategory() != null
            && schema.getMainCategory().getTransactionType().equals(TransactionType.INCOME)) {
            throw new IllegalStatisticsSchemaException(schema, scaleStatisticsCanHaveOutcomeCategory);
        }
    }

}
