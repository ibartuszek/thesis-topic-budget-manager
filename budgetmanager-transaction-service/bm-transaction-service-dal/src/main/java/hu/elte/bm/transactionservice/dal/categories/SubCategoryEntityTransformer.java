package hu.elte.bm.transactionservice.dal.categories;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;

@Component
public class SubCategoryEntityTransformer {

    SubCategory transformToSubCategory(final SubCategoryEntity subCategoryEntity) {
        return SubCategory.builder()
            .withId(subCategoryEntity.getId())
            .withName(subCategoryEntity.getName())
            .withCategoryType(subCategoryEntity.getCategoryType())
            .build();
    }

    SubCategoryEntity transformToSubCategoryEntity(final SubCategory subCategory) {
        return SubCategoryEntity.builder()
            .withId(subCategory.getId())
            .withName(subCategory.getName())
            .withCategoryType(subCategory.getCategoryType())
            .build();
    }
}
