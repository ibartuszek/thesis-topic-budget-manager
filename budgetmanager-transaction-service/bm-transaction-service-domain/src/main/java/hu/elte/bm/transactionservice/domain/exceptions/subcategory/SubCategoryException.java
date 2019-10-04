package hu.elte.bm.transactionservice.domain.exceptions.subcategory;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.exceptions.CustomException;

public interface SubCategoryException extends CustomException {

    SubCategory getSubCategory();
}
