package hu.elte.bm.calculationservice.app.test.schema;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.http.HttpStatus;

import hu.elte.bm.calculationservice.app.test.AbstractCalculationServiceApplicationTest;
import hu.elte.bm.calculationservice.schema.ChartType;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.calculationservice.web.schema.StatisticsSchemaRequestContext;
import hu.elte.bm.transactionservice.Currency;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;

public abstract class AbstractSchemaTest extends AbstractCalculationServiceApplicationTest {

    protected static final String RESERVED_SCHEMA_TITLE = "Standard schema";
    protected static final String MODIFIED_TITLE = "New schema title";
    protected static final Long INVALID_SCHEMA_ID = 7L;
    private static final long EXISTING_SCHEMA_ID = 2L;
    private static final String EXISTING_SCHEMA_TITLE = "Example scale";
    private static final Currency EXISTING_SCHEMA_CURRENCY = Currency.HUF;
    private static final StatisticsType EXISTING_SCHEMA_TYPE = StatisticsType.SCALE;
    private static final ChartType EXISTING_SCHEMA_CHART_TYPE = ChartType.BAR;
    private static final String EMPTY_STRING = "";
    private static final String TOO_LONG_SCHEMA_TITLE = "Tooooo Loooong title!";
    private static final String NOT_EXISTED_MAIN_CATEGORY_NAME = "not existed main category name";
    private static final Long NOT_EXISTED_SUB_CATEGORY_ID = 99L;
    private static final String NOT_EXISTED_SUB_CATEGORY_NAME = "not existed sub category name";

    protected static Stream<Arguments> createSchemaValidationParams() {
        StatisticsSchema.Builder schemaBuilderWithEmptyTitle = createSchemaBuilderWithDefaultValues().withTitle(EMPTY_STRING);
        StatisticsSchema.Builder schemaBuilderWithTooLongTitle = createSchemaBuilderWithDefaultValues().withTitle(TOO_LONG_SCHEMA_TITLE);
        StatisticsSchema.Builder schemaBuilderWithNullType = createSchemaBuilderWithDefaultValues().withType(null);
        StatisticsSchema.Builder schemaBuilderWithNullCurrency = createSchemaBuilderWithDefaultValues().withCurrency(null);
        StatisticsSchema.Builder schemaBuilderWithNullChartType = createSchemaBuilderWithDefaultValues().withChartType(null);

        return Stream.of(
            Arguments.of(schemaBuilderWithEmptyTitle, HttpStatus.BAD_REQUEST.value(), "Title cannot be empty!"),
            Arguments.of(schemaBuilderWithTooLongTitle, HttpStatus.BAD_REQUEST.value(), "Title must be shorter than 20 characters!"),
            Arguments.of(schemaBuilderWithNullType, HttpStatus.BAD_REQUEST.value(), "Types cannot be null!"),
            Arguments.of(schemaBuilderWithNullCurrency, HttpStatus.BAD_REQUEST.value(), "Currency cannot be null!"),
            Arguments.of(schemaBuilderWithNullChartType, HttpStatus.BAD_REQUEST.value(), "ChartType cannot be null!")
        );
    }

    protected static Stream<Arguments> createSchemaValidationParamsWithCategories() {
        MainCategory notExistingMainCategory = createDefaultMainCategoryBuilder()
            .withName(NOT_EXISTED_MAIN_CATEGORY_NAME)
            .build();

        SubCategory notExistingSubCategory = createSubCategory(NOT_EXISTED_SUB_CATEGORY_ID, NOT_EXISTED_SUB_CATEGORY_NAME);
        Set<SubCategory> invalidSubCategorySet = new HashSet<>(createDefaultSubCategorySet());
        invalidSubCategorySet.add(notExistingSubCategory);
        MainCategory mainCategoryWithNotExistingSubCategory = createDefaultMainCategoryBuilder()
            .withSubCategorySet(invalidSubCategorySet)
            .build();

        MainCategory mainCategory = createDefaultMainCategoryBuilder()
            .build();

        StatisticsSchema.Builder sumSchemaBuilderWithNoCategory = createSchemaBuilderWithDefaultValues()
            .withType(StatisticsType.SUM)
            .withMainCategory(null);

        StatisticsSchema.Builder schemaBuilderWithNotExistedMainCategory = createSchemaBuilderWithDefaultValues()
            .withMainCategory(notExistingMainCategory);

        StatisticsSchema.Builder schemaBuilderWithMainCategoryWithNotExistedSubCategory = createSchemaBuilderWithDefaultValues()
            .withMainCategory(mainCategoryWithNotExistingSubCategory)
            .withSubCategory(notExistingSubCategory);

        StatisticsSchema.Builder schemaBuilderWithNotExistedSubCategory = createSchemaBuilderWithDefaultValues()
            .withMainCategory(mainCategory)
            .withSubCategory(notExistingSubCategory);

        return Stream.of(
            Arguments.of(sumSchemaBuilderWithNoCategory, HttpStatus.BAD_REQUEST.value(), "Sum schema must have a category!"),
            Arguments.of(schemaBuilderWithNotExistedMainCategory, HttpStatus.BAD_REQUEST.value(), "Schema's main category cannot be found!"),
            Arguments.of(schemaBuilderWithMainCategoryWithNotExistedSubCategory, HttpStatus.BAD_REQUEST.value(), "Schema's main category cannot be found!"),
            Arguments.of(schemaBuilderWithNotExistedSubCategory, HttpStatus.BAD_REQUEST.value(), "Schema's supplementary category cannot be found!")
        );
    }

    protected StatisticsSchemaRequestContext createContext(final Long userId, final StatisticsSchema schema) {
        StatisticsSchemaRequestContext context = new StatisticsSchemaRequestContext();
        context.setSchema(schema);
        context.setUserId(userId);
        return context;
    }

    protected StatisticsSchema.Builder createExampleBuilderForUpdate() {
        return StatisticsSchema.builder()
            .withId(EXISTING_SCHEMA_ID)
            .withTitle(EXISTING_SCHEMA_TITLE)
            .withCurrency(EXISTING_SCHEMA_CURRENCY)
            .withType(EXISTING_SCHEMA_TYPE)
            .withChartType(EXISTING_SCHEMA_CHART_TYPE);
    }

}
