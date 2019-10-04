package hu.elte.bm.transactionservice.web.maincategory;

import java.util.List;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.web.common.ResponseModel;

public final class MainCategoryListResponse extends ResponseModel {

    private List<MainCategory> mainCategoryList;

    private MainCategoryListResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    static MainCategoryListResponse createSuccessfulSubCategoryResponse(final List<MainCategory> mainCategoryList) {
        return createSuccessfulSubCategoryResponse(mainCategoryList, null);
    }

    static MainCategoryListResponse createSuccessfulSubCategoryResponse(final List<MainCategory> mainCategoryList, final String message) {
        MainCategoryListResponse response = new MainCategoryListResponse(message, true);
        response.setMainCategoryList(mainCategoryList);
        return response;
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
                + "message='" + getMessage() + '\''
                + ", successful=" + isSuccessful()
                + "mainCategoryList=" + mainCategoryList
                + '}';
    }
}
