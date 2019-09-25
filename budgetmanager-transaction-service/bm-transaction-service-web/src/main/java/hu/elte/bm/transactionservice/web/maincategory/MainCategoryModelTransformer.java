package hu.elte.bm.transactionservice.web.maincategory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Component
public class MainCategoryModelTransformer {

    private final SubCategoryModelTransformer subCategoryModelTransformer;

    @Value("${main_category.name.maximum_length}")
    private Integer mainCategoryNameMaximumLength;

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
        setValidationFields(mainCategoryModel);
        return mainCategoryModel;
    }

    public MainCategory transformToMainCategory(final MainCategoryModel mainCategoryModel) {
        String name = mainCategoryModel.getName() == null ? null : mainCategoryModel.getName().getValue();
        TransactionType type =
            mainCategoryModel.getTransactionType() == null ? null : TransactionType.valueOf(mainCategoryModel.getTransactionType().getValue());
        return MainCategory.builder()
            .withId(mainCategoryModel.getId())
            .withName(name)
            .withTransactionType(type)
            .withSubCategorySet(transformToSubCategorySet(mainCategoryModel.getSubCategoryModelSet()))
            .build();
    }

    void setValidationFields(final MainCategoryModel mainCategoryModel) {
        mainCategoryModel.getName().setMaximumLength(mainCategoryNameMaximumLength);
        mainCategoryModel.getTransactionType().setPossibleEnumValues(Arrays.stream(TransactionType.values())
            .map(TransactionType::name)
            .collect(Collectors.toSet()));
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
