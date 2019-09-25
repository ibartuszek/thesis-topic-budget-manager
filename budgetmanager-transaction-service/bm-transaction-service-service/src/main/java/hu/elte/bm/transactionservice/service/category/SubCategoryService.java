package hu.elte.bm.transactionservice.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryConflictException;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryNotFoundException;
import hu.elte.bm.transactionservice.service.database.SubCategoryDao;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@Service
public class SubCategoryService {

    private final SubCategoryDao subCategoryDao;

    @Value("${sub_category.user_id_cannot_be_null}")
    private String userIdCannotBeNullExceptionMessage;

    @Value("${sub_category.sub_category_cannot_be_null}")
    private String categoryCannotBeNullExceptionMessage;

    @Value("{sub_category.sub_category_id_cannot_be_null}")
    private String categoryIdCannotBeNullExceptionMessage;

    @Value("${sub_category.sub_category_has_been_saved_before}")
    private String categoryHasBeenSavedBeforeMessage;

    @Value("${sub_category.category_type_cannot_be_null}")
    private String typeCannotBeNullExceptionMessage;

    @Value("${sub_category.sub_category_cannot_be_found}")
    private String originalSubCategoryCannotBeFoundExceptionMessage;

    @Value("${sub_category.transaction_type_cannot_be_changed}")
    private String transactionTypeCannotBeChangedExceptionMessage;

    SubCategoryService(final SubCategoryDao subCategoryDao) {
        this.subCategoryDao = subCategoryDao;
    }

    public List<SubCategory> getSubCategoryList(final TransactionContext context) {
        validate(context);
        return subCategoryDao.findAll(context);
    }

    public SubCategory save(final SubCategory subCategory, final TransactionContext context) {
        validate(context);
        validateSavableSubCategory(subCategory);
        validateSubCategoryIsNotReserved(subCategory, context);
        return subCategoryDao.save(subCategory, context);
    }

    public SubCategory update(final SubCategory subCategory, final TransactionContext context) {
        validate(context);
        validateUpdatableSubCategory(subCategory, context);
        validateSubCategoryIsNotReserved(subCategory, context);
        return subCategoryDao.update(subCategory, context);
    }

    private void validate(final TransactionContext context) {
        Assert.notNull(context.getUserId(), userIdCannotBeNullExceptionMessage);
        Assert.notNull(context.getTransactionType(), typeCannotBeNullExceptionMessage);
    }

    private void validateSavableSubCategory(final SubCategory subCategory) {
        Assert.notNull(subCategory, categoryCannotBeNullExceptionMessage);
        Assert.isNull(subCategory.getId(), categoryIdCannotBeNullExceptionMessage);
    }

    private void validateSubCategoryIsNotReserved(final SubCategory subCategory, final TransactionContext context) {
        if (subCategoryDao.findByName(subCategory.getName(), context).isPresent()) {
            throw new SubCategoryConflictException(subCategory, categoryHasBeenSavedBeforeMessage);
        }
    }

    private void validateUpdatableSubCategory(final SubCategory subCategory, final TransactionContext context) {
        Assert.notNull(subCategory, categoryCannotBeNullExceptionMessage);
        Assert.notNull(subCategory.getId(), categoryIdCannotBeNullExceptionMessage);
        Optional<SubCategory> originalSubCategory = subCategoryDao.findById(subCategory.getId(), context);
        if (originalSubCategory.isEmpty()) {
            throw new SubCategoryNotFoundException(originalSubCategoryCannotBeFoundExceptionMessage, subCategory);
        } else if (subCategory.getTransactionType() != originalSubCategory.get().getTransactionType()) {
            throw new IllegalArgumentException(transactionTypeCannotBeChangedExceptionMessage);
        }
    }

}
