package hu.elte.bm.calculationservice.app.test.mocks;

import org.springframework.http.ResponseEntity;

import hu.elte.bm.calculationservice.transactionserviceclient.categories.MainCategoryListResponse;
import hu.elte.bm.calculationservice.transactionserviceclient.categories.MainCategoryProxy;

public class MainCategoryMockProxy extends MainCategoryProxy {

    private String response;

    public MainCategoryMockProxy() {
        super(null);
    }

    @Override
    protected ResponseEntity<MainCategoryListResponse> getResponseEntity(final String url) {
        return null;
    }

    public void setResponse(final String response) {
        this.response = response;
    }
}
