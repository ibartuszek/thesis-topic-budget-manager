package hu.elte.bm.transactionservice.app.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryController;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModelRequestContext;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModelResponse;

public class SubCategoryTest extends AbstractTransactionServiceApplicationTest {

    private static final String EMPTY_NAME = "";
    private static final String TOO_LONG_NAME = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";
    private static final String NEW_CATEGORY_NAME = "new supplementary category";
    private static final String INVALID_TYPE = "INCOME1";
    private static final String INCOME = "INCOME";
    private static final String OUTCOME = "OUTCOME";
    private static final Long EXISTING_INCOME_ID = 1L;
    private static final Long EXISTING_OUTCOME_ID = 4L;
    private static final Long NEW_ID = 6L;
    private static final Long INVALID_ID = 7L;
    private static final String RESERVED_CATEGORY_NAME = "supplementary category 1";
    private static final String OTHER_RESERVED_CATEGORY_NAME = "supplementary category 2";

    @Autowired
    private SubCategoryController subCategoryController;

    @Test
    public void testSaveCategoryWhenCategoryHasNoName() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(null)
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
    }

    @Test
    public void testSaveCategoryWhenCategoryNameValueIsNull() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(null).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Validated field value cannot be null!");
    }

    @Test
    public void testSaveCategoryWhenCategoryHasEmptyName() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(EMPTY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
        Assert.assertEquals(response.getSubCategoryModel().getName().getErrorMessage(), "Name cannot be empty!");
    }

    @Test
    public void testSaveCategoryWhenCategoryHasTooLongName() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(TOO_LONG_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
        Assert.assertEquals(response.getSubCategoryModel().getName().getErrorMessage(), "Name cannot be longer than 50!");
    }

    @Test
    public void testSaveCategoryWhenCategoryHasNoType() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(null)
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
    }

    @Test
    public void testSaveCategoryWhenCategoryTypeValueIsNull() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(null).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Validated field value cannot be null!");
    }

    @Test
    public void testSaveCategoryWhenCategoryHasInvalidType() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INVALID_TYPE).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
        Assert.assertEquals(response.getSubCategoryModel().getTransactionType().getErrorMessage(), "Type must be one of them: [INCOME, OUTCOME]!");
    }

    @Test
    public void testSaveCategoryWhenIdIsNotNull() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withId(NEW_ID)
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
    }

    @Test
    public void testSaveCategoryWhenCategoryHasNewName() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The category has been saved.");
        Assert.assertEquals(NEW_ID, response.getSubCategoryModel().getId());
        Assert.assertEquals(NEW_CATEGORY_NAME, response.getSubCategoryModel().getName().getValue());
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithDifferentCategory() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(OUTCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The category has been saved.");
        Assert.assertEquals(response.getSubCategoryModel().getId(), NEW_ID);
        Assert.assertEquals(response.getSubCategoryModel().getName().getValue(), RESERVED_CATEGORY_NAME);
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithSameCategory() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The category has been saved before.");
    }

    @Test
    public void testUpdateCategoryWhenCategoryIdIsNull() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withId(null)
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.updateSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new category is invalid.");
    }

    @Test
    public void testUpdateCategoryWhenCategoryCannotBeFoundInRepository() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withId(INVALID_ID)
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.updateSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Original subCategory cannot be found in the repository!");
    }

    @Test
    public void testUpdateCategoryWhenCategoryTypeHasChanged() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(OUTCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.updateSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Transaction type cannot be changed!");
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewName() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.updateSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The category has been updated.");
        Assert.assertEquals(response.getSubCategoryModel().getId(), EXISTING_INCOME_ID);
        Assert.assertEquals(response.getSubCategoryModel().getName().getValue(), NEW_CATEGORY_NAME);
    }

    @Test
    public void testUpdateCategorWhenCategoryHasNewNameAndOtherTypeHasThisName() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withId(EXISTING_OUTCOME_ID)
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(OUTCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.updateSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The category has been updated.");
        Assert.assertEquals(response.getSubCategoryModel().getId(), EXISTING_OUTCOME_ID);
        Assert.assertEquals(response.getSubCategoryModel().getName().getValue(), RESERVED_CATEGORY_NAME);
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndItIsReserved() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(OTHER_RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.updateSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "You cannot update this category, because it exists.");
    }

    private SubCategoryModelRequestContext createContext(final TransactionType type, final SubCategoryModel subCategoryModel) {
        SubCategoryModelRequestContext context = new SubCategoryModelRequestContext();
        context.setTransactionType(TransactionType.INCOME);
        context.setSubCategoryModel(subCategoryModel);
        return context;
    }

}
