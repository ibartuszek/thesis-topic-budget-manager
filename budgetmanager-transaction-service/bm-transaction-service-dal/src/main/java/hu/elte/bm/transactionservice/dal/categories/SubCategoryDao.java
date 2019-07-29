package hu.elte.bm.transactionservice.dal.categories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Component
public class SubCategoryDao {

    private final SubCategoryRepository subCategoryRepository;

    private final SubCategoryEntityTransformer subCategoryEntityTransformer;

    SubCategoryDao(final SubCategoryRepository subCategoryRepository, final SubCategoryEntityTransformer subCategoryEntityTransformer) {
        this.subCategoryRepository = subCategoryRepository;
        this.subCategoryEntityTransformer = subCategoryEntityTransformer;
    }

    public List<SubCategory> findAll(final TransactionType transactionType) {
        Iterable<SubCategoryEntity> subCategoryEntities = subCategoryRepository.findAllSubcategory(transactionType);
        return transformToSubCategoryList(subCategoryEntities);
    }

    private List<SubCategory> transformToSubCategoryList(final Iterable<SubCategoryEntity> subCategoryEntities) {
        List<SubCategory> subCategoryList = new ArrayList<>();
        subCategoryEntities.iterator().forEachRemaining(subCategoryEntity ->
            subCategoryList.add(subCategoryEntityTransformer.transformToSubCategory(subCategoryEntity)));
        return subCategoryList;
    }

    public Optional<SubCategory> findById(final Long id) {
        Optional<SubCategoryEntity> subCategoryEntity = subCategoryRepository.findById(id);
        SubCategory result = subCategoryEntity.map(subCategoryEntityTransformer::transformToSubCategory).orElse(null);
        return Optional.ofNullable(result);
    }

    public Optional<SubCategory> findByName(final String name, final TransactionType transactionType) {
        Optional<SubCategoryEntity> subCategoryEntity = subCategoryRepository.findByName(name, transactionType);
        SubCategory result = subCategoryEntity.map(subCategoryEntityTransformer::transformToSubCategory).orElse(null);
        return Optional.ofNullable(result);
    }

    public Optional<SubCategory> save(final SubCategory subCategory) {
        SubCategoryEntity subCategoryEntity = subCategoryEntityTransformer.transformToSubCategoryEntity(subCategory);
        SubCategoryEntity response = subCategoryRepository.save(subCategoryEntity);
        SubCategory result = subCategoryEntityTransformer.transformToSubCategory(response);
        return Optional.ofNullable(result);
    }

    public Optional<SubCategory> update(final SubCategory subCategory) {
        return save(subCategory);
    }

}