package hu.elte.bm.transactionservice.app.test.transaction;

import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.commonpack.validator.ModelDateValue;
import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.transaction.TransactionRequestContext;
import hu.elte.bm.transactionservice.web.transaction.TransactionResponse;

public class UpdateTransactionTest extends AbstractTransactionTest {

    @Test(dataProvider = "dataForTransactionModelValidationOfTitle")
    public void testUpdateWhenTransactionModelTitleValidationFails(final Transaction transaction,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionRequestContext context = createContext(TransactionType.INCOME, transaction);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        Transaction responseModel = response.getTransaction();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);

        if (responseModel.getTitle() != null && responseModel.getTitle().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getTitle().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForTransactionModelValidationOfAmount")
    public void testUpdateWhenTransactionModelAmountValidationFails(final Transaction transaction,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionRequestContext context = createContext(TransactionType.INCOME, transaction);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        Transaction responseModel = response.getTransaction();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);

        if (responseModel.getAmount() != null && responseModel.getAmount().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getAmount().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForTransactionModelValidationOfCurrency")
    public void testUpdateWhenTransactionModelCurrencyValidationFails(final Transaction transaction,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionRequestContext context = createContext(TransactionType.INCOME, transaction);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        Transaction responseModel = response.getTransaction();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);

        if (responseModel.getCurrency() != null && responseModel.getCurrency().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getCurrency().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForTransactionModelValidationOfType")
    public void testUpdateWhenTransactionModelTypeValidationFails(final Transaction transaction,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionRequestContext context = createContext(TransactionType.INCOME, transaction);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        Transaction responseModel = response.getTransaction();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);

        if (responseModel.getTransactionType() != null && responseModel.getTransactionType().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getTransactionType().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForTransactionModelValidationOfDates")
    public void testUpdateWhenTransactionModelDateValidationsFails(final Transaction transaction,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionRequestContext context = createContext(TransactionType.INCOME, transaction);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        Transaction responseModel = response.getTransaction();
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
    public void testUpdateWhenTransactionModelDescriptionValidationFails(final Transaction transaction,
        final String responseErrorMessage, final String fieldErrorMessage) {
        // GIVEN
        TransactionRequestContext context = createContext(TransactionType.INCOME, transaction);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        Transaction responseModel = response.getTransaction();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), responseErrorMessage);

        if (responseModel.getDescription() != null && responseModel.getDescription().getErrorMessage() != null) {
            Assert.assertEquals(responseModel.getDescription().getErrorMessage(), fieldErrorMessage);
        }
    }

    @Test(dataProvider = "dataForContextValidation")
    public void testUpdateCategoryWhenContextValidationFails(final TransactionRequestContext context, final String errorMessage) {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithDefaultValues(mainCategoryModel).build();
        context.setTransaction(transactionToUpdate);
        // WHEN
        ResponseEntity result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), errorMessage);
    }

    @Test
    public void testUpdateWhenTransactionDoesNotHaveId() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategoryModel).withId(null).build();
        TransactionRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), THE_NEW_TRANSACTION_IS_INVALID);
    }

    @Test
    public void testUpdateWhenTransactionCannotBeFound() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategoryModel)
            .withId(INVALID_ID)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .build();
        TransactionRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), ORIGINAL_TRANSACTION_CANNOT_BE_FOUND_IN_THE_REPOSITORY);
    }

    @Test
    public void testUpdateWhenTransactionIsLocked() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategoryModel)
            .withId(LOCKED_ID)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .build();
        TransactionRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Transaction is locked, cannot be changed!");
    }

    @Test
    public void testUpdateWhenTransactionTypeChanged() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .withTransactionType(ModelStringValue.builder().withValue(TransactionType.OUTCOME.name()).build())
            .build();
        TransactionRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "Transaction type cannot be changed!");
    }

    @Test
    public void testUpdateWhenThereIsOneTransactionWithSameDateAndNameAndMainCategoryAndSubCategory() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(RESERVED_TITLE_2).build())
            .withSubCategory(mainCategoryModel.getSubCategoryModelSet().iterator().next())
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .build();
        TransactionRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "You cannot update this transaction, because it exists.");
    }

    @Test
    public void testUpdate() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .build();
        TransactionRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = getTransactionController().updateTransaction(context);
        TransactionResponse response = (TransactionResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "The transaction has been updated.");
    }

}
