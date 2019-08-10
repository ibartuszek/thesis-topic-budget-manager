package hu.elte.bm.transactionservice.app.test;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryController;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModelRequestContext;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModelResponse;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;

public class MainCategoryTest extends AbstractTransactionServiceApplicationTest {

    private static final Long EXISTING_INCOME_ID = 1L;
    private static final Long EXISTING_OUTCOME_ID = 4L;
    private static final Long NEW_ID = 6L;
    private static final Long INVALID_ID = 7L;
    private static final String EMPTY_NAME = "";
    private static final String TOO_LONG_NAME = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";
    private static final String NEW_CATEGORY_NAME = "new main category";
    private static final String RESERVED_CATEGORY_NAME = "main category 1";
    private static final String OTHER_RESERVED_CATEGORY_NAME = "main category 2";
    private static final String INCOME = "INCOME";
    private static final String OUTCOME = "OUTCOME";
    private static final String INVALID_TYPE = "OUTCOME1";

    @Autowired
    private MainCategoryController mainCategoryController;

    @Test
    public void testSaveCategoryWhenCategoryNameIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
                .withName(null)
                .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
    }

    @Test
    public void testSaveCategoryWhenCategoryNameValueIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
                .withName(ModelStringValue.builder().withValue(null).build())
                .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Validated field value cannot be null!");
    }

    @Test
    public void testSaveCategoryWhenCategoryNameIsEmpty() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
                .withName(ModelStringValue.builder().withValue(EMPTY_NAME).build())
                .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
        Assert.assertEquals(response.getMainCategoryModel().getName().getErrorMessage(), "Name cannot be empty!");
    }

    @Test
    public void testSaveCategoryWhenCategoryNameIsTooLong() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
                .withName(ModelStringValue.builder().withValue(TOO_LONG_NAME).build())
                .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
        Assert.assertEquals(response.getMainCategoryModel().getName().getErrorMessage(), "Name cannot be longer than 50!");
    }

    @Test
    public void testSaveCategoryWhenCategoryTypeIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
                .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
                .withTransactionType(null)
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
    }

    @Test
    public void testSaveCategoryWhenCategoryTypeValueIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
                .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
                .withTransactionType(ModelStringValue.builder().withValue(null).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Validated field value cannot be null!");
    }

    @Test
    public void testSaveCategoryWhenCategoryValueIsInvalid() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
                .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
                .withTransactionType(ModelStringValue.builder().withValue(INVALID_TYPE).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
        Assert.assertEquals(response.getMainCategoryModel().getTransactionType().getErrorMessage(), "Type must be one of them: [INCOME, OUTCOME]!");
    }

    @Test
    public void testSaveCategoryWhenCategoryHasASubCategoryWithoutId() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
                .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
                .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        mainCategoryToSave.getSubCategoryModelSet().add(createSubCategoryModel(null, "illegal supplementary category", INCOME));
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "A subCategory does not have id!");
    }

    @Test
    public void testSaveCategoryWhenCategoryIdIsNotNull() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
                .withId(INVALID_ID)
                .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
                .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
    }

    @Test
    public void testSaveCategoryWhenCategoryHasNewName() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The category has been saved.");
        Assert.assertEquals(response.getMainCategoryModel().getId(), NEW_ID);
        Assert.assertEquals(response.getMainCategoryModel().getName().getValue(), NEW_CATEGORY_NAME);
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithDifferentCategory() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(OUTCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The category has been saved.");
        Assert.assertEquals(response.getMainCategoryModel().getId(), NEW_ID);
        Assert.assertEquals(response.getMainCategoryModel().getName().getValue(), RESERVED_CATEGORY_NAME);
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithSameCategory() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The category has been saved before.");
    }

    @Test
    public void testUpdateCategoryWhenCategoryIdIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
                .withId(null)
                .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
                .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
    }

    @Test
    public void testUpdateCategoryWhenCategoryTypeIsChanged() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
                .withId(EXISTING_INCOME_ID)
                .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
                .withTransactionType(ModelStringValue.builder().withValue(OUTCOME).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Transaction type cannot be changed!");
    }


    @Test
    public void testUpdateCategoryWhenCategoryHasOneSubCategoryWithoutId() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
                .withId(EXISTING_INCOME_ID)
                .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
                .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        mainCategoryToUpdate.getSubCategoryModelSet()
                .add(createSubCategoryModel(null, "illegal supplementary category", INCOME));
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "A subCategory does not have id!");
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasLessSubCategory() {
        // GIVEN
        Set<SubCategoryModel> modifiedSet = new HashSet<>();
        modifiedSet.add(createSubCategoryModel(1L, "supplementary category 1", INCOME));
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(modifiedSet)
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "New mainCategory does not contain all original subCategory!");
    }

    @Test
    public void testUpdateCategoryWhenCategoryCannotBeFoundInTheRepository() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
                .withId(INVALID_ID)
                .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
                .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
                .withSubCategoryModelSet(createSubCategoryModelSet())
                .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Original mainCategory cannot be found in the repository!");
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewName() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMainCategoryModel().getId(), EXISTING_INCOME_ID);
        Assert.assertEquals(response.getMainCategoryModel().getName().getValue(), NEW_CATEGORY_NAME);
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndOtherTypeHasThisName() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
            .withId(EXISTING_OUTCOME_ID)
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(OUTCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMainCategoryModel().getId(), EXISTING_OUTCOME_ID);
        Assert.assertEquals(response.getMainCategoryModel().getName().getValue(), RESERVED_CATEGORY_NAME);
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndItIsReserved() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(OTHER_RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "You cannot update this category, because it exists.");
    }

    private Set<SubCategoryModel> createSubCategoryModelSet() {
        Set<SubCategoryModel> subCategorySet = new HashSet<>();
        subCategorySet.add(createSubCategoryModel(1L, "supplementary category 1", INCOME));
        subCategorySet.add(createSubCategoryModel(2L, "supplementary category 2", INCOME));
        return subCategorySet;
    }

    private SubCategoryModel createSubCategoryModel(final Long id, final String name, final String type) {
        ModelStringValue nameValue = ModelStringValue.builder()
            .withValue(name)
            .build();
        ModelStringValue typeValue = ModelStringValue.builder()
            .withValue(type)
            .build();
        return SubCategoryModel.builder()
            .withId(id)
            .withName(nameValue)
            .withTransactionType(typeValue)
            .build();
    }

    private MainCategoryModelRequestContext createContext(final TransactionType type, final MainCategoryModel mainCategoryModel) {
        MainCategoryModelRequestContext context = new MainCategoryModelRequestContext();
        context.setTransactionType(TransactionType.INCOME);
        context.setMainCategoryModel(mainCategoryModel);
        return context;
    }
}
