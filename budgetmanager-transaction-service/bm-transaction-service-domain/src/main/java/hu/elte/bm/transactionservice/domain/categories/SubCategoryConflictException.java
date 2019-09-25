package hu.elte.bm.transactionservice.domain.categories;

public class SubCategoryConflictException extends RuntimeException {

    private final SubCategory subCategory;

    public SubCategoryConflictException(final SubCategory subCategory, final String message) {
        super(message);
        this.subCategory = subCategory;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }
}
