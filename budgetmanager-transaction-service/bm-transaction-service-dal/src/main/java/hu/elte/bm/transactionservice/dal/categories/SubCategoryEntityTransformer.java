package hu.elte.bm.transactionservice.dal.categories;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;

@Component
public class SubCategoryEntityTransformer {

    public SubCategory transformToSubCategory(final SubCategoryEntity subCategoryEntity) {
        return SubCategory.builder()
            .withId(subCategoryEntity.getId())
            .withName(subCategoryEntity.getName())
            .withTransactionType(subCategoryEntity.getTransactionType())
            .build();
    }

    public SubCategoryEntity transformToSubCategoryEntity(final SubCategory subCategory) {
        return new SubCategoryEntity(subCategory.getId(), subCategory.getName(), subCategory.getTransactionType());
    }
}
