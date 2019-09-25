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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryException;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.database.DatabaseProxy;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

public class DefaultSubCategoryServiceTest {

    private static final long EXPECTED_CATEGORY_ID = 1L;
    private static final long NEW_ID = 3L;
    private static final long INVALID_ID = 4L;
    private static final String EXPECTED_CATEGORY_NAME = "category1";
    private static final String NEW_CATEGORY_NAME = "category2";
    private static final Long USER_ID = 1L;

    private DatabaseProxy databaseProxy;
    private IMocksControl control;

    private DefaultSubCategoryService underTest;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createStrictControl();
        databaseProxy = control.createMock(DatabaseProxy.class);
        underTest = new DefaultSubCategoryService(databaseProxy);
    }

    @Test
    public void testGetSubCategoryListWhenThereIsNoSubCategory() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        EasyMock.expect(databaseProxy.findAllSubCategory(context)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.getSubCategoryList(context);
        // THEN
        control.verify();
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void testGetSubCategoryListWhenThereAreMoreSubCategories() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        List<SubCategory> subCategoryList = createSubCategoryList();
        EasyMock.expect(databaseProxy.findAllSubCategory(context)).andReturn(subCategoryList);
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.getSubCategoryList(context);
        // THEN
        control.verify();
        Assert.assertEquals(result, subCategoryList);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataForValidation")
    public void testSaveWhenValidationFails(final SubCategory subCategory, final TransactionType type, final Long userId) {
        // GIVEN
        // WHEN
        underTest.save(subCategory, createTransactionContext(type, userId));
        // THEN
    }

    @DataProvider
    private Object[][] dataForValidation() {
        SubCategory subCategory = createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME);
        return new Object[][] {
            { null, INCOME, USER_ID },
            { subCategory, null, USER_ID },
            { subCategory, INCOME, null }
        };
    }

    @Test
    public void testSaveWhenThisCategoryHasBeenFoundInRepository() {
        // GIVEN
        SubCategory subCategoryToSave = createExampleSubCategory(null, EXPECTED_CATEGORY_NAME, INCOME);
        Optional<SubCategory> subCategoryFromRepository = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findSubCategoryByName(EXPECTED_CATEGORY_NAME, createTransactionContext(INCOME, USER_ID)))
            .andReturn(subCategoryFromRepository);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.save(subCategoryToSave, createTransactionContext(INCOME, USER_ID));
        // THEN
        control.verify();
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testSave() {
        // GIVEN
        SubCategory subCategoryToSave = createExampleSubCategory(null, EXPECTED_CATEGORY_NAME, OUTCOME);
        Optional<SubCategory> expectedSubCategory = Optional.of(createExampleSubCategory(NEW_ID, EXPECTED_CATEGORY_NAME, OUTCOME));
        EasyMock.expect(databaseProxy.findSubCategoryByName(EXPECTED_CATEGORY_NAME, createTransactionContext(OUTCOME, USER_ID))).andReturn(Optional.empty());
        EasyMock.expect(databaseProxy.saveSubCategory(subCategoryToSave, createTransactionContext(OUTCOME, USER_ID))).andReturn(expectedSubCategory);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.save(subCategoryToSave, createTransactionContext(OUTCOME, USER_ID));
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedSubCategory);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataForValidation")
    public void testUpdateWhenValidationFails(final SubCategory subCategory, final TransactionType type, final Long userId) {
        // GIVEN
        // WHEN
        underTest.update(subCategory, createTransactionContext(type, userId));
        // THEN
    }

    @Test(expectedExceptions = SubCategoryException.class)
    public void testUpdateWhenCategoryCannotBeFoundInRepository() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        SubCategory subCategoryToUpdate = createExampleSubCategory(INVALID_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseProxy.findSubCategoryById(INVALID_ID, context)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        try {
            underTest.update(subCategoryToUpdate, context);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test(expectedExceptions = SubCategoryException.class)
    public void testUpdateWhenTransactionTypeHasChanged() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        SubCategory subCategoryToUpdate = createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME);
        Optional<SubCategory> subCategoryFromRepository = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, OUTCOME));
        EasyMock.expect(databaseProxy.findSubCategoryById(EXPECTED_CATEGORY_ID, context)).andReturn(subCategoryFromRepository);
        control.replay();
        // WHEN
        try {
            underTest.update(subCategoryToUpdate, context);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testUpdateWhenThereIsOtherCategoryWithSameName() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        SubCategory subCategoryToUpdate = createExampleSubCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<SubCategory> subCategoryFromRepository = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        Optional<SubCategory> subCategoryWithSameName = Optional.of(createExampleSubCategory(NEW_ID, NEW_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findSubCategoryById(EXPECTED_CATEGORY_ID, context)).andReturn(subCategoryFromRepository);
        EasyMock.expect(databaseProxy.findSubCategoryByName(NEW_CATEGORY_NAME, context)).andReturn(subCategoryWithSameName);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.update(subCategoryToUpdate, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testUpdate() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        SubCategory subCategoryToUpdate = createExampleSubCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<SubCategory> subCategoryFromRepository = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        Optional<SubCategory> expectedSubCategory = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findSubCategoryById(EXPECTED_CATEGORY_ID, context)).andReturn(subCategoryFromRepository);
        EasyMock.expect(databaseProxy.findSubCategoryByName(NEW_CATEGORY_NAME, context)).andReturn(Optional.empty());
        EasyMock.expect(databaseProxy.updateSubCategory(subCategoryToUpdate, context)).andReturn(expectedSubCategory);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.update(subCategoryToUpdate, context);
        // THEN
        control.verify();
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
