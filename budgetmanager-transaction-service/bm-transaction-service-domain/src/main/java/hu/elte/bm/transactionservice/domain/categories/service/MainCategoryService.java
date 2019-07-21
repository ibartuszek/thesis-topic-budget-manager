package hu.elte.bm.transactionservice.domain.categories.service;

import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryException;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;

public interface MainCategoryService {

    /**
     * Returns a list of MainCategory for incomes.
     * If there is no result it will returns with an empty list.
     *
     * @return list of {@link MainCategory}.
     */
    List<MainCategory> getMainCategoryListForIncomes();

    /**
     * It will save the main category into the repository.
     *
     * @param mainCategory the object that needs to be saved. Cannot be null.
     * @param categoryType the type of the transaction. (It can be only CategoryType.INCOME or
     *                     CategoryType.OUTCOME. It is needed to control the flow.
     * @return it returns an {@link Optional<MainCategory>}, which contains a new object with an id
     * if the actions was successful or it is an empty optional if the category has been saved already.
     * @throws MainCategoryException    if the mainCategory object cannot be saved. It can happen when
     *                                  exception was thrown by the repository.
     * @throws IllegalArgumentException if the CategoryType parameter has CategoryType.BOTH value
     *                                  or mainCategory is null.
     */
    Optional<MainCategory> saveMainCategory(MainCategory mainCategory, CategoryType categoryType) throws MainCategoryException, IllegalArgumentException;

    /**
     * It will update the main category. Only its name can be modified or add new {@link SubCategory}
     * to its subCategories.
     *
     * @param mainCategory the object that needs to be updated. Cannot be null.
     * @param categoryType the type of the transaction. (It can be only CategoryType.INCOME or
     *                     CategoryType.OUTCOME. It is needed to control the flow.
     * @return it returns an {@link Optional<MainCategory>}, which contains the modifications if the
     * actions was successful or it is an empty optional if the category has been saved already.
     * @throws MainCategoryException    if the mainCategory object cannot be saved. It can happen when
     *                                  exception was thrown by the repository.
     * @throws IllegalArgumentException if the CategoryType parameter has CategoryType.BOTH value
     *                                  or mainCategory is null.
     */
    Optional<MainCategory> updateMainCategory(MainCategory mainCategory, CategoryType categoryType) throws MainCategoryException, IllegalArgumentException;

}
