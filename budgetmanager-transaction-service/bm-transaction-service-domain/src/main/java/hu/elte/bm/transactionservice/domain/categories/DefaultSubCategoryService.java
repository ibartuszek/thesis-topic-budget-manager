package hu.elte.bm.transactionservice.domain.categories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Service("subCategoryService")
@PropertySource("classpath:messages.properties")
public class DefaultSubCategoryService implements SubCategoryService {

    private final DatabaseProxy databaseProxy;

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
    public List<SubCategory> getSubCategoryList(final TransactionType transactionType) {
        Assert.notNull(transactionType, typeCannotBeNullExceptionMessage);
        return databaseProxy.findAllSubCategory(transactionType);
    }

    @Override
    public Optional<SubCategory> save(final SubCategory subCategory) {
        validate(subCategory);
        return isSavable(subCategory) ? databaseProxy.saveSubCategory(subCategory) : Optional.empty();
    }

    private void validate(final SubCategory subCategory) {
        Assert.notNull(subCategory, categoryCannotBeNullExceptionMessage);
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
            throw new SubCategoryException(subCategory, originalSubCategoryCannotBeFoundExceptionMessage);
        } else if (subCategory.getTransactionType() != originalSubCategory.getTransactionType()) {
            throw new SubCategoryException(subCategory, transactionTypeCannotBeChangedExceptionMessage);
        }
    }

}
