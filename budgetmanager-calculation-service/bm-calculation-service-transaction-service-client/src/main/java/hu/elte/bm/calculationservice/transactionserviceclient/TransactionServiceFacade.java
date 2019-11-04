package hu.elte.bm.calculationservice.transactionserviceclient;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.transactionserviceclient.categories.MainCategoryProxy;
import hu.elte.bm.calculationservice.transactionserviceclient.categories.SubCategoryProxy;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class TransactionServiceFacade {

    private final MainCategoryProxy mainCategoryProxy;
    private final SubCategoryProxy subCategoryProxy;

    public TransactionServiceFacade(final MainCategoryProxy mainCategoryProxy, final SubCategoryProxy subCategoryProxy) {
        this.mainCategoryProxy = mainCategoryProxy;
        this.subCategoryProxy = subCategoryProxy;
    }

    public List<MainCategory> getMainCategories(final TransactionType type, final Long userId) {
        return mainCategoryProxy.getCategories(type, userId);
    }

    public Optional<MainCategory> getMainCategoryById(final TransactionType type, final Long userId, final Long mainCategoryId) {
        List<MainCategory> mainCategoryList = getMainCategories(type, userId);
        return mainCategoryList.stream()
            .filter(mainCategory -> mainCategory.getId().equals(mainCategoryId))
            .findAny();
    }

    public List<SubCategory> getSubCategories(final TransactionType type, final Long userId) {
        return subCategoryProxy.getCategories(type, userId);
    }

    public Optional<SubCategory> getSubCategoryById(final TransactionType type, final Long userId, final Long subCategoryId) {
        List<SubCategory> subCategoryList = getSubCategories(type, userId);
        return subCategoryList.stream()
            .filter(subCategory -> subCategory.getId().equals(subCategoryId))
            .findAny();
    }
}
