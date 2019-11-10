package hu.elte.bm.calculationservice.service.schema;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import hu.elte.bm.calculationservice.exceptions.schema.IllegalStatisticsSchemaException;
import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaConflictException;
import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaNotFoundException;
import hu.elte.bm.calculationservice.schema.ChartType;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.calculationservice.transactionserviceclient.TransactionServiceFacade;
import hu.elte.bm.transactionservice.Currency;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsSchemaServiceTest {

    private static final Long USER_ID = 3L;
    private static final TransactionType TYPE = TransactionType.OUTCOME;
    private static final Currency CURRENCY = Currency.EUR;
    private static final long STANDARD_SCHEMA_ID = 1L;
    private static final long CUSTOM_SCHEMA_ID = 2L;
    private static final long OTHER_SCHEMA_ID = 3L;
    private static final String STANDARD_SCHEMA_TITLE = "Standard schema";
    private static final String CUSTOM_SCHEMA_TITLE = "Custom scale";
    private static final String ORIGINAL_SCHEMA_TITLE = "original title";
    private static final long MAIN_CATEGORY_ID = 4L;
    private static final String MAIN_CATEGORY_NAME = "Main category";
    private static final long SUBCATEGORY_ID = 5L;
    private static final String SUBCATEGORY_NAME = "Supplementary category";

    @InjectMocks
    private StatisticsSchemaService underTest;

    @Mock
    private StatisticsSchemaDao schemaDao;

    @Mock
    private TransactionServiceFacade transactionServiceFacade;

    @Test
    public void testGetStandardSchema() {
        // GIVEN
        StatisticsSchema standardSchema = createStandardSchema();
        Mockito.when(schemaDao.getStandardSchema(USER_ID)).thenReturn(standardSchema);
        // WHEN
        var result = underTest.getStandardSchema(USER_ID);
        // THEN
        Mockito.verify(schemaDao).getStandardSchema(USER_ID);
        Assert.assertEquals(standardSchema, result);
    }

    @Test
    public void testGetCustomSchemas() {
        // GIVEN
        List<StatisticsSchema> statisticsSchemaList = List.of(createCustomScaleSchemaBuilder().build());
        Mockito.when(schemaDao.getCustomSchemas(USER_ID)).thenReturn(statisticsSchemaList);
        // WHEN
        var result = underTest.getCustomSchemas(USER_ID);
        // THEN
        Mockito.verify(schemaDao).getCustomSchemas(USER_ID);
        Assert.assertEquals(statisticsSchemaList, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWhenSchemaIdIsNotNull() {
        // GIVEN
        StatisticsSchema schemaToSave = createCustomScaleSchemaBuilder().build();
        // WHEN
        underTest.save(schemaToSave, USER_ID);
        // THEN
    }

    @Test(expected = IllegalStatisticsSchemaException.class)
    public void testSaveWhenSchemaIsStandardSchema() {
        // GIVEN
        StatisticsSchema schemaToSave = createCustomScaleSchemaBuilder()
            .withId(null)
            .withType(StatisticsType.STANDARD)
            .build();
        // WHEN
        underTest.save(schemaToSave, USER_ID);
        // THEN
    }

    @Test(expected = IllegalStatisticsSchemaException.class)
    public void testSaveWhenSchemaIsSumSchemaAndNotHaveAnyCategory() {
        // GIVEN
        StatisticsSchema schemaToSave = createCustomScaleSchemaBuilder()
            .withId(null)
            .withType(StatisticsType.SUM)
            .withMainCategory(null)
            .build();
        // WHEN
        underTest.save(schemaToSave, USER_ID);
        // THEN
    }

    @Test(expected = StatisticsSchemaConflictException.class)
    public void testSaveWhenSchemaTitleIsReserved() {
        // GIVEN
        StatisticsSchema schemaToSave = createCustomScaleSchemaBuilder()
            .withId(null)
            .build();
        StatisticsSchema schemaWithSameTitle = createCustomScaleSchemaBuilder().build();
        Mockito.when(schemaDao.findByTitle(schemaToSave.getTitle(), USER_ID)).thenReturn(Optional.of(schemaWithSameTitle));
        // WHEN
        underTest.save(schemaToSave, USER_ID);
        // THEN
    }

    @Test(expected = IllegalStatisticsSchemaException.class)
    public void testSaveWhenMainCategoryDoesNotExists() {
        // GIVEN
        StatisticsSchema schemaToSave = createCustomScaleSchemaBuilder()
            .withMainCategory(createMainCategoryBuilder().build())
            .withId(null)
            .build();
        Mockito.when(schemaDao.findByTitle(schemaToSave.getTitle(), USER_ID)).thenReturn(Optional.empty());
        Mockito.when(transactionServiceFacade.getMainCategories(TransactionType.OUTCOME, USER_ID)).thenReturn(Collections.emptyList());
        // WHEN
        underTest.save(schemaToSave, USER_ID);
        // THEN
    }

    @Test(expected = IllegalStatisticsSchemaException.class)
    public void testSaveWhenSubCategoryDoesNotExists() {
        // GIVEN
        SubCategory subCategory = createSubCategoryBuilder().build();
        MainCategory mainCategory = createMainCategoryBuilder()
            .withSubCategorySet(Set.of(subCategory))
            .build();
        StatisticsSchema schemaToSave = createCustomScaleSchemaBuilder()
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory)
            .withId(null)
            .build();
        Mockito.when(schemaDao.findByTitle(schemaToSave.getTitle(), USER_ID)).thenReturn(Optional.empty());
        Mockito.when(transactionServiceFacade.getMainCategories(TransactionType.OUTCOME, USER_ID)).thenReturn(List.of(mainCategory));
        Mockito.when(transactionServiceFacade.getSubCategories(TransactionType.OUTCOME, USER_ID)).thenReturn(Collections.emptyList());
        // WHEN
        underTest.save(schemaToSave, USER_ID);
        // THEN
    }

    @Test
    public void testSave() {
        // GIVEN
        SubCategory subCategory = createSubCategoryBuilder().build();
        MainCategory mainCategory = createMainCategoryBuilder()
            .withSubCategorySet(Set.of(subCategory))
            .build();
        StatisticsSchema schemaToSave = createCustomScaleSchemaBuilder()
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory)
            .withId(null)
            .build();
        StatisticsSchema expectedSchema = createCustomScaleSchemaBuilder()
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory)
            .build();
        Mockito.when(schemaDao.findByTitle(schemaToSave.getTitle(), USER_ID)).thenReturn(Optional.empty());
        Mockito.when(transactionServiceFacade.getMainCategories(TYPE, USER_ID)).thenReturn(List.of(mainCategory));
        Mockito.when(transactionServiceFacade.getSubCategories(TYPE, USER_ID)).thenReturn(List.of(subCategory));
        Mockito.when(schemaDao.save(schemaToSave, USER_ID)).thenReturn(expectedSchema);
        // WHEN
        var result = underTest.save(schemaToSave, USER_ID);
        // THEN
        Mockito.verify(schemaDao).findByTitle(schemaToSave.getTitle(), USER_ID);
        Mockito.verify(transactionServiceFacade).getMainCategories(TYPE, USER_ID);
        Mockito.verify(transactionServiceFacade).getSubCategories(TYPE, USER_ID);
        Mockito.verify(schemaDao).save(schemaToSave, USER_ID);
        Assert.assertEquals(expectedSchema, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWhenSchemaIdIsNull() {
        // GIVEN
        StatisticsSchema schemaToUpdate = createCustomScaleSchemaBuilder()
            .withId(null)
            .build();
        // WHEN
        underTest.update(schemaToUpdate, USER_ID);
        // THEN
    }

    @Test(expected = IllegalStatisticsSchemaException.class)
    public void testUpdateWhenSchemaIsStandardSchema() {
        // GIVEN
        StatisticsSchema schemaToSave = createCustomScaleSchemaBuilder()
            .withType(StatisticsType.STANDARD)
            .build();
        // WHEN
        underTest.update(schemaToSave, USER_ID);
        // THEN
    }

    @Test(expected = IllegalStatisticsSchemaException.class)
    public void testUpdateWhenSchemaIsSumSchemaAndNotHaveAnyCategory() {
        // GIVEN
        StatisticsSchema schemaToSave = createCustomScaleSchemaBuilder()
            .withType(StatisticsType.SUM)
            .withMainCategory(null)
            .build();
        // WHEN
        underTest.update(schemaToSave, USER_ID);
        // THEN
    }

    @Test(expected = StatisticsSchemaNotFoundException.class)
    public void testUpdateWhenOriginalSchemaCannotBeFound() {
        // GIVEN
        StatisticsSchema schemaToUpdate = createCustomScaleSchemaBuilder().build();
        Mockito.when(schemaDao.findById(schemaToUpdate.getId(), USER_ID))
            .thenThrow(new StatisticsSchemaNotFoundException(schemaToUpdate.getId(), null));
        // WHEN
        underTest.update(schemaToUpdate, USER_ID);
        // THEN
    }

    @Test(expected = IllegalStatisticsSchemaException.class)
    public void testUpdateWhenSchemaNotChanged() {
        // GIVEN
        StatisticsSchema schemaToUpdate = createCustomScaleSchemaBuilder().build();
        StatisticsSchema originalSchema = createCustomScaleSchemaBuilder().build();
        Mockito.when(schemaDao.findById(schemaToUpdate.getId(), USER_ID)).thenReturn(originalSchema);
        // WHEN
        underTest.update(schemaToUpdate, USER_ID);
        // THEN
    }

    @Test(expected = StatisticsSchemaConflictException.class)
    public void testUpdateWhenSchemaTitleIsReserved() {
        // GIVEN
        StatisticsSchema schemaToUpdate = createCustomScaleSchemaBuilder().build();
        StatisticsSchema originalSchema = createCustomScaleSchemaBuilder()
            .withTitle(ORIGINAL_SCHEMA_TITLE)
            .build();
        StatisticsSchema schemaWithSameTitle = createCustomScaleSchemaBuilder()
            .withId(OTHER_SCHEMA_ID)
            .build();
        Mockito.when(schemaDao.findById(schemaToUpdate.getId(), USER_ID)).thenReturn(originalSchema);
        Mockito.when(schemaDao.findByTitle(schemaToUpdate.getTitle(), USER_ID)).thenReturn(Optional.of(schemaWithSameTitle));
        // WHEN
        underTest.update(schemaToUpdate, USER_ID);
        // THEN
    }

    @Test(expected = IllegalStatisticsSchemaException.class)
    public void testUpdateWhenMainCategoryDoesNotExists() {
        // GIVEN
        StatisticsSchema schemaToUpdate = createCustomScaleSchemaBuilder()
            .withMainCategory(createMainCategoryBuilder().build())
            .build();
        StatisticsSchema originalSchema = createCustomScaleSchemaBuilder().build();
        Mockito.when(schemaDao.findById(schemaToUpdate.getId(), USER_ID)).thenReturn(originalSchema);
        Mockito.when(schemaDao.findByTitle(schemaToUpdate.getTitle(), USER_ID)).thenReturn(Optional.of(originalSchema));
        Mockito.when(transactionServiceFacade.getMainCategories(TYPE, USER_ID)).thenReturn(Collections.emptyList());
        // WHEN
        underTest.update(schemaToUpdate, USER_ID);
        // THEN
    }

    @Test(expected = IllegalStatisticsSchemaException.class)
    public void testUpdateWhenSubCategoryDoesNotExists() {
        // GIVEN
        SubCategory subCategory = createSubCategoryBuilder().build();
        MainCategory mainCategory = createMainCategoryBuilder()
            .withSubCategorySet(Set.of(subCategory))
            .build();
        StatisticsSchema schemaToUpdate = createCustomScaleSchemaBuilder()
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory)
            .build();
        StatisticsSchema originalSchema = createCustomScaleSchemaBuilder()
            .withMainCategory(mainCategory)
            .build();
        Mockito.when(schemaDao.findById(schemaToUpdate.getId(), USER_ID)).thenReturn(originalSchema);
        Mockito.when(schemaDao.findByTitle(schemaToUpdate.getTitle(), USER_ID)).thenReturn(Optional.of(originalSchema));
        Mockito.when(transactionServiceFacade.getMainCategories(TYPE, USER_ID)).thenReturn(List.of(mainCategory));
        Mockito.when(transactionServiceFacade.getSubCategories(TYPE, USER_ID)).thenReturn(Collections.emptyList());
        // WHEN
        underTest.update(schemaToUpdate, USER_ID);
        // THEN
    }

    @Test
    public void testUpdate() {
        // GIVEN
        SubCategory subCategory = createSubCategoryBuilder().build();
        MainCategory mainCategory = createMainCategoryBuilder()
            .withSubCategorySet(Set.of(subCategory))
            .build();
        StatisticsSchema schemaToUpdate = createCustomScaleSchemaBuilder()
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory)
            .build();
        StatisticsSchema originalSchema = createCustomScaleSchemaBuilder()
            .withMainCategory(mainCategory)
            .build();
        Mockito.when(schemaDao.findById(schemaToUpdate.getId(), USER_ID)).thenReturn(originalSchema);
        Mockito.when(schemaDao.findByTitle(schemaToUpdate.getTitle(), USER_ID)).thenReturn(Optional.of(originalSchema));
        Mockito.when(transactionServiceFacade.getMainCategories(TYPE, USER_ID)).thenReturn(List.of(mainCategory));
        Mockito.when(transactionServiceFacade.getSubCategories(TYPE, USER_ID)).thenReturn(List.of(subCategory));
        Mockito.when(schemaDao.update(schemaToUpdate, USER_ID)).thenReturn(schemaToUpdate);
        // WHEN
        var result = underTest.update(schemaToUpdate, USER_ID);
        // THEN
        Mockito.verify(schemaDao).findById(schemaToUpdate.getId(), USER_ID);
        Mockito.verify(schemaDao).findByTitle(schemaToUpdate.getTitle(), USER_ID);
        Mockito.verify(transactionServiceFacade).getMainCategories(TYPE, USER_ID);
        Mockito.verify(transactionServiceFacade).getSubCategories(TYPE, USER_ID);
        Mockito.verify(schemaDao).update(schemaToUpdate, USER_ID);
        Assert.assertEquals(schemaToUpdate, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteWhenSchemaIdIsNull() {
        // GIVEN
        StatisticsSchema schemaToDelete = createCustomScaleSchemaBuilder()
            .withId(null)
            .build();
        // WHEN
        underTest.delete(schemaToDelete, USER_ID);
        // THEN
    }

    @Test(expected = IllegalStatisticsSchemaException.class)
    public void testDeleteWhenSchemaIsStandardSchema() {
        // GIVEN
        StatisticsSchema schemaToSave = createCustomScaleSchemaBuilder()
            .withType(StatisticsType.STANDARD)
            .build();
        // WHEN
        underTest.delete(schemaToSave, USER_ID);
        // THEN
    }

    @Test(expected = StatisticsSchemaNotFoundException.class)
    public void testDeleteWhenOriginalSchemaCannotBeFound() {
        // GIVEN
        StatisticsSchema schemaToDelete = createCustomScaleSchemaBuilder().build();
        Mockito.when(schemaDao.findById(schemaToDelete.getId(), USER_ID))
            .thenThrow(new StatisticsSchemaNotFoundException(schemaToDelete.getId(), null));
        // WHEN
        underTest.delete(schemaToDelete, USER_ID);
        // THEN

    }

    @Test(expected = IllegalStatisticsSchemaException.class)
    public void testDeleteWhenSchemaHasBeenChanged() {
        // GIVEN
        StatisticsSchema schemaToDelete = createCustomScaleSchemaBuilder()
            .build();
        StatisticsSchema originalSchema = createCustomScaleSchemaBuilder()
            .withTitle(ORIGINAL_SCHEMA_TITLE)
            .build();
        Mockito.when(schemaDao.findById(schemaToDelete.getId(), USER_ID)).thenReturn(originalSchema);
        // WHEN
        underTest.delete(schemaToDelete, USER_ID);
        // THEN
    }

    @Test
    public void testDelete() {
        // GIVEN
        StatisticsSchema schemaToDelete = createCustomScaleSchemaBuilder().build();
        Mockito.when(schemaDao.findById(schemaToDelete.getId(), USER_ID)).thenReturn(schemaToDelete);
        Mockito.when(schemaDao.delete(schemaToDelete, USER_ID)).thenReturn(schemaToDelete);
        // WHEN
        var result = underTest.delete(schemaToDelete, USER_ID);
        // THEN
        Mockito.verify(schemaDao).findById(schemaToDelete.getId(), USER_ID);
        Mockito.verify(schemaDao).delete(schemaToDelete, USER_ID);
        Assert.assertEquals(schemaToDelete, result);
    }

    private StatisticsSchema createStandardSchema() {
        return StatisticsSchema.builder()
            .withId(STANDARD_SCHEMA_ID)
            .withTitle(STANDARD_SCHEMA_TITLE)
            .withType(StatisticsType.STANDARD)
            .withChartType(ChartType.RADIAL)
            .withCurrency(CURRENCY)
            .build();
    }

    private StatisticsSchema.Builder createCustomScaleSchemaBuilder() {
        return StatisticsSchema.builder()
            .withId(CUSTOM_SCHEMA_ID)
            .withTitle(CUSTOM_SCHEMA_TITLE)
            .withType(StatisticsType.SCALE)
            .withChartType(ChartType.BAR)
            .withCurrency(CURRENCY);
    }

    private MainCategory.Builder createMainCategoryBuilder() {
        return MainCategory.builder()
            .withId(MAIN_CATEGORY_ID)
            .withName(MAIN_CATEGORY_NAME)
            .withTransactionType(TYPE)
            .withSubCategorySet(new HashSet<>());
    }

    private SubCategory.Builder createSubCategoryBuilder() {
        return SubCategory.builder()
            .withId(SUBCATEGORY_ID)
            .withName(SUBCATEGORY_NAME)
            .withTransactionType(TYPE);
    }

}
