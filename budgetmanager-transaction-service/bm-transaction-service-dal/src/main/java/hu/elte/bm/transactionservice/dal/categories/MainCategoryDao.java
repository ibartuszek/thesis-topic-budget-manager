package hu.elte.bm.transactionservice.dal.categories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Component
public class MainCategoryDao {

    private final MainCategoryRepository mainCategoryRepository;

    private final MainCategoryEntityTransformer mainCategoryEntityTransformer;

    MainCategoryDao(final MainCategoryRepository mainCategoryRepository, final MainCategoryEntityTransformer mainCategoryEntityTransformer) {
        this.mainCategoryRepository = mainCategoryRepository;
        this.mainCategoryEntityTransformer = mainCategoryEntityTransformer;
    }

    public List<MainCategory> findAll(final TransactionType transactionType) {
        Iterable<MainCategoryEntity> mainCategoryEntities = mainCategoryRepository.findAllMainCategory(transactionType);
        return transformToSubCategoryList(mainCategoryEntities);
    }

    private List<MainCategory> transformToSubCategoryList(final Iterable<MainCategoryEntity> mainCategoryEntities) {
        List<MainCategory> mainCategoryList = new ArrayList<>();
        mainCategoryEntities.iterator().forEachRemaining(subCategoryEntity ->
            mainCategoryList.add(mainCategoryEntityTransformer.transformToMainCategory(subCategoryEntity)));
        return mainCategoryList;
    }

    public Optional<MainCategory> findById(final Long id) {
        Optional<MainCategoryEntity> mainCategoryEntity = mainCategoryRepository.findById(id);
        MainCategory result = mainCategoryEntity.map(mainCategoryEntityTransformer::transformToMainCategory).orElse(null);
        return Optional.ofNullable(result);
    }

    public Optional<MainCategory> findByName(final String name, final TransactionType transactionType) {
        Optional<MainCategoryEntity> mainCategoryEntity = mainCategoryRepository.findByName(name, transactionType);
        MainCategory result = mainCategoryEntity.map(mainCategoryEntityTransformer::transformToMainCategory).orElse(null);
        return Optional.ofNullable(result);
    }

    public Optional<MainCategory> save(final MainCategory mainCategory) {
        MainCategoryEntity mainCategoryEntity = mainCategoryEntityTransformer.transformToMainCategoryEntity(mainCategory);
        MainCategoryEntity response = mainCategoryRepository.save(mainCategoryEntity);
        MainCategory result = mainCategoryEntityTransformer.transformToMainCategory(response);
        return Optional.ofNullable(result);
    }

    public Optional<MainCategory> update(final MainCategory mainCategory) {
        return save(mainCategory);
    }

}
