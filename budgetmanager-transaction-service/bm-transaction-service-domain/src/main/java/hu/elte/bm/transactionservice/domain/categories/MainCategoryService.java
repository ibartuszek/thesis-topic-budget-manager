package hu.elte.bm.transactionservice.domain.categories;

import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public interface MainCategoryService {

    /**
     * Returns a list of MainCategory for the given types.
     * If there is no result it will returns with an empty list.
     *
     * @param transactionType the type of the transaction. It is needed to control the flow.
     * @return list of {@link MainCategory}.
     */
    List<MainCategory> getMainCategoryList(TransactionType transactionType);

    /**
     * It will save the main category into the repository.
     *
     * @param mainCategory the object that needs to be saved. Cannot be null.
     * @return it returns an {@link Optional<MainCategory>}, which contains a new object with an id
     * if the actions was successful or it is an empty optional if the category has been saved already.
     */
    Optional<MainCategory> save(MainCategory mainCategory);

    /**
     * It will update the main category. Only its name can be modified or add new {@link SubCategory}
     * to its subCategories.
     *
     * @param mainCategory the object that needs to be updated. Cannot be null.
     * @return it returns an {@link Optional<MainCategory>}, which contains the modifications if the
     * actions was successful or it is an empty optional if the category has been saved already.
     */
    Optional<MainCategory> update(MainCategory mainCategory);

}
