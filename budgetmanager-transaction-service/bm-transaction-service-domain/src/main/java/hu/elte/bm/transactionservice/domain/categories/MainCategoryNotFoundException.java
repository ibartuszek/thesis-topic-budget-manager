package hu.elte.bm.transactionservice.domain.categories;

public class MainCategoryNotFoundException extends RuntimeException {

    private final MainCategory mainCategory;

    public MainCategoryNotFoundException(final MainCategory mainCategory, final String message) {
        super(message);
        this.mainCategory = mainCategory;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }

}
