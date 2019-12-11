package hu.elte.bm.transactionservice.domain.exceptions.maincategory;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;

public class MainCategoryConflictException extends RuntimeException implements MainCategoryException  {

    private final MainCategory mainCategory;

    public MainCategoryConflictException(final MainCategory mainCategory, final String message) {
        super(message);
        this.mainCategory = mainCategory;
    }

    @Override
    public MainCategory getMainCategory() {
        return mainCategory;
    }
}
