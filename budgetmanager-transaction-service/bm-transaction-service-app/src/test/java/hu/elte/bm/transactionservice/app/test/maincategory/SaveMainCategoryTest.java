package hu.elte.bm.transactionservice.app.test.maincategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.util.HashSet;

import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryRequestContext;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryResponse;

public class SaveMainCategoryTest extends AbstractMainCategoryTest {

    @Test(dataProvider = "dataForMainCategoryModelValidation")
    public void testSaveCategoryWhenMainCategoryModelValidationFails(final MainCategoryModel mainCategoryModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        MainCategoryRequestContext context = createContext(INCOME, mainCategoryModel);
        // WHEN
        ResponseEntity result = getMainCategoryController().createMainCategory(context);
        MainCategoryResponse response = (MainCategoryResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);
        if (response.getMainCategoryModel().getName() != null && response.getMainCategoryModel().getName().getErrorMessage() != null) {
            Assert.assertEquals(response.getMainCategoryModel().getName().getErrorMessage(), fieldErrorMessage);
        }
        if (response.getMainCategoryModel().getTransactionType() != null && response.getMainCategoryModel().getTransactionType().getErrorMessage() != null) {
            Assert.assertEquals(response.getMainCategoryModel().getTransactionType().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForContextValidation")
    public void testSaveCategoryWhenContextValidationFails(final MainCategoryRequestContext context, final String errorMessage) {
        // GIVEN
        MainCategoryModel mainCategoryToSave = createMainCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME, new HashSet<>()).build();
        context.setMainCategory(mainCategoryToSave);
        // WHEN
        ResponseEntity result = getMainCategoryController().createMainCategory(context);
        MainCategoryResponse response = (MainCategoryResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), errorMessage);
    }

    @Test
    public void testSaveCategoryWhenCategoryHasASubCategoryWithoutId() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = createMainCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        mainCategoryToSave.getSubCategoryModelSet().add(createSubCategoryModel(null, "illegal supplementary category", INCOME));
        MainCategoryRequestContext context = createContext(INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = getMainCategoryController().createMainCategory(context);
        MainCategoryResponse response = (MainCategoryResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "A subCategory does not have id!");
    }

    @Test
    public void testSaveCategoryWhenCategoryIdIsNotNull() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = createMainCategoryModelBuilder(INVALID_ID, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = getMainCategoryController().createMainCategory(context);
        MainCategoryResponse response = (MainCategoryResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_NEW_CATEGORY_IS_INVALID);
    }

    @Test
    public void testSaveCategoryWhenCategoryHasNewName() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = createMainCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = getMainCategoryController().createMainCategory(context);
        MainCategoryResponse response = (MainCategoryResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_CATEGORY_HAS_BEEN_SAVED);
        Assert.assertEquals(response.getMainCategoryModel().getId(), NEW_ID);
        Assert.assertEquals(response.getMainCategoryModel().getName().getValue(), NEW_CATEGORY_NAME);
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithDifferentCategory() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = createMainCategoryModelBuilder(null, RESERVED_CATEGORY_NAME, OUTCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(OUTCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = getMainCategoryController().createMainCategory(context);
        MainCategoryResponse response = (MainCategoryResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_CATEGORY_HAS_BEEN_SAVED);
        Assert.assertEquals(response.getMainCategoryModel().getId(), NEW_ID);
        Assert.assertEquals(response.getMainCategoryModel().getName().getValue(), RESERVED_CATEGORY_NAME);
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithSameCategory() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = createMainCategoryModelBuilder(null, RESERVED_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = getMainCategoryController().createMainCategory(context);
        MainCategoryResponse response = (MainCategoryResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The category has been saved before.");
    }

}
