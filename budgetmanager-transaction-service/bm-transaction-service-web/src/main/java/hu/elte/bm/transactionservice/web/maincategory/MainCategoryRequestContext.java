package hu.elte.bm.transactionservice.web.maincategory;

import javax.validation.Valid;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.web.common.RequestContext;

public class MainCategoryRequestContext extends RequestContext {

    @Valid
    private MainCategory mainCategory;

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(final MainCategory mainCategory) {
        this.mainCategory = mainCategory;
    }
}
