package hu.elte.bm.transactionservice.web.subcategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.DefaultSubCategoryService;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryService;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.common.ModelValidator;

public class SubCategoryModelServiceTest {

    private static final long RESERVED_ID = 1L;
    private static final String RESERVED_NAME = "category 1";
    private static final String NEW_NAME = "new category";
    private static final String INVALID_NAME = "";

    private static final String CATEGORY_IS_INVALID_MESSAGE = "The new category is invalid.";
    private static final String CATEGORY_HAS_BEEN_SAVED = "The category has been saved.";
    private static final String CATEGORY_HAS_BEEN_SAVED_BEFORE_MESSAGE = "The category has been saved before.";
    private static final String CATEGORY_HAS_BEEN_UPDATED = "The category has been updated.";
    private static final String CATEGORY_CANNOT_BE_UPDATED_MESSAGE = "You cannot update this category, because it exists.";

    private SubCategoryModelService underTest;
    private IMocksControl control;

    private SubCategoryService subCategoryService;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        subCategoryService = control.createMock(DefaultSubCategoryService.class);
        underTest = new SubCategoryModelService(new ModelValidator(), subCategoryService, new SubCategoryModelTransformer());
    }

    @Test
    public void testFindAll() {
        // GIVEN
        SubCategoryModelRequestContext context = new SubCategoryModelRequestContext();
        context.setTransactionType(INCOME);
        List<SubCategory> categoryList = createExampleSubCategoryList();
        List<SubCategoryModel> expectedList = createExampleSubCategoryModelList();
        EasyMock.expect(subCategoryService.getSubCategoryList(INCOME)).andReturn(categoryList);
        control.replay();
        // WHEN
        List<SubCategoryModel> result = underTest.findAll(context);
        // THEN
        control.verify();
        Assert.assertEquals(expectedList.size(), result.size());
        Assert.assertEquals(expectedList.get(0).getId(), result.get(0).getId());
        Assert.assertEquals(expectedList.get(0).getName().getValue(), result.get(0).getName().getValue());
        Assert.assertEquals(expectedList.get(0).getTransactionType().getValue(), result.get(0).getTransactionType().getValue());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenIdIsNotNull() {
        // GIVEN
        SubCategoryModel categoryModelToSave = createSubCategoryModelBuilderWithDefaultValues(RESERVED_ID, NEW_NAME, INCOME).build();
        // WHEN
        underTest.saveSubCategory(categoryModelToSave);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenNamIsInvalid() {
        // GIVEN
        SubCategoryModel categoryModelToSave = SubCategoryModel.builder()
            .withName(null)
            .withTransactionType(ModelStringValue.builder()
                .withValue(INCOME.name())
                .build())
            .build();
        // WHEN
        underTest.saveSubCategory(categoryModelToSave);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenTypeIsInvalid() {
        // GIVEN
        SubCategoryModel categoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder()
                .withValue(NEW_NAME)
                .build())
            .build();
        // WHEN
        underTest.saveSubCategory(categoryModelToSave);
        // THEN
    }

    @Test
    public void testSaveWhenNameValueIsInvalid() {
        // GIVEN
        SubCategoryModel categoryModelToSave = createSubCategoryModelBuilderWithDefaultValues(null, INVALID_NAME, INCOME).build();
        // WHEN
        SubCategoryModelResponse result = underTest.saveSubCategory(categoryModelToSave);
        // THEN
        Assert.assertEquals(CATEGORY_IS_INVALID_MESSAGE, result.getMessage());
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSaveWhenTypeValueIsInvalid() {
        // GIVEN
        SubCategoryModel categoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder()
                .withValue(NEW_NAME)
                .build())
            .withTransactionType(ModelStringValue.builder()
                .withValue(INVALID_NAME)
                .build())
            .build();
        // WHEN
        SubCategoryModelResponse result = underTest.saveSubCategory(categoryModelToSave);
        // THEN
        Assert.assertEquals(CATEGORY_IS_INVALID_MESSAGE, result.getMessage());
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSaveWhenCategoryHasBeenSavedAlready() {
        // GIVEN
        SubCategoryModel categoryModelToSave = createSubCategoryModelBuilderWithDefaultValues(null, RESERVED_NAME, INCOME).build();
        SubCategory categoryToSave = createSubCategoryBuilderWithDefaultValues().withId(null).build();
        EasyMock.expect(subCategoryService.save(categoryToSave)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        SubCategoryModelResponse result = underTest.saveSubCategory(categoryModelToSave);
        // THEN
        control.verify();
        Assert.assertEquals(CATEGORY_HAS_BEEN_SAVED_BEFORE_MESSAGE, result.getMessage());
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSave() {
        // GIVEN
        SubCategoryModel categoryModelToSave = createSubCategoryModelBuilderWithDefaultValues(null, NEW_NAME, INCOME).build();
        SubCategory categoryToSave = createSubCategoryBuilderWithDefaultValues().withId(null).withName(NEW_NAME).build();
        Optional<SubCategory> savedCategory = Optional.of(createSubCategoryBuilderWithDefaultValues().build());
        EasyMock.expect(subCategoryService.save(categoryToSave)).andReturn(savedCategory);
        control.replay();
        // WHEN
        SubCategoryModelResponse result = underTest.saveSubCategory(categoryModelToSave);
        // THEN
        control.verify();
        Assert.assertEquals(CATEGORY_HAS_BEEN_SAVED, result.getMessage());
        Assert.assertTrue(result.isSuccessful());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenIdNotNull() {
        // GIVEN
        SubCategoryModel categoryModelToUpdate = createSubCategoryModelBuilderWithDefaultValues(null, NEW_NAME, INCOME).build();
        // WHEN
        underTest.updateSubCategory(categoryModelToUpdate);
        // THEN
    }

    @Test
    public void testUpdateWhenCategoryCannotBeUpdated() {
        // GIVEN
        SubCategoryModel categoryModelToUpdate = createSubCategoryModelBuilderWithDefaultValues(RESERVED_ID, RESERVED_NAME, INCOME).build();
        SubCategory categoryToUpdate = createSubCategoryBuilderWithDefaultValues().withName(RESERVED_NAME).build();
        EasyMock.expect(subCategoryService.update(categoryToUpdate)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        SubCategoryModelResponse result = underTest.updateSubCategory(categoryModelToUpdate);
        // THEN
        control.verify();
        Assert.assertEquals(CATEGORY_CANNOT_BE_UPDATED_MESSAGE, result.getMessage());
        Assert.assertFalse(result.isSuccessful());
    }

    public void testUpdate() {
        // GIVEN
        SubCategoryModel categoryModelToUpdate = createSubCategoryModelBuilderWithDefaultValues(RESERVED_ID, NEW_NAME, INCOME).build();
        SubCategory categoryToUpdate = createSubCategoryBuilderWithDefaultValues().withName(NEW_NAME).build();
        Optional<SubCategory> updatedCategory = Optional.of(createSubCategoryBuilderWithDefaultValues().build());
        EasyMock.expect(subCategoryService.update(categoryToUpdate)).andReturn(updatedCategory);
        control.replay();
        // WHEN
        SubCategoryModelResponse result = underTest.updateSubCategory(categoryModelToUpdate);
        // THEN
        control.verify();
        Assert.assertEquals(CATEGORY_HAS_BEEN_UPDATED, result.getMessage());
        Assert.assertTrue(result.isSuccessful());
    }

    private List<SubCategory> createExampleSubCategoryList() {
        List<SubCategory> subCategoryList = new ArrayList<>();
        subCategoryList.add(createSubCategoryBuilderWithDefaultValues().build());
        return subCategoryList;
    }

    private SubCategory.Builder createSubCategoryBuilderWithDefaultValues() {
        return SubCategory.builder()
            .withId(RESERVED_ID)
            .withName(RESERVED_NAME)
            .withTransactionType(INCOME);
    }

    private List<SubCategoryModel> createExampleSubCategoryModelList() {
        List<SubCategoryModel> subCategoryModelList = new ArrayList<>();
        subCategoryModelList.add(createSubCategoryModelBuilderWithDefaultValues(RESERVED_ID, RESERVED_NAME, INCOME).build());
        return subCategoryModelList;
    }

    private SubCategoryModel.Builder createSubCategoryModelBuilderWithDefaultValues(final Long id, final String name, final TransactionType type) {
        return SubCategoryModel.builder()
            .withId(id)
            .withName(ModelStringValue.builder().withValue(name).build())
            .withTransactionType(ModelStringValue.builder().withValue(type.name()).build());
    }

}
