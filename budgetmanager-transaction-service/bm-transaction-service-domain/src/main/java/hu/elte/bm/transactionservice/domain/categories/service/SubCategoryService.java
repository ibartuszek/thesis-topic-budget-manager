package hu.elte.bm.transactionservice.domain.categories.service;

import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;

public interface SubCategoryService {

    /**
     * Returns a list of SubCategory for incomes.
     * If there is no result it will returns with an empty list.
     *
     * @return list of {@link SubCategory}.
     */
    List<SubCategory> getSubCategoryListForIncomes();

    /**
     * It will save the subCategory into the repository.
     *
     * @param subCategory  the object that needs to be saved. Cannot be null.
     * @param categoryType the type of the transaction. (It can be only CategoryType.INCOME or
     *                     CategoryType.OUTCOME. It is needed to control the flow.
     * @return it returns an {@link Optional<SubCategory>}, which contains a new object with an id
     * if the actions was successful or it is an empty optional if the category has been saved already.
     * @throws IllegalArgumentException if the CategoryType parameter has CategoryType.BOTH value
     *                                  or subCategory is null.
     */
    Optional<SubCategory> saveSubCategory(SubCategory subCategory, CategoryType categoryType) throws IllegalArgumentException;

    /**
     * It will update the main category. Only its name can be modified or add new {@link SubCategory}
     * to its subCategories.
     *
     * @param subCategory  the object that needs to be updated. Cannot be null.
     * @param categoryType the type of the transaction. (It can be only CategoryType.INCOME or
     *                     CategoryType.OUTCOME. It is needed to control the flow.
     * @return it returns an {@link Optional<SubCategory>}, which contains the modifications if the
     * actions was successful or it is an empty optional if the category has been saved already.
     * @throws IllegalArgumentException if the CategoryType parameter has CategoryType.BOTH value
     *                                  or subCategory is null.
     */
    Optional<SubCategory> updateSubCategory(SubCategory subCategory, CategoryType categoryType) throws IllegalArgumentException;

}
