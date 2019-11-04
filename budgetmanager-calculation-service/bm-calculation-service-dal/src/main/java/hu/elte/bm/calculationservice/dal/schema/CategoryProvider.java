package hu.elte.bm.calculationservice.dal.schema;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.transactionserviceclient.TransactionServiceFacade;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;
import hu.elte.bm.transactionservice.exceptions.maincategory.IllegalMainCategoryException;
import hu.elte.bm.transactionservice.exceptions.maincategory.MainCategoryNotFoundException;

@Component
public class CategoryProvider {

    private final TransactionServiceFacade transactionServiceFacade;

    @Value("${schema.main_category_not_found:Main category cannot be found!}")
    private String mainCategoryNotFound;

    @Value("${schema.sub_category_not_found:Sub category cannot be found!}")
    private String subCategoryNotFound;

    public CategoryProvider(final TransactionServiceFacade transactionServiceFacade) {
        this.transactionServiceFacade = transactionServiceFacade;
    }

    List<MainCategory> provideMainCategoryList(final Set<Long> mainCategoryIdSet, final Long userId, final TransactionType type) {
        List<MainCategory> result;
        if (mainCategoryIdSet.isEmpty()) {
            result = Collections.emptyList();
        } else {
            result = createMainCategoryList(mainCategoryIdSet, userId, type);
        }
        return result;
    }

    MainCategory provideMainCategory(final Long mainCategoryId, final Long userId, final TransactionType type) {
        return transactionServiceFacade.getMainCategories(type, userId).stream()
            .filter(mainCategory -> mainCategory.getId().equals(mainCategoryId))
            .findAny()
            .orElseThrow(() -> new MainCategoryNotFoundException(null, mainCategoryNotFound));
    }

    SubCategory provideSubCategory(final Long subCategoryId, final MainCategory mainCategory) {
        return mainCategory.getSubCategorySet().stream()
            .filter(subCategory -> subCategory.getId().equals(subCategoryId))
            .findAny()
            .orElseThrow(() -> new IllegalMainCategoryException(mainCategory, subCategoryNotFound));
    }

    private List<MainCategory> createMainCategoryList(final Set<Long> mainCategoryIdSet, final Long userId, final TransactionType type) {
        List<MainCategory> result;
        result = transactionServiceFacade.getMainCategories(type, userId).stream()
            .filter(mainCategory -> mainCategoryIdSet.contains(mainCategory.getId()))
            .collect(Collectors.toList());
        if (result.size() != mainCategoryIdSet.size()) {
            throw new MainCategoryNotFoundException(null, mainCategoryNotFound);
        }
        return result;
    }

}
