package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.util.List;

import hu.elte.bm.calculationservice.transactionserviceclient.BaseResponse;
import hu.elte.bm.transactionservice.MainCategory;

public final class MainCategoryListResponse extends BaseResponse {

    private List<MainCategory> mainCategoryList;

    public List<MainCategory> getMainCategoryList() {
        return mainCategoryList;
    }

    public void setMainCategoryList(final List<MainCategory> mainCategoryList) {
        this.mainCategoryList = mainCategoryList;
    }

    @Override
    public String toString() {
        return "MainCategoryListResponse{"
            + "message='" + getMessage() + '\''
            + ", successful=" + isSuccessful()
            + ", mainCategoryList=" + mainCategoryList
            + '}';
    }
}
