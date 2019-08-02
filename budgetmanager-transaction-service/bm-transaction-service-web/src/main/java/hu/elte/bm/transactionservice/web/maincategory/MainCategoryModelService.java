package hu.elte.bm.transactionservice.web.maincategory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryService;
import hu.elte.bm.transactionservice.web.common.ModelValidator;

@Service
public class MainCategoryModelService {

    private static final String CATEGORY_IS_INVALID_MESSAGE = "The new category is invalid.";
    private static final String CATEGORY_HAS_BEEN_SAVED = "The category has been saved.";
    private static final String CATEGORY_HAS_BEEN_SAVED_BEFORE_MESSAGE = "The category has been saved before.";
    private static final String CATEGORY_HAS_BEEN_UPDATED = "The category has been updated.";
    private static final String CATEGORY_CANNOT_BE_UPDATED_MESSAGE = "You cannot update this category, because it exists.";

    private final ModelValidator validator;
    private final MainCategoryService mainCategoryService;
    private final MainCategoryModelTransformer transformer;

    MainCategoryModelService(final ModelValidator validator, final MainCategoryService mainCategoryService,
        final MainCategoryModelTransformer transformer) {
        this.validator = validator;
        this.mainCategoryService = mainCategoryService;
        this.transformer = transformer;
    }

    List<MainCategoryModel> findAll(final MainCategoryModelRequestContext context) {
        List<MainCategory> subCategoryList = mainCategoryService.getMainCategoryList(context.getTransactionType());
        return subCategoryList.stream()
            .map(transformer::transformToMainCategoryModel)
            .collect(Collectors.toList());
    }

    MainCategoryModelResponse saveMainCategory(final MainCategoryModel mainCategoryModel) {
        preValidateSavableCategory(mainCategoryModel);
        MainCategoryModelResponse result = createResponseWithDefaultValues(transformer.transformToMainCategoryModel(mainCategoryModel));
        if (isValid(result.getMainCategoryModel())) {
            Optional<MainCategory> savedMainCategory = mainCategoryService.save(transformer.transformToMainCategory(result.getMainCategoryModel()));
            updateResponse(savedMainCategory, result, CATEGORY_HAS_BEEN_SAVED, CATEGORY_HAS_BEEN_SAVED_BEFORE_MESSAGE);
        } else {
            result.setMessage(CATEGORY_IS_INVALID_MESSAGE);
        }
        return result;
    }

    MainCategoryModelResponse updateMainCategory(final MainCategoryModel mainCategoryModel) {
        preValidateUpdatableCategory(mainCategoryModel);
        MainCategoryModelResponse result = createResponseWithDefaultValues(transformer.transformToMainCategoryModel(mainCategoryModel));
        if (isValid(result.getMainCategoryModel())) {
            Optional<MainCategory> savedMainCategory = mainCategoryService.update(transformer.transformToMainCategory(result.getMainCategoryModel()));
            updateResponse(savedMainCategory, result, CATEGORY_HAS_BEEN_UPDATED, CATEGORY_CANNOT_BE_UPDATED_MESSAGE);
        } else {
            result.setMessage(CATEGORY_IS_INVALID_MESSAGE);
        }
        return result;
    }

    private void preValidateSavableCategory(final MainCategoryModel mainCategoryModel) {
        if (mainCategoryModel.getId() != null) {
            throw new IllegalArgumentException(CATEGORY_IS_INVALID_MESSAGE);
        }
        validateSubCategoryModelFields(mainCategoryModel);
    }

    private void preValidateUpdatableCategory(final MainCategoryModel mainCategoryModel) {
        Assert.notNull(mainCategoryModel.getId(), CATEGORY_IS_INVALID_MESSAGE);
        validateSubCategoryModelFields(mainCategoryModel);
    }

    private void validateSubCategoryModelFields(final MainCategoryModel mainCategoryModel) {
        Assert.notNull(mainCategoryModel.getName(), CATEGORY_IS_INVALID_MESSAGE);
        Assert.notNull(mainCategoryModel.getTransactionType(), CATEGORY_IS_INVALID_MESSAGE);
    }

    private MainCategoryModelResponse createResponseWithDefaultValues(final MainCategoryModel mainCategoryModel) {
        MainCategoryModelResponse result = new MainCategoryModelResponse();
        result.setMainCategoryModel(mainCategoryModel);
        return result;
    }

    private boolean isValid(final MainCategoryModel mainCategoryModel) {
        boolean name = validator.validateModelStringValue(mainCategoryModel.getName(), "Name");
        boolean type = validator.validateModelStringValue(mainCategoryModel.getTransactionType(), "Type");
        return name && type;
    }

    private void updateResponse(final Optional<MainCategory> mainCategory,
        final MainCategoryModelResponse result, final String successMessage, final String unSuccessMessage) {
        if (mainCategory.isPresent()) {
            result.setMainCategoryModel(transformer.transformToMainCategoryModel(mainCategory.get()));
            result.setSuccessful(true);
            result.setMessage(successMessage);
        } else {
            result.setMessage(unSuccessMessage);
        }
    }

}
