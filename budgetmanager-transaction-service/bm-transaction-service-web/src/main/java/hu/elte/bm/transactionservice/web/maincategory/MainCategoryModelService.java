package hu.elte.bm.transactionservice.web.maincategory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.commonpack.validator.ModelValidator;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryService;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Service
public class MainCategoryModelService {

    private final ModelValidator validator;
    private final MainCategoryService mainCategoryService;
    private final MainCategoryModelTransformer transformer;

    @Value("${main_category.main_category_is_invalid}")
    private String categoryIsInvalidMessage;

    @Value("${main_category.main_category_has_been_saved}")
    private String categoryHasBeenSaved;

    @Value("${main_category.main_category_has_been_saved_before}")
    private String categoryHasBeenSavedBeforeMessage;

    @Value("${main_category.main_category_has_been_updated}")
    private String categoryHasBeenUpdated;

    @Value("${main_category.main_category_cannot_be_updated}")
    private String categoryCannotBeUpdatedMessage;

    MainCategoryModelService(final ModelValidator validator, final MainCategoryService mainCategoryService,
        final MainCategoryModelTransformer transformer) {
        this.validator = validator;
        this.mainCategoryService = mainCategoryService;
        this.transformer = transformer;
    }

    List<MainCategoryModel> findAll(final TransactionType type) {
        List<MainCategory> subCategoryList = mainCategoryService.getMainCategoryList(type);
        return subCategoryList.stream()
            .map(transformer::transformToMainCategoryModel)
            .collect(Collectors.toList());
    }

    MainCategoryModelResponse saveMainCategory(final MainCategoryModel mainCategoryModel) {
        preValidateSavableCategory(mainCategoryModel);
        MainCategoryModelResponse result = createResponseWithDefaultValues(mainCategoryModel);
        if (isValid(result.getMainCategoryModel())) {
            Optional<MainCategory> savedMainCategory = mainCategoryService.save(transformer.transformToMainCategory(result.getMainCategoryModel()));
            updateResponse(savedMainCategory, result, categoryHasBeenSaved, categoryHasBeenSavedBeforeMessage);
        } else {
            result.setMessage(categoryIsInvalidMessage);
        }
        return result;
    }

    MainCategoryModelResponse updateMainCategory(final MainCategoryModel mainCategoryModel) {
        preValidateUpdatableCategory(mainCategoryModel);
        MainCategoryModelResponse result = createResponseWithDefaultValues(mainCategoryModel);
        if (isValid(result.getMainCategoryModel())) {
            Optional<MainCategory> savedMainCategory = mainCategoryService.update(transformer.transformToMainCategory(result.getMainCategoryModel()));
            updateResponse(savedMainCategory, result, categoryHasBeenUpdated, categoryCannotBeUpdatedMessage);
        } else {
            result.setMessage(categoryIsInvalidMessage);
        }
        return result;
    }

    private void preValidateSavableCategory(final MainCategoryModel mainCategoryModel) {
        if (mainCategoryModel.getId() != null) {
            throw new IllegalArgumentException(categoryIsInvalidMessage);
        }
        validateMainCategoryModelFields(mainCategoryModel);
    }

    private void preValidateUpdatableCategory(final MainCategoryModel mainCategoryModel) {
        Assert.notNull(mainCategoryModel.getId(), categoryIsInvalidMessage);
        validateMainCategoryModelFields(mainCategoryModel);
    }

    private void validateMainCategoryModelFields(final MainCategoryModel mainCategoryModel) {
        Assert.notNull(mainCategoryModel.getName(), categoryIsInvalidMessage);
        Assert.notNull(mainCategoryModel.getTransactionType(), categoryIsInvalidMessage);
    }

    private MainCategoryModelResponse createResponseWithDefaultValues(final MainCategoryModel mainCategoryModel) {
        transformer.setValidationFields(mainCategoryModel);
        MainCategoryModelResponse result = new MainCategoryModelResponse();
        result.setMainCategoryModel(mainCategoryModel);
        return result;
    }

    private boolean isValid(final MainCategoryModel mainCategoryModel) {
        boolean name = validator.validate(mainCategoryModel.getName(), "Name");
        boolean type = validator.validate(mainCategoryModel.getTransactionType(), "Type");
        return name && type;
    }

    private void updateResponse(final Optional<MainCategory> mainCategory,
        final MainCategoryModelResponse response, final String successMessage, final String unSuccessMessage) {
        if (mainCategory.isPresent()) {
            response.setMainCategoryModel(transformer.transformToMainCategoryModel(mainCategory.get()));
            response.setSuccessful(true);
            response.setMessage(successMessage);
        } else {
            response.setMessage(unSuccessMessage);
        }
    }

}
