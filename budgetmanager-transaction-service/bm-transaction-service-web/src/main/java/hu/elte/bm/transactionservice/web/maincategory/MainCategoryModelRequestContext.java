package hu.elte.bm.transactionservice.web.maincategory;

import hu.elte.bm.transactionservice.web.common.RequestModelContext;

public class MainCategoryModelRequestContext extends RequestModelContext {

    private MainCategoryModel mainCategoryModel;

    public MainCategoryModel getMainCategoryModel() {
        return mainCategoryModel;
    }

    public void setMainCategoryModel(final MainCategoryModel mainCategoryModel) {
        this.mainCategoryModel = mainCategoryModel;
    }
}
