package hu.elte.bm.transactionservice.domain.database;

import java.time.LocalDate;
import java.util.List;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.income.Income;

public interface DatabaseFacade {

    List<MainCategory> findAllMainCategory(CategoryType categoryType);

    MainCategory saveMainCategory(MainCategory mainCategory);

    MainCategory updateMainCategory(MainCategory target);

    List<SubCategory> findAllSubCategory(CategoryType categoryType);

    SubCategory saveSubCategory(SubCategory subCategory);

    SubCategory updateSubCategory(SubCategory subCategory);

    List<Income> getIncomeList(LocalDate start, LocalDate end);

    Income getIncomeById(Long id);

    Income saveIncome(Income income);

    Income updateIncome(Income income);

    Income deleteIncome(Income income);

}
