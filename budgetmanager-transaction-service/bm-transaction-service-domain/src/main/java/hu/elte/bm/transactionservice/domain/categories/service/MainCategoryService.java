package hu.elte.bm.transactionservice.domain.categories.service;

import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;

public interface MainCategoryService {

    /**
     * Returns a list of MainCategory for the given types.
     * If there is no result it will returns with an empty list.
     *
     * @param categoryType the type of the transaction. It is needed to control the flow.
     * @return list of {@link MainCategory}.
     */
    List<MainCategory> getMainCategoryList(CategoryType categoryType);

    /**
     * It will save the main category into the repository.
     *
     * @param mainCategory the object that needs to be saved. Cannot be null.
     * @return it returns an {@link Optional<MainCategory>}, which contains a new object with an id
     * if the actions was successful or it is an empty optional if the category has been saved already.
     */
    Optional<MainCategory> saveMainCategory(MainCategory mainCategory);

    /**
     * It will update the main category. Only its name can be modified or add new {@link SubCategory}
     * to its subCategories.
     *
     * @param mainCategory the object that needs to be updated. Cannot be null.
     * @return it returns an {@link Optional<MainCategory>}, which contains the modifications if the
     * actions was successful or it is an empty optional if the category has been saved already.
     */
    Optional<MainCategory> updateMainCategory(MainCategory mainCategory);

}
