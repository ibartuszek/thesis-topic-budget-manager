package hu.elte.bm.calculationservice.dal.schema;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaNotFoundException;
import hu.elte.bm.calculationservice.schema.ChartType;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.transactionservice.Currency;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStatisticsSchemaDaoTest {

    private static final long SCHEMA_ID = 1L;
    private static final String SCHEMA_TITLE = "Schema title";
    private static final String NEW_SCHEMA_TITLE = "new schema title";
    private static final String STANDARD_SCHEMA_TITLE = "Standard schema title";
    private static final TransactionType TYPE = TransactionType.OUTCOME;
    private static final long MAIN_CATEGORY_ID = 1L;
    private static final String MAIN_CATEGORY_NAME = "main category name";
    private static final long SUB_CATEGORY_ID = 1L;
    private static final String SUBCATEGORY_NAME = "Subcategory name";
    private static final long USER_ID = 1L;

    @InjectMocks
    private DefaultStatisticsSchemaDao underTest;

    @Mock
    private CategoryProvider categoryProvider;

    @Mock
    private StatisticsSchemaRepository repository;

    @Mock
    private StatisticsSchemaTransformer transformer;

    @Before
    public void setUnderTest() {
        ReflectionTestUtils.setField(underTest, "standardSchemaTitle", STANDARD_SCHEMA_TITLE);
    }

    @Test(expected = StatisticsSchemaNotFoundException.class)
    public void testGetStandardSchemaWhenRepositoryReturnsWithOptionalEmpty() {
        // GIVEN
        Mockito.when(repository.findByTitleAndUserId(STANDARD_SCHEMA_TITLE, USER_ID)).thenReturn(Optional.empty());
        // WHEN
        underTest.getStandardSchema(USER_ID);
        // THEN
    }

    @Test
    public void testGetStandardSchema() {
        // GIVEN
        StatisticsSchemaEntity standardSchemaEntity = createStatisticsSchemaEntityBuilder()
            .withType(StatisticsType.STANDARD)
            .withChartType(ChartType.RADIAL)
            .build();
        StatisticsSchema standardSchema = createStatisticsSchemaBuilder()
            .withType(StatisticsType.STANDARD)
            .withChartType(ChartType.RADIAL)
            .build();
        Mockito.when(repository.findByTitleAndUserId(STANDARD_SCHEMA_TITLE, USER_ID))
            .thenReturn(Optional.of(standardSchemaEntity));
        Mockito.when(transformer.transformToStatistcisSchema(standardSchemaEntity, null, null))
            .thenReturn(standardSchema);
        // WHEN
        var result = underTest.getStandardSchema(USER_ID);
        // THEN
        Mockito.verify(repository).findByTitleAndUserId(STANDARD_SCHEMA_TITLE, USER_ID);
        Mockito.verify(transformer).transformToStatistcisSchema(standardSchemaEntity, null, null);
        Assert.assertEquals(standardSchema, result);
    }

    @Test
    public void testGetCustomSchemasWhenRepositoryReturnsWithEmptyList() {
        // GIVEN
        Mockito.when(repository.findAllSchema(USER_ID)).thenReturn(Collections.emptyList());
        Mockito.when(categoryProvider.provideMainCategoryList(Collections.emptySet(), USER_ID, TYPE))
            .thenReturn(Collections.emptyList());
        // WHEN
        var result = underTest.getCustomSchemas(USER_ID);
        // THEN
        Mockito.verify(repository).findAllSchema(USER_ID);
        Mockito.verify(categoryProvider).provideMainCategoryList(Collections.emptySet(), USER_ID, TYPE);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetCustomSchemas() {
        // GIVEN
        StatisticsSchemaEntity standardEntity = createStatisticsSchemaEntityBuilder()
            .withType(StatisticsType.STANDARD)
            .withChartType(ChartType.RADIAL)
            .build();
        StatisticsSchemaEntity exampleEntity = createStatisticsSchemaEntityBuilderWithCategoryIds().build();
        StatisticsSchema exampleSchema = createStatisticsSchemaWithCategories().build();
        MainCategory mainCategory = exampleSchema.getMainCategory();
        SubCategory subCategory = exampleSchema.getSubCategory();
        Mockito.when(repository.findAllSchema(USER_ID)).thenReturn(List.of(standardEntity, exampleEntity));
        Mockito.when(categoryProvider.provideMainCategoryList(Set.of(mainCategory.getId()), USER_ID, TYPE))
            .thenReturn(List.of(mainCategory));
        Mockito.when(categoryProvider.provideSubCategory(subCategory.getId(), mainCategory)).thenReturn(subCategory);
        Mockito.when(transformer.transformToStatistcisSchema(exampleEntity, mainCategory, subCategory)).thenReturn(exampleSchema);
        // WHEN
        var result = underTest.getCustomSchemas(USER_ID);
        // THEN
        Mockito.verify(repository).findAllSchema(USER_ID);
        Mockito.verify(categoryProvider).provideMainCategoryList(Set.of(mainCategory.getId()), USER_ID, TYPE);
        Mockito.verify(categoryProvider).provideSubCategory(subCategory.getId(), mainCategory);
        Assert.assertEquals(List.of(exampleSchema), result);
    }

    @Test(expected = StatisticsSchemaNotFoundException.class)
    public void testFindByIdWhenRepositoryReturnsOptionalEmpty() {
        // GIVEN
        Mockito.when(repository.findByIdAndUserId(SCHEMA_ID, USER_ID)).thenReturn(Optional.empty());
        // WHEN
        underTest.findById(SCHEMA_ID, USER_ID);
        // THEN
    }

    @Test
    public void testFindById() {
        // GIVEN
        StatisticsSchemaEntity exampleEntity = createStatisticsSchemaEntityBuilderWithCategoryIds().build();
        StatisticsSchema exampleSchema = createStatisticsSchemaWithCategories().build();
        MainCategory mainCategory = exampleSchema.getMainCategory();
        SubCategory subCategory = exampleSchema.getSubCategory();
        Mockito.when(repository.findByIdAndUserId(SCHEMA_ID, USER_ID)).thenReturn(Optional.of(exampleEntity));
        Mockito.when(categoryProvider.provideMainCategory(mainCategory.getId(), USER_ID, TYPE)).thenReturn(mainCategory);
        Mockito.when(categoryProvider.provideSubCategory(subCategory.getId(), mainCategory)).thenReturn(subCategory);
        Mockito.when(transformer.transformToStatistcisSchema(exampleEntity, mainCategory, subCategory)).thenReturn(exampleSchema);
        // WHEN
        var result = underTest.findById(SCHEMA_ID, USER_ID);
        // THEN
        Mockito.verify(repository).findByIdAndUserId(SCHEMA_ID, USER_ID);
        Mockito.verify(categoryProvider).provideMainCategory(mainCategory.getId(), USER_ID, TYPE);
        Mockito.verify(categoryProvider).provideSubCategory(subCategory.getId(), mainCategory);
        Mockito.verify(transformer).transformToStatistcisSchema(exampleEntity, mainCategory, subCategory);
        Assert.assertEquals(exampleSchema, result);
    }

    @Test
    public void testFindByTitleWhenRepositoryReturnsOptionalEmpty() {
        // GIVEN
        Mockito.when(repository.findByTitleAndUserId(SCHEMA_TITLE, USER_ID)).thenReturn(Optional.empty());
        // WHEN
        var result = underTest.findByTitle(SCHEMA_TITLE, USER_ID);
        // THEN
        Mockito.verify(repository).findByTitleAndUserId(SCHEMA_TITLE, USER_ID);
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testFindByTitle() {
        // GIVEN
        StatisticsSchemaEntity exampleEntity = createStatisticsSchemaEntityBuilderWithCategoryIds().build();
        StatisticsSchema exampleSchema = createStatisticsSchemaWithCategories().build();
        MainCategory mainCategory = exampleSchema.getMainCategory();
        SubCategory subCategory = exampleSchema.getSubCategory();
        Mockito.when(repository.findByTitleAndUserId(SCHEMA_TITLE, USER_ID)).thenReturn(Optional.of(exampleEntity));
        Mockito.when(categoryProvider.provideMainCategory(mainCategory.getId(), USER_ID, TYPE)).thenReturn(mainCategory);
        Mockito.when(categoryProvider.provideSubCategory(subCategory.getId(), mainCategory)).thenReturn(subCategory);
        Mockito.when(transformer.transformToStatistcisSchema(exampleEntity, mainCategory, subCategory)).thenReturn(exampleSchema);
        // WHEN
        var result = underTest.findByTitle(SCHEMA_TITLE, USER_ID);
        // THEN
        Mockito.verify(repository).findByTitleAndUserId(SCHEMA_TITLE, USER_ID);
        Mockito.verify(categoryProvider).provideMainCategory(mainCategory.getId(), USER_ID, TYPE);
        Mockito.verify(categoryProvider).provideSubCategory(subCategory.getId(), mainCategory);
        Mockito.verify(transformer).transformToStatistcisSchema(exampleEntity, mainCategory, subCategory);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(exampleSchema, result.get());
    }

    @Test
    public void testSave() {
        // GIVEN
        StatisticsSchema schemaToSave = createStatisticsSchemaWithCategories()
            .withId(null)
            .build();
        MainCategory mainCategory = schemaToSave.getMainCategory();
        SubCategory subCategory = schemaToSave.getSubCategory();
        StatisticsSchemaEntity entityToSave = createStatisticsSchemaEntityBuilderWithCategoryIds()
            .withId(null)
            .build();
        StatisticsSchemaEntity savedEntity = createStatisticsSchemaEntityBuilderWithCategoryIds().build();
        StatisticsSchema savedSchema = createStatisticsSchemaWithCategories().build();
        Mockito.when(transformer.transformToStatisticsSchemaEntity(schemaToSave, USER_ID)).thenReturn(entityToSave);
        Mockito.when(repository.save(entityToSave)).thenReturn(savedEntity);
        Mockito.when(transformer.transformToStatistcisSchema(savedEntity, mainCategory, subCategory)).thenReturn(savedSchema);
        // WHEN
        var result = underTest.save(schemaToSave, USER_ID);
        // THEN
        Mockito.verify(transformer).transformToStatisticsSchemaEntity(schemaToSave, USER_ID);
        Mockito.verify(repository).save(entityToSave);
        Mockito.verify(transformer).transformToStatistcisSchema(savedEntity, mainCategory, subCategory);
        Assert.assertEquals(savedSchema, result);
    }

    @Test
    public void testUpdate() {
        // GIVEN
        StatisticsSchema schemaToUpdate = createStatisticsSchemaWithCategories()
            .withTitle(NEW_SCHEMA_TITLE)
            .build();
        MainCategory mainCategory = schemaToUpdate.getMainCategory();
        SubCategory subCategory = schemaToUpdate.getSubCategory();
        StatisticsSchemaEntity entityToUpdate = createStatisticsSchemaEntityBuilderWithCategoryIds()
            .withTitle(NEW_SCHEMA_TITLE)
            .build();
        Mockito.when(transformer.transformToStatisticsSchemaEntity(schemaToUpdate, USER_ID)).thenReturn(entityToUpdate);
        Mockito.when(repository.save(entityToUpdate)).thenReturn(entityToUpdate);
        Mockito.when(transformer.transformToStatistcisSchema(entityToUpdate, mainCategory, subCategory)).thenReturn(schemaToUpdate);
        // WHEN
        var result = underTest.update(schemaToUpdate, USER_ID);
        // THEN
        Mockito.verify(transformer).transformToStatisticsSchemaEntity(schemaToUpdate, USER_ID);
        Mockito.verify(repository).save(entityToUpdate);
        Mockito.verify(transformer).transformToStatistcisSchema(entityToUpdate, mainCategory, subCategory);
        Assert.assertEquals(schemaToUpdate, result);
    }

    @Test
    public void testDelete() {
        // GIVEN
        StatisticsSchema schemaToDelete = createStatisticsSchemaBuilder().build();
        StatisticsSchemaEntity entityToDelete = createStatisticsSchemaEntityBuilder().build();
        Mockito.when(transformer.transformToStatisticsSchemaEntity(schemaToDelete, USER_ID)).thenReturn(entityToDelete);
        Mockito.doNothing().when(repository).delete(entityToDelete);
        // WHEN
        var result = underTest.delete(schemaToDelete, USER_ID);
        // THEN
        Mockito.verify(transformer).transformToStatisticsSchemaEntity(schemaToDelete, USER_ID);
        Mockito.verify(repository).delete(entityToDelete);
        Assert.assertEquals(schemaToDelete, result);
    }

    private StatisticsSchemaEntity.Builder createStatisticsSchemaEntityBuilder() {
        return StatisticsSchemaEntity.builder()
            .withId(SCHEMA_ID)
            .withTitle(SCHEMA_TITLE)
            .withType(StatisticsType.SUM)
            .withChartType(ChartType.BAR)
            .withCurrency(Currency.EUR)
            .withUserId(USER_ID);
    }

    private StatisticsSchema.Builder createStatisticsSchemaBuilder() {
        return StatisticsSchema.builder()
            .withId(SCHEMA_ID)
            .withTitle(SCHEMA_TITLE)
            .withType(StatisticsType.SUM)
            .withChartType(ChartType.BAR)
            .withCurrency(Currency.EUR);
    }

    private MainCategory.Builder createMainCategoryBuilder() {
        return MainCategory.builder()
            .withId(MAIN_CATEGORY_ID)
            .withName(MAIN_CATEGORY_NAME)
            .withTransactionType(TYPE);
    }

    private SubCategory.Builder createSubCategoryBuilder() {
        return SubCategory.builder()
            .withId(SUB_CATEGORY_ID)
            .withName(SUBCATEGORY_NAME)
            .withTransactionType(TYPE);
    }

    private StatisticsSchemaEntity.Builder createStatisticsSchemaEntityBuilderWithCategoryIds() {
        return createStatisticsSchemaEntityBuilder()
            .withMainCategoryId(MAIN_CATEGORY_ID)
            .withSubCategoryId(SUB_CATEGORY_ID);
    }

    private StatisticsSchema.Builder createStatisticsSchemaWithCategories() {
        SubCategory subCategory = createSubCategoryBuilder().build();
        MainCategory mainCategory = createMainCategoryBuilder()
            .withSubCategorySet(Set.of(subCategory))
            .build();
        return createStatisticsSchemaBuilder()
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory);
    }

}
