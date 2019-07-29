package hu.elte.bm.transactionservice.domain.categories.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Service("mainCategoryService")
public class DefaultMainCategoryService implements MainCategoryService {

    private static final String CATEGORY_CANNOT_BE_NULL_EXCEPTION_MESSAGE = "mainCategory cannot be null!";
    private static final String TYPE_CANNOT_BE_NULL_EXCEPTION_MESSAGE = "categoryType cannot be null!";
    private final DatabaseProxy databaseProxy;

    DefaultMainCategoryService(final DatabaseProxy databaseProxy) {
        this.databaseProxy = databaseProxy;
    }

    @Override
    public List<MainCategory> getMainCategoryList(final TransactionType transactionType) {
        Assert.notNull(transactionType, TYPE_CANNOT_BE_NULL_EXCEPTION_MESSAGE);
        return databaseProxy.findAllMainCategory(transactionType);
    }

    @Override
    public Optional<MainCategory> saveMainCategory(final MainCategory mainCategory) {
        Assert.notNull(mainCategory, CATEGORY_CANNOT_BE_NULL_EXCEPTION_MESSAGE);
        return isSavable(mainCategory) ? databaseProxy.saveMainCategory(mainCategory) : Optional.empty();
    }

    private boolean isSavable(final MainCategory mainCategory) {
        return hasAllSubCategoryId(mainCategory.getSubCategorySet()) && thereIsNoCategoryWithSameName(mainCategory);
    }

    private boolean hasAllSubCategoryId(final Set<SubCategory> subCategorySet) {
        return subCategorySet.stream().noneMatch(subCategory -> subCategory.getId() == null);
    }

    private boolean thereIsNoCategoryWithSameName(final MainCategory mainCategory) {
        Optional<MainCategory> mainCategoryWithSameName = databaseProxy.findMainCategoryByName(mainCategory.getName(), mainCategory.getTransactionType());
        return mainCategoryWithSameName.isEmpty();
    }

    @Override
    public Optional<MainCategory> updateMainCategory(final MainCategory mainCategory) {
        Assert.notNull(mainCategory, CATEGORY_CANNOT_BE_NULL_EXCEPTION_MESSAGE);
        return isUpdatable(mainCategory) ? databaseProxy.updateMainCategory(mainCategory) : Optional.empty();
    }

    private boolean isUpdatable(final MainCategory mainCategory) {
        boolean result = false;
        Optional<MainCategory> originalMainCategory = databaseProxy.findMainCategoryById(mainCategory.getId());
        if (originalMainCategory.isPresent() && mainCategoryTypeWasNotChangedAndHasOldSubCategories(mainCategory, originalMainCategory.get())) {
            result = hasAllSubCategoryId(mainCategory.getSubCategorySet()) && hasDifferentType(mainCategory, originalMainCategory.get());
        }
        return result;
    }

    private boolean mainCategoryTypeWasNotChangedAndHasOldSubCategories(final MainCategory mainCategory, final MainCategory originalMainCategory) {
        return originalMainCategory != null
            && mainCategory.getTransactionType() == originalMainCategory.getTransactionType()
            && mainCategory.getSubCategorySet().containsAll(originalMainCategory.getSubCategorySet());
    }

    private boolean hasDifferentType(final MainCategory mainCategory, final MainCategory originalMainCategory) {
        Optional<MainCategory> mainCategoryWithSameName = databaseProxy.findMainCategoryByName(mainCategory.getName(), mainCategory.getTransactionType());
        return mainCategoryWithSameName.isEmpty()
            || originalMainCategory.equals(mainCategoryWithSameName.get());
    }

}
