package hu.elte.bm.transactionservice.domain.categories;

import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.transaction.TransactionContext;

public interface SubCategoryService {

    /**
     * Returns a list of SubCategory for the given types.
     * If there is no result it will returns with an empty list.
     *
     * @param transactionContext {@link TransactionContext} holder object of: transactionType and userId.
     * @return list of {@link SubCategory}.
     */
    List<SubCategory> getSubCategoryList(TransactionContext transactionContext);

    /**
     * It will save the subCategory into the repository.
     *
     * @param subCategory        the object that needs to be updated. Cannot be null.
     * @param transactionContext {@link TransactionContext} holder object of: transactionType and userId.
     * @return it returns an {@link Optional<SubCategory>}, which contains a new object with an id
     * if the actions was successful or it is an empty optional if the category has been saved already.
     */
    Optional<SubCategory> save(SubCategory subCategory, TransactionContext transactionContext);

    /**
     * It will update the main category. Only its name can be modified.
     *
     * @param subCategory        the object that needs to be updated. Cannot be null.
     * @param transactionContext {@link TransactionContext} holder object of: transactionType and userId.
     * @return it returns an {@link Optional<SubCategory>}, which contains the modifications if the
     * actions was successful or it is an empty optional if the category has been saved already.
     */
    Optional<SubCategory> update(SubCategory subCategory, TransactionContext transactionContext);

}
