package hu.elte.bm.transactionservice.domain.categories.service;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;

final class CategoryServiceUtility {

    private static final String CATEGORY_TYPE_EXCEPTION_MESSAGE = "categoryType cannot be CategoryType.BOTH!";
    private static final String CATEGORY_CANNOT_BE_NULL_EXCEPTION_MESSAGE = "category cannot be null!";

    private CategoryServiceUtility() {
    }

    static void checkParameters(final CategoryType categoryType, final Object category) {
        if (categoryType.equals(CategoryType.BOTH)) {
            throw new IllegalArgumentException(CATEGORY_TYPE_EXCEPTION_MESSAGE);
        } else if (category == null) {
            throw new IllegalArgumentException(CATEGORY_CANNOT_BE_NULL_EXCEPTION_MESSAGE);
        }
    }

}
