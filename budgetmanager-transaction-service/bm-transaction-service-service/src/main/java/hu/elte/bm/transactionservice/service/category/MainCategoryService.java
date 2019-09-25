package hu.elte.bm.transactionservice.service.category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryConflictException;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryNotFoundException;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.service.database.MainCategoryDao;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@Service("mainCategoryService")
public class MainCategoryService {

    private final MainCategoryDao mainCategoryDao;

    @Value("${main_category.main_category_cannot_be_found}")
    private String categoryCannotBeFound;

    @Value("${main_category.main_category_cannot_be_null}")
    private String categoryCannotBeNull;

    @Value("${main_category.main_category_has_been_saved_before}")
    private String categoryHasBeenSavedBefore;

    @Value("${main_category.main_category_id_cannot_be_null}")
    private String categoryIdCannotBeNull;

    @Value("${main_category.main_category_id_must_be_null}")
    private String categoryIdMustBeNull;

    @Value("${main_category.main_category_does_not_have_original_sub_categories}")
    private String categoryNotContainsAllSubcategory;

    @Value("${main_category.main_category_not_changed}")
    private String categoryNotChanged;

    @Value("${main_category.sub_category_without_id}")
    private String subCategoryWithoutId;

    @Value("${main_category.transaction_type_cannot_be_changed}")
    private String typeCannotBeChanged;

    @Value("${main_category.category_type_cannot_be_null}")
    private String typeCannotBeNull;

    @Value("${main_category.user_id_cannot_be_null}")
    private String userIdCannotBeNull;

    MainCategoryService(final MainCategoryDao mainCategoryDao) {
        this.mainCategoryDao = mainCategoryDao;
    }

    public List<MainCategory> getMainCategoryList(final TransactionContext context) {
        validate(context);
        return mainCategoryDao.findAll(context);
    }

    public MainCategory save(final MainCategory mainCategory, final TransactionContext context) {
        validate(context);
        validateSavableMainCategory(mainCategory);
        validateSubCategoriesHasId(mainCategory.getSubCategorySet());
        validateMainCategoryIsNotReserved(mainCategory, context);
        return mainCategoryDao.save(mainCategory, context);
    }

    public MainCategory update(final MainCategory mainCategory, final TransactionContext context) {
        validate(context);
        validateUpdatableMainCategory(mainCategory);
        validateSubCategoriesHasId(mainCategory.getSubCategorySet());
        validateMainCategoryIsNotReserved(mainCategory, context);
        validateForUpdate(mainCategory, context);
        return mainCategoryDao.update(mainCategory, context);
    }

    private void validate(final TransactionContext context) {
        Assert.notNull(context.getTransactionType(), typeCannotBeNull);
        Assert.notNull(context.getUserId(), userIdCannotBeNull);
    }

    private void validateSavableMainCategory(final MainCategory mainCategory) {
        Assert.notNull(mainCategory, categoryCannotBeNull);
        Assert.isNull(mainCategory.getId(), categoryIdMustBeNull);
    }

    private void validateSubCategoriesHasId(final Set<SubCategory> subCategorySet) {
        if (!hasAllSubCategoryId(subCategorySet)) {
            throw new IllegalArgumentException(subCategoryWithoutId);
        }
    }

    private boolean hasAllSubCategoryId(final Set<SubCategory> subCategorySet) {
        return subCategorySet.stream()
            .noneMatch(subCategory -> subCategory.getId() == null);
    }

    private void validateMainCategoryIsNotReserved(final MainCategory mainCategory, final TransactionContext context) {
        Optional<MainCategory> mainCategoryWithSameName = mainCategoryDao.findByName(mainCategory.getName(), context);
        if (mainCategoryWithSameName.isPresent() && !mainCategoryWithSameName.get().getId().equals(mainCategory.getId())) {
            throw new MainCategoryConflictException(mainCategory, categoryHasBeenSavedBefore);
        }
    }

    private void validateUpdatableMainCategory(final MainCategory mainCategory) {
        Assert.notNull(mainCategory, categoryCannotBeNull);
        Assert.notNull(mainCategory.getId(), categoryIdCannotBeNull);
    }

    private void validateForUpdate(final MainCategory mainCategory, final TransactionContext context) {
        Optional<MainCategory> originalMainCategory = mainCategoryDao.findById(mainCategory.getId(), context);
        if (originalMainCategory.isEmpty()) {
            throw new MainCategoryNotFoundException(categoryCannotBeFound, mainCategory);
        } else if (mainCategory.getTransactionType() != originalMainCategory.get().getTransactionType()) {
            throw new IllegalArgumentException(typeCannotBeChanged);
        } else if (!mainCategory.getSubCategorySet().containsAll(originalMainCategory.get().getSubCategorySet())) {
            throw new IllegalArgumentException(categoryNotContainsAllSubcategory);
        } else if (mainCategory.equals(originalMainCategory.get())) {
            throw new IllegalArgumentException(categoryNotChanged);
        }
    }

}
