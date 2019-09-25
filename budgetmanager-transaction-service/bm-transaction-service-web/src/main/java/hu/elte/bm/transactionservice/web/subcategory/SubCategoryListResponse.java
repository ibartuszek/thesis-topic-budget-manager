package hu.elte.bm.transactionservice.web.subcategory;

import java.util.List;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.web.common.ResponseModel;

public final class SubCategoryListResponse extends ResponseModel {

    private List<SubCategory> subCategoryList;

    private SubCategoryListResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    public List<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(final List<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    static SubCategoryListResponse createSuccessfulSubCategoryResponse(final List<SubCategory> subCategoryList, final String message) {
        SubCategoryListResponse response = new SubCategoryListResponse(message, true);
        response.setSubCategoryList(subCategoryList);
        return response;
    }

    static SubCategoryListResponse createSuccessfulSubCategoryResponse(final List<SubCategory> subCategoryList) {
        SubCategoryListResponse response = new SubCategoryListResponse(null, true);
        response.setSubCategoryList(subCategoryList);
        return response;
    }
}
