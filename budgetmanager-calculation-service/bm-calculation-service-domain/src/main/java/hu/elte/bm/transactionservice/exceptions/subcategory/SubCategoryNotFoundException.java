package hu.elte.bm.transactionservice.exceptions.subcategory;

import hu.elte.bm.transactionservice.SubCategory;

public class SubCategoryNotFoundException extends RuntimeException implements SubCategoryException {

    private final SubCategory subCategory;

    public SubCategoryNotFoundException(final SubCategory subCategory, final String message) {
        super(message);
        this.subCategory = subCategory;
    }

    @Override
    public SubCategory getSubCategory() {
        return subCategory;
    }

}
