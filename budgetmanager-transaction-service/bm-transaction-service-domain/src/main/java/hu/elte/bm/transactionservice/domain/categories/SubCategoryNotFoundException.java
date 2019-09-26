package hu.elte.bm.transactionservice.domain.categories;

public class SubCategoryNotFoundException extends RuntimeException {

    private final SubCategory subCategory;

    public SubCategoryNotFoundException(final SubCategory subCategory, final String message) {
        super(message);
        this.subCategory = subCategory;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

}
