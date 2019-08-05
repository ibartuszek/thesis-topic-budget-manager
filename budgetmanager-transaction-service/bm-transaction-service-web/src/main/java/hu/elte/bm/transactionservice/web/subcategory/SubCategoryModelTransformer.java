package hu.elte.bm.transactionservice.web.subcategory;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;

@Component
public class SubCategoryModelTransformer {

    private static final Integer SUB_CATEGORY_NAME_MAXIMUM_LENGTH = 50;

    public SubCategoryModel transformToSubCategoryModel(final SubCategory subCategory) {
        ModelStringValue name = ModelStringValue.builder()
            .withValue(subCategory.getName())
            .build();
        ModelStringValue type = ModelStringValue.builder()
            .withValue(subCategory.getTransactionType().name())
            .build();
        SubCategoryModel subCategoryModel = SubCategoryModel.builder()
            .withId(subCategory.getId())
            .withName(name)
            .withTransactionType(type)
            .build();
        setValidationFields(subCategoryModel);
        return subCategoryModel;
    }

    public SubCategory transformToSubCategory(final SubCategoryModel subCategoryModel) {
        return SubCategory.builder()
            .withId(subCategoryModel.getId())
            .withName(subCategoryModel.getName().getValue())
            .withTransactionType(TransactionType.valueOf(subCategoryModel.getTransactionType().getValue()))
            .build();
    }

    void setValidationFields(final SubCategoryModel subCategoryModel) {
        subCategoryModel.getName().setMaximumLength(SUB_CATEGORY_NAME_MAXIMUM_LENGTH);
        subCategoryModel.getTransactionType().setPossibleEnumValues(Arrays.stream(TransactionType.values())
                .map(TransactionType::name)
                .collect(Collectors.toSet()));
    }
}
