package hu.elte.bm.transactionservice.domain.categories.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.elte.bm.transactionservice.domain.DatabaseFacade;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;

@Service("subCategoryService")
public class DefaultSubCategoryService implements SubCategoryService {

    @Autowired
    private DatabaseFacade databaseFacade;

    @Override
    public SubCategory saveSubCategoryForIncome(final SubCategory subCategory) {
        return this.databaseFacade.saveSubCategoryForIncome(subCategory);
    }

    @Override
    public SubCategory updateSubCategoryForIncome(final SubCategory subCategory) {
        return this.databaseFacade.updateSubCategoryForIncome(subCategory);
    }

}
