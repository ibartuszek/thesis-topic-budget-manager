package hu.elte.bm.transactionservice.domain.categories.service;

import static hu.elte.bm.transactionservice.domain.categories.CategoryType.BOTH;
import static hu.elte.bm.transactionservice.domain.categories.CategoryType.INCOME;
import static hu.elte.bm.transactionservice.domain.categories.CategoryType.OUTCOME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.dao.QueryTimeoutException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.DatabaseFacade;
import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryException;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;

public class DefaultMainCategoryServiceTest {

    private static final long EXPECTED_CATEGORY_ID = 2L;
    private static final long NEW_CATEGORY_ID = 4L;
    private static final long INVALID_ID = 5L;
    private static final long CATEGORY_ID_1 = 1L;
    private static final long CATEGORY_ID_2 = 3L;
    private static final String EXPECTED_CATEGORY_NAME = "category2";
    private static final String NEW_CATEGORY_NAME = "category4";
    private static final String CATEGORY_NAME_1 = "category1";
    private static final String CATEGORY_NAME_2 = "category3";
    private static final String REPOSITORY_EXCEPTION_MESSAGE = "message";
    private DatabaseFacade databaseFacade;
    private IMocksControl control;

    private DefaultMainCategoryService underTest;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createStrictControl();
        databaseFacade = control.createMock(DatabaseFacade.class);
        underTest = new DefaultMainCategoryService(databaseFacade);
    }

    @Test
    public void testGetMainCategoryListForIncomesWhenThereIsNoMainCategory() {
        // GIVEN
        EasyMock.expect(databaseFacade.findAllMainCategory(INCOME)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.getMainCategoryListForIncomes();
        // THEN
        control.verify();
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetMainCategoryListForIncomesWhenThereAreMoreMainCategories() {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        EasyMock.expect(databaseFacade.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.getMainCategoryListForIncomes();
        // THEN
        control.verify();
        Assert.assertEquals(mainCategoryList, result);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveMainCategoryThrowIllegalArgumentExceptionWhenGotWrongCategoryType() throws MainCategoryException {
        // GIVEN
        MainCategory mainCategory = createExampleMainCategory(null, NEW_CATEGORY_NAME, INCOME);
        // WHEN
        underTest.saveMainCategory(mainCategory, BOTH);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveMainCategoryThrowIllegalArgumentExceptionWhenGotNullCategory() throws MainCategoryException {
        // GIVEN
        // WHEN
        underTest.saveMainCategory(null, INCOME);
        // THEN
    }

    @Test
    public void testSaveMainCategoryWhenThisCategoryHasBeenFoundInRepository() throws MainCategoryException {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.saveMainCategory(mainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testSaveMainCategoryWhenRepositoryThrowsDataAccessException() throws MainCategoryException {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(NEW_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        EasyMock.expect(databaseFacade.saveMainCategory(mainCategory))
            .andThrow(new QueryTimeoutException(REPOSITORY_EXCEPTION_MESSAGE));
        control.replay();
        // WHEN
        try {
            underTest.saveMainCategory(mainCategory, INCOME);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testSaveMainCategoryWhenThisCategoryHasBeenFoundInRepositoryWithDifferentOtherType() throws MainCategoryException {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(null, EXPECTED_CATEGORY_NAME, OUTCOME);
        MainCategory expectedMainCategory = createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, BOTH);
        EasyMock.expect(databaseFacade.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        EasyMock.expect(databaseFacade.saveMainCategory(expectedMainCategory)).andReturn(expectedMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.saveMainCategory(mainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(expectedMainCategory, result.get());
    }

    @Test
    public void testSaveMainCategoryWhenNewCategorySavedIntoRepository() throws MainCategoryException {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(null, NEW_CATEGORY_NAME, INCOME);
        MainCategory expectedMainCategory = createExampleMainCategory(NEW_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        EasyMock.expect(databaseFacade.saveMainCategory(expectedMainCategory)).andReturn(expectedMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.saveMainCategory(mainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(expectedMainCategory, result.get());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateMainCategoryThrowIllegalArgumentExceptionWhenGotWrongCategoryType() throws MainCategoryException {
        // GIVEN
        MainCategory mainCategory = createExampleMainCategory(null, NEW_CATEGORY_NAME, INCOME);
        // WHEN
        underTest.updateMainCategory(mainCategory, BOTH);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateMainCategoryThrowIllegalArgumentExceptionWhenGotNullCategory() throws MainCategoryException {
        // GIVEN
        // WHEN
        underTest.updateMainCategory(null, INCOME);
        // THEN
    }

    @Test
    public void testUpdateMainCategoryWhenMainCategoryCannotBeFoundInRepository() throws MainCategoryException {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(INVALID_ID, CATEGORY_NAME_1, INCOME);
        EasyMock.expect(databaseFacade.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.updateMainCategory(mainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateMainCategoryWhenNewCategoryHasNotGotAllSubCategory() throws MainCategoryException {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory newMainCategory = createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.updateMainCategory(newMainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateMainCategoryWhenNewCategoryShouldBeUpdated() throws MainCategoryException {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        Set<SubCategory> subCategorySet = createSubcategorySet();
        subCategorySet.add(createExampleSubCategory(NEW_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME));
        MainCategory newMainCategory = createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME, subCategorySet);
        EasyMock.expect(databaseFacade.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        EasyMock.expect(databaseFacade.updateMainCategory(newMainCategory)).andReturn(newMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.updateMainCategory(newMainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(newMainCategory, result.get());
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testUpdateMainCategoryWhenRepositoryThrowsDataAccessException() throws MainCategoryException {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        Set<SubCategory> subCategorySet = createSubcategorySet();
        subCategorySet.add(createExampleSubCategory(NEW_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME));
        MainCategory newMainCategory = createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME, subCategorySet);
        EasyMock.expect(databaseFacade.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        EasyMock.expect(databaseFacade.updateMainCategory(newMainCategory)).andThrow(new QueryTimeoutException(REPOSITORY_EXCEPTION_MESSAGE));
        control.replay();
        // WHEN
        try {
            underTest.updateMainCategory(newMainCategory, INCOME);
        } finally {
            // THEN
            control.verify();
        }
    }

    private List<MainCategory> createMainCategoryList() {
        List<MainCategory> mainCategoryList = new ArrayList<>();
        mainCategoryList.add(createExampleMainCategory(CATEGORY_ID_1, CATEGORY_NAME_1, INCOME, createSubcategorySet()));
        mainCategoryList.add(createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME, createSubcategorySet()));
        mainCategoryList.add(createExampleMainCategory(CATEGORY_ID_2, CATEGORY_NAME_2, BOTH, createSubcategorySet()));
        return mainCategoryList;
    }

    private Set<SubCategory> createSubcategorySet() {
        Set<SubCategory> subCategorySet = new HashSet<>();
        subCategorySet.add(createExampleSubCategory(CATEGORY_ID_1, CATEGORY_NAME_1, INCOME));
        subCategorySet.add(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        subCategorySet.add(createExampleSubCategory(CATEGORY_ID_2, CATEGORY_NAME_2, BOTH));
        return subCategorySet;
    }

    private MainCategory createExampleMainCategory(final Long id, final String categoryName,
        final CategoryType type) {
        return createExampleMainCategory(id, categoryName, type, new HashSet<>());
    }

    private MainCategory createExampleMainCategory(final Long id, final String categoryName,
        final CategoryType type, final Set<SubCategory> subCategorySet) {
        return MainCategory.MainCategoryBuilder.newInstance()
            .withId(id)
            .withName(categoryName)
            .withCategoryType(type)
            .withSubCategorySet(subCategorySet)
            .build();
    }

    private SubCategory createExampleSubCategory(final Long id, final String categoryName, final CategoryType type) {
        return SubCategory.SubCategoryBuilder.newInstance()
            .withId(id)
            .withName(categoryName)
            .withCategoryType(type)
            .build();
    }

}
