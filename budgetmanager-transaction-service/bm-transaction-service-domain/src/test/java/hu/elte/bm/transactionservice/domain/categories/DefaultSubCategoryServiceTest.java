package hu.elte.bm.transactionservice.domain.categories;

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
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class DefaultSubCategoryServiceTest {

    private static final long EXPECTED_CATEGORY_ID = 1L;
    private static final long OTHER_ID = 2L;
    private static final long NEW_ID = 3L;
    private static final long INVALID_ID = 4L;
    private static final String EXPECTED_CATEGORY_NAME = "category1";
    private static final String NEW_CATEGORY_NAME = "category2";

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
        EasyMock.expect(databaseProxy.findAllSubCategory(INCOME)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.getSubCategoryList(INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetSubCategoryListForIncomesWhenThereAreMoreSubCategories() {
        // GIVEN
        List<SubCategory> subCategoryList = createSubCategoryList();
        EasyMock.expect(databaseProxy.findAllSubCategory(INCOME)).andReturn(subCategoryList);
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.getSubCategoryList(INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(subCategoryList, result);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveSubCategoryThrowIllegalArgumentExceptionWhenGotNullCategory() {
        // GIVEN
        // WHEN
        underTest.save(null);
        // THEN
    }

    @Test
    public void testSaveSubCategoryWhenThisCategoryHasBeenFoundInRepository() {
        // GIVEN
        SubCategory subCategoryToSave = createExampleSubCategory(null, EXPECTED_CATEGORY_NAME, INCOME);
        Optional<SubCategory> subCategoryFromRepository = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findSubCategoryByName(EXPECTED_CATEGORY_NAME, INCOME)).andReturn(subCategoryFromRepository);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.save(subCategoryToSave);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSaveSubCategoryWhenNewCategorySavedIntoRepository() {
        // GIVEN
        SubCategory subCategoryToSave = createExampleSubCategory(null, EXPECTED_CATEGORY_NAME, OUTCOME);
        Optional<SubCategory> expectedSubCategory = Optional.of(createExampleSubCategory(NEW_ID, EXPECTED_CATEGORY_NAME, OUTCOME));
        EasyMock.expect(databaseProxy.findSubCategoryByName(EXPECTED_CATEGORY_NAME, OUTCOME)).andReturn(Optional.empty());
        EasyMock.expect(databaseProxy.saveSubCategory(subCategoryToSave)).andReturn(expectedSubCategory);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.save(subCategoryToSave);
        // THEN
        control.verify();
        Assert.assertEquals(expectedSubCategory, result);
    }

    @Test
    public void testSaveSubCategoryWhenThisCategoryHasBeenFoundInRepositoryWithDifferentType() {
        // GIVEN
        SubCategory subCategoryToSave = createExampleSubCategory(null, EXPECTED_CATEGORY_NAME, OUTCOME);
        Optional<SubCategory> subCategoryFromRepository = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        Optional<SubCategory> expectedSubCategory = Optional.of(createExampleSubCategory(NEW_ID, EXPECTED_CATEGORY_NAME, OUTCOME));
        EasyMock.expect(databaseProxy.findSubCategoryByName(EXPECTED_CATEGORY_NAME, OUTCOME)).andReturn(subCategoryFromRepository);
        EasyMock.expect(databaseProxy.saveSubCategory(subCategoryToSave)).andReturn(expectedSubCategory);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.save(subCategoryToSave);
        // THEN
        control.verify();
        Assert.assertEquals(expectedSubCategory, result);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateSubCategoryThrowIllegalArgumentExceptionWhenGotNullCategory() {
        // GIVEN
        // WHEN
        underTest.update(null);
        // THEN
    }

    @Test
    public void testUpdateSubCategoryWhenCategoryCannotBeFoundInRepository() {
        // GIVEN
        SubCategory subCategoryToUpdate = createExampleSubCategory(INVALID_ID, NEW_CATEGORY_NAME, INCOME);
        EasyMock.expect(databaseProxy.findSubCategoryById(INVALID_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.update(subCategoryToUpdate);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateSubCategoryWhenFoundByIdButButNotByName() {
        // GIVEN
        SubCategory subCategoryToUpdate = createExampleSubCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<SubCategory> originalSubCategory = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        Optional<SubCategory> copyNewSubCategory = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findSubCategoryById(EXPECTED_CATEGORY_ID)).andReturn(originalSubCategory);
        EasyMock.expect(databaseProxy.findSubCategoryByName(NEW_CATEGORY_NAME, INCOME)).andReturn(Optional.empty());
        EasyMock.expect(databaseProxy.updateSubCategory(subCategoryToUpdate)).andReturn(copyNewSubCategory);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.update(subCategoryToUpdate);
        // THEN
        control.verify();
        Assert.assertEquals(copyNewSubCategory, result);
    }

    @Test
    public void testUpdateSubCategoryWhenFoundByIdAndByName() {
        // GIVEN
        SubCategory subCategoryToUpdate = createExampleSubCategory(EXPECTED_CATEGORY_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<SubCategory> originalSubCategory = Optional.of(createExampleSubCategory(EXPECTED_CATEGORY_ID, EXPECTED_CATEGORY_NAME, INCOME));
        Optional<SubCategory> subCategoryWithSameName = Optional.of(createExampleSubCategory(NEW_ID, NEW_CATEGORY_NAME, INCOME));
        EasyMock.expect(databaseProxy.findSubCategoryById(EXPECTED_CATEGORY_ID)).andReturn(originalSubCategory);
        EasyMock.expect(databaseProxy.findSubCategoryByName(NEW_CATEGORY_NAME, INCOME)).andReturn(subCategoryWithSameName);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.update(subCategoryToUpdate);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
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

}
