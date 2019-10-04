package hu.elte.bm.transactionservice.domain.exceptions.maincategory;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.exceptions.CustomException;

public interface MainCategoryException extends CustomException {

    MainCategory getMainCategory();
}
