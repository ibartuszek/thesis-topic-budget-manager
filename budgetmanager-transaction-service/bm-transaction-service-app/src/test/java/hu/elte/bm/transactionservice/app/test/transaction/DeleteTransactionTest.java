package hu.elte.bm.transactionservice.app.test.transaction;

import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelRequestContext;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelResponse;

public class DeleteTransactionTest extends AbstractTransactionTest {

    @Test(dataProvider = "dataForTransactionModelValidationOfTitle")
    public void testDeleteWhenTransactionModelTitleValidationFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().deleteTransaction(context);
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
    public void testDeleteWhenTransactionModelAmountValidationFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().deleteTransaction(context);
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
    public void testDeleteWhenTransactionModelCurrencyValidationFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().deleteTransaction(context);
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
    public void testDeleteWhenTransactionModelTypeValidationFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().deleteTransaction(context);
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
    public void testDeleteWhenTransactionModelDateValidationsFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().deleteTransaction(context);
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
    public void testDeleteWhenTransactionModelDescriptionValidationFails(final TransactionModel transactionModel,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModel);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().deleteTransaction(context);
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
    public void testDeleteCategoryWhenContextValidationFails(final TransactionModelRequestContext context, final String errorMessage) {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel transactionModelToDelete = createTransactionBuilderWithDefaultValues(mainCategoryModel).build();
        context.setTransactionModel(transactionModelToDelete);
        // WHEN
        ResponseEntity result = getTransactionController().deleteTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), errorMessage);
    }

    @Test
    public void testDeleteWhenTransactionDoesNotHaveId() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel transactionToDelete = createTransactionBuilderWithValuesForUpdate(mainCategoryModel).withId(null).build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToDelete);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().deleteTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_NEW_TRANSACTION_IS_INVALID);
    }

    @Test
    public void testDeleteWhenTransactionCannotBeFound() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel transactionToDelete = createTransactionBuilderWithValuesForUpdate(mainCategoryModel)
            .withId(INVALID_ID)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToDelete);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().deleteTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), ORIGINAL_TRANSACTION_CANNOT_BE_FOUND_IN_THE_REPOSITORY);
    }

    @Test
    public void testDeleteWhenTransactionIsLocked() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel transactionToDelete = createTransactionBuilderWithValuesForUpdate(mainCategoryModel)
            .withId(LOCKED_ID)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToDelete);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().deleteTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Transaction is locked, cannot be deleted!");
    }

    @Test
    public void testDelete() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel transactionToDelete = createTransactionBuilderWithValuesForUpdate(mainCategoryModel).build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToDelete);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().deleteTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The transaction has been deleted.");
    }

}
