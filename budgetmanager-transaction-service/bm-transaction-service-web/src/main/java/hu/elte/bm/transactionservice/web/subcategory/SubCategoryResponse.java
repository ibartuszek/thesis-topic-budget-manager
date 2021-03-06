package hu.elte.bm.transactionservice.web.subcategory;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.web.common.ResponseModel;

public final class SubCategoryResponse extends ResponseModel {

    private SubCategory subCategory;

    private SubCategoryResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    static SubCategoryResponse createSuccessfulSubCategoryResponse(final SubCategory subCategory, final String message) {
        SubCategoryResponse response = new SubCategoryResponse(message, true);
        response.setSubCategory(subCategory);
        return response;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(final SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return "SubCategoryResponse{"
            + "message='" + getMessage() + '\''
            + ", successful=" + isSuccessful()
            + ", subCategory=" + subCategory
            + '}';
    }
}
