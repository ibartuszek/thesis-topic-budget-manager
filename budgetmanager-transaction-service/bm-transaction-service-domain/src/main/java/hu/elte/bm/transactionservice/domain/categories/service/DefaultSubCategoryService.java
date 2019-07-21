package hu.elte.bm.transactionservice.domain.categories.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import hu.elte.bm.transactionservice.domain.DatabaseFacade;
import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryException;

@Service("subCategoryService")
public class DefaultSubCategoryService implements SubCategoryService {

    private static final String SUBCATEGORY_EXCEPTION_MESSAGE = "Unexpected error happens during execution. Please try again later.";
    private final DatabaseFacade databaseFacade;

    DefaultSubCategoryService(final DatabaseFacade databaseFacade) {
        this.databaseFacade = databaseFacade;
    }

    private List<SubCategory> getSubCategoryList(final CategoryType categoryType) {
        return databaseFacade.findAllSubCategory(categoryType);
    }

    @Override
    public List<SubCategory> getSubCategoryListForIncomes() {
        return getSubCategoryList(CategoryType.INCOME);
    }

    @Override
    public Optional<SubCategory> saveSubCategory(final SubCategory subCategory, final CategoryType categoryType)
        throws SubCategoryException, IllegalArgumentException {
        CategoryServiceUtility.checkParameters(categoryType, subCategory);
        Optional<SubCategory> result = Optional.empty();
        List<SubCategory> subCategoryList = getSubCategoryList(categoryType);
        if (!hasFoundInRepository(subCategory, subCategoryList)) {
            SubCategory target = getTargetSubCategory(subCategory, subCategoryList);
            try {
                result = Optional.of(databaseFacade.saveSubCategory(target));
            } catch (DataAccessException exception) {
                throw new SubCategoryException(target, SUBCATEGORY_EXCEPTION_MESSAGE, exception);
            }
        }
        return result;
    }

    @Override
    public Optional<SubCategory> updateSubCategory(final SubCategory subCategory, final CategoryType categoryType)
        throws SubCategoryException, IllegalArgumentException {
        CategoryServiceUtility.checkParameters(categoryType, subCategory);
        Optional<SubCategory> result = Optional.empty();
        List<SubCategory> subCategoryList = getSubCategoryList(categoryType);
        if (canUpdated(subCategory, subCategoryList)) {
            try {
                result = Optional.of(databaseFacade.updateSubCategory(subCategory));
            } catch (DataAccessException exception) {
                throw new SubCategoryException(subCategory, SUBCATEGORY_EXCEPTION_MESSAGE, exception);
            }
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
            target = SubCategory.SubCategoryBuilder.newInstance(subCategory)
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
