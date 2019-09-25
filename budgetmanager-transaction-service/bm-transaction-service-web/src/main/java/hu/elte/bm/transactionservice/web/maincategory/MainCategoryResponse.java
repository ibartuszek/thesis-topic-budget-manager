package hu.elte.bm.transactionservice.web.maincategory;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.web.common.ResponseModel;

public final class MainCategoryResponse extends ResponseModel {

    private MainCategory mainCategory;

    private MainCategoryResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    static MainCategoryResponse createSuccessfulSubCategoryResponse(final MainCategory mainCategory, final String message) {
        MainCategoryResponse response = new MainCategoryResponse(message, true);
        response.setMainCategory(mainCategory);
        return response;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(final MainCategory mainCategory) {
        this.mainCategory = mainCategory;
    }
}
