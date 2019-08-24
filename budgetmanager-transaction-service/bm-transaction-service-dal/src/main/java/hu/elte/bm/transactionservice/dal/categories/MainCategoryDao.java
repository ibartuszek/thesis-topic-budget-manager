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
import hu.elte.bm.transactionservice.domain.transaction.TransactionContext;

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

    public List<MainCategory> findAll(final TransactionContext context) {
        Iterable<MainCategoryEntity> mainCategoryEntities = mainCategoryRepository.findAllMainCategory(context.getTransactionType(), context.getUserId());
        return transformToMainCategoryList(mainCategoryEntities, context);
    }

    public Optional<MainCategory> findById(final Long id, final TransactionContext context) {
        Optional<MainCategoryEntity> mainCategoryEntity = mainCategoryRepository.findByIdAndUserId(id, context.getUserId());
        return mainCategoryEntity.map(mainCategoryEntityTransformer::transformToMainCategory);
    }

    public Optional<MainCategory> findByName(final String name, final TransactionContext context) {
        Optional<MainCategoryEntity> mainCategoryEntity = mainCategoryRepository.findByName(name, context.getTransactionType(), context.getUserId());
        return mainCategoryEntity.map(mainCategoryEntityTransformer::transformToMainCategory);
    }

    @Transactional
    public Optional<MainCategory> save(final MainCategory mainCategory, final TransactionContext context) {
        Set<SubCategoryEntity> subCategoryEntitySet = getSubCategories(mainCategory.getSubCategorySet(), context.getUserId());
        MainCategoryEntity mainCategoryEntity = mainCategoryEntityTransformer
            .transformToMainCategoryEntity(mainCategory, subCategoryEntitySet, context.getUserId());
        MainCategoryEntity response = mainCategoryRepository.save(mainCategoryEntity);
        return Optional.ofNullable(mainCategoryEntityTransformer.transformToMainCategory(response));
    }

    public Optional<MainCategory> update(final MainCategory mainCategory, final TransactionContext context) {
        return save(mainCategory, context);
    }

    private List<MainCategory> transformToMainCategoryList(final Iterable<MainCategoryEntity> mainCategoryEntities, final TransactionContext context) {
        List<MainCategory> mainCategoryList = new ArrayList<>();
        mainCategoryEntities.iterator().forEachRemaining(mainCategoryEntity ->
            mainCategoryList.add(mainCategoryEntityTransformer.transformToMainCategory(mainCategoryEntity)));
        return mainCategoryList;
    }

    private Set<SubCategoryEntity> getSubCategories(final Set<SubCategory> subCategorySet, final Long userId) {
        Set<SubCategoryEntity> subCategoryEntitySet = new HashSet<>();
        subCategorySet.forEach(subCategory -> subCategoryRepository.findByIdAndUserId(subCategory.getId(), userId)
            .ifPresent(subCategoryEntitySet::add));
        return subCategoryEntitySet;
    }

}
