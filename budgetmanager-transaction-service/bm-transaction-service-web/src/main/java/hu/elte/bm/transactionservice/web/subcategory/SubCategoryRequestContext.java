package hu.elte.bm.transactionservice.web.subcategory;

import javax.validation.Valid;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.web.common.RequestContext;

public class SubCategoryRequestContext extends RequestContext {

    @Valid
    private SubCategory subCategory;

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(final SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return "SubCategoryRequestContext{"
                + "transactionType=" + getTransactionType()
                + ", userId=" + getUserId()
                + "subCategory=" + subCategory
                + '}';
    }
}
