package hu.elte.bm.transactionservice.app.test.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.commonpack.validator.ModelDateValue;
import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelRequestContext;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelResponse;

public class SaveTransactionTest extends AbstractTransactionTest {

    @Test(dataProvider = "dataForTransactionModelValidationOfTitle")
    public void testSaveWhenTransactionModelTitleValidationFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        transactionModel.setId(null);
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        TransactionModel responseModel = response.getTransactionModel();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);

        if (responseModel.getTitle() != null && responseModel.getTitle().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getTitle().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForTransactionModelValidationOfAmount")
    public void testSaveWhenTransactionModelAmountValidationFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        transactionModel.setId(null);
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        TransactionModel responseModel = response.getTransactionModel();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);

        if (responseModel.getAmount() != null && responseModel.getAmount().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getAmount().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForTransactionModelValidationOfCurrency")
    public void testSaveWhenTransactionModelCurrencyValidationFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        transactionModel.setId(null);
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        TransactionModel responseModel = response.getTransactionModel();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);

        if (responseModel.getCurrency() != null && responseModel.getCurrency().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getCurrency().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForTransactionModelValidationOfType")
    public void testSaveWhenTransactionModelTypeValidationFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        transactionModel.setId(null);
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        TransactionModel responseModel = response.getTransactionModel();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);

        if (responseModel.getTransactionType() != null && responseModel.getTransactionType().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getTransactionType().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForTransactionModelValidationOfDates")
    public void testSaveWhenTransactionModelDateValidationsFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        transactionModel.setId(null);
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        TransactionModel responseModel = response.getTransactionModel();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);

        if (responseModel.getDate() != null && responseModel.getDate().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getDate().getErrorMessage(), fieldErrorMessage);
        }

        if (responseModel.getEndDate() != null && responseModel.getEndDate().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getEndDate().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForTransactionModelValidationOfDescription")
    public void testSaveWhenTransactionModelDescriptionValidationFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        transactionModel.setId(null);
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        TransactionModel responseModel = response.getTransactionModel();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);

        if (responseModel.getDescription() != null && responseModel.getDescription().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getDescription().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForContextValidation")
    public void testSaveCategoryWhenContextValidationFails(final TransactionModelRequestContext context, final String errorMessage) {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel).withId(null).build();
        context.setTransactionModel(transactionModelToSave);
        // WHEN
        ResponseEntity result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), errorMessage);
    }

    @Test
    public void testSaveWhenIdIsNotNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(RESERVED_ID)
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_NEW_TRANSACTION_IS_INVALID);
    }

    @Test
    public void testSaveWhenThereIsOneWithSameDateAndType() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryModel(EXISTING_MAIN_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_TRANSACTION_HAS_BEEN_SAVED);
        Assert.assertEquals(response.getTransactionModel().getId(), EXPECTED_ID);
    }

    @Test
    public void testSaveWhenThereIsOneWithSameDateAndTypeAndTitle() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryModel(EXISTING_MAIN_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withTitle(ModelStringValue.builder().withValue(RESERVED_TITLE).build())
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_TRANSACTION_HAS_BEEN_SAVED);
        Assert.assertEquals(response.getTransactionModel().getId(), EXPECTED_ID);
    }

    @Test
    public void testSaveWhenThereIsOneWithSameDateAndTypeAndTitleAndMainCategory() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withTitle(ModelStringValue.builder().withValue(RESERVED_TITLE).build())
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The transaction has been saved before.");
    }

    @Test
    public void testSaveWhenOptionalFieldsPresent() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withSubCategory(mainCategoryModel.getSubCategoryModelSet().iterator().next())
            .withEndDate(ModelDateValue.builder().withValue(EXPECTED_END_DATE).build())
            .withDescription(ModelStringValue.builder().withValue(EXPECTED_DESCRIPTION).build())
            .withMonthly(true)
            .withLocked(true)
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_TRANSACTION_HAS_BEEN_SAVED);
        Assert.assertEquals(response.getTransactionModel().getId(), EXPECTED_ID);
    }

}
