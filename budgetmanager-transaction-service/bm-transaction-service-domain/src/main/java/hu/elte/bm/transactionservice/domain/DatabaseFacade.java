package hu.elte.bm.transactionservice.domain;

import java.util.List;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.income.Income;

public interface DatabaseFacade {

    List<MainCategory> findAllMainCategory();

    List<MainCategory> findAllMainCategoryForIncomes();

    MainCategory saveMainCategory(MainCategory mainCategory);

    MainCategory updateMainCategoryForIncome(MainCategory mainCategory);

    SubCategory saveSubCategoryForIncome(SubCategory subCategory);

    SubCategory updateSubCategoryForIncome(SubCategory subCategory);

    List<Income> findAllIncome();

    Income saveIncome(Income income);

    Income updateIncome(Income income);

    Income deleteIncome(Income income);
}
