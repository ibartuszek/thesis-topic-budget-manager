package hu.elte.bm.transactionservice.web.subcategory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.commonpack.validator.ModelValidator;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.category.SubCategoryService;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@Service
public class SubCategoryModelService {

    private final ModelValidator validator;
    private final SubCategoryService subCategoryService;
    private final SubCategoryModelTransformer transformer;

    @Value("${main_category.category_type_cannot_be_null}")
    private String typeCannotBeNull;

    @Value("${sub_category.user_id_cannot_be_null}")
    private String userIdCannotBeNull;

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

    List<SubCategoryModel> findAll(final TransactionType type, final Long userId) {
        validateTransactionContext(userId, type);
        List<SubCategory> subCategoryList = subCategoryService.getSubCategoryList(createTransactionContext(type, userId));
        return subCategoryList.stream()
            .map(transformer::transformToSubCategoryModel)
            .collect(Collectors.toList());
    }

    SubCategoryModelResponse saveSubCategory(final SubCategoryModelRequestContext context) {
        preValidateSavableCategory(context);
        SubCategoryModelResponse result = createResponseWithDefaultValues(context.getSubCategoryModel());
        if (isValid(result.getSubCategoryModel())) {
            Optional<SubCategory> savedSubCategory = subCategoryService.save(
                transformer.transformToSubCategory(result.getSubCategoryModel()),
                createTransactionContext(context.getTransactionType(), context.getUserId()));
            updateResponse(savedSubCategory, result, categoryHasBeenSaved, categoryHasBeenSavedBeforeMessage);
        } else {
            result.setMessage(categoryIsInvalidMessage);
        }
        return result;
    }

    SubCategoryModelResponse updateSubCategory(final SubCategoryModelRequestContext context) {
        preValidateUpdatableCategory(context);
        SubCategoryModelResponse result = createResponseWithDefaultValues(context.getSubCategoryModel());
        if (isValid(result.getSubCategoryModel())) {
            Optional<SubCategory> savedSubCategory = subCategoryService.update(
                transformer.transformToSubCategory(result.getSubCategoryModel()),
                createTransactionContext(context.getTransactionType(), context.getUserId()));
            updateResponse(savedSubCategory, result, categoryHasBeenUpdated, categoryCannotBeUpdatedMessage);
        } else {
            result.setMessage(categoryIsInvalidMessage);
        }
        return result;
    }

    private void preValidateSavableCategory(final SubCategoryModelRequestContext context) {
        if (context.getSubCategoryModel().getId() != null) {
            throw new IllegalArgumentException(categoryIsInvalidMessage);
        }
        validateSubCategoryModelFields(context);
    }

    private void preValidateUpdatableCategory(final SubCategoryModelRequestContext context) {
        Assert.notNull(context.getSubCategoryModel().getId(), categoryIsInvalidMessage);
        validateSubCategoryModelFields(context);
    }

    private void validateSubCategoryModelFields(final SubCategoryModelRequestContext context) {
        validateTransactionContext(context.getUserId(), context.getTransactionType());
        Assert.notNull(context.getSubCategoryModel().getName(), categoryIsInvalidMessage);
        Assert.notNull(context.getSubCategoryModel().getTransactionType(), categoryIsInvalidMessage);
    }

    private void validateTransactionContext(final Long userId, final TransactionType transactionType) {
        Assert.notNull(userId, userIdCannotBeNull);
        Assert.notNull(transactionType, typeCannotBeNull);
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

    private TransactionContext createTransactionContext(final TransactionType transactionType, final Long userId) {
        return TransactionContext.builder()
            .withTransactionType(transactionType)
            .withUserId(userId)
            .build();
    }
}
