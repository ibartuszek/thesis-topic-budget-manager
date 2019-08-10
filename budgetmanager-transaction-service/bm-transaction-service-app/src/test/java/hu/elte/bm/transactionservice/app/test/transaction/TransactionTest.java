package hu.elte.bm.transactionservice.app.test.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelDateValue;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionController;
import hu.elte.bm.transactionservice.web.transaction.TransactionModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelRequestContext;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelResponse;

public class TransactionTest extends AbstactTransactionTest {

    @Autowired
    private TransactionController transactionController;

    @Test
    public void testSaveWhenIdIsNotNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(RESERVED_ID)
                .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new transaction is invalid.");
    }

    @Test
    public void testSaveWhenThereIsOneWithSameDateAndType() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The transaction has been saved.");
        Assert.assertEquals(response.getTransactionModel().getId(), EXPECTED_ID);
    }

    @Test
    public void testSaveWhenThereIsOneWithSameDateAndTypeAndTitle() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withTitle(ModelStringValue.builder().withValue(RESERVED_TITLE).build())
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The transaction has been saved before.");
    }

    @Test
    public void testSaveWhenOptionalFieldsPresent() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withSubCategory(subCategoryModel)
            .withEndDate(ModelDateValue.builder().withValue(EXPECTED_END_DATE).build())
            .withDescription(ModelStringValue.builder().withValue(EXPECTED_DESCRIPTION).build())
            .withMonthly(true)
            .withLocked(true)
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The transaction has been saved.");
        Assert.assertEquals(response.getTransactionModel().getId(), EXPECTED_ID);
    }

    @Test
    public void testUpdateWhenTransactionDoesNotHaveId() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        TransactionModel transactionToUpdate = createTransactionBuilderWithForUpdateValues(mainCategoryModel)
                .withId(null)
                .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = transactionController.updateTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new transaction is invalid.");
    }

    @Test
    public void testUpdateWhenTransactionCannotBeFound() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        TransactionModel transactionToUpdate = createTransactionBuilderWithForUpdateValues(mainCategoryModel)
            .withId(INVALID_ID)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = transactionController.updateTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Original transaction cannot be found in the repository!");
    }

    @Test
    public void testUpdateWhenTransactionIsLocked() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        TransactionModel transactionToUpdate = createTransactionBuilderWithForUpdateValues(mainCategoryModel)
            .withId(LOCKED_ID)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = transactionController.updateTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Transaction is locked, cannot be changed!");
    }

    @Test
    public void testUpdateWhenTransactionTypeChanged() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionToUpdate = createTransactionBuilderWithForUpdateValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .withTransactionType(ModelStringValue.builder().withValue(TransactionType.OUTCOME.name()).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = transactionController.updateTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Transaction type cannot be changed!");
    }

    @Test
    public void testUpdateWhenThereIsOneTransactionWithSameDateAndName() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionToUpdate = createTransactionBuilderWithForUpdateValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(RESERVED_TITLE_2).build())
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = transactionController.updateTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "You cannot update this transaction, because it exists.");
    }

    @Test
    public void testUpdate() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionToUpdate = createTransactionBuilderWithForUpdateValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = transactionController.updateTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The transaction has been updated.");
    }

    @Test
    public void testDelete() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionToDelete = createTransactionBuilderWithForUpdateValues(mainCategoryModel).build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToDelete);
        // WHEN
        ResponseEntity<Object> result = transactionController.deleteTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The transaction has been deleted.");
    }

}
