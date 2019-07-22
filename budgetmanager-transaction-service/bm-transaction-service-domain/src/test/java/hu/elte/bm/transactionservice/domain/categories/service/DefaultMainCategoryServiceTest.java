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
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;

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

    private DatabaseProxy databaseProxy;
    private IMocksControl control;

    private DefaultMainCategoryService underTest;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createStrictControl();
        databaseProxy = control.createMock(DatabaseProxy.class);
        underTest = new DefaultMainCategoryService(databaseProxy);
    }

    @Test
    public void testGetMainCategoryListForIncomesWhenThereIsNoMainCategory() {
        // GIVEN
        EasyMock.expect(databaseProxy.findAllMainCategory(INCOME)).andReturn(Collections.emptyList());
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
        EasyMock.expect(databaseProxy.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.getMainCategoryListForIncomes();
        // THEN
        control.verify();
        Assert.assertEquals(mainCategoryList, result);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveMainCategoryThrowIllegalArgumentExceptionWhenGotWrongCategoryType() {
        // GIVEN
        MainCategory mainCategory = createExampleMainCategory(null, NEW_CATEGORY_NAME, INCOME);
        // WHEN
        underTest.saveMainCategory(mainCategory, BOTH);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveMainCategoryThrowIllegalArgumentExceptionWhenGotNullCategory() {
        // GIVEN
        // WHEN
        underTest.saveMainCategory(null, INCOME);
        // THEN
    }

    @Test
    public void testSaveMainCategoryWhenThisCategoryHasBeenFoundInRepository() {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseProxy.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.saveMainCategory(mainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSaveMainCategoryWhenThisCategoryHasBeenFoundInRepositoryWithDifferentOtherType() {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(null, EXPECTED_CATEGORY_NAME, OUTCOME);
        MainCategory expectedMainCategory = createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, BOTH);
        EasyMock.expect(databaseProxy.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        EasyMock.expect(databaseProxy.saveMainCategory(expectedMainCategory)).andReturn(expectedMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.saveMainCategory(mainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(expectedMainCategory, result.get());
    }

    @Test
    public void testSaveMainCategoryWhenNewCategorySavedIntoRepository() {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(null, NEW_CATEGORY_NAME, INCOME);
        MainCategory expectedMainCategory = createExampleMainCategory(NEW_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseProxy.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        EasyMock.expect(databaseProxy.saveMainCategory(expectedMainCategory)).andReturn(expectedMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.saveMainCategory(mainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(expectedMainCategory, result.get());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateMainCategoryThrowIllegalArgumentExceptionWhenGotWrongCategoryType() {
        // GIVEN
        MainCategory mainCategory = createExampleMainCategory(null, NEW_CATEGORY_NAME, INCOME);
        // WHEN
        underTest.updateMainCategory(mainCategory, BOTH);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateMainCategoryThrowIllegalArgumentExceptionWhenGotNullCategory() {
        // GIVEN
        // WHEN
        underTest.updateMainCategory(null, INCOME);
        // THEN
    }

    @Test
    public void testUpdateMainCategoryWhenMainCategoryCannotBeFoundInRepository() {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(INVALID_ID, CATEGORY_NAME_1, INCOME);
        EasyMock.expect(databaseProxy.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.updateMainCategory(mainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateMainCategoryWhenNewCategoryHasNotGotAllSubCategory() {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory newMainCategory = createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseProxy.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.updateMainCategory(newMainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateMainCategoryWhenNewCategoryShouldBeUpdated() {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        Set<SubCategory> subCategorySet = createSubcategorySet();
        subCategorySet.add(createExampleSubCategory(NEW_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME));
        MainCategory newMainCategory = createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME, subCategorySet);
        EasyMock.expect(databaseProxy.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        EasyMock.expect(databaseProxy.updateMainCategory(newMainCategory)).andReturn(newMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.updateMainCategory(newMainCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(newMainCategory, result.get());
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
        return MainCategory.builder()
            .withId(id)
            .withName(categoryName)
            .withCategoryType(type)
            .withSubCategorySet(subCategorySet)
            .build();
    }

    private SubCategory createExampleSubCategory(final Long id, final String categoryName, final CategoryType type) {
        return SubCategory.builder()
            .withId(id)
            .withName(categoryName)
            .withCategoryType(type)
            .build();
    }

}
