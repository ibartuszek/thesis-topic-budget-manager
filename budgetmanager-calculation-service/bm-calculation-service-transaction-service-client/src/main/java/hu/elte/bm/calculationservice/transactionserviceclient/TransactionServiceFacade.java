package hu.elte.bm.calculationservice.transactionserviceclient;

import java.util.List;

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

    public List<SubCategory> getSubCategories(final TransactionType type, final Long userId) {
        return subCategoryProvider.provide(type, userId);
    }
}
