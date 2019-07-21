package hu.elte.bm.transactionservice.domain.categories.service;

import static hu.elte.bm.transactionservice.domain.categories.CategoryType.BOTH;
import static hu.elte.bm.transactionservice.domain.categories.CategoryType.INCOME;
import static hu.elte.bm.transactionservice.domain.categories.CategoryType.OUTCOME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.dao.QueryTimeoutException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.DatabaseFacade;
import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryException;

public class DefaultSubCategoryServiceTest {

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

    private DefaultSubCategoryService underTest;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createStrictControl();
        databaseFacade = control.createMock(DatabaseFacade.class);
        underTest = new DefaultSubCategoryService(databaseFacade);
    }

    @Test
    public void testGetSubCategoryListForIncomesWhenThereIsNoSubCategory() {
        // GIVEN
        EasyMock.expect(databaseFacade.findAllSubCategory(INCOME)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.getSubCategoryListForIncomes();
        // THEN
        control.verify();
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetSubCategoryListForIncomesWhenThereAreMoreMainCategories() {
        // GIVEN
        List<SubCategory> subCategoryList = createSubCategoryList();
        EasyMock.expect(databaseFacade.findAllSubCategory(INCOME)).andReturn(subCategoryList);
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.getSubCategoryListForIncomes();
        // THEN
        control.verify();
        Assert.assertEquals(subCategoryList, result);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveSubCategoryThrowIllegalArgumentExceptionWhenGotWrongCategoryType() throws SubCategoryException {
        // GIVEN
        SubCategory subCategory = createExampleSubCategory(null, NEW_CATEGORY_NAME, INCOME);
        // WHEN
        underTest.saveSubCategory(subCategory, BOTH);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveSubCategoryThrowIllegalArgumentExceptionWhenGotNullCategory() throws SubCategoryException {
        // GIVEN
        // WHEN
        underTest.saveSubCategory(null, INCOME);
        // THEN
    }

    @Test
    public void testSaveSubCategoryWhenThisCategoryHasBeenFoundInRepository() throws SubCategoryException {
        // GIVEN
        List<SubCategory> subCategoryList = createSubCategoryList();
        SubCategory subCategory = createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllSubCategory(INCOME)).andReturn(subCategoryList);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.saveSubCategory(subCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test(expectedExceptions = SubCategoryException.class)
    public void testSaveSubCategoryWhenRepositoryThrowsDataAccessException() throws SubCategoryException {
        // GIVEN
        List<SubCategory> subCategoryList = createSubCategoryList();
        SubCategory subCategory = createExampleSubCategory(NEW_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllSubCategory(INCOME)).andReturn(subCategoryList);
        EasyMock.expect(databaseFacade.saveSubCategory(subCategory)).andThrow(new QueryTimeoutException(REPOSITORY_EXCEPTION_MESSAGE));
        control.replay();
        // WHEN
        try {
            underTest.saveSubCategory(subCategory, INCOME);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testSaveSubCategoryWhenThisCategoryHasBeenFoundInRepositoryWithDifferentOtherType() throws SubCategoryException {
        // GIVEN
        List<SubCategory> subCategoryList = createSubCategoryList();
        SubCategory subCategory = createExampleSubCategory(null, EXPECTED_CATEGORY_NAME, OUTCOME);
        SubCategory expectedSubCategory = createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, BOTH);
        EasyMock.expect(databaseFacade.findAllSubCategory(INCOME)).andReturn(subCategoryList);
        EasyMock.expect(databaseFacade.saveSubCategory(expectedSubCategory)).andReturn(expectedSubCategory);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.saveSubCategory(subCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(expectedSubCategory, result.get());
    }

    @Test
    public void testSaveSubCategoryWhenNewCategorySavedIntoRepository() throws SubCategoryException {
        // GIVEN
        List<SubCategory> subCategoryList = createSubCategoryList();
        SubCategory subCategory = createExampleSubCategory(null, NEW_CATEGORY_NAME, INCOME);
        SubCategory expectedSubCategory = createExampleSubCategory(NEW_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllSubCategory(INCOME)).andReturn(subCategoryList);
        EasyMock.expect(databaseFacade.saveSubCategory(expectedSubCategory)).andReturn(expectedSubCategory);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.saveSubCategory(subCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(expectedSubCategory, result.get());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateSubCategoryThrowIllegalArgumentExceptionWhenGotWrongCategoryType() throws SubCategoryException {
        // GIVEN
        SubCategory subCategory = createExampleSubCategory(null, NEW_CATEGORY_NAME, INCOME);
        // WHEN
        underTest.updateSubCategory(subCategory, BOTH);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateSubCategoryThrowIllegalArgumentExceptionWhenGotNullCategory() throws SubCategoryException {
        // GIVEN
        // WHEN
        underTest.updateSubCategory(null, INCOME);
        // THEN
    }

    @Test
    public void testUpdateSubCategoryWhenCategoryCannotBeFoundInRepository() throws SubCategoryException {
        // GIVEN
        List<SubCategory> subCategoryList = createSubCategoryList();
        SubCategory subCategory = createExampleSubCategory(INVALID_ID, CATEGORY_NAME_1, INCOME);
        EasyMock.expect(databaseFacade.findAllSubCategory(INCOME)).andReturn(subCategoryList);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.saveSubCategory(subCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateSubCategoryWhenNewCategoryShouldBeUpdated() throws SubCategoryException {
        // GIVEN
        List<SubCategory> subCategoryList = createSubCategoryList();
        SubCategory newSubCategory = createExampleSubCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllSubCategory(INCOME)).andReturn(subCategoryList);
        EasyMock.expect(databaseFacade.updateSubCategory(newSubCategory)).andReturn(newSubCategory);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.updateSubCategory(newSubCategory, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(newSubCategory, result.get());
    }

    @Test(expectedExceptions = SubCategoryException.class)
    public void testUpdateSubCategoryWhenRepositoryThrowsDataAccessException() throws SubCategoryException {
        // GIVEN
        List<SubCategory> mainCategoryList = createSubCategoryList();
        SubCategory newMainCategory = createExampleSubCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseFacade.findAllSubCategory(INCOME)).andReturn(mainCategoryList);
        EasyMock.expect(databaseFacade.updateSubCategory(newMainCategory)).andThrow(new QueryTimeoutException(REPOSITORY_EXCEPTION_MESSAGE));
        control.replay();
        // WHEN
        try {
            Optional<SubCategory> result = underTest.updateSubCategory(newMainCategory, INCOME);
        } finally {
            // THEN
            control.verify();
        }
    }

    private List<SubCategory> createSubCategoryList() {
        List<SubCategory> subCategoryList = new ArrayList<>();
        subCategoryList.add(createExampleSubCategory(CATEGORY_ID_1, CATEGORY_NAME_1, INCOME));
        subCategoryList.add(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        subCategoryList.add(createExampleSubCategory(CATEGORY_ID_2, CATEGORY_NAME_2, BOTH));
        return subCategoryList;
    }

    private SubCategory createExampleSubCategory(final Long id, final String categoryName, final CategoryType type) {
        return SubCategory.SubCategoryBuilder.newInstance()
            .withId(id)
            .withName(categoryName)
            .withCategoryType(type)
            .build();
    }

}
