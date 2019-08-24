package hu.elte.bm.transactionservice.domain.categories;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;
import hu.elte.bm.transactionservice.domain.transaction.TransactionContext;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class DefaultMainCategoryServiceTest {

    private static final long EXPECTED_CATEGORY_ID = 1L;
    private static final long NEW_ID = 3L;
    private static final long OTHER_ID = 2L;
    private static final long INVALID_ID = 4L;
    private static final String EXPECTED_CATEGORY_NAME = "category1";
    private static final String OTHER_CATEGORY_NAME = "category2";
    private static final String NEW_CATEGORY_NAME = "category3";
    private static final Long USER_ID = 1L;

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
    public void testGetMainCategoryListWhenThereIsNoCategory() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        EasyMock.expect(databaseProxy.findAllMainCategory(context)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.getMainCategoryList(context);
        // THEN
        control.verify();
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void testGetMainCategoryListWhenThereAreMoreCategories() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        List<MainCategory> mainCategoryList = createMainCategoryList();
        EasyMock.expect(databaseProxy.findAllMainCategory(context)).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.getMainCategoryList(context);
        // THEN
        control.verify();
        Assert.assertEquals(result, mainCategoryList);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "testDataForValidation")
    public void testSaveWhenValidationFails(final MainCategory mainCategory, final TransactionType type, final Long userId) {
        // GIVEN
        // WHEN
        underTest.save(mainCategory, createTransactionContext(type, userId));
        // THEN
    }

    @DataProvider
    private Object[][] testDataForValidation() {
        MainCategory mainCategory = createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME);
        return new Object[][] {
            { null, INCOME, USER_ID },
            { mainCategory, null, USER_ID },
            { mainCategory, INCOME, null }
        };
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testSaveWhenThereIsOneSubCategoryWithoutId() {
        // GIVEN
        MainCategory mainCategoryToSave = createExampleMainCategory(null, EXPECTED_CATEGORY_NAME, INCOME);
        mainCategoryToSave.getSubCategorySet().add(createSubCategory(null, NEW_CATEGORY_NAME, INCOME));
        // WHEN
        underTest.save(mainCategoryToSave, createTransactionContext(INCOME, USER_ID));
        // THEN
    }

    @Test
    public void testSaveWhenCategoryWithSameNameAndTypeFoundInRepository() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        MainCategory mainCategoryToSave = createExampleMainCategory(null, EXPECTED_CATEGORY_NAME, INCOME);
        Optional<MainCategory> mainCategoryFromRepository = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findMainCategoryByName(EXPECTED_CATEGORY_NAME, context)).andReturn(mainCategoryFromRepository);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.save(mainCategoryToSave, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testSave() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        MainCategory mainCategoryToSave = createExampleMainCategory(null, EXPECTED_CATEGORY_NAME, INCOME);
        Optional<MainCategory> expectedMainCategory = Optional.of(createExampleMainCategory(NEW_ID, EXPECTED_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findMainCategoryByName(EXPECTED_CATEGORY_NAME, context)).andReturn(Optional.empty());
        EasyMock.expect(databaseProxy.saveMainCategory(mainCategoryToSave, context)).andReturn(expectedMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.save(mainCategoryToSave, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedMainCategory);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "testDataForValidation")
    public void testUpdateWhenValidationFails(final MainCategory mainCategory, final TransactionType type, final Long userId) {
        // GIVEN
        // WHEN
        underTest.update(mainCategory, createTransactionContext(type, userId));
        // THEN
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testUpdateWhenThereIsOneSubCategoryWithoutId() {
        // GIVEN
        MainCategory mainCategoryToUpdate = createExampleMainCategory(null, EXPECTED_CATEGORY_NAME, INCOME);
        mainCategoryToUpdate.getSubCategorySet().add(createSubCategory(null, NEW_CATEGORY_NAME, INCOME));
        // WHEN
        underTest.update(mainCategoryToUpdate, createTransactionContext(INCOME, USER_ID));
        // THEN
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testUpdateWhenCategoryCannotBeFoundById() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        MainCategory mainCategoryToUpdate = createExampleMainCategory(INVALID_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseProxy.findMainCategoryById(INVALID_ID, context)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        try {
            underTest.update(mainCategoryToUpdate, context);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testUpdateWhenCategoryTypeHasChanged() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        MainCategory mainCategoryToUpdate = createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<MainCategory> mainCategoryFromRepository = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, OUTCOME));
        EasyMock.expect(databaseProxy.findMainCategoryById(EXPECTED_CATEGORY_ID, context)).andReturn(mainCategoryFromRepository);
        control.replay();
        // WHEN
        try {
            underTest.update(mainCategoryToUpdate, context);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testUpdateWhenCategoryContainsLessSubCategory() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        MainCategory mainCategoryToUpdate = createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        mainCategoryToUpdate.getSubCategorySet().remove(createSubCategory(OTHER_ID, OTHER_CATEGORY_NAME, INCOME));
        Optional<MainCategory> mainCategoryFromRepository = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findMainCategoryById(EXPECTED_CATEGORY_ID, context)).andReturn(mainCategoryFromRepository);
        control.replay();
        // WHEN
        try {
            underTest.update(mainCategoryToUpdate, context);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testUpdateWhenCategoryWithSameNameAndTypeFoundInRepository() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        MainCategory mainCategoryToUpdate = createExampleMainCategory(NEW_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<MainCategory> originalMainCategory = Optional.of(createExampleMainCategory(NEW_ID, EXPECTED_CATEGORY_NAME, INCOME));
        Optional<MainCategory> sameMainCategory = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findMainCategoryById(NEW_ID, context)).andReturn(originalMainCategory);
        EasyMock.expect(databaseProxy.findMainCategoryByName(NEW_CATEGORY_NAME, context)).andReturn(sameMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.update(mainCategoryToUpdate, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testUpdate() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        MainCategory mainCategoryToUpdate = createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<MainCategory> originalMainCategory = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        Optional<MainCategory> copyNewMainCategory = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findMainCategoryById(EXPECTED_CATEGORY_ID, context)).andReturn(originalMainCategory);
        EasyMock.expect(databaseProxy.findMainCategoryByName(NEW_CATEGORY_NAME, context)).andReturn(Optional.empty());
        EasyMock.expect(databaseProxy.updateMainCategory(mainCategoryToUpdate, context)).andReturn(copyNewMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.update(mainCategoryToUpdate, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, copyNewMainCategory);
    }

    private List<MainCategory> createMainCategoryList() {
        List<MainCategory> mainCategoryList = new ArrayList<>();
        mainCategoryList.add(createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        return mainCategoryList;
    }

    private MainCategory createExampleMainCategory(final Long id, final String categoryName, final TransactionType type) {
        return MainCategory.builder()
            .withId(id)
            .withName(categoryName)
            .withTransactionType(type)
            .withSubCategorySet(createSubCategorySet(type))
            .build();
    }

    private Set<SubCategory> createSubCategorySet(final TransactionType type) {
        Set<SubCategory> subCategorySet = new HashSet<>();
        subCategorySet.add(createSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, type));
        subCategorySet.add(createSubCategory(OTHER_ID, OTHER_CATEGORY_NAME, type));
        return subCategorySet;
    }

    private SubCategory createSubCategory(final Long id, final String categoryName, final TransactionType type) {
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
