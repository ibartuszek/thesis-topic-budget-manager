package hu.elte.bm.transactionservice.dal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryDao;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryDao;
import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.database.DatabaseFacade;
import hu.elte.bm.transactionservice.domain.income.Income;

@Service("databaseFacade")
public class DefaultDatabaseFacade implements DatabaseFacade {

    @Autowired
    private MainCategoryDao mainCategoryDao;

    @Autowired
    private SubCategoryDao subCategoryDao;

    @Override
    public List<MainCategory> findAllMainCategory(final CategoryType categoryType) {
        return mainCategoryDao.findAll(categoryType);
    }

    @Override
    public Optional<MainCategory> findMainCategoryById(final Long id) {
        return mainCategoryDao.findById(id);
    }

    @Override
    public Optional<MainCategory> findMainCategoryByName(final String name, final CategoryType categoryType) {
        return mainCategoryDao.findByName(name, categoryType);
    }

    @Override
    public Optional<MainCategory> saveMainCategory(final MainCategory mainCategory) {
        return mainCategoryDao.save(mainCategory);
    }

    @Override
    public Optional<MainCategory> updateMainCategory(final MainCategory mainCategory) {
        return mainCategoryDao.update(mainCategory);
    }

    @Override
    public List<SubCategory> findAllSubCategory(final CategoryType categoryType) {
        return subCategoryDao.findAll(categoryType);
    }

    @Override
    public Optional<SubCategory> findSubCategoryById(final Long id) {
        return subCategoryDao.findById(id);
    }

    @Override
    public Optional<SubCategory> findSubCategoryByName(final String name, final CategoryType categoryType) {
        return subCategoryDao.findByName(name, categoryType);
    }

    @Override
    public Optional<SubCategory> saveSubCategory(final SubCategory subCategory) {
        return subCategoryDao.save(subCategory);
    }

    @Override
    public Optional<SubCategory> updateSubCategory(final SubCategory subCategory) {
        return subCategoryDao.update(subCategory);
    }

    @Override
    public List<Income> getIncomeList(final LocalDate start, final LocalDate end) {
        return null;
    }

    @Override
    public Income getIncomeById(final Long id) {
        return null;
    }

    @Override
    public Income saveIncome(final Income income) {
        return null;
    }

    @Override
    public Income updateIncome(final Income income) {
        return null;
    }

    @Override
    public Income deleteIncome(final Income income) {
        return null;
    }
}
