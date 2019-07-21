package hu.elte.bm.transactionservice.domain.categories;

public class SubCategoryException extends Exception {

    private final SubCategory subCategory;

    public SubCategoryException(final SubCategory subCategory, final String message) {
        super(message);
        this.subCategory = subCategory;
    }

    public SubCategoryException(final SubCategory subCategory, final String message,
        final Throwable throwable) {
        super(message, throwable);
        this.subCategory = subCategory;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }
}
