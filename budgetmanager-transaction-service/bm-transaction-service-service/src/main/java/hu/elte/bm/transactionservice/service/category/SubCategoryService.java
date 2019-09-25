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

    @Value("${sub_category.sub_category_cannot_be_found}")
    private String categoryCannotBeFound;

    @Value("${sub_category.sub_category_cannot_be_null}")
    private String categoryCannotBeNull;

    @Value("${sub_category.sub_category_has_been_saved_before}")
    private String categoryHasBeenSavedBefore;

    @Value("${sub_category.sub_category_id_cannot_be_null}")
    private String categoryIdCannotBeNull;

    @Value("${sub_category.sub_category_id_must_be_null}")
    private String categoryIdMustBeNull;

    @Value("${sub_category.sub_category_not_changed}")
    private String categoryNotChanged;

    @Value("${sub_category.transaction_type_cannot_be_changed}")
    private String typeCannotBeChanged;

    @Value("${sub_category.category_type_cannot_be_null}")
    private String typeCannotBeNull;

    @Value("${sub_category.user_id_cannot_be_null}")
    private String userIdCannotBeNull;

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
        Assert.notNull(context.getUserId(), userIdCannotBeNull);
        Assert.notNull(context.getTransactionType(), typeCannotBeNull);
    }

    private void validateSavableSubCategory(final SubCategory subCategory) {
        Assert.notNull(subCategory, categoryCannotBeNull);
        Assert.isNull(subCategory.getId(), categoryIdCannotBeNull);
    }

    private void validateSubCategoryIsNotReserved(final SubCategory subCategory, final TransactionContext context) {
        // Only name can be changed with update!!! If there is any new updatable parameter it should be updated!
        Optional<SubCategory> subCategoryWithSameName = subCategoryDao.findByName(subCategory.getName(), context);
        if (subCategoryWithSameName.isPresent()) {
            if (!subCategoryWithSameName.get().getId().equals(subCategory.getId())) {
                throw new SubCategoryConflictException(subCategory, categoryHasBeenSavedBefore);
            } else {
                throw new IllegalArgumentException(categoryNotChanged);
            }
        }
    }

    private void validateUpdatableSubCategory(final SubCategory subCategory, final TransactionContext context) {
        Assert.notNull(subCategory, categoryCannotBeNull);
        Assert.notNull(subCategory.getId(), categoryIdCannotBeNull);
        Optional<SubCategory> originalSubCategory = subCategoryDao.findById(subCategory.getId(), context);
        if (originalSubCategory.isEmpty()) {
            throw new SubCategoryNotFoundException(categoryCannotBeFound, subCategory);
        } else if (subCategory.getTransactionType() != originalSubCategory.get().getTransactionType()) {
            throw new IllegalArgumentException(typeCannotBeChanged);
        }
    }

}
