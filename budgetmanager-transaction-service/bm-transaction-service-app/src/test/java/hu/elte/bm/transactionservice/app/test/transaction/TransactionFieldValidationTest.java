package hu.elte.bm.transactionservice.app.test.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelAmountValue;
import hu.elte.bm.transactionservice.web.common.ModelDateValue;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionController;
import hu.elte.bm.transactionservice.web.transaction.TransactionModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelRequestContext;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelResponse;

public class TransactionFieldValidationTest extends AbstactTransactionTest {

    @Autowired
    private TransactionController transactionController;

    @Test
    public void testSaveWhenTitleIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withTitle(null)
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
    public void testSaveWhenTitleValueIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withTitle(ModelStringValue.builder().withValue(null).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Validated field value cannot be null!");
    }

    @Test
    public void testSaveWhenTitleIsEmpty() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withTitle(ModelStringValue.builder().withValue(EMPTY_STRING).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new transaction is invalid.");
        Assert.assertEquals(response.getTransactionModel().getTitle().getErrorMessage(), "Title cannot be empty!");
    }

    @Test
    public void testSaveWhenTitleIsTooLong() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withTitle(ModelStringValue.builder().withValue(TOO_LONG_TITLE).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new transaction is invalid.");
        Assert.assertEquals(response.getTransactionModel().getTitle().getErrorMessage(), "Title cannot be longer than 50!");
    }

    @Test
    public void testSaveWhenAmountIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withAmount(null)
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
    public void testSaveWhenAmountValueIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withAmount(ModelAmountValue.builder().withValue(null).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Validated field value cannot be null!");
    }

    @Test
    public void testSaveWhenAmountIsNotPositive() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withAmount(ModelAmountValue.builder().withValue(ZERO).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new transaction is invalid.");
        Assert.assertEquals(response.getTransactionModel().getAmount().getErrorMessage(), "Amount must be positive number!");
    }

    @Test
    public void testSaveWhenCurrencyIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withCurrency(null)
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
    public void testSaveWhenCurrencyValueIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withCurrency(ModelStringValue.builder().withValue(null).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Validated field value cannot be null!");
    }

    @Test
    public void testSaveWhenCurrencyIsInvalid() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withCurrency(ModelStringValue.builder().withValue(INVALID_CURRENCY).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new transaction is invalid.");
        Assert.assertEquals(response.getTransactionModel().getCurrency().getErrorMessage(), "Currency must be one of them: [EUR, USD, HUF]!");
    }

    @Test
    public void testSaveWhenTransactionTypeIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withTransactionType(null)
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
    public void testSaveWhenTransactionTypeValueIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withTransactionType(ModelStringValue.builder().withValue(null).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Validated field value cannot be null!");
    }

    @Test
    public void testSaveWhenTransactionTypeIsInvalid() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withTransactionType(ModelStringValue.builder().withValue(INVALID_TYPE).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new transaction is invalid.");
        Assert.assertEquals(response.getTransactionModel().getTransactionType().getErrorMessage(), "Type must be one of them: [INCOME, OUTCOME]!");
    }

    @Test
    public void testSaveWhenMainCategoryIsNull() {
        // GIVEN
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(null)
                .withId(null)
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
    public void testSaveWhenMainCategoryIdIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(null, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withSubCategory(subCategoryModel)
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The Id of mainCategory cannot be null!");
    }

    @Test
    public void testSaveWhenOneOfTheSubCategorySetsSubCategoryIdIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(null, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The Id of subCategory cannot be null!");
    }

    @Test
    public void testSaveWhenSubCategoryIdIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(null, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withSubCategory(subCategoryModel)
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The Id of subCategory cannot be null!");
    }

    @Test
    public void testSaveWhenDateIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withDate(null)
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
    public void testSaveWhenDateValueIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withDate(ModelDateValue.builder().withValue(null).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Validated field value cannot be null!");
    }

    @Test
    public void testSaveWhenTheDateIsBeforeTheEndOfLastPeriod() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withSubCategory(subCategoryModel)
            .withDate(ModelDateValue.builder().withValue(BEFORE_THE_DEADLINE_DATE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new transaction is invalid.");
        Assert.assertEquals(response.getTransactionModel().getDate().getErrorMessage(), MessageFormat.format("Date cannot be before {0}!", FIRST_DATE_OF_THE_PERIOD));
    }

    @Test
    public void testSaveWhenEndDateValueIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withEndDate(ModelDateValue.builder().withValue(null).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Validated field value cannot be null!");
    }

    @Test
    public void testSaveWhenTheEndDateIsNotAfterDate() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withSubCategory(subCategoryModel)
                .withEndDate(ModelDateValue.builder().withValue(EXPECTED_DATE).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new transaction is invalid.");
        Assert.assertEquals(response.getTransactionModel().getEndDate().getErrorMessage(),
                MessageFormat.format("End date cannot be before {0}!", EXPECTED_DATE.plusDays(1)));
    }

    @Test
    public void testSaveWhenDescriptionValueIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withDescription(ModelStringValue.builder().withValue(null).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Validated field value cannot be null!");
    }

    @Test
    public void testSaveWhenDescriptionIsEmpty() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withDescription(ModelStringValue.builder().withValue(EMPTY_STRING).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new transaction is invalid.");
        Assert.assertEquals(response.getTransactionModel().getDescription().getErrorMessage(), "Description cannot be empty!");
    }

    @Test
    public void testSaveWhenDescriptionIsTooLong() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
                .withId(null)
                .withDescription(ModelStringValue.builder().withValue(TOO_LONG_DESCRIPTION).build())
                .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The new transaction is invalid.");
        Assert.assertEquals(response.getTransactionModel().getDescription().getErrorMessage(), "Description cannot be longer than 100!");
    }

}
