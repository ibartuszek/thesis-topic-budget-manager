package hu.elte.bm.transactionservice.domain.database;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.income.Income;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public interface DatabaseFacade {

    List<MainCategory> findAllMainCategory(TransactionType transactionType);

    Optional<MainCategory> findMainCategoryById(Long id);

    Optional<MainCategory> findMainCategoryByName(String name, TransactionType transactionType);

    Optional<MainCategory> saveMainCategory(MainCategory mainCategory);

    Optional<MainCategory> updateMainCategory(MainCategory target);

    List<SubCategory> findAllSubCategory(TransactionType transactionType);

    Optional<SubCategory> findSubCategoryById(Long id);

    Optional<SubCategory> findSubCategoryByName(String name, TransactionType transactionType);

    Optional<SubCategory> saveSubCategory(SubCategory subCategory);

    Optional<SubCategory> updateSubCategory(SubCategory subCategory);

    List<Income> getIncomeList(LocalDate start, LocalDate end);

    Income getIncomeById(Long id);

    Income saveIncome(Income income);

    Income updateIncome(Income income);

    Income deleteIncome(Income income);

}
