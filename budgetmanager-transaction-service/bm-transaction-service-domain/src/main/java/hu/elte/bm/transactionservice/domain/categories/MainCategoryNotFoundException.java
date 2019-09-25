package hu.elte.bm.transactionservice.domain.categories;

public class MainCategoryNotFoundException extends RuntimeException {

    private final MainCategory mainCategory;
    private final Long mainCategoryId;
    private final String mainCategoryName;

    public MainCategoryNotFoundException(final String message, final MainCategory mainCategory) {
        super(message);
        this.mainCategory = mainCategory;
        this.mainCategoryId = null;
        this.mainCategoryName = null;
    }

    public MainCategoryNotFoundException(final String message, final Long subCategoryId) {
        super(message);
        this.mainCategory = null;
        this.mainCategoryId = subCategoryId;
        this.mainCategoryName = null;
    }

    public MainCategoryNotFoundException(final String message, final String mainCategoryName) {
        super(message);
        this.mainCategory = null;
        this.mainCategoryId = null;
        this.mainCategoryName = mainCategoryName;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public Long getMainCategoryId() {
        return mainCategoryId;
    }

    public String getMainCategoryName() {
        return mainCategoryName;
    }

}
