package hu.elte.bm.transactionservice.domain.categories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

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

    DefaultMainCategoryService(final DatabaseProxy databaseProxy) {
        this.databaseProxy = databaseProxy;
    }

    @Override
    public List<MainCategory> getMainCategoryList(final TransactionType transactionType) {
        Assert.notNull(transactionType, typeCannotBeNullExceptionMessage);
        return databaseProxy.findAllMainCategory(transactionType);
    }

    @Override
    public Optional<MainCategory> save(final MainCategory mainCategory) {
        validate(mainCategory);
        return thereIsNoCategoryWithSameName(mainCategory) ? databaseProxy.saveMainCategory(mainCategory) : Optional.empty();
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
            throw new MainCategoryException(mainCategory, originalMainCategoryCannotBeFoundExceptionMessage);
        } else if (mainCategory.getTransactionType() != originalMainCategory.getTransactionType()) {
            throw new MainCategoryException(mainCategory, transactionTypeCannotBeChangedExceptionMessage);
        } else if (!mainCategory.getSubCategorySet().containsAll(originalMainCategory.getSubCategorySet())) {
            throw new MainCategoryException(mainCategory, mainCategoryNotContainsAllSubcategoryExceptionMessage);
        }

    }

}
