package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.util.List;

import hu.elte.bm.transactionservice.MainCategory;

public final class MainCategoryListResponse {

    private String message;
    private boolean successful;
    private List<MainCategory> mainCategoryList;

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

    public List<MainCategory> getMainCategoryList() {
        return mainCategoryList;
    }

    public void setMainCategoryList(final List<MainCategory> mainCategoryList) {
        this.mainCategoryList = mainCategoryList;
    }

    @Override
    public String toString() {
        return "MainCategoryListResponse{"
            + "message='" + message + '\''
            + ", successful=" + successful
            + ", mainCategoryList=" + mainCategoryList
            + '}';
    }
}
