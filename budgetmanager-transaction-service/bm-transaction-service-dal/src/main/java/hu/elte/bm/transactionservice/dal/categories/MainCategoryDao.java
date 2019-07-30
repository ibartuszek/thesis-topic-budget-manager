package hu.elte.bm.transactionservice.dal.categories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Component
public class MainCategoryDao {

    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final MainCategoryEntityTransformer mainCategoryEntityTransformer;

    MainCategoryDao(final MainCategoryRepository mainCategoryRepository, final SubCategoryRepository subCategoryRepository,
        final MainCategoryEntityTransformer mainCategoryEntityTransformer) {
        this.mainCategoryRepository = mainCategoryRepository;
        this.mainCategoryEntityTransformer = mainCategoryEntityTransformer;
        this.subCategoryRepository = subCategoryRepository;
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

    @Transactional
    public Optional<MainCategory> save(final MainCategory mainCategory) {
        Set<SubCategoryEntity> subCategoryEntitySet = getSubCategories(mainCategory.getSubCategorySet());
        MainCategoryEntity mainCategoryEntity = mainCategoryEntityTransformer.transformToMainCategoryEntity(mainCategory, subCategoryEntitySet);
        MainCategoryEntity response = mainCategoryRepository.save(mainCategoryEntity);
        MainCategory result = mainCategoryEntityTransformer.transformToMainCategory(response);
        return Optional.ofNullable(result);
    }

    private Set<SubCategoryEntity> getSubCategories(final Set<SubCategory> subCategorySet) {
        Set<SubCategoryEntity> subCategoryEntitySet = new HashSet<>();
        subCategorySet.forEach(subCategory -> subCategoryEntitySet.add(subCategoryRepository.findById(subCategory.getId()).get()));
        return subCategoryEntitySet;
    }

    public Optional<MainCategory> update(final MainCategory mainCategory) {
        return save(mainCategory);
    }

}
