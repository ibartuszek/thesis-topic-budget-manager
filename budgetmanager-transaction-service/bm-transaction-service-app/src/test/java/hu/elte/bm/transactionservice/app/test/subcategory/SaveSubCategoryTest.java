package hu.elte.bm.transactionservice.app.test.subcategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModelRequestContext;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModelResponse;

public class SaveSubCategoryTest extends AbstractSubCategoryTest {

    @Test(dataProvider = "dataForMainCategoryModelValidation")
    public void testSaveCategoryWhenCategoryValidationFails(final SubCategoryModel subCategoryModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        SubCategoryModelRequestContext context = createContext(INCOME, subCategoryModel);
        // WHEN
        ResponseEntity result = getSubCategoryController().createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
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
    public void testSaveCategoryWhenContextValidationFails(final SubCategoryModelRequestContext context, final String errorMessage) {
        // GIVEN
        SubCategoryModel subCategoryToSave = createSubCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME).build();
        context.setSubCategoryModel(subCategoryToSave);
        // WHEN
        ResponseEntity result = getSubCategoryController().createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), errorMessage);
    }

    @Test
    public void testSaveCategoryWhenIdIsNotNull() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = createSubCategoryModelBuilder(NEW_ID, NEW_CATEGORY_NAME, INCOME).build();
        SubCategoryModelRequestContext context = createContext(INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = getSubCategoryController().createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_NEW_CATEGORY_IS_INVALID);
    }

    @Test
    public void testSaveCategoryWhenCategoryHasNewName() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = createSubCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME).build();
        SubCategoryModelRequestContext context = createContext(INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = getSubCategoryController().createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_CATEGORY_HAS_BEEN_SAVED);
        Assert.assertEquals(NEW_ID, response.getSubCategoryModel().getId());
        Assert.assertEquals(NEW_CATEGORY_NAME, response.getSubCategoryModel().getName().getValue());
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithDifferentCategory() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = createSubCategoryModelBuilder(null, RESERVED_CATEGORY_NAME, OUTCOME).build();
        SubCategoryModelRequestContext context = createContext(OUTCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = getSubCategoryController().createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_CATEGORY_HAS_BEEN_SAVED);
        Assert.assertEquals(response.getSubCategoryModel().getId(), NEW_ID);
        Assert.assertEquals(response.getSubCategoryModel().getName().getValue(), RESERVED_CATEGORY_NAME);
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithSameCategory() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = createSubCategoryModelBuilder(null, RESERVED_CATEGORY_NAME, INCOME).build();
        SubCategoryModelRequestContext context = createContext(INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = getSubCategoryController().createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The category has been saved before.");
    }

}
