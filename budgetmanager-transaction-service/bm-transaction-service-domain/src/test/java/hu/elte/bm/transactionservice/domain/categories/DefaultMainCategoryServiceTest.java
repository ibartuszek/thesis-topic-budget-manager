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
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class DefaultMainCategoryServiceTest {

    private static final long EXPECTED_CATEGORY_ID = 1L;
    private static final long NEW_ID = 3L;
    private static final long OTHER_ID = 2L;
    private static final long INVALID_ID = 4L;
    private static final String EXPECTED_CATEGORY_NAME = "category1";
    private static final String OTHER_CATEGORY_NAME = "category2";
    private static final String NEW_CATEGORY_NAME = "category3";

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
        EasyMock.expect(databaseProxy.findAllMainCategory(INCOME)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.getMainCategoryList(INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void testGetMainCategoryListWhenThereAreMoreCategories() {
        // GIVEN
        List<MainCategory> mainCategoryList = createMainCategoryList();
        EasyMock.expect(databaseProxy.findAllMainCategory(INCOME)).andReturn(mainCategoryList);
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.getMainCategoryList(INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(result, mainCategoryList);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveMainCategoryThrowIllegalArgumentExceptionWhenGotNullAsCategory() {
        // GIVEN
        // WHEN
        underTest.save(null);
        // THEN
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testSaveWhenThereIsOneSubCategoryWithoutId() {
        // GIVEN
        MainCategory mainCategoryToSave = createExampleMainCategory(null, EXPECTED_CATEGORY_NAME, INCOME);
        mainCategoryToSave.getSubCategorySet().add(createSubCategory(null, NEW_CATEGORY_NAME, INCOME));
        // WHEN
        underTest.save(mainCategoryToSave);
        // THEN
    }

    @Test
    public void testSaveWhenCategoryWithSameNameAndTypeFoundInRepository() {
        // GIVEN
        MainCategory mainCategoryToSave = createExampleMainCategory(null, EXPECTED_CATEGORY_NAME, INCOME);
        Optional<MainCategory> mainCategoryFromRepository = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findMainCategoryByName(EXPECTED_CATEGORY_NAME, INCOME)).andReturn(mainCategoryFromRepository);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.save(mainCategoryToSave);
        // THEN
        control.verify();
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testSave() {
        // GIVEN
        MainCategory mainCategoryToSave = createExampleMainCategory(null, EXPECTED_CATEGORY_NAME, INCOME);
        Optional<MainCategory> expectedMainCategory = Optional.of(createExampleMainCategory(NEW_ID, EXPECTED_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findMainCategoryByName(EXPECTED_CATEGORY_NAME, INCOME)).andReturn(Optional.empty());
        EasyMock.expect(databaseProxy.saveMainCategory(mainCategoryToSave)).andReturn(expectedMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.save(mainCategoryToSave);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedMainCategory);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateThrowIllegalArgumentExceptionWhenCategoryIsNull() {
        // GIVEN
        // WHEN
        underTest.update(null);
        // THEN
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testUpdateWhenThereIsOneSubCategoryWithoutId() {
        // GIVEN
        MainCategory mainCategoryToUpdate = createExampleMainCategory(null, EXPECTED_CATEGORY_NAME, INCOME);
        mainCategoryToUpdate.getSubCategorySet().add(createSubCategory(null, NEW_CATEGORY_NAME, INCOME));
        // WHEN
        underTest.update(mainCategoryToUpdate);
        // THEN
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testUpdateWhenCategoryCannotBeFoundById() {
        // GIVEN
        MainCategory mainCategoryToUpdate = createExampleMainCategory(INVALID_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseProxy.findMainCategoryById(INVALID_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        try {
            underTest.update(mainCategoryToUpdate);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testUpdateWhenCategoryTypeHasChanged() {
        // GIVEN
        MainCategory mainCategoryToUpdate = createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<MainCategory> mainCategoryFromRepository = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, OUTCOME));
        EasyMock.expect(databaseProxy.findMainCategoryById(EXPECTED_CATEGORY_ID)).andReturn(mainCategoryFromRepository);
        control.replay();
        // WHEN
        try {
            underTest.update(mainCategoryToUpdate);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test(expectedExceptions = MainCategoryException.class)
    public void testUpdateWhenCategoryContainsLessSubCategory() {
        // GIVEN
        MainCategory mainCategoryToUpdate = createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        mainCategoryToUpdate.getSubCategorySet().remove(createSubCategory(OTHER_ID, OTHER_CATEGORY_NAME, INCOME));
        Optional<MainCategory> mainCategoryFromRepository = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findMainCategoryById(EXPECTED_CATEGORY_ID)).andReturn(mainCategoryFromRepository);
        control.replay();
        // WHEN
        try {
            underTest.update(mainCategoryToUpdate);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testUpdateWhenCategoryWithSameNameAndTypeFoundInRepository() {
        // GIVEN
        MainCategory mainCategoryToUpdate = createExampleMainCategory(NEW_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<MainCategory> originalMainCategory = Optional.of(createExampleMainCategory(NEW_ID, EXPECTED_CATEGORY_NAME, INCOME));
        Optional<MainCategory> sameMainCategory = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findMainCategoryById(NEW_ID)).andReturn(originalMainCategory);
        EasyMock.expect(databaseProxy.findMainCategoryByName(NEW_CATEGORY_NAME, INCOME)).andReturn(sameMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.update(mainCategoryToUpdate);
        // THEN
        control.verify();
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testUpdate() {
        // GIVEN
        MainCategory mainCategoryToUpdate = createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<MainCategory> originalMainCategory = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        Optional<MainCategory> copyNewMainCategory = Optional.of(createExampleMainCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findMainCategoryById(EXPECTED_CATEGORY_ID)).andReturn(originalMainCategory);
        EasyMock.expect(databaseProxy.findMainCategoryByName(NEW_CATEGORY_NAME, INCOME)).andReturn(Optional.empty());
        EasyMock.expect(databaseProxy.updateMainCategory(mainCategoryToUpdate)).andReturn(copyNewMainCategory);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.update(mainCategoryToUpdate);
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

}
