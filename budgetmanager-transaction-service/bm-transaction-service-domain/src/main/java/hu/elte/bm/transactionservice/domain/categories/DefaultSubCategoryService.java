package hu.elte.bm.transactionservice.domain.categories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;
import hu.elte.bm.transactionservice.domain.transaction.TransactionContext;

@Service("subCategoryService")
public class DefaultSubCategoryService implements SubCategoryService {

    private final DatabaseProxy databaseProxy;

    @Value("${sub_category.user_id_cannot_be_null}")
    private String userIdCannotBeNullExceptionMessage;

    @Value("${sub_category.sub_category_cannot_be_null}")
    private String categoryCannotBeNullExceptionMessage;

    @Value("${sub_category.category_type_cannot_be_null}")
    private String typeCannotBeNullExceptionMessage;

    @Value("${sub_category.sub_category_cannot_be_found}")
    private String originalSubCategoryCannotBeFoundExceptionMessage;

    @Value("${sub_category.transaction_type_cannot_be_changed}")
    private String transactionTypeCannotBeChangedExceptionMessage;

    DefaultSubCategoryService(final DatabaseProxy databaseProxy) {
        this.databaseProxy = databaseProxy;
    }

    @Override
    public List<SubCategory> getSubCategoryList(final TransactionContext context) {
        validate(context);
        return databaseProxy.findAllSubCategory(context);
    }

    @Override
    public Optional<SubCategory> save(final SubCategory subCategory, final TransactionContext context) {
        validate(context);
        validate(subCategory);
        return isSavable(subCategory, context) ? databaseProxy.saveSubCategory(subCategory, context) : Optional.empty();
    }

    @Override
    public Optional<SubCategory> update(final SubCategory subCategory, final TransactionContext context) {
        validateForUpdate(subCategory, context);
        return isSavable(subCategory, context) ? databaseProxy.updateSubCategory(subCategory, context) : Optional.empty();
    }

    private void validate(final TransactionContext context) {
        Assert.notNull(context.getUserId(), userIdCannotBeNullExceptionMessage);
        Assert.notNull(context.getTransactionType(), typeCannotBeNullExceptionMessage);
    }

    private void validate(final SubCategory subCategory) {
        Assert.notNull(subCategory, categoryCannotBeNullExceptionMessage);
    }

    private boolean isSavable(final SubCategory subCategory, final TransactionContext context) {
        Optional<SubCategory> subCategoryWithSameName = databaseProxy.findSubCategoryByName(subCategory.getName(), context);
        return subCategoryWithSameName.isEmpty();
    }

    private void validateForUpdate(final SubCategory subCategory, final TransactionContext context) {
        validate(context);
        validate(subCategory);
        SubCategory originalSubCategory = databaseProxy.findSubCategoryById(subCategory.getId(), context).orElse(null);
        if (originalSubCategory == null) {
            throw new SubCategoryException(subCategory, originalSubCategoryCannotBeFoundExceptionMessage);
        } else if (subCategory.getTransactionType() != originalSubCategory.getTransactionType()) {
            throw new SubCategoryException(subCategory, transactionTypeCannotBeChangedExceptionMessage);
        }
    }

}
