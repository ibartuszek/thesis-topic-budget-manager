package hu.elte.bm.transactionservice.web.subcategory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.commonpack.validator.ModelValidator;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryService;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Service
@PropertySource("classpath:messages.properties")
public class SubCategoryModelService {

    private final ModelValidator validator;
    private final SubCategoryService subCategoryService;
    private final SubCategoryModelTransformer transformer;
    @Value("${sub_category.sub_category_is_invalid}")
    private String categoryIsInvalidMessage;
    @Value("${sub_category.sub_category_has_been_saved}")
    private String categoryHasBeenSaved;
    @Value("${sub_category.sub_category_has_been_saved_before}")
    private String categoryHasBeenSavedBeforeMessage;
    @Value("${sub_category.sub_category_has_been_updated}")
    private String categoryHasBeenUpdated;
    @Value("${sub_category.sub_category_cannot_be_updated}")
    private String categoryCannotBeUpdatedMessage;

    SubCategoryModelService(final ModelValidator validator, final SubCategoryService subCategoryService,
        final SubCategoryModelTransformer transformer) {
        this.validator = validator;
        this.subCategoryService = subCategoryService;
        this.transformer = transformer;
    }

    List<SubCategoryModel> findAll(final TransactionType type) {
        List<SubCategory> subCategoryList = subCategoryService.getSubCategoryList(type);
        return subCategoryList.stream()
            .map(transformer::transformToSubCategoryModel)
            .collect(Collectors.toList());
    }

    SubCategoryModelResponse saveSubCategory(final SubCategoryModel subCategoryModel) {
        preValidateSavableCategory(subCategoryModel);
        SubCategoryModelResponse result = createResponseWithDefaultValues(subCategoryModel);
        if (isValid(result.getSubCategoryModel())) {
            Optional<SubCategory> savedSubCategory = subCategoryService.save(transformer.transformToSubCategory(result.getSubCategoryModel()));
            updateResponse(savedSubCategory, result, categoryHasBeenSaved, categoryHasBeenSavedBeforeMessage);
        } else {
            result.setMessage(categoryIsInvalidMessage);
        }
        return result;
    }

    SubCategoryModelResponse updateSubCategory(final SubCategoryModel subCategoryModel) {
        preValidateUpdatableCategory(subCategoryModel);
        SubCategoryModelResponse result = createResponseWithDefaultValues(subCategoryModel);
        if (isValid(result.getSubCategoryModel())) {
            Optional<SubCategory> savedSubCategory = subCategoryService.update(transformer.transformToSubCategory(result.getSubCategoryModel()));
            updateResponse(savedSubCategory, result, categoryHasBeenUpdated, categoryCannotBeUpdatedMessage);
        } else {
            result.setMessage(categoryIsInvalidMessage);
        }
        return result;
    }

    private void preValidateSavableCategory(final SubCategoryModel subCategoryModel) {
        if (subCategoryModel.getId() != null) {
            throw new IllegalArgumentException(categoryIsInvalidMessage);
        }
        validateSubCategoryModelFields(subCategoryModel);
    }

    private void preValidateUpdatableCategory(final SubCategoryModel subCategoryModel) {
        Assert.notNull(subCategoryModel.getId(), categoryIsInvalidMessage);
        validateSubCategoryModelFields(subCategoryModel);
    }

    private void validateSubCategoryModelFields(final SubCategoryModel subCategoryModel) {
        Assert.notNull(subCategoryModel.getName(), categoryIsInvalidMessage);
        Assert.notNull(subCategoryModel.getTransactionType(), categoryIsInvalidMessage);
    }

    private SubCategoryModelResponse createResponseWithDefaultValues(final SubCategoryModel subCategoryModel) {
        transformer.setValidationFields(subCategoryModel);
        SubCategoryModelResponse result = new SubCategoryModelResponse();
        result.setSubCategoryModel(subCategoryModel);
        return result;
    }

    private boolean isValid(final SubCategoryModel subCategoryModel) {
        boolean name = validator.validate(subCategoryModel.getName(), "Name");
        boolean type = validator.validate(subCategoryModel.getTransactionType(), "Type");
        return name && type;
    }

    private void updateResponse(final Optional<SubCategory> subCategory,
        final SubCategoryModelResponse response, final String successMessage, final String unSuccessMessage) {
        if (subCategory.isPresent()) {
            response.setSubCategoryModel(transformer.transformToSubCategoryModel(subCategory.get()));
            response.setSuccessful(true);
            response.setMessage(successMessage);
        } else {
            response.setMessage(unSuccessMessage);
        }
    }
}
