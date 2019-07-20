package hu.elte.bm.transactionservice.domain.categories.service;

import static hu.elte.bm.transactionservice.domain.categories.CategoryType.BOTH;
import static hu.elte.bm.transactionservice.domain.categories.CategoryType.INCOME;
import static hu.elte.bm.transactionservice.domain.categories.CategoryType.OUTCOME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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

public class DefaultMainCategoryServiceTest {

    private static final long EXPECTED_CATEGORY_ID = 2L;
    private static final long NEW_CATEGORY_ID = 4L;
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
        EasyMock.expect(databaseFacade.findAllMainCategoryForIncomes()).andReturn(Collections.emptyList());
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
        EasyMock.expect(databaseFacade.findAllMainCategoryForIncomes()).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.getMainCategoryListForIncomes();
        // THEN
        control.verify();
        Assert.assertEquals(mainCategoryList, result);
    }

    @Test
    public void testSaveMainCategoryWhenThisCategoryHasBeenFoundInRepository() throws MainCategoryException {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllMainCategory()).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        MainCategory result = underTest.saveMainCategory(mainCategory);
        // THEN
        control.verify();
        Assert.assertNull(result);
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testSaveMainCategoryWhenRepositoryThrowsDataAccessException() throws MainCategoryException {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(NEW_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllMainCategory()).andReturn(mainCategoryList);
        EasyMock.expect(databaseFacade.saveMainCategory(mainCategory))
            .andThrow(new QueryTimeoutException(REPOSITORY_EXCEPTION_MESSAGE));
        control.replay();
        // WHEN
        try {
            underTest.saveMainCategory(mainCategory);
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
        EasyMock.expect(databaseFacade.findAllMainCategory()).andReturn(mainCategoryList);
        EasyMock.expect(databaseFacade.saveMainCategory(expectedMainCategory)).andReturn(expectedMainCategory);
        control.replay();
        // WHEN
        MainCategory result = underTest.saveMainCategory(mainCategory);
        // THEN
        control.verify();
        Assert.assertEquals(expectedMainCategory, result);
    }

    @Test
    public void testSaveMainCategoryWhenNewCategorySavedIntoRepository() throws MainCategoryException {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        MainCategory mainCategory = createExampleMainCategory(null, NEW_CATEGORY_NAME, INCOME);
        MainCategory expectedMainCategory = createExampleMainCategory(NEW_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllMainCategory()).andReturn(mainCategoryList);
        EasyMock.expect(databaseFacade.saveMainCategory(expectedMainCategory)).andReturn(expectedMainCategory);
        control.replay();
        // WHEN
        MainCategory result = underTest.saveMainCategory(mainCategory);
        // THEN
        control.verify();
        Assert.assertEquals(expectedMainCategory, result);
    }

    private List<MainCategory> createMainCategoryList() {
        List<MainCategory> mainCategoryList = new ArrayList<>();
        mainCategoryList.add(createExampleMainCategory(CATEGORY_ID_1, CATEGORY_NAME_1, INCOME));
        mainCategoryList.add(createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        mainCategoryList.add(createExampleMainCategory(CATEGORY_ID_2, CATEGORY_NAME_2, BOTH));
        return mainCategoryList;
    }

    private MainCategory createExampleMainCategory(final Long id, final String categoryName,
        final CategoryType type) {
        return MainCategory.MainCategoryBuilder.newInstance()
            .withId(id)
            .withName(categoryName)
            .withCategoryType(type)
            .withSubCategorySet(new HashSet<>())
            .build();
    }
}
