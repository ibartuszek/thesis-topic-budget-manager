package hu.elte.bm.transactionservice.web.subcategory;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;

@Component
public class SubCategoryModelTransformer {

    private static final Integer SUB_CATEGORY_NAME_MAXIMUM_LENGTH = 50;

    SubCategoryModel transformToSubCategoryModel(final SubCategory subCategory) {
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
        populateValidationFields(subCategoryModel);
        return subCategoryModel;
    }

    SubCategoryModel transformToSubCategoryModel(final SubCategoryModel subCategoryModel) {
        ModelStringValue name = ModelStringValue.builder()
            .withValue(subCategoryModel.getName().getValue())
            .build();
        ModelStringValue type = ModelStringValue.builder()
            .withValue(subCategoryModel.getTransactionType().getValue())
            .build();
        SubCategoryModel copySubCategoryModel = SubCategoryModel.builder()
            .withId(subCategoryModel.getId())
            .withName(name)
            .withTransactionType(type)
            .build();
        populateValidationFields(copySubCategoryModel);
        return copySubCategoryModel;
    }

    SubCategory transformToSubCategory(final SubCategoryModel subCategoryModel) {
        return SubCategory.builder()
            .withId(subCategoryModel.getId())
            .withName(subCategoryModel.getName().getValue())
            .withTransactionType(TransactionType.valueOf(subCategoryModel.getTransactionType().getValue()))
            .build();
    }

    private void populateValidationFields(final SubCategoryModel subCategoryModel) {
        subCategoryModel.getName().setMaximumLength(SUB_CATEGORY_NAME_MAXIMUM_LENGTH);
        subCategoryModel.getTransactionType().setPossibleEnumValues(TransactionType.getPossibleValues());
    }
}
