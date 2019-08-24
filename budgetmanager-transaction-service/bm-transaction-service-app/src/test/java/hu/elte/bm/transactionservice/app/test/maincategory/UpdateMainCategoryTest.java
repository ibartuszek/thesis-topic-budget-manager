package hu.elte.bm.transactionservice.app.test.maincategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModelRequestContext;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModelResponse;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;

public class UpdateMainCategoryTest extends AbstractMainCategoryTest {

    @Test(dataProvider = "dataForMainCategoryModelValidation")
    public void testUpdateCategoryWhenMainCategoryModelValidationFails(final MainCategoryModel mainCategoryModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        mainCategoryModel.setId(EXISTING_INCOME_ID);
        MainCategoryModelRequestContext context = createContext(INCOME, mainCategoryModel);
        // WHEN
        ResponseEntity result = getMainCategoryController().updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
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
    public void testUpdateCategoryWhenContextValidationFails(final MainCategoryModelRequestContext context, final String errorMessage) {
        // GIVEN
        MainCategoryModel mainCategoryToSave = createMainCategoryModelBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME, new HashSet<>()).build();
        context.setMainCategoryModel(mainCategoryToSave);
        // WHEN
        ResponseEntity result = getMainCategoryController().updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), errorMessage);
    }

    @Test
    public void testUpdateCategoryWhenCategoryIdIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = createMainCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = getMainCategoryController().updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_NEW_CATEGORY_IS_INVALID);
    }

    @Test
    public void testUpdateCategoryWhenCategoryTypeIsChanged() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate =
            createMainCategoryModelBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, OUTCOME, createSubCategoryModelSet()).build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = getMainCategoryController().updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Transaction type cannot be changed!");
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasOneSubCategoryWithoutId() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate =
            createMainCategoryModelBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        mainCategoryToUpdate.getSubCategoryModelSet().add(createSubCategoryModel(null, "illegal supplementary category", INCOME));
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = getMainCategoryController().updateMainCategory(context);
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
        MainCategoryModel mainCategoryToUpdate = createMainCategoryModelBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME, modifiedSet).build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = getMainCategoryController().updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "New mainCategory does not contain all original subCategory!");
    }

    @Test
    public void testUpdateCategoryWhenCategoryCannotBeFoundInTheRepository() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = createMainCategoryModelBuilder(INVALID_ID, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = getMainCategoryController().updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Original mainCategory cannot be found in the repository!");
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewName() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate =
            createMainCategoryModelBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = getMainCategoryController().updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMainCategoryModel().getId(), EXISTING_INCOME_ID);
        Assert.assertEquals(response.getMainCategoryModel().getName().getValue(), NEW_CATEGORY_NAME);
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndOtherTypeHasThisName() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate =
            createMainCategoryModelBuilder(EXISTING_OUTCOME_ID, RESERVED_CATEGORY_NAME, OUTCOME, createSubCategoryModelSet()).build();
        MainCategoryModelRequestContext context = createContext(OUTCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = getMainCategoryController().updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMainCategoryModel().getId(), EXISTING_OUTCOME_ID);
        Assert.assertEquals(response.getMainCategoryModel().getName().getValue(), RESERVED_CATEGORY_NAME);
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndItIsReserved() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate =
            createMainCategoryModelBuilder(EXISTING_INCOME_ID, OTHER_RESERVED_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = getMainCategoryController().updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "You cannot update this category, because it exists.");
    }

}
