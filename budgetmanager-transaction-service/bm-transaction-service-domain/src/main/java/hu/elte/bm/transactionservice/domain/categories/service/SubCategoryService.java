package hu.elte.bm.transactionservice.domain.categories.service;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;

public interface SubCategoryService {

    SubCategory saveSubCategoryForIncome(SubCategory subCategory);

    SubCategory updateSubCategoryForIncome(SubCategory subCategory);

}
