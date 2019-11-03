package hu.elte.bm.calculationservice.transactionserviceclient;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.transactionserviceclient.categories.MainCategoryProvider;
import hu.elte.bm.calculationservice.transactionserviceclient.categories.SubCategoryProvider;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class TransactionServiceFacade {

    private final MainCategoryProvider mainCategoryProvider;
    private final SubCategoryProvider subCategoryProvider;

    public TransactionServiceFacade(final MainCategoryProvider mainCategoryProvider, final SubCategoryProvider subCategoryProvider) {
        this.mainCategoryProvider = mainCategoryProvider;
        this.subCategoryProvider = subCategoryProvider;
    }

    public List<MainCategory> getMainCategories(final TransactionType type, final Long userId) {
        return mainCategoryProvider.provide(type, userId);
    }

    public Optional<MainCategory> getMainCategoryById(final TransactionType type, final Long userId, final Long mainCategoryId) {
        List<MainCategory> mainCategoryList = getMainCategories(type, userId);
        return mainCategoryList.stream()
            .filter(mainCategory -> mainCategory.getId().equals(mainCategoryId))
            .findAny();
    }

    public List<SubCategory> getSubCategories(final TransactionType type, final Long userId) {
        return subCategoryProvider.provide(type, userId);
    }

    public Optional<SubCategory> getSubCategoryById(final TransactionType type, final Long userId, final Long subCategoryId) {
        List<SubCategory> subCategoryList = getSubCategories(type, userId);
        return subCategoryList.stream()
            .filter(subCategory -> subCategory.getId().equals(subCategoryId))
            .findAny();
    }
}
