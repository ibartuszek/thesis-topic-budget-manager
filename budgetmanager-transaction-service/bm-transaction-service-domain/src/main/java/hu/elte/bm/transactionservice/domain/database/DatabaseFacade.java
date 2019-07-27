package hu.elte.bm.transactionservice.domain.database;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.income.Income;

public interface DatabaseFacade {

    List<MainCategory> findAllMainCategory(CategoryType categoryType);

    Optional<MainCategory> findMainCategoryById(Long id);

    Optional<MainCategory> findMainCategoryByName(String name, CategoryType categoryType);

    Optional<MainCategory> saveMainCategory(MainCategory mainCategory);

    Optional<MainCategory> updateMainCategory(MainCategory target);

    List<SubCategory> findAllSubCategory(CategoryType categoryType);

    Optional<SubCategory> findSubCategoryById(Long id);

    Optional<SubCategory> findSubCategoryByName(String name, CategoryType categoryType);

    Optional<SubCategory> saveSubCategory(SubCategory subCategory);

    Optional<SubCategory> updateSubCategory(SubCategory subCategory);

    List<Income> getIncomeList(LocalDate start, LocalDate end);

    Income getIncomeById(Long id);

    Income saveIncome(Income income);

    Income updateIncome(Income income);

    Income deleteIncome(Income income);

}
