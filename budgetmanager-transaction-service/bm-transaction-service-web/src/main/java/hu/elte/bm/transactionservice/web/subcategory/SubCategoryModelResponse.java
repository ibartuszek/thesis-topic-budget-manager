package hu.elte.bm.transactionservice.web.subcategory;

import hu.elte.bm.transactionservice.web.common.ResponseModel;

public class SubCategoryModelResponse extends ResponseModel {

    private SubCategoryModel subCategoryModel;

    public SubCategoryModel getSubCategoryModel() {
        return subCategoryModel;
    }

    public void setSubCategoryModel(final SubCategoryModel subCategoryModel) {
        this.subCategoryModel = subCategoryModel;
    }
}
