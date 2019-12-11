package hu.elte.bm.transactionservice.exceptions.maincategory;

import hu.elte.bm.calculationservice.exceptions.CustomException;
import hu.elte.bm.transactionservice.MainCategory;

public interface MainCategoryException extends CustomException {

    MainCategory getMainCategory();
}
