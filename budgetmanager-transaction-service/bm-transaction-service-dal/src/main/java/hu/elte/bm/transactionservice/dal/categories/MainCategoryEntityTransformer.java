package hu.elte.bm.transactionservice.dal.categories;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;

@Component
public class MainCategoryEntityTransformer {

    private final SubCategoryEntityTransformer subCategoryEntityTransformer;

    MainCategoryEntityTransformer(final SubCategoryEntityTransformer subCategoryEntityTransformer) {
        this.subCategoryEntityTransformer = subCategoryEntityTransformer;
    }

    MainCategory transformToMainCategory(final MainCategoryEntity mainCategoryEntity) {
        return MainCategory.builder()
            .withId(mainCategoryEntity.getId())
            .withName(mainCategoryEntity.getName())
            .withCategoryType(mainCategoryEntity.getCategoryType())
            .withSubCategorySet(transformToSubCategorySet(mainCategoryEntity.getSubCategoryEntitySet()))
            .build();
    }

    private Set<SubCategory> transformToSubCategorySet(final Set<SubCategoryEntity> subCategoryEntitySet) {
        return subCategoryEntitySet.stream()
            .map(subCategoryEntityTransformer::transformToSubCategory)
            .collect(Collectors.toSet());
    }

    MainCategoryEntity transformToMainCategoryEntity(final MainCategory mainCategory) {
        return MainCategoryEntity.builder()
            .withId(mainCategory.getId())
            .withName(mainCategory.getName())
            .withCategoryType(mainCategory.getCategoryType())
            .withSubCategoryEntitySet(transformToSubCategoryEntitySet(mainCategory.getSubCategorySet()))
            .build();
    }

    private Set<SubCategoryEntity> transformToSubCategoryEntitySet(final Set<SubCategory> subCategorySet) {
        return subCategorySet.stream()
            .map(subCategoryEntityTransformer::transformToSubCategoryEntity)
            .collect(Collectors.toSet());
    }
}
