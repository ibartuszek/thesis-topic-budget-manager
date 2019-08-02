package hu.elte.bm.transactionservice.web.maincategory;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModelTransformer;

@Component
public class MainCategoryModelTransformer {

    private static final Integer MAIN_CATEGORY_NAME_MAXIMUM_LENGTH = 50;
    private final SubCategoryModelTransformer subCategoryModelTransformer;

    MainCategoryModelTransformer(final SubCategoryModelTransformer subCategoryModelTransformer) {
        this.subCategoryModelTransformer = subCategoryModelTransformer;
    }

    public MainCategoryModel transformToMainCategoryModel(final MainCategory mainCategory) {
        ModelStringValue name = ModelStringValue.builder()
            .withValue(mainCategory.getName())
            .build();
        ModelStringValue type = ModelStringValue.builder()
            .withValue(mainCategory.getTransactionType().name())
            .build();
        MainCategoryModel mainCategoryModel = MainCategoryModel.builder()
            .withId(mainCategory.getId())
            .withName(name)
            .withTransactionType(type)
            .withSubCategoryModelSet(transformToSubCategoryModelSet(mainCategory.getSubCategorySet()))
            .build();
        populateValidationFields(mainCategoryModel);
        return mainCategoryModel;
    }

    MainCategoryModel transformToMainCategoryModel(final MainCategoryModel mainCategoryModel) {
        ModelStringValue name = ModelStringValue.builder()
            .withValue(mainCategoryModel.getName().getValue())
            .build();
        ModelStringValue type = ModelStringValue.builder()
            .withValue(mainCategoryModel.getTransactionType().getValue())
            .build();
        MainCategoryModel copySubCategoryModel = MainCategoryModel.builder()
            .withId(mainCategoryModel.getId())
            .withName(name)
            .withTransactionType(type)
            .withSubCategoryModelSet(mainCategoryModel.getSubCategoryModelSet())
            .build();
        populateValidationFields(copySubCategoryModel);
        return copySubCategoryModel;
    }

    public MainCategory transformToMainCategory(final MainCategoryModel mainCategoryModel) {
        return MainCategory.builder()
            .withId(mainCategoryModel.getId())
            .withName(mainCategoryModel.getName().getValue())
            .withTransactionType(TransactionType.valueOf(mainCategoryModel.getTransactionType().getValue()))
            .withSubCategorySet(transformToSubCategorySet(mainCategoryModel.getSubCategoryModelSet()))
            .build();
    }

    private void populateValidationFields(final MainCategoryModel mainCategoryModel) {
        mainCategoryModel.getName().setMaximumLength(MAIN_CATEGORY_NAME_MAXIMUM_LENGTH);
        mainCategoryModel.getTransactionType().setPossibleEnumValues(TransactionType.getPossibleValues());
    }

    private Set<SubCategoryModel> transformToSubCategoryModelSet(final Set<SubCategory> subCategorySet) {
        Set<SubCategoryModel> subCategoryModelSet = new HashSet<>();
        subCategorySet.forEach(subCategory ->
            subCategoryModelSet.add(subCategoryModelTransformer.transformToSubCategoryModel(subCategory)));
        return subCategoryModelSet;
    }

    private Set<SubCategory> transformToSubCategorySet(final Set<SubCategoryModel> subCategoryModelSet) {
        Set<SubCategory> subCategorySet = new HashSet<>();
        subCategoryModelSet.forEach(subCategoryModel ->
            subCategorySet.add(subCategoryModelTransformer.transformToSubCategory(subCategoryModel)));
        return subCategorySet;
    }
}
