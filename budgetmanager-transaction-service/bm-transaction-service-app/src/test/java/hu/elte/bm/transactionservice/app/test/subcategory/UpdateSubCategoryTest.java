package hu.elte.bm.transactionservice.app.test.subcategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.web.subcategory.SubCategoryRequestContext;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryResponse;

public class UpdateSubCategoryTest extends AbstractSubCategoryTest {

    @Test(dataProvider = "dataForMainCategoryModelValidation")
    public void testUpdateCategoryWhenCategoryValidationFails(final SubCategoryModel subCategoryModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        subCategoryModel.setId(EXISTING_INCOME_ID);
        SubCategoryRequestContext context = createContext(INCOME, subCategoryModel);
        // WHEN
        ResponseEntity result = getSubCategoryController().updateSubCategory(context);
        SubCategoryResponse response = (SubCategoryResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);
        if (response.getSubCategoryModel().getName() != null && response.getSubCategoryModel().getName().getErrorMessage() != null) {
            Assert.assertEquals(response.getSubCategoryModel().getName().getErrorMessage(), fieldErrorMessage);
        }
        if (response.getSubCategoryModel().getTransactionType() != null && response.getSubCategoryModel().getTransactionType().getErrorMessage() != null) {
            Assert.assertEquals(response.getSubCategoryModel().getTransactionType().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForContextValidation")
    public void testUpdateCategoryWhenContextValidationFails(final SubCategoryRequestContext context, final String errorMessage) {
        // GIVEN
        SubCategoryModel subCategoryToUpdate = createSubCategoryModelBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME).build();
        context.setSubCategory(subCategoryToUpdate);
        // WHEN
        ResponseEntity result = getSubCategoryController().updateSubCategory(context);
        SubCategoryResponse response = (SubCategoryResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), errorMessage);
    }

    @Test
    public void testUpdateCategoryWhenCategoryIdIsNull() {
        // GIVEN
        SubCategoryModel subCategoryModelToUpdate = createSubCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryModelToUpdate);
        // WHEN
        ResponseEntity result = getSubCategoryController().updateSubCategory(context);
        SubCategoryResponse response = (SubCategoryResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_NEW_CATEGORY_IS_INVALID);
    }

    @Test
    public void testUpdateCategoryWhenCategoryCannotBeFoundInRepository() {
        // GIVEN
        SubCategoryModel subCategoryModelToUpdate = createSubCategoryModelBuilder(INVALID_ID, NEW_CATEGORY_NAME, INCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryModelToUpdate);
        // WHEN
        ResponseEntity result = getSubCategoryController().updateSubCategory(context);
        SubCategoryResponse response = (SubCategoryResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Original subCategory cannot be found in the repository!");
    }

    @Test
    public void testUpdateCategoryWhenCategoryTypeHasChanged() {
        // GIVEN
        SubCategoryModel subCategoryModelToUpdate = createSubCategoryModelBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, OUTCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryModelToUpdate);
        // WHEN
        ResponseEntity result = getSubCategoryController().updateSubCategory(context);
        SubCategoryResponse response = (SubCategoryResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Transaction type cannot be changed!");
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewName() {
        // GIVEN
        SubCategoryModel subCategoryModelToUpdate = createSubCategoryModelBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryModelToUpdate);
        // WHEN
        ResponseEntity result = getSubCategoryController().updateSubCategory(context);
        SubCategoryResponse response = (SubCategoryResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_CATEGORY_HAS_BEEN_UPDATED);
        Assert.assertEquals(response.getSubCategoryModel().getId(), EXISTING_INCOME_ID);
        Assert.assertEquals(response.getSubCategoryModel().getName().getValue(), NEW_CATEGORY_NAME);
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewNameAndOtherTypeHasThisName() {
        // GIVEN
        SubCategoryModel subCategoryModelToUpdate = createSubCategoryModelBuilder(EXISTING_OUTCOME_ID, RESERVED_CATEGORY_NAME, OUTCOME).build();
        SubCategoryRequestContext context = createContext(OUTCOME, subCategoryModelToUpdate);
        // WHEN
        ResponseEntity result = getSubCategoryController().updateSubCategory(context);
        SubCategoryResponse response = (SubCategoryResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_CATEGORY_HAS_BEEN_UPDATED);
        Assert.assertEquals(response.getSubCategoryModel().getId(), EXISTING_OUTCOME_ID);
        Assert.assertEquals(response.getSubCategoryModel().getName().getValue(), RESERVED_CATEGORY_NAME);
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndItIsReserved() {
        // GIVEN
        SubCategoryModel subCategoryModelToUpdate = createSubCategoryModelBuilder(EXISTING_INCOME_ID, OTHER_RESERVED_CATEGORY_NAME, INCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryModelToUpdate);
        // WHEN
        ResponseEntity result = getSubCategoryController().updateSubCategory(context);
        SubCategoryResponse response = (SubCategoryResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "You cannot update this category, because it exists.");
    }

}
