package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.util.List;

import hu.elte.bm.transactionservice.SubCategory;

public final class SubCategoryListResponse {

    private String message;
    private boolean successful;
    private List<SubCategory> subCategoryList;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(final boolean successful) {
        this.successful = successful;
    }

    public List<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(final List<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    @Override
    public String toString() {
        return "SubCategoryListResponse{"
            + "message='" + message + '\''
            + ", successful=" + successful
            + ", subCategoryList=" + subCategoryList
            + '}';
    }
}
