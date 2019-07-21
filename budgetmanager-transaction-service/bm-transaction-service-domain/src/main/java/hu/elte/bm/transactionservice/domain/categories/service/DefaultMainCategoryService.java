package hu.elte.bm.transactionservice.domain.categories.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import hu.elte.bm.transactionservice.domain.DatabaseFacade;
import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryException;

@Service("mainCategoryService")
public class DefaultMainCategoryService implements MainCategoryService {

    private static final String MAIN_CATEGORY_EXCEPTION_MESSAGE = "Unexpected error happens during execution. Please try again later.";
    private final DatabaseFacade databaseFacade;

    DefaultMainCategoryService(final DatabaseFacade databaseFacade) {
        this.databaseFacade = databaseFacade;
    }

    private List<MainCategory> getMainCategoryList(final CategoryType categoryType) {
        return databaseFacade.findAllMainCategory(categoryType);
    }

    @Override
    public List<MainCategory> getMainCategoryListForIncomes() {
        return getMainCategoryList(CategoryType.INCOME);
    }

    @Override
    public Optional<MainCategory> saveMainCategory(final MainCategory mainCategory, final CategoryType categoryType)
        throws MainCategoryException, IllegalArgumentException {
        CategoryServiceUtility.checkParameters(categoryType, mainCategory);
        Optional<MainCategory> result = Optional.empty();
        List<MainCategory> mainCategoryList = getMainCategoryList(categoryType);
        if (!hasFoundInRepository(mainCategory, mainCategoryList)) {
            MainCategory target = getCategoryWithUpdatedCategoryType(mainCategory, mainCategoryList);
            try {
                result = Optional.of(databaseFacade.saveMainCategory(target));
            } catch (DataAccessException exception) {
                throw new MainCategoryException(target, MAIN_CATEGORY_EXCEPTION_MESSAGE, exception);
            }
        }
        return result;
    }

    @Override
    public Optional<MainCategory> updateMainCategory(final MainCategory mainCategory, final CategoryType categoryType)
        throws MainCategoryException, IllegalArgumentException {
        CategoryServiceUtility.checkParameters(categoryType, mainCategory);
        Optional<MainCategory> result = Optional.empty();
        List<MainCategory> mainCategoryList = getMainCategoryList(categoryType);
        if (canUpdated(mainCategory, mainCategoryList)) {
            try {
                result = Optional.of(databaseFacade.updateMainCategory(mainCategory));
            } catch (DataAccessException exception) {
                throw new MainCategoryException(mainCategory, MAIN_CATEGORY_EXCEPTION_MESSAGE, exception);
            }
        }
        return result;
    }

    private boolean hasFoundInRepository(final MainCategory mainCategory,
        final List<MainCategory> mainCategoryList) {
        return mainCategoryList.stream()
            .anyMatch(mainCategory::equals);
    }

    private MainCategory getCategoryWithUpdatedCategoryType(final MainCategory mainCategory,
        final List<MainCategory> mainCategoryList) {
        MainCategory target = mainCategory;
        if (hasFoundInRepositoryWithDifferentType(mainCategory, mainCategoryList)) {
            target = MainCategory.MainCategoryBuilder.newInstance(mainCategory)
                .withCategoryType(CategoryType.BOTH)
                .build();
        }
        return target;
    }

    private boolean hasFoundInRepositoryWithDifferentType(final MainCategory mainCategory,
        final List<MainCategory> mainCategoryList) {
        return mainCategoryList.stream()
            .anyMatch(category -> mainCategory.getName().equals(category.getName()));
    }

    private boolean canUpdated(final MainCategory mainCategory, final List<MainCategory> mainCategoryList) {
        boolean result = false;
        Long id = mainCategory.getId();
        MainCategory mainCategoryFromRepo = mainCategoryList.stream()
            .filter(category -> id.equals(category.getId()))
            .findAny().orElse(null);
        if (mainCategoryFromRepo != null) {
            result = checkSubcategories(mainCategory, mainCategoryFromRepo);
        }
        return result;
    }

    private boolean checkSubcategories(final MainCategory mainCategory, final MainCategory mainCategoryFromRepo) {
        return mainCategory.getSubCategorySet().containsAll(mainCategoryFromRepo.getSubCategorySet());
    }

}
