package hu.elte.bm.transactionservice.domain.categories;

public class MainCategoryException extends Exception {

    private final MainCategory mainCategory;

    public MainCategoryException(final MainCategory mainCategory, final String message) {
        super(message);
        this.mainCategory = mainCategory;
    }

    public MainCategoryException(final MainCategory mainCategory, final String message,
        final Throwable throwable) {
        super(message, throwable);
        this.mainCategory = mainCategory;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }
}
