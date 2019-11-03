package hu.elte.bm.calculationservice.dal.schema;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;

@Component
public class StatisticsSchemaTransformer {

    StatisticsSchema transformToStatistcisSchema(final StatisticsSchemaEntity entity,
        final MainCategory mainCategory, final SubCategory subCategory) {
        return StatisticsSchema.builder()
            .withId(entity.getId())
            .withTitle(entity.getTitle())
            .withType(entity.getType())
            .withChartType(entity.getChartType())
            .withCurrency(entity.getCurrency())
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory)
            .build();
    }

    StatisticsSchemaEntity transformToStatisticsSchemaEntity(final StatisticsSchema schema, final Long userId) {
        Long mainCategoryId = schema.getMainCategory() != null ? schema.getMainCategory().getId() : null;
        Long subCategoryId = schema.getSubCategory() != null ? schema.getSubCategory().getId() : null;
        return StatisticsSchemaEntity.builder()
            .withId(schema.getId())
            .withTitle(schema.getTitle())
            .withType(schema.getType())
            .withChartType(schema.getChartType())
            .withCurrency(schema.getCurrency())
            .withMainCategoryId(mainCategoryId)
            .withSubCategoryId(subCategoryId)
            .withUserId(userId)
            .build();
    }

}
