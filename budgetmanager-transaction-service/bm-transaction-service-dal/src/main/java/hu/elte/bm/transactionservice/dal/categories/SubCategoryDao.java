package hu.elte.bm.transactionservice.dal.categories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@Component
public class SubCategoryDao {

    private final SubCategoryRepository subCategoryRepository;

    private final SubCategoryEntityTransformer subCategoryEntityTransformer;

    SubCategoryDao(final SubCategoryRepository subCategoryRepository, final SubCategoryEntityTransformer subCategoryEntityTransformer) {
        this.subCategoryRepository = subCategoryRepository;
        this.subCategoryEntityTransformer = subCategoryEntityTransformer;
    }

    public List<SubCategory> findAll(final TransactionContext context) {
        Iterable<SubCategoryEntity> subCategoryEntities = subCategoryRepository.findAllSubcategory(context.getTransactionType(), context.getUserId());
        return transformToSubCategoryList(subCategoryEntities);
    }

    public Optional<SubCategory> findById(final Long id, final TransactionContext context) {
        Optional<SubCategoryEntity> subCategoryEntity = subCategoryRepository.findByIdAndUserId(id, context.getUserId());
        SubCategory result = subCategoryEntity.map(subCategoryEntityTransformer::transformToSubCategory).orElse(null);
        return Optional.ofNullable(result);
    }

    public Optional<SubCategory> findByName(final String name, final TransactionContext context) {
        Optional<SubCategoryEntity> subCategoryEntity = subCategoryRepository.findByName(name, context.getTransactionType(), context.getUserId());
        SubCategory result = subCategoryEntity.map(subCategoryEntityTransformer::transformToSubCategory).orElse(null);
        return Optional.ofNullable(result);
    }

    public Optional<SubCategory> save(final SubCategory subCategory, final TransactionContext context) {
        SubCategoryEntity subCategoryEntity = subCategoryEntityTransformer.transformToSubCategoryEntity(subCategory, context.getUserId());
        SubCategoryEntity response = subCategoryRepository.save(subCategoryEntity);
        return Optional.ofNullable(subCategoryEntityTransformer.transformToSubCategory(response));
    }

    public Optional<SubCategory> update(final SubCategory subCategory, final TransactionContext context) {
        return save(subCategory, context);
    }

    private List<SubCategory> transformToSubCategoryList(final Iterable<SubCategoryEntity> subCategoryEntities) {
        List<SubCategory> subCategoryList = new ArrayList<>();
        subCategoryEntities.iterator().forEachRemaining(subCategoryEntity ->
            subCategoryList.add(subCategoryEntityTransformer.transformToSubCategory(subCategoryEntity)));
        return subCategoryList;
    }

}
