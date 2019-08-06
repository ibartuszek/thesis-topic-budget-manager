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

import hu.elte.bm.transactionservice.domain.categories.DefaultMainCategoryService;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryService;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.common.ModelValidator;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModelTransformer;

public class MainCategoryModelServiceTest {

    private static final long RESERVED_ID = 1L;
    private static final String RESERVED_NAME = "category 1";
    private static final String NEW_NAME = "new category";
    private static final String INVALID_NAME = "";
    private static final String NAME_FIELD_NAME = "Name";
    private static final String TYPE_FIELD_NAME = "Type";

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
        MainCategoryModelRequestContext context = new MainCategoryModelRequestContext();
        context.setTransactionType(INCOME);
        List<MainCategory> categoryList = createExampleMainCategoryList();
        List<MainCategoryModel> expectedList = createExampleMainCategoryModelList();
        EasyMock.expect(mainCategoryService.getMainCategoryList(INCOME)).andReturn(categoryList);
        control.replay();
        // WHEN
        List<MainCategoryModel> result = underTest.findAll(context);
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
        MainCategoryModel categoryModelToSave = createMainCategoryModelBuilderWithDefaultValues(RESERVED_ID, NEW_NAME, INCOME).build();
        // WHEN
        underTest.saveMainCategory(categoryModelToSave);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenNamIsInvalid() {
        // GIVEN
        MainCategoryModel categoryModelToSave = MainCategoryModel.builder()
            .withName(null)
            .withTransactionType(ModelStringValue.builder()
                .withValue(INCOME.name())
                .build())
            .withSubCategoryModelSet(new HashSet<>())
            .build();
        // WHEN
        underTest.saveMainCategory(categoryModelToSave);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenTypeIsInvalid() {
        // GIVEN
        MainCategoryModel categoryModelToSave = MainCategoryModel.builder()
            .withName(ModelStringValue.builder()
                .withValue(NEW_NAME)
                .build())
            .build();
        // WHEN
        underTest.saveMainCategory(categoryModelToSave);
        // THEN
    }

    @Test
    public void testSaveWhenNameValueIsInvalid() {
        // GIVEN
        MainCategoryModel categoryModelToSave = createMainCategoryModelBuilderWithDefaultValues(null, INVALID_NAME, INCOME).build();
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(false);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.saveMainCategory(categoryModelToSave);
        // THEN
        control.verify();
        Assert.assertEquals(CATEGORY_IS_INVALID_MESSAGE, result.getMessage());
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSaveWhenTypeValueIsInvalid() {
        // GIVEN
        MainCategoryModel categoryModelToSave = MainCategoryModel.builder()
            .withName(ModelStringValue.builder()
                .withValue(NEW_NAME)
                .build())
            .withTransactionType(ModelStringValue.builder()
                .withValue(INVALID_NAME)
                .build())
            .withSubCategoryModelSet(new HashSet<>())
            .build();
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(false);
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.saveMainCategory(categoryModelToSave);
        // THEN
        control.verify();
        Assert.assertEquals(CATEGORY_IS_INVALID_MESSAGE, result.getMessage());
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSaveWhenCategoryHasBeenSavedAlready() {
        // GIVEN
        MainCategoryModel categoryModelToSave = createMainCategoryModelBuilderWithDefaultValues(null, RESERVED_NAME, INCOME).build();
        MainCategory categoryToSave = createMainCategoryBuilderWithDefaultValues().withId(null).build();
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(mainCategoryService.save(categoryToSave)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.saveMainCategory(categoryModelToSave);
        // THEN
        control.verify();
        Assert.assertEquals(CATEGORY_HAS_BEEN_SAVED_BEFORE_MESSAGE, result.getMessage());
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testSave() {
        // GIVEN
        MainCategoryModel categoryModelToSave = createMainCategoryModelBuilderWithDefaultValues(null, NEW_NAME, INCOME).build();
        MainCategory categoryToSave = createMainCategoryBuilderWithDefaultValues().withId(null).withName(NEW_NAME).build();
        Optional<MainCategory> savedCategory = Optional.of(createMainCategoryBuilderWithDefaultValues().build());
        EasyMock.expect(validator.validate(categoryModelToSave.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToSave.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(mainCategoryService.save(categoryToSave)).andReturn(savedCategory);
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.saveMainCategory(categoryModelToSave);
        // THEN
        control.verify();
        Assert.assertEquals(CATEGORY_HAS_BEEN_SAVED, result.getMessage());
        Assert.assertTrue(result.isSuccessful());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenIdNotNull() {
        // GIVEN
        MainCategoryModel categoryModelToUpdate = createMainCategoryModelBuilderWithDefaultValues(null, NEW_NAME, INCOME).build();
        // WHEN
        underTest.updateMainCategory(categoryModelToUpdate);
        // THEN
    }

    @Test
    public void testUpdateWhenCategoryCannotBeUpdated() {
        // GIVEN
        MainCategoryModel categoryModelToUpdate = createMainCategoryModelBuilderWithDefaultValues(RESERVED_ID, RESERVED_NAME, INCOME).build();
        MainCategory categoryToUpdate = createMainCategoryBuilderWithDefaultValues().withName(RESERVED_NAME).build();
        EasyMock.expect(validator.validate(categoryModelToUpdate.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToUpdate.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(mainCategoryService.update(categoryToUpdate)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.updateMainCategory(categoryModelToUpdate);
        // THEN
        control.verify();
        Assert.assertEquals(CATEGORY_CANNOT_BE_UPDATED_MESSAGE, result.getMessage());
        Assert.assertFalse(result.isSuccessful());
    }

    @Test
    public void testUpdate() {
        // GIVEN
        MainCategoryModel categoryModelToUpdate = createMainCategoryModelBuilderWithDefaultValues(RESERVED_ID, NEW_NAME, INCOME).build();
        MainCategory categoryToUpdate = createMainCategoryBuilderWithDefaultValues().withName(NEW_NAME).build();
        Optional<MainCategory> updatedCategory = Optional.of(createMainCategoryBuilderWithDefaultValues().build());
        EasyMock.expect(validator.validate(categoryModelToUpdate.getName(), NAME_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(categoryModelToUpdate.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(mainCategoryService.update(categoryToUpdate)).andReturn(updatedCategory);
        control.replay();
        // WHEN
        MainCategoryModelResponse result = underTest.updateMainCategory(categoryModelToUpdate);
        // THEN
        control.verify();
        Assert.assertEquals(CATEGORY_HAS_BEEN_UPDATED, result.getMessage());
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

}
