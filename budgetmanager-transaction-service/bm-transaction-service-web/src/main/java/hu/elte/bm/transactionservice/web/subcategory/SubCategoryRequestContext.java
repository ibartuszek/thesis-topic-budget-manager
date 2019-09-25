package hu.elte.bm.transactionservice.web.subcategory;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.web.common.RequestModelContext;

public class SubCategoryRequestContext extends RequestModelContext {

    private SubCategory subCategory;

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(final SubCategory subCategory) {
        this.subCategory = subCategory;
    }
}
