package hu.elte.bm.transactionservice.web.subcategory;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;

@Component
@PropertySource("classpath:common_constraints.properties")
public class SubCategoryModelTransformer {

    @Value("${sub_category.name.maximum_length}")
    private Integer subCategoryNameMaximumLength;

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
        String name = subCategoryModel.getName() == null ? null : subCategoryModel.getName().getValue();
        TransactionType type = subCategoryModel.getTransactionType() == null ? null : TransactionType.valueOf(subCategoryModel.getTransactionType().getValue());
        return SubCategory.builder()
            .withId(subCategoryModel.getId())
            .withName(name)
            .withTransactionType(type)
            .build();
    }

    void setValidationFields(final SubCategoryModel subCategoryModel) {
        subCategoryModel.getName().setMaximumLength(subCategoryNameMaximumLength);
        subCategoryModel.getTransactionType().setPossibleEnumValues(Arrays.stream(TransactionType.values())
                .map(TransactionType::name)
                .collect(Collectors.toSet()));
    }
}
