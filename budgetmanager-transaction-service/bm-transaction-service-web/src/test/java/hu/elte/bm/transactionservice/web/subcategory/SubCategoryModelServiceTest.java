package hu.elte.bm.transactionservice.web.subcategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.commonpack.validator.ModelValidator;
import hu.elte.bm.transactionservice.domain.categories.DefaultSubCategoryService;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryService;
import hu.elte.bm.transactionservice.domain.transaction.TransactionContext;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

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
    private static final String NAME_FIELD_NAME = "Name";
    private static final String TYPE_FIELD_NAME = "Type";
    private static final Long USER_ID = 1L;

    private SubCategoryModelService underTest;
    private IMocksControl control;
    private SubCategoryService subCategoryService;
    private ModelValidator validator;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        subCategoryService = control.createMock(DefaultSubCategoryService.class);
        validator = control.createMock(ModelValidator.class);
        underTest = new SubCategoryModelService(validator, subCategoryService, new SubCategoryModelTransformer());
        updateUnderTestProperties();
    }

    @Test
    public void testFindAll() {
        // GIVEN
        List<SubCategory> categoryList = createExampleSubCategoryList();
        List<SubCategoryModel> expectedList = createExampleSubCategoryModelList();
        EasyMock.expect(subCategoryService.getSubCategoryList(createTransactionContext(INCOME, USER_ID))).andReturn(categoryList);
        control.replay();
        // WHEN
        List<SubCategoryModel> result = underTest.findAll(INCOME, USER_ID);
        // THEN
        control.verify();
        Assert.assertEquals(result.size(), expectedList.size());
        Assert.assertEquals(result.get(0).getId(), expectedList.get(0).getId());
        Assert.assertEquals(result.get(0).getName().getValue(), expectedList.get(0).getName().getValue());
        Assert.assertEquals(result.get(0).getTransactionType().getValue(), expectedList.get(0).getTransactionType().getValue());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenIdIsNotNull() {
        // GIVEN
        SubCategoryModel categoryModelToSave = createSubCategoryModelBuilderWithDefaultValues(RESERVED_ID, NEW_NAME, INCOME).build();
        SubCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        // WHEN
        underTest.saveSubCategory(context);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenUserIdIsNull() {
        // GIVEN
        SubCategoryModel categoryModelToSave = createSubCategoryModelBuilderWithDefaultValues(null, NEW_NAME, INCOME).build();
        SubCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, null);
        // WHEN
        underTest.saveSubCategory(context);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenNamIsInvalid() {
        // GIVEN
        SubCategoryModel categoryModelToSave = SubCategoryModel.builder()
            .withName(null)
            .withTransactionType(ModelStringValue.builder().withValue(INCOME.name()).build())
            .build();
        SubCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        // WHEN
        underTest.saveSubCategory(context);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenTypeIsInvalid() {
        // GIVEN
        SubCategoryModel categoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_NAME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        // WHEN
        underTest.saveSubCategory(context);
        // THEN
    }

    @Test
    public void testSaveWhenNameValueIsInvalid() {
        // GIVEN
        SubCategoryModel categoryModelToSave = createSubCategoryModelBuilderWithDefaultValues(null, INVALID_NAME, INCOME).build();
        SubCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(false);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        control.replay();
        // WHEN
        SubCategoryModelResponse result = underTest.saveSubCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_IS_INVALID_MESSAGE);
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSaveWhenTypeValueIsInvalid() {
        // GIVEN
        SubCategoryModel categoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INVALID_NAME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(false);
        control.replay();
        // WHEN
        SubCategoryModelResponse result = underTest.saveSubCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_IS_INVALID_MESSAGE);
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSaveWhenCategoryHasBeenSavedAlready() {
        // GIVEN
        SubCategoryModel categoryModelToSave = createSubCategoryModelBuilderWithDefaultValues(null, RESERVED_NAME, INCOME).build();
        SubCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        SubCategory categoryToSave = createSubCategoryBuilderWithDefaultValues().withId(null).build();
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(subCategoryService.save(categoryToSave, createTransactionContext(context.getTransactionType(), context.getUserId())))
            .andReturn(Optional.empty());
        control.replay();
        // WHEN
        SubCategoryModelResponse result = underTest.saveSubCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_HAS_BEEN_SAVED_BEFORE_MESSAGE);
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSave() {
        // GIVEN
        SubCategoryModel categoryModelToSave = createSubCategoryModelBuilderWithDefaultValues(null, NEW_NAME, INCOME).build();
        SubCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        SubCategory categoryToSave = createSubCategoryBuilderWithDefaultValues().withId(null).withName(NEW_NAME).build();
        Optional<SubCategory> savedCategory = Optional.of(createSubCategoryBuilderWithDefaultValues().build());
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(subCategoryService.save(categoryToSave, createTransactionContext(context.getTransactionType(), context.getUserId())))
            .andReturn(savedCategory);
        control.replay();
        // WHEN
        SubCategoryModelResponse result = underTest.saveSubCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_HAS_BEEN_SAVED);
        Assert.assertTrue(result.isSuccessful());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenIdNotNull() {
        // GIVEN
        SubCategoryModel categoryModelToUpdate = createSubCategoryModelBuilderWithDefaultValues(null, NEW_NAME, INCOME).build();
        SubCategoryModelRequestContext context = createContext(categoryModelToUpdate, INCOME, USER_ID);
        // WHEN
        underTest.updateSubCategory(context);
        // THEN
    }

    @Test
    public void testUpdateWhenCategoryCannotBeUpdated() {
        // GIVEN
        SubCategoryModel categoryModelToUpdate = createSubCategoryModelBuilderWithDefaultValues(RESERVED_ID, RESERVED_NAME, INCOME).build();
        SubCategoryModelRequestContext context = createContext(categoryModelToUpdate, INCOME, USER_ID);
        SubCategory categoryToUpdate = createSubCategoryBuilderWithDefaultValues().withName(RESERVED_NAME).build();
        EasyMock.expect(validator.validate(categoryModelToUpdate.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToUpdate.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(subCategoryService.update(categoryToUpdate, createTransactionContext(context.getTransactionType(), context.getUserId())))
            .andReturn(Optional.empty());
        control.replay();
        // WHEN
        SubCategoryModelResponse result = underTest.updateSubCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_CANNOT_BE_UPDATED_MESSAGE);
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testUpdate() {
        // GIVEN
        SubCategoryModel categoryModelToUpdate = createSubCategoryModelBuilderWithDefaultValues(RESERVED_ID, NEW_NAME, INCOME).build();
        SubCategoryModelRequestContext context = createContext(categoryModelToUpdate, INCOME, USER_ID);
        SubCategory categoryToUpdate = createSubCategoryBuilderWithDefaultValues().withName(NEW_NAME).build();
        Optional<SubCategory> updatedCategory = Optional.of(createSubCategoryBuilderWithDefaultValues().build());
        EasyMock.expect(validator.validate(categoryModelToUpdate.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToUpdate.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(subCategoryService.update(categoryToUpdate, createTransactionContext(context.getTransactionType(), context.getUserId())))
            .andReturn(updatedCategory);
        control.replay();
        // WHEN
        SubCategoryModelResponse result = underTest.updateSubCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_HAS_BEEN_UPDATED);
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

    private void updateUnderTestProperties() {
        ReflectionTestUtils.setField(underTest, "categoryIsInvalidMessage", "The new category is invalid.");
        ReflectionTestUtils.setField(underTest, "categoryHasBeenSaved", "The category has been saved.");
        ReflectionTestUtils.setField(underTest, "categoryHasBeenSavedBeforeMessage", "The category has been saved before.");
        ReflectionTestUtils.setField(underTest, "categoryHasBeenUpdated", "The category has been updated.");
        ReflectionTestUtils.setField(underTest, "categoryCannotBeUpdatedMessage", "You cannot update this category, because it exists.");
    }

    private SubCategoryModelRequestContext createContext(final SubCategoryModel subCategoryModel, final TransactionType transactionType, final Long userId) {
        SubCategoryModelRequestContext context = new SubCategoryModelRequestContext();
        context.setSubCategoryModel(subCategoryModel);
        context.setTransactionType(transactionType);
        context.setUserId(userId);
        return context;
    }

    private TransactionContext createTransactionContext(final TransactionType transactionType, final Long userId) {
        return TransactionContext.builder()
            .withTransactionType(transactionType)
            .withUserId(userId)
            .build();
    }

}
