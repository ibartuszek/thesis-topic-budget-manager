package hu.elte.bm.transactionservice.domain.categories;

public class SubCategoryNotFoundException extends RuntimeException {

    private final SubCategory subCategory;
    private final Long subCategoryId;
    private final String subCategoryName;

    public SubCategoryNotFoundException(final String message, final SubCategory subCategory) {
        super(message);
        this.subCategory = subCategory;
        this.subCategoryId = null;
        this.subCategoryName = null;
    }

    public SubCategoryNotFoundException(final String message, final Long subCategoryId) {
        super(message);
        this.subCategory = null;
        this.subCategoryId = subCategoryId;
        this.subCategoryName = null;
    }

    public SubCategoryNotFoundException(final String message, final String subCategoryName) {
        super(message);
        this.subCategory = null;
        this.subCategoryId = null;
        this.subCategoryName = subCategoryName;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }



}
