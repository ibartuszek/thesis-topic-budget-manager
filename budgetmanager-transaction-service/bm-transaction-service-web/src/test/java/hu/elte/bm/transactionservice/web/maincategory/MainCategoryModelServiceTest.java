package hu.elte.bm.transactionservice.web.maincategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.util.ArrayList;
import java.util.HashSet;
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
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.category.DefaultMainCategoryService;
import hu.elte.bm.transactionservice.service.category.MainCategoryService;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModelTransformer;

public class MainCategoryModelServiceTest {

    private static final long RESERVED_ID = 1L;
    private static final String RESERVED_NAME = "category 1";
    private static final String NEW_NAME = "new category";
    private static final String INVALID_NAME = "";
    private static final String NAME_FIELD_NAME = "Name";
    private static final String TYPE_FIELD_NAME = "Type";
    private static final Long USER_ID = 1L;

    private static final String CATEGORY_IS_INVALID_MESSAGE = "The new category is invalid.";
    private static final String CATEGORY_HAS_BEEN_SAVED = "The category has been saved.";
    private static final String CATEGORY_HAS_BEEN_SAVED_BEFORE_MESSAGE = "The category has been saved before.";
    private static final String CATEGORY_HAS_BEEN_UPDATED = "The category has been updated.";
    private static final String CATEGORY_CANNOT_BE_UPDATED_MESSAGE = "You cannot update this category, because it exists.";

