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

    public MainCategory transformToMainCategory(final MainCategoryEntity mainCategoryEntity) {
        return MainCategory.builder()
            .withId(mainCategoryEntity.getId())
            .withName(mainCategoryEntity.getName())
            .withTransactionType(mainCategoryEntity.getTransactionType())
            .withSubCategorySet(transformToSubCategorySet(mainCategoryEntity.getSubCategoryEntitySet()))
            .build();
    }

    private Set<SubCategory> transformToSubCategorySet(final Set<SubCategoryEntity> subCategoryEntitySet) {
        return subCategoryEntitySet.stream()
            .map(subCategoryEntityTransformer::transformToSubCategory)
            .collect(Collectors.toSet());
    }

    public MainCategoryEntity transformToMainCategoryEntity(final MainCategory mainCategory, final Set<SubCategoryEntity> subCategoryEntitySet) {
        return new MainCategoryEntity(mainCategory.getId(), mainCategory.getName(), mainCategory.getTransactionType(), subCategoryEntitySet);
    }

}
