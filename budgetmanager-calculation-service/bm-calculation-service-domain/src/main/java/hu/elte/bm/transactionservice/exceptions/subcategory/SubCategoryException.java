package hu.elte.bm.transactionservice.exceptions.subcategory;

import hu.elte.bm.calculationservice.exceptions.CustomException;
import hu.elte.bm.transactionservice.SubCategory;

public interface SubCategoryException extends CustomException {

    SubCategory getSubCategory();
}