    private MainCategoryModelService underTest;
    private IMocksControl control;
    private MainCategoryService mainCategoryService;
    private ModelValidator validator;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        mainCategoryService = control.createMock(DefaultMainCategoryService.class);
        validator = control.createMock(ModelValidator.class);
        underTest = new MainCategoryModelService(validator, mainCategoryService,
            new MainCategoryModelTransformer(new SubCategoryModelTransformer()));
        updateUnderTestProperties();
    }

    @Test
    public void testFindAll() {
        // GIVEN
        List<MainCategory> categoryList = createExampleMainCategoryList();
        List<MainCategoryModel> expectedList = createExampleMainCategoryModelList();
        EasyMock.expect(mainCategoryService.getMainCategoryList(createTransactionContext(INCOME, USER_ID))).andReturn(categoryList);
        control.replay();
        // WHEN
        List<MainCategoryModel> result = underTest.findAll(INCOME, USER_ID);
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
        MainCategoryModel categoryModelToSave = createMainCategoryModelBuilderWithDefaultValues(RESERVED_ID, NEW_NAME, INCOME).build();
        MainCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        // WHEN
        underTest.saveMainCategory(context);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenUserIdISNull() {
        // GIVEN
        MainCategoryModel categoryModelToSave = createMainCategoryModelBuilderWithDefaultValues(null, NEW_NAME, INCOME).build();
        MainCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, null);
        // WHEN
        underTest.saveMainCategory(context);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenTransactionTypeIsNull() {
        // GIVEN
        MainCategoryModel categoryModelToSave = createMainCategoryModelBuilderWithDefaultValues(null, NEW_NAME, INCOME).build();
        MainCategoryModelRequestContext context = createContext(categoryModelToSave, null, USER_ID);
        // WHEN
        underTest.saveMainCategory(context);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenNamIsInvalid() {
        // GIVEN
        MainCategoryModel categoryModelToSave = MainCategoryModel.builder()
            .withName(null)
            .withTransactionType(ModelStringValue.builder().withValue(INCOME.name()).build())
            .withSubCategoryModelSet(new HashSet<>())
            .build();
        MainCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        // WHEN
        underTest.saveMainCategory(context);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenTypeIsInvalid() {
        // GIVEN
        MainCategoryModel categoryModelToSave = MainCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_NAME).build())
            .build();
        MainCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        // WHEN
        underTest.saveMainCategory(context);
        // THEN
    }

    @Test
    public void testSaveWhenNameValueIsInvalid() {
        // GIVEN
        MainCategoryModel categoryModelToSave = createMainCategoryModelBuilderWithDefaultValues(null, INVALID_NAME, INCOME).build();
        MainCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(false);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.saveMainCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_IS_INVALID_MESSAGE);
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSaveWhenTypeValueIsInvalid() {
        // GIVEN
        MainCategoryModel categoryModelToSave = MainCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INVALID_NAME).build())
            .withSubCategoryModelSet(new HashSet<>())
            .build();
        MainCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(false);
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.saveMainCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_IS_INVALID_MESSAGE);
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSaveWhenCategoryHasBeenSavedAlready() {
        // GIVEN
        MainCategoryModel categoryModelToSave = createMainCategoryModelBuilderWithDefaultValues(null, RESERVED_NAME, INCOME).build();
        MainCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        MainCategory categoryToSave = createMainCategoryBuilderWithDefaultValues().withId(null).build();
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(mainCategoryService.save(categoryToSave, createTransactionContext(INCOME, USER_ID))).andReturn(Optional.empty());
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.saveMainCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_HAS_BEEN_SAVED_BEFORE_MESSAGE);
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSave() {
        // GIVEN
        MainCategoryModel categoryModelToSave = createMainCategoryModelBuilderWithDefaultValues(null, NEW_NAME, INCOME).build();
        MainCategoryModelRequestContext context = createContext(categoryModelToSave, INCOME, USER_ID);
        MainCategory categoryToSave = createMainCategoryBuilderWithDefaultValues().withId(null).withName(NEW_NAME).build();
        Optional<MainCategory> savedCategory = Optional.of(createMainCategoryBuilderWithDefaultValues().build());
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(mainCategoryService.save(categoryToSave, createTransactionContext(INCOME, USER_ID))).andReturn(savedCategory);
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.saveMainCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_HAS_BEEN_SAVED);
        Assert.assertTrue(result.isSuccessful());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenIdNotNull() {
        // GIVEN
        MainCategoryModel categoryModelToUpdate = createMainCategoryModelBuilderWithDefaultValues(null, NEW_NAME, INCOME).build();
        MainCategoryModelRequestContext context = createContext(categoryModelToUpdate, INCOME, USER_ID);
        // WHEN
        underTest.updateMainCategory(context);
        // THEN
    }

    @Test
    public void testUpdateWhenCategoryCannotBeUpdated() {
        // GIVEN
        MainCategoryModel categoryModelToUpdate = createMainCategoryModelBuilderWithDefaultValues(RESERVED_ID, RESERVED_NAME, INCOME).build();
        MainCategoryModelRequestContext context = createContext(categoryModelToUpdate, INCOME, USER_ID);
        MainCategory categoryToUpdate = createMainCategoryBuilderWithDefaultValues().withName(RESERVED_NAME).build();
        EasyMock.expect(validator.validate(categoryModelToUpdate.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToUpdate.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(mainCategoryService.update(categoryToUpdate, createTransactionContext(INCOME, USER_ID))).andReturn(Optional.empty());
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.updateMainCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_CANNOT_BE_UPDATED_MESSAGE);
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testUpdate() {
        // GIVEN
        MainCategoryModel categoryModelToUpdate = createMainCategoryModelBuilderWithDefaultValues(RESERVED_ID, NEW_NAME, INCOME).build();
        MainCategoryModelRequestContext context = createContext(categoryModelToUpdate, INCOME, USER_ID);
        MainCategory categoryToUpdate = createMainCategoryBuilderWithDefaultValues().withName(NEW_NAME).build();
        Optional<MainCategory> updatedCategory = Optional.of(createMainCategoryBuilderWithDefaultValues().build());
        EasyMock.expect(validator.validate(categoryModelToUpdate.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToUpdate.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(mainCategoryService.update(categoryToUpdate, createTransactionContext(INCOME, USER_ID))).andReturn(updatedCategory);
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.updateMainCategory(context);
        // THEN
        control.verify();
        Assert.assertEquals(result.getMessage(), CATEGORY_HAS_BEEN_UPDATED);
        Assert.assertTrue(result.isSuccessful());
    }

    private List<MainCategory> createExampleMainCategoryList() {
        List<MainCategory> mainCategoryList = new ArrayList<>();
        mainCategoryList.add(createMainCategoryBuilderWithDefaultValues().build());
        return mainCategoryList;
    }

    private MainCategory.Builder createMainCategoryBuilderWithDefaultValues() {
        return MainCategory.builder()
            .withId(RESERVED_ID)
            .withName(RESERVED_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(new HashSet<>());
    }

    private List<MainCategoryModel> createExampleMainCategoryModelList() {
        List<MainCategoryModel> mainCategoryModelList = new ArrayList<>();
        mainCategoryModelList.add(createMainCategoryModelBuilderWithDefaultValues(RESERVED_ID, RESERVED_NAME, INCOME).build());
        return mainCategoryModelList;
    }

    private MainCategoryModel.Builder createMainCategoryModelBuilderWithDefaultValues(final Long id, final String name, final TransactionType type) {
        return MainCategoryModel.builder()
            .withId(id)
            .withName(ModelStringValue.builder().withValue(name).build())
            .withTransactionType(ModelStringValue.builder().withValue(type.name()).build())
            .withSubCategoryModelSet(new HashSet<>());
    }

    private void updateUnderTestProperties() {
        ReflectionTestUtils.setField(underTest, "categoryIsInvalidMessage", "The new category is invalid.");
        ReflectionTestUtils.setField(underTest, "categoryHasBeenSaved", "The category has been saved.");
        ReflectionTestUtils.setField(underTest, "categoryHasBeenSavedBeforeMessage", "The category has been saved before.");
        ReflectionTestUtils.setField(underTest, "categoryHasBeenUpdated", "The category has been updated.");
        ReflectionTestUtils.setField(underTest, "categoryCannotBeUpdatedMessage", "You cannot update this category, because it exists.");
    }

    private MainCategoryModelRequestContext createContext(final MainCategoryModel categoryModel, final TransactionType transactionType, final Long userId) {
        MainCategoryModelRequestContext context = new MainCategoryModelRequestContext();
        context.setMainCategoryModel(categoryModel);
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
