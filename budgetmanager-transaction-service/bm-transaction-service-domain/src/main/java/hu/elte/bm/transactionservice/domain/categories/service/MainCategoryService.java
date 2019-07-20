package hu.elte.bm.transactionservice.domain.categories.service;

import java.util.List;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryException;

public interface MainCategoryService {

    /**
     * Returns a list of MainCategory for incomes.
     * If there is no result it will returns with an empty list.
     *
     * @return list of {@link MainCategory}.
     */
    List<MainCategory> getMainCategoryListForIncomes();

    /**
     * It will save the main Category into the database.
     *
     * @param mainCategory the object that needs to be saved.
     * @return it returns a new {@link MainCategory} object with an id if the actions was successful
     * or null if the category has been saved already.
     * @throws {@link MainCategoryException} if the Maincategory cannot be saved. It can
     *                happen when exception was thrown by the repository.
     */
    MainCategory saveMainCategory(MainCategory mainCategory) throws MainCategoryException;

}
