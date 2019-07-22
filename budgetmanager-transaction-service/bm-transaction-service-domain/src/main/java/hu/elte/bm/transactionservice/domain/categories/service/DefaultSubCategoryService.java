package hu.elte.bm.transactionservice.domain.categories.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;

@Service("subCategoryService")
public class DefaultSubCategoryService implements SubCategoryService {

    private final DatabaseProxy databaseProxy;

    DefaultSubCategoryService(final DatabaseProxy databaseProxy) {
        this.databaseProxy = databaseProxy;
    }

    private List<SubCategory> getSubCategoryList(final CategoryType categoryType) {
        return databaseProxy.findAllSubCategory(categoryType);
    }

    @Override
    public List<SubCategory> getSubCategoryListForIncomes() {
        return getSubCategoryList(CategoryType.INCOME);
    }

    @Override
    public Optional<SubCategory> saveSubCategory(final SubCategory subCategory, final CategoryType categoryType) throws IllegalArgumentException {
        CategoryServiceUtility.checkParameters(categoryType, subCategory);
        Optional<SubCategory> result = Optional.empty();
        List<SubCategory> subCategoryList = getSubCategoryList(categoryType);
        if (!hasFoundInRepository(subCategory, subCategoryList)) {
            SubCategory target = getTargetSubCategory(subCategory, subCategoryList);
            result = Optional.of(databaseProxy.saveSubCategory(target));
        }
        return result;
    }

    @Override
    public Optional<SubCategory> updateSubCategory(final SubCategory subCategory, final CategoryType categoryType)
        throws IllegalArgumentException {
        CategoryServiceUtility.checkParameters(categoryType, subCategory);
        Optional<SubCategory> result = Optional.empty();
        List<SubCategory> subCategoryList = getSubCategoryList(categoryType);
        if (canUpdated(subCategory, subCategoryList)) {
            result = Optional.of(databaseProxy.updateSubCategory(subCategory));
        }
        return result;
    }

    private boolean hasFoundInRepository(final SubCategory subCategory, final List<SubCategory> subCategoryList) {
        return subCategoryList.stream()
            .anyMatch(subCategory::equals);
    }

    private SubCategory getTargetSubCategory(final SubCategory subCategory, final List<SubCategory> mainCategoryList) {
        SubCategory target = subCategory;
        if (hasFoundInRepositoryWithDifferentType(subCategory, mainCategoryList)) {
            target = SubCategory.builder(subCategory)
                .withCategoryType(CategoryType.BOTH)
                .build();
        }
        return target;
    }

    private boolean hasFoundInRepositoryWithDifferentType(final SubCategory subCategory,
        final List<SubCategory> subCategoryList) {
        return subCategoryList.stream()
            .anyMatch(category -> subCategory.getName().equals(category.getName()));
    }

    private boolean canUpdated(final SubCategory subCategory, final List<SubCategory> subCategoryList) {
        Long id = subCategory.getId();
        Optional<SubCategory> subCategoryFromRepo = subCategoryList.stream()
            .filter(category -> id.equals(category.getId()))
            .findAny();
        return !subCategoryFromRepo.equals(Optional.empty());
    }

}
