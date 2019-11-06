package hu.elte.bm.calculationservice.app.test.schema;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.http.HttpStatus;

import hu.elte.bm.calculationservice.app.test.AbstractCalculationServiceApplicationTest;
import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.web.schema.StatisticsSchemaRequestContext;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;

public abstract class AbstractSchemaTest extends AbstractCalculationServiceApplicationTest {

    protected static final String RESERVED_SCHEMA_TITLE = "Standard schema";
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

        StatisticsSchema.Builder schemaBuilderWithNotExistedMainCategory = createSchemaBuilderWithDefaultValues()
            .withMainCategory(notExistingMainCategory);

        StatisticsSchema.Builder schemaBuilderWithMainCategoryWithNotExistedSubCategory = createSchemaBuilderWithDefaultValues()
            .withMainCategory(mainCategoryWithNotExistingSubCategory)
            .withSubCategory(notExistingSubCategory);

        StatisticsSchema.Builder schemaBuilderWithNotExistedSubCategory = createSchemaBuilderWithDefaultValues()
            .withMainCategory(mainCategory)
            .withSubCategory(notExistingSubCategory);

        return Stream.of(
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

}
