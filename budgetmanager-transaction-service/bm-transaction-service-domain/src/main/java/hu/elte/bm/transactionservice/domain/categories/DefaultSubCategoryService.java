package hu.elte.bm.transactionservice.domain.categories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Service("subCategoryService")
public class DefaultSubCategoryService implements SubCategoryService {

    private static final String CATEGORY_CANNOT_BE_NULL_EXCEPTION_MESSAGE = "subCategory cannot be null!";
    private static final String TYPE_CANNOT_BE_NULL_EXCEPTION_MESSAGE = "categoryType cannot be null!";
    private static final String ORIGINAL_SUB_CATEGORY_CANNOT_BE_FOUND_EXCEPTION_MESSAGE = "Original subCategory cannot be found in the repository!";
    private static final String TRANSACTION_TYPE_CANNOT_BE_CHANGED_EXCEPTION_MESSAGE = "Transaction type cannot be changed!";

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
    public Optional<SubCategory> save(final SubCategory subCategory) {
        validate(subCategory);
        return isSavable(subCategory) ? databaseProxy.saveSubCategory(subCategory) : Optional.empty();
    }

    private void validate(final SubCategory subCategory) {
        Assert.notNull(subCategory, CATEGORY_CANNOT_BE_NULL_EXCEPTION_MESSAGE);
    }

    private boolean isSavable(final SubCategory subCategory) {
        Optional<SubCategory> subCategoryWithSameName = databaseProxy.findSubCategoryByName(subCategory.getName(), subCategory.getTransactionType());
        return subCategoryWithSameName.isEmpty();
    }

    @Override
    public Optional<SubCategory> update(final SubCategory subCategory) {
        validateForUpdate(subCategory);
        return isSavable(subCategory) ? databaseProxy.updateSubCategory(subCategory) : Optional.empty();
    }

    private void validateForUpdate(final SubCategory subCategory) {
        validate(subCategory);
        SubCategory originalSubCategory = databaseProxy.findSubCategoryById(subCategory.getId()).orElse(null);
        if (originalSubCategory == null) {
            throw new SubCategoryException(subCategory, ORIGINAL_SUB_CATEGORY_CANNOT_BE_FOUND_EXCEPTION_MESSAGE);
        } else if (subCategory.getTransactionType() != originalSubCategory.getTransactionType()) {
            throw new SubCategoryException(subCategory, TRANSACTION_TYPE_CANNOT_BE_CHANGED_EXCEPTION_MESSAGE);
        }
    }

}
