package hu.elte.bm.transactionservice.domain.categories;

public class MainCategoryConflictException extends RuntimeException {

    private final MainCategory mainCategory;

    public MainCategoryConflictException(final MainCategory mainCategory, final String message) {
        super(message);
        this.mainCategory = mainCategory;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }
}
