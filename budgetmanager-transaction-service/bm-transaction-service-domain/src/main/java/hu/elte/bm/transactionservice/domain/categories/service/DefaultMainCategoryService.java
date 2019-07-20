package hu.elte.bm.transactionservice.domain.categories.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import hu.elte.bm.transactionservice.domain.DatabaseFacade;
import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryException;

@Service("mainCategoryService")
public class DefaultMainCategoryService implements MainCategoryService {

    private static final String EXCEPTION_MESSAGE = "Unexpected error happens during execution. Please try again later.";
    private final DatabaseFacade databaseFacade;

    DefaultMainCategoryService(final DatabaseFacade databaseFacade) {
        this.databaseFacade = databaseFacade;
    }

    private List<MainCategory> getMainCategoryList() {
        return this.databaseFacade.findAllMainCategory();
    }

    @Override
    public List<MainCategory> getMainCategoryListForIncomes() {
        return this.databaseFacade.findAllMainCategoryForIncomes();
    }

    @Override
    public MainCategory saveMainCategory(final MainCategory mainCategory) throws MainCategoryException {
        MainCategory result = null;
        List<MainCategory> mainCategoryList = getMainCategoryList();
        if (!hasFoundInRepository(mainCategory, mainCategoryList)) {
            MainCategory target = getTargetMainCategory(mainCategory, mainCategoryList);
            try {
                result = this.databaseFacade.saveMainCategory(target);
            } catch (DataAccessException exception) {
                throw new MainCategoryException(target, EXCEPTION_MESSAGE, exception);
            }
        }
        return result;
    }

    private boolean hasFoundInRepository(final MainCategory mainCategory,
        final List<MainCategory> mainCategoryList) {
        return mainCategoryList.stream()
            .anyMatch(mainCategory::equals);
    }

    private MainCategory getTargetMainCategory(final MainCategory mainCategory,
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
}
