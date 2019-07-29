package hu.elte.bm.transactionservice.domain.categories.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Service("subCategoryService")
public class DefaultSubCategoryService implements SubCategoryService {

    private static final String CATEGORY_CANNOT_BE_NULL_EXCEPTION_MESSAGE = "subCategory cannot be null!";
    private static final String TYPE_CANNOT_BE_NULL_EXCEPTION_MESSAGE = "categoryType cannot be null!";
    private final DatabaseProxy databaseProxy;

    DefaultSubCategoryService(final DatabaseProxy databaseProxy) {
        this.databaseProxy = databaseProxy;
    }

    @Override
    public List<SubCategory> getSubCategoryList(final TransactionType transactionType) {
        Assert.notNull(transactionType, TYPE_CANNOT_BE_NULL_EXCEPTION_MESSAGE);
        return databaseProxy.findAllSubCategory(transactionType);
    }

    @Override
    public Optional<SubCategory> saveSubCategory(final SubCategory subCategory) {
        Assert.notNull(subCategory, CATEGORY_CANNOT_BE_NULL_EXCEPTION_MESSAGE);
        return isSavable(subCategory) ? databaseProxy.saveSubCategory(subCategory) : Optional.empty();
    }

    private boolean isSavable(final SubCategory subCategory) {
        Optional<SubCategory> subCategoryWithSameName = databaseProxy.findSubCategoryByName(subCategory.getName(), subCategory.getTransactionType());
        return subCategoryWithSameName.isEmpty() || subCategoryWithSameName.map(category -> hasDifferentType(subCategory, category)).get();
    }

    private boolean hasDifferentType(final SubCategory subCategory, final SubCategory subCategoryWithSameName) {
        return !subCategory.equals(subCategoryWithSameName);
    }

    @Override
    public Optional<SubCategory> updateSubCategory(final SubCategory subCategory) {
        Assert.notNull(subCategory, CATEGORY_CANNOT_BE_NULL_EXCEPTION_MESSAGE);
        return isUpdatable(subCategory) ? databaseProxy.updateSubCategory(subCategory) : Optional.empty();
    }

    private boolean isUpdatable(final SubCategory subCategory) {
        boolean result = false;
        Optional<SubCategory> originalSubCategory = databaseProxy.findSubCategoryById(subCategory.getId());
        if (originalSubCategory.isPresent() && subCategoryTypeWasNotChanged(subCategory, originalSubCategory.get())) {
            Optional<SubCategory> subCategoryWithSameName = databaseProxy.findSubCategoryByName(subCategory.getName(), subCategory.getTransactionType());
            result = subCategoryWithSameName.isEmpty();
        }
        return result;
    }

    private boolean subCategoryTypeWasNotChanged(final SubCategory subCategory, final SubCategory originalSubCategory) {
        return subCategory.getTransactionType() == originalSubCategory.getTransactionType();
    }

}
