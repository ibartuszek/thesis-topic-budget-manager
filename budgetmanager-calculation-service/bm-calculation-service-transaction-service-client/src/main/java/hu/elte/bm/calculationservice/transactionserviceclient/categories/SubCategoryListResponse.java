package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.util.List;

import hu.elte.bm.calculationservice.transactionserviceclient.BaseResponse;
import hu.elte.bm.transactionservice.SubCategory;

public final class SubCategoryListResponse extends BaseResponse {

    private List<SubCategory> subCategoryList;

    public List<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(final List<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    @Override
    public String toString() {
        return "SubCategoryListResponse{"
            + "message='" + getMessage() + '\''
            + ", successful=" + isSuccessful()
            + ", subCategoryList=" + subCategoryList
            + '}';
    }
}
