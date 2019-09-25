package hu.elte.bm.transactionservice.service.category;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryConflictException;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryNotFoundException;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.database.SubCategoryDao;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

public class DefaultSubCategoryServiceTest {

    private static final long EXPECTED_CATEGORY_ID = 1L;
    private static final long NEW_ID = 3L;
    private static final long INVALID_ID = 4L;
    private static final String EXPECTED_CATEGORY_NAME = "category1";
    private static final String NEW_CATEGORY_NAME = "category2";
    private static final Long USER_ID = 1L;

    private SubCategoryDao subCategoryDao;
    private IMocksControl control;

    private SubCategoryService underTest;

    @BeforeClass
    public void setup() {
        control = EasyMock.createStrictControl();
        subCategoryDao = control.createMock(SubCategoryDao.class);
        underTest = new SubCategoryService(subCategoryDao);
    }

    @BeforeMethod
    public void beforeMethod() {
        control.reset();
    }

    @AfterMethod
    public void afterMethod() {
        control.verify();
    }

    @Test
    public void testGetSubCategoryListWhenThereIsNoSubCategory() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        EasyMock.expect(subCategoryDao.findAll(context)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.getSubCategoryList(context);
        // THEN
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void testGetSubCategoryListWhenThereAreMoreSubCategories() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        List<SubCategory> subCategoryList = createSubCategoryList();
        EasyMock.expect(subCategoryDao.findAll(context)).andReturn(subCategoryList);
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.getSubCategoryList(context);
        // THEN
        Assert.assertEquals(result, subCategoryList);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataForValidation")
    public void testSaveWhenValidationFails(final SubCategory subCategory, final TransactionType type, final Long userId) {
        // GIVEN
        control.replay();
        // WHEN
        underTest.save(subCategory, createTransactionContext(type, userId));
        // THEN
    }

    @DataProvider
    private Object[][] dataForValidation() {
        SubCategory subCategory = createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME);
        return new Object[][]{
                {null, INCOME, USER_ID},
                {subCategory, null, USER_ID},
                {subCategory, INCOME, null}
        };
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenSubCategoryHasId() {
        // GIVEN
        SubCategory subCategory = createExampleSubCategory(NEW_ID, NEW_CATEGORY_NAME, INCOME);
        control.replay();
        // WHEN
        underTest.save(subCategory, createTransactionContext(INCOME, USER_ID));
        // THEN
    }

    @Test(expectedExceptions = SubCategoryConflictException.class)
    public void testSaveWhenThisCategoryHasBeenFoundInRepository() {
        // GIVEN
        SubCategory subCategoryToSave = createExampleSubCategory(null, EXPECTED_CATEGORY_NAME, INCOME);
        Optional<SubCategory> subCategoryFromRepository = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        EasyMock.expect(subCategoryDao.findByName(EXPECTED_CATEGORY_NAME, createTransactionContext(INCOME, USER_ID)))
                .andReturn(subCategoryFromRepository);
        control.replay();
        // WHEN
        underTest.save(subCategoryToSave, createTransactionContext(INCOME, USER_ID));
        // THEN
    }

    @Test
    public void testSave() {
        // GIVEN
        SubCategory subCategoryToSave = createExampleSubCategory(null, EXPECTED_CATEGORY_NAME, OUTCOME);
        SubCategory expectedSubCategory = createExampleSubCategory(NEW_ID, EXPECTED_CATEGORY_NAME, OUTCOME);
        EasyMock.expect(subCategoryDao.findByName(EXPECTED_CATEGORY_NAME, createTransactionContext(OUTCOME, USER_ID))).andReturn(Optional.empty());
        EasyMock.expect(subCategoryDao.save(subCategoryToSave, createTransactionContext(OUTCOME, USER_ID))).andReturn(expectedSubCategory);
        control.replay();
        // WHEN
        SubCategory result = underTest.save(subCategoryToSave, createTransactionContext(OUTCOME, USER_ID));
        // THEN
        Assert.assertEquals(result, expectedSubCategory);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataForValidation")
    public void testUpdateWhenValidationFails(final SubCategory subCategory, final TransactionType type, final Long userId) {
        // GIVEN
        control.replay();
        // WHEN
        underTest.update(subCategory, createTransactionContext(type, userId));
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenSubCategoryDoesNotHaveId() {
        // GIVEN
        SubCategory subCategory = createExampleSubCategory(null, NEW_CATEGORY_NAME, INCOME);
        control.replay();
        // WHEN
        underTest.update(subCategory, createTransactionContext(INCOME, USER_ID));
        // THEN
    }

    @Test(expectedExceptions = SubCategoryNotFoundException.class)
    public void testUpdateWhenCategoryCannotBeFoundInRepository() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        SubCategory subCategoryToUpdate = createExampleSubCategory(INVALID_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(subCategoryDao.findById(INVALID_ID, context)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        underTest.update(subCategoryToUpdate, context);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenTransactionTypeHasChanged() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        SubCategory subCategoryToUpdate = createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME);
        Optional<SubCategory> subCategoryFromRepository = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, OUTCOME));
        EasyMock.expect(subCategoryDao.findById(EXPECTED_CATEGORY_ID, context)).andReturn(subCategoryFromRepository);
        control.replay();
        // WHEN
        underTest.update(subCategoryToUpdate, context);
        // THEN
    }

    @Test(expectedExceptions = SubCategoryConflictException.class)
    public void testUpdateWhenThereIsOtherCategoryWithSameName() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        SubCategory subCategoryToUpdate = createExampleSubCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<SubCategory> subCategoryFromRepository = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        Optional<SubCategory> subCategoryWithSameName = Optional.of(createExampleSubCategory(NEW_ID, NEW_CATEGORY_NAME, INCOME));
        EasyMock.expect(subCategoryDao.findById(EXPECTED_CATEGORY_ID, context)).andReturn(subCategoryFromRepository);
        EasyMock.expect(subCategoryDao.findByName(NEW_CATEGORY_NAME, context)).andReturn(subCategoryWithSameName);
        control.replay();
        // WHEN
        underTest.update(subCategoryToUpdate, context);
        // THEN
    }

    @Test
    public void testUpdate() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        SubCategory subCategoryToUpdate = createExampleSubCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<SubCategory> subCategoryFromRepository = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        SubCategory expectedSubCategory = createExampleSubCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(subCategoryDao.findById(EXPECTED_CATEGORY_ID, context)).andReturn(subCategoryFromRepository);
        EasyMock.expect(subCategoryDao.findByName(NEW_CATEGORY_NAME, context)).andReturn(Optional.empty());
        EasyMock.expect(subCategoryDao.update(subCategoryToUpdate, context)).andReturn(expectedSubCategory);
        control.replay();
        // WHEN
        SubCategory result = underTest.update(subCategoryToUpdate, context);
        // THEN
        Assert.assertEquals(result, expectedSubCategory);
    }

    private List<SubCategory> createSubCategoryList() {
        List<SubCategory> subCategoryList = new ArrayList<>();
        subCategoryList.add(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        return subCategoryList;
    }

    private SubCategory createExampleSubCategory(final Long id, final String categoryName, final TransactionType type) {
        return SubCategory.builder()
                .withId(id)
                .withName(categoryName)
                .withTransactionType(type)
                .build();
    }

    private TransactionContext createTransactionContext(final TransactionType transactionType, final Long userId) {
        return TransactionContext.builder()
                .withTransactionType(transactionType)
                .withUserId(userId)
                .build();
    }

}
