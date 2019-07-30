package hu.elte.bm.transactionservice.domain.categories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Service("mainCategoryService")
public class DefaultMainCategoryService implements MainCategoryService {

    private static final String SUB_CATEGORY_ID_EXCEPTION_MESSAGE = "A subCategory does not have id!";
    private static final String ORIGINAL_MAIN_CATEGORY_CANNOT_BE_FOUND_EXCEPTION_MESSAGE = "Original mainCategory cannot be found in the repository!";
    private static final String TRANSACTION_TYPE_CANNOT_BE_CHANGED_EXCEPTION_MESSAGE = "Transaction type cannot be changed!";
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
    public Optional<MainCategory> save(final MainCategory mainCategory) {
        validate(mainCategory);
        return thereIsNoCategoryWithSameName(mainCategory) ? databaseProxy.saveMainCategory(mainCategory) : Optional.empty();
    }

    private void validate(final MainCategory mainCategory) {
        Assert.notNull(mainCategory, CATEGORY_CANNOT_BE_NULL_EXCEPTION_MESSAGE);
        if (!hasAllSubCategoryId(mainCategory)) {
            throw new MainCategoryException(mainCategory, SUB_CATEGORY_ID_EXCEPTION_MESSAGE);
        }
    }

    private boolean hasAllSubCategoryId(final MainCategory mainCategory) {
        return mainCategory.getSubCategorySet().stream()
            .noneMatch(subCategory -> subCategory.getId() == null);
    }

    private boolean thereIsNoCategoryWithSameName(final MainCategory mainCategory) {
        Optional<MainCategory> mainCategoryWithSameName = databaseProxy.findMainCategoryByName(mainCategory.getName(), mainCategory.getTransactionType());
        return mainCategoryWithSameName.isEmpty();
    }

    @Override
    public Optional<MainCategory> update(final MainCategory mainCategory) {
        validateForUpdate(mainCategory);
        return thereIsNoCategoryWithSameName(mainCategory) ? databaseProxy.updateMainCategory(mainCategory) : Optional.empty();
    }

    private void validateForUpdate(final MainCategory mainCategory) {
        validate(mainCategory);
        MainCategory originalMainCategory = databaseProxy.findMainCategoryById(mainCategory.getId()).orElse(null);
        if (originalMainCategory == null) {
            throw new MainCategoryException(mainCategory, ORIGINAL_MAIN_CATEGORY_CANNOT_BE_FOUND_EXCEPTION_MESSAGE);
        } else if (mainCategory.getTransactionType() != originalMainCategory.getTransactionType()) {
            throw new MainCategoryException(mainCategory, TRANSACTION_TYPE_CANNOT_BE_CHANGED_EXCEPTION_MESSAGE);
        }

    }

}
