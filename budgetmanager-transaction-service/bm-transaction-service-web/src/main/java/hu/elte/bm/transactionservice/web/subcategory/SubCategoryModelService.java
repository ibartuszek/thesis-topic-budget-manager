package hu.elte.bm.transactionservice.web.subcategory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryService;
import hu.elte.bm.transactionservice.web.common.ModelValidator;

@Service
public class SubCategoryModelService {

    private static final String CATEGORY_IS_INVALID_MESSAGE = "The new category is invalid.";
    private static final String CATEGORY_HAS_BEEN_SAVED = "The category has been saved.";
    private static final String CATEGORY_HAS_BEEN_SAVED_BEFORE_MESSAGE = "The category has been saved before.";
    private static final String CATEGORY_HAS_BEEN_UPDATED = "The category has been updated.";
    private static final String CATEGORY_CANNOT_BE_UPDATED_MESSAGE = "You cannot update this category, because it exists.";

    private final ModelValidator validator;
    private final SubCategoryService subCategoryService;
    private final SubCategoryModelTransformer transformer;

    SubCategoryModelService(final ModelValidator validator, final SubCategoryService subCategoryService,
        final SubCategoryModelTransformer transformer) {
        this.validator = validator;
        this.subCategoryService = subCategoryService;
        this.transformer = transformer;
    }

    List<SubCategoryModel> findAll(final SubCategoryModelRequestContext context) {
        List<SubCategory> subCategoryList = subCategoryService.getSubCategoryList(context.getTransactionType());
        return subCategoryList.stream()
            .map(transformer::transformToSubCategoryModel)
            .collect(Collectors.toList());
    }

    SubCategoryModelResponse saveSubCategory(final SubCategoryModel subCategoryModel) {
        preValidateSavableCategory(subCategoryModel);
        SubCategoryModelResponse result = createResponseWithDefaultValues(transformer.transformToSubCategoryModel(subCategoryModel));
        if (isValid(result.getSubCategoryModel())) {
            Optional<SubCategory> savedSubCategory = subCategoryService.save(transformer.transformToSubCategory(result.getSubCategoryModel()));
            updateResponse(savedSubCategory, result, CATEGORY_HAS_BEEN_SAVED, CATEGORY_HAS_BEEN_SAVED_BEFORE_MESSAGE);
        } else {
            result.setMessage(CATEGORY_IS_INVALID_MESSAGE);
        }
        return result;
    }

    SubCategoryModelResponse updateSubCategory(final SubCategoryModel subCategoryModel) {
        preValidateUpdatableCategory(subCategoryModel);
        SubCategoryModelResponse result = createResponseWithDefaultValues(transformer.transformToSubCategoryModel(subCategoryModel));
        if (isValid(result.getSubCategoryModel())) {
            Optional<SubCategory> savedSubCategory = subCategoryService.update(transformer.transformToSubCategory(result.getSubCategoryModel()));
            updateResponse(savedSubCategory, result, CATEGORY_HAS_BEEN_UPDATED, CATEGORY_CANNOT_BE_UPDATED_MESSAGE);
        } else {
            result.setMessage(CATEGORY_IS_INVALID_MESSAGE);
        }
        return result;
    }

    private void preValidateSavableCategory(final SubCategoryModel subCategoryModel) {
        if (subCategoryModel.getId() != null) {
            throw new IllegalArgumentException(CATEGORY_IS_INVALID_MESSAGE);
        }
        validateSubCategoryModelFields(subCategoryModel);
    }

    private void preValidateUpdatableCategory(final SubCategoryModel subCategoryModel) {
        Assert.notNull(subCategoryModel.getId(), CATEGORY_IS_INVALID_MESSAGE);
        validateSubCategoryModelFields(subCategoryModel);
    }

    private void validateSubCategoryModelFields(final SubCategoryModel subCategoryModel) {
        Assert.notNull(subCategoryModel.getName(), CATEGORY_IS_INVALID_MESSAGE);
        Assert.notNull(subCategoryModel.getTransactionType(), CATEGORY_IS_INVALID_MESSAGE);
    }

    private SubCategoryModelResponse createResponseWithDefaultValues(final SubCategoryModel subCategoryModel) {
        SubCategoryModelResponse result = new SubCategoryModelResponse();
        result.setSubCategoryModel(subCategoryModel);
        return result;
    }

    private boolean isValid(final SubCategoryModel subCategoryModel) {
        boolean name = validator.validateModelStringValue(subCategoryModel.getName(), "Name");
        boolean type = validator.validateModelStringValue(subCategoryModel.getTransactionType(), "Type");
        return name && type;
    }

    private void updateResponse(final Optional<SubCategory> subCategory,
        final SubCategoryModelResponse result, final String successMessage, final String unSuccessMessage) {
        if (subCategory.isPresent()) {
            result.setSubCategoryModel(transformer.transformToSubCategoryModel(subCategory.get()));
            result.setSuccessful(true);
            result.setMessage(successMessage);
        } else {
            result.setMessage(unSuccessMessage);
        }
    }
}
