package hu.elte.bm.transactionservice.web.subcategory;

import hu.elte.bm.transactionservice.web.common.RequestModelContext;

public class SubCategoryModelRequestContext extends RequestModelContext {

    private SubCategoryModel subCategoryModel;

    public SubCategoryModel getSubCategoryModel() {
        return subCategoryModel;
    }

    public void setSubCategoryModel(final SubCategoryModel subCategoryModel) {
        this.subCategoryModel = subCategoryModel;
    }
}
