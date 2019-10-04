package hu.elte.bm.transactionservice.domain.exceptions.subcategory;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;

public class SubCategoryConflictException extends RuntimeException implements SubCategoryException {

    private final SubCategory subCategory;

    public SubCategoryConflictException(final SubCategory subCategory, final String message) {
        super(message);
        this.subCategory = subCategory;
    }

    @Override
    public SubCategory getSubCategory() {
        return subCategory;
    }
}
