package hu.elte.bm.transactionservice.web.maincategory;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.web.common.RequestContext;

public class MainCategoryRequestContext extends RequestContext {

    private MainCategory mainCategory;

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(final MainCategory mainCategory) {
        this.mainCategory = mainCategory;
    }
}
