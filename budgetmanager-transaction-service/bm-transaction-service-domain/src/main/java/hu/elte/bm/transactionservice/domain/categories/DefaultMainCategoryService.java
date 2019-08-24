package hu.elte.bm.transactionservice.domain.categories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;
import hu.elte.bm.transactionservice.domain.transaction.TransactionContext;

@Service("mainCategoryService")
public class DefaultMainCategoryService implements MainCategoryService {

    private final DatabaseProxy databaseProxy;

    @Value("${main_category.sub_category_without_id}")
    private String subCategoryIdExceptionMessage;

    @Value("${main_category.main_category_cannot_be_found}")
    private String originalMainCategoryCannotBeFoundExceptionMessage;

    @Value("${main_category.transaction_type_cannot_be_changed}")
    private String transactionTypeCannotBeChangedExceptionMessage;

    @Value("${main_category.main_category_cannot_be_null}")
    private String categoryCannotBeNullExceptionMessage;

    @Value("${main_category.category_type_cannot_be_null}")
    private String typeCannotBeNullExceptionMessage;

    @Value("${main_category.main_category_does_not_have_original_sub_categories}")
    private String mainCategoryNotContainsAllSubcategoryExceptionMessage;

    @Value("${main_category.user_id_cannot_be_null}")
    private String userIdCannotBeNullExceptionMessage;

    DefaultMainCategoryService(final DatabaseProxy databaseProxy) {
        this.databaseProxy = databaseProxy;
    }

    @Override
    public List<MainCategory> getMainCategoryList(final TransactionContext context) {
        validate(context);
        return databaseProxy.findAllMainCategory(context);
    }

    @Override
    public Optional<MainCategory> save(final MainCategory mainCategory, final TransactionContext context) {
        validate(context);
        validate(mainCategory);
        return thereIsNoCategoryWithSameName(mainCategory, context) ? databaseProxy.saveMainCategory(mainCategory, context) : Optional.empty();
    }

    private void validate(final TransactionContext context) {
        Assert.notNull(context.getTransactionType(), typeCannotBeNullExceptionMessage);
        Assert.notNull(context.getUserId(), userIdCannotBeNullExceptionMessage);
    }

    private void validate(final MainCategory mainCategory) {
        Assert.notNull(mainCategory, categoryCannotBeNullExceptionMessage);
        if (!hasAllSubCategoryId(mainCategory)) {
            throw new MainCategoryException(mainCategory, subCategoryIdExceptionMessage);
        }
    }

    private boolean hasAllSubCategoryId(final MainCategory mainCategory) {
        return mainCategory.getSubCategorySet().stream()
            .noneMatch(subCategory -> subCategory.getId() == null);
    }

    private boolean thereIsNoCategoryWithSameName(final MainCategory mainCategory, final TransactionContext context) {
        Optional<MainCategory> mainCategoryWithSameName = databaseProxy.findMainCategoryByName(mainCategory.getName(), context);
        return mainCategoryWithSameName.isEmpty();
    }

    @Override
    public Optional<MainCategory> update(final MainCategory mainCategory, final TransactionContext context) {
        validateForUpdate(mainCategory, context);
        return thereIsNoCategoryWithSameName(mainCategory, context) ? databaseProxy.updateMainCategory(mainCategory, context) : Optional.empty();
    }

    private void validateForUpdate(final MainCategory mainCategory, final TransactionContext context) {
        validate(context);
        validate(mainCategory);
        MainCategory originalMainCategory = databaseProxy.findMainCategoryById(mainCategory.getId(), context).orElse(null);
        if (originalMainCategory == null) {
            throw new MainCategoryException(mainCategory, originalMainCategoryCannotBeFoundExceptionMessage);
        } else if (mainCategory.getTransactionType() != originalMainCategory.getTransactionType()) {
            throw new MainCategoryException(mainCategory, transactionTypeCannotBeChangedExceptionMessage);
        } else if (!mainCategory.getSubCategorySet().containsAll(originalMainCategory.getSubCategorySet())) {
            throw new MainCategoryException(mainCategory, mainCategoryNotContainsAllSubcategoryExceptionMessage);
        }

    }

}
