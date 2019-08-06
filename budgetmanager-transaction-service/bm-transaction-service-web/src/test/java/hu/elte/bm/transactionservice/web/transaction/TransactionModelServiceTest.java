package hu.elte.bm.transactionservice.web.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionService;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelAmountValue;
import hu.elte.bm.transactionservice.web.common.ModelDateValue;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.common.ModelValidator;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;

public class TransactionModelServiceTest {

    private static final String TRANSACTION_IS_INVALID_MESSAGE = "The new transaction is invalid.";
    private static final String TRANSACTION_HAS_BEEN_SAVED = "The transaction has been saved.";
    private static final String TRANSACTION_HAS_BEEN_SAVED_BEFORE = "The transaction has been saved before.";
    private static final String TRANSACTION_HAS_BEEN_UPDATED = "The transaction has been updated.";
    private static final String TRANSACTION_CANNOT_BE_UPDATED = "You cannot update this transaction, because it exists.";
    private static final String TRANSACTION_HAS_BEEN_DELETED = "The transaction has been deleted.";
    private static final String TRANSACTION_CANNOT_BE_DELETED = "You cannot delete this transaction.";
    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_TITLE = "title";
    private static final Integer TRANSACTION_TITLE_MAXIMUM_LENGTH = 12;
    private static final String INVALID_TITLE = "too long for title";
    private static final Double DEFAULT_AMOUNT = 100.0d;
    private static final Currency DEFAULT_CURRENCY = Currency.EUR;
    private static final TransactionType DEFAULT_TYPE = INCOME;
    private static final LocalDate DEFAULT_DATE = LocalDate.now();
    private static final LocalDate DEFAULT_START_OF_NEW_PERIOD = LocalDate.now().minusDays(1);
    private static final LocalDate END = LocalDate.now();
    private static final boolean LOCKED = true;
    private static final boolean NOT_LOCKED = false;
    private static final MainCategoryModel MAIN_CATEGORY_MODEL = MainCategoryModel.builder().build();
    private static final Set<String> POSSIBLE_CURRENCIES = Arrays.stream(Currency.values()).map(Currency::name).collect(Collectors.toSet());
    private static final Set<String> POSSIBLE_TRANSACTION_TYPES = Arrays.stream(TransactionType.values()).map(TransactionType::name)
            .collect(Collectors.toSet());
    private static final String TITLE_FIELD_NAME = "Title";
    private static final String AMOUNT_FIELD_NAME = "Amount";
    private static final String CURRENCY_FIELD_NAME = "Currency";
    private static final String TYPE_FIELD_NAME = "Type";
    private static final String DATE_FIELD_NAME = "Date";

    private TransactionModelService underTest;

    private IMocksControl control;
    private TransactionService transactionService;
    private TransactionModelTransformer transformer;
    private ModelValidator validator;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        transactionService = control.createMock(TransactionService.class);
        transformer = control.createMock(TransactionModelTransformer.class);
        validator = control.createMock(ModelValidator.class);
        underTest = new TransactionModelService(validator, transactionService, transformer);
        updateUnderTestProperties();
    }

    @Test
    public void testFindAllWhenEndIsNull() {
        // GIVEN
        TransactionModelRequestContext context = createContext(null, null);
        EasyMock.expect(transactionService.findAllTransaction(DEFAULT_START_OF_NEW_PERIOD, INCOME)).andReturn(Collections.emptyList());
        EasyMock.expect(transactionService.getTheFirstDateOfTheNewPeriod(INCOME)).andReturn(DEFAULT_START_OF_NEW_PERIOD);
        control.replay();
        // WHEN
        List<TransactionModel> result = underTest.findAll(context);
        // THEN
        control.verify();
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFindAllWhenEndIsNotNull() {
        // GIVEN
        TransactionModelRequestContext context = createContext(END, null);
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(createExampleTransactionBuilderWithDefaultValues().build());
        List<TransactionModel> transactionModelList = new ArrayList<>();
        transactionModelList.add(createExampleTransactionModelBuilderWithDefaultValues().build());
        EasyMock.expect(transactionService.findAllTransaction(DEFAULT_START_OF_NEW_PERIOD, END, INCOME)).andReturn(transactionList);
        EasyMock.expect(transactionService.getTheFirstDateOfTheNewPeriod(INCOME)).andReturn(DEFAULT_START_OF_NEW_PERIOD);
        EasyMock.expect(transformer.transformToTransactionModel(transactionList.get(0), DEFAULT_START_OF_NEW_PERIOD)).andReturn(transactionModelList.get(0));
        control.replay();
        // WHEN
        List<TransactionModel> result = underTest.findAll(context);
        // THEN
        control.verify();
        Assert.assertEquals(transactionModelList, result);
        Assert.assertEquals(transactionModelList.get(0), result.get(0));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "getData")
    public void testSaveWhenTransactionModelIsInvalidViaPreValidation(final Long id, final ModelStringValue title, final ModelAmountValue amount,
            final ModelStringValue currency, final ModelStringValue type, final MainCategoryModel mainCategoryModel, final ModelDateValue date) {
        // GIVEN
        TransactionModel transactionModel = createExampleTransactionModelBuilderWithDefaultValues()
                .withId(id)
                .withTitle(title)
                .withAmount(amount)
                .withCurrency(currency)
                .withTransactionType(type)
                .withMainCategory(mainCategoryModel)
                .withDate(date)
                .build();
        TransactionModelRequestContext context = createContext(END, transactionModel);
        // WHEN
        underTest.saveTransaction(context);
        // THEN
    }

    @DataProvider
    public Object[][] getData() {
        ModelStringValue title = ModelStringValue.builder().withValue(DEFAULT_TITLE).build();
        ModelAmountValue amount = ModelAmountValue.builder().withValue(DEFAULT_AMOUNT).build();
        ModelStringValue currency = ModelStringValue.builder().withValue(DEFAULT_CURRENCY.name()).build();
        ModelStringValue type = ModelStringValue.builder().withValue(DEFAULT_TYPE.name()).build();
        ModelDateValue date = ModelDateValue.builder().withValue(DEFAULT_DATE).build();
        return new Object[][]{
                {DEFAULT_ID, title, amount, currency, type, MAIN_CATEGORY_MODEL, date},
                {null, null, amount, currency, type, MAIN_CATEGORY_MODEL, date},
                {null, title, null, currency, type, MAIN_CATEGORY_MODEL, date},
                {null, title, amount, null, type, MAIN_CATEGORY_MODEL, date},
                {null, title, amount, currency, null, MAIN_CATEGORY_MODEL, date},
                {null, title, amount, currency, type, null, date},
                {null, title, amount, currency, type, MAIN_CATEGORY_MODEL, null},
        };
    }

    @Test
    public void testSaveWhenTransactionModelIsInvalidViaValidation() {
        // GIVEN
        TransactionModel invalidTransactionModel = createExampleTransactionModelBuilderWithDefaultValues()
                .withId(null)
                .withTitle(ModelStringValue.builder()
                        .withValue(INVALID_TITLE)
                        .withMaximumLength(TRANSACTION_TITLE_MAXIMUM_LENGTH)
                        .build())
                .build();
        TransactionModelRequestContext context = createContext(END, invalidTransactionModel);
        prepareForValidation(invalidTransactionModel, false);
        EasyMock.expect(transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType())).andReturn(DEFAULT_START_OF_NEW_PERIOD);
        transformer.populateValidationFields(invalidTransactionModel, DEFAULT_START_OF_NEW_PERIOD);
        control.replay();
        // WHEN
        TransactionModelResponse result = underTest.saveTransaction(context);
        // THEN
        control.verify();
        Assert.assertFalse(result.isSuccessful());
        Assert.assertEquals(TRANSACTION_IS_INVALID_MESSAGE, result.getMessage());
    }

    @Test
    public void testSaveWhenTransactionHasBeenAlreadySaved() {
        // GIVEN
        TransactionModel transactionModel = createExampleTransactionModelBuilderWithDefaultValues().withId(null).build();
        TransactionModelRequestContext context = createContext(END, transactionModel);
        Transaction transaction = createExampleTransactionBuilderWithDefaultValues().build();
        prepareForValidation(transactionModel, true);
        EasyMock.expect(transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType())).andReturn(DEFAULT_START_OF_NEW_PERIOD);
        transformer.populateValidationFields(transactionModel, DEFAULT_START_OF_NEW_PERIOD);
        EasyMock.expect(transformer.transformToTransaction(transactionModel)).andReturn(transaction);
        EasyMock.expect(transactionService.save(transaction)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        TransactionModelResponse result = underTest.saveTransaction(context);
        // THEN
        control.verify();
        Assert.assertFalse(result.isSuccessful());
        Assert.assertEquals(TRANSACTION_HAS_BEEN_SAVED_BEFORE, result.getMessage());
    }

    @Test
    public void testSave() {
        // GIVEN
        TransactionModel transactionModel = createExampleTransactionModelBuilderWithDefaultValues().withId(null).build();
        TransactionModelRequestContext context = createContext(END, transactionModel);
        Transaction transaction = createExampleTransactionBuilderWithDefaultValues().build();
        prepareForValidation(transactionModel, true);
        EasyMock.expect(transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType())).andReturn(DEFAULT_START_OF_NEW_PERIOD);
        transformer.populateValidationFields(transactionModel, DEFAULT_START_OF_NEW_PERIOD);
        EasyMock.expect(transformer.transformToTransaction(transactionModel)).andReturn(transaction);
        EasyMock.expect(transactionService.save(transaction)).andReturn(Optional.of(transaction));
        EasyMock.expect(transformer.transformToTransactionModel(transaction, DEFAULT_START_OF_NEW_PERIOD)).andReturn(transactionModel);
        control.replay();
        // WHEN
        TransactionModelResponse result = underTest.saveTransaction(context);
        // THEN
        control.verify();
        Assert.assertTrue(result.isSuccessful());
        Assert.assertEquals(TRANSACTION_HAS_BEEN_SAVED, result.getMessage());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenTransactionIsInvalid() {
        // GIVEN
        TransactionModel transactionModel = createExampleTransactionModelBuilderWithDefaultValues().withId(null).build();
        TransactionModelRequestContext context = createContext(END, transactionModel);
        // WHEN
        underTest.updateTransaction(context);
        // THEN
    }

    @Test
    public void testUpdateWhenTransactionIsLocked() {
        // GIVEN
        TransactionModel transactionModel = createExampleTransactionModelBuilderWithDefaultValues().build();
        TransactionModelRequestContext context = createContext(END, transactionModel);
        prepareForValidation(transactionModel, true);
        EasyMock.expect(transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType())).andReturn(DEFAULT_START_OF_NEW_PERIOD);
        transformer.populateValidationFields(transactionModel, DEFAULT_START_OF_NEW_PERIOD);
        EasyMock.expect(transactionService.isLockedTransaction(transactionModel.getId(), INCOME)).andReturn(LOCKED);
        control.replay();
        // WHEN
        TransactionModelResponse result = underTest.updateTransaction(context);
        // THEN
        control.verify();
        Assert.assertFalse(result.isSuccessful());
        Assert.assertEquals(TRANSACTION_IS_INVALID_MESSAGE, result.getMessage());
    }

    @Test
    public void testUpdateWhenTransactionCannotBeUpdated() {
        // GIVEN
        TransactionModel transactionModel = createExampleTransactionModelBuilderWithDefaultValues().build();
        TransactionModelRequestContext context = createContext(END, transactionModel);
        Transaction transaction = createExampleTransactionBuilderWithDefaultValues().build();
        prepareForValidation(transactionModel, true);
        EasyMock.expect(transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType())).andReturn(DEFAULT_START_OF_NEW_PERIOD);
        transformer.populateValidationFields(transactionModel, DEFAULT_START_OF_NEW_PERIOD);
        EasyMock.expect(transactionService.isLockedTransaction(transactionModel.getId(), INCOME)).andReturn(NOT_LOCKED);
        EasyMock.expect(transformer.transformToTransaction(transactionModel)).andReturn(transaction);
        EasyMock.expect(transactionService.update(transaction)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        TransactionModelResponse result = underTest.updateTransaction(context);
        // THEN
        control.verify();
        Assert.assertFalse(result.isSuccessful());
        Assert.assertEquals(TRANSACTION_CANNOT_BE_UPDATED, result.getMessage());
    }

    @Test
    public void testUpdate() {
        // GIVEN
        TransactionModel transactionModel = createExampleTransactionModelBuilderWithDefaultValues().build();
        TransactionModelRequestContext context = createContext(END, transactionModel);
        Transaction transaction = createExampleTransactionBuilderWithDefaultValues().build();
        prepareForValidation(transactionModel, true);
        EasyMock.expect(transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType())).andReturn(DEFAULT_START_OF_NEW_PERIOD);
        transformer.populateValidationFields(transactionModel, DEFAULT_START_OF_NEW_PERIOD);
        EasyMock.expect(transactionService.isLockedTransaction(transactionModel.getId(), INCOME)).andReturn(NOT_LOCKED);
        EasyMock.expect(transformer.transformToTransaction(transactionModel)).andReturn(transaction);
        EasyMock.expect(transactionService.update(transaction)).andReturn(Optional.of(transaction));
        EasyMock.expect(transformer.transformToTransactionModel(transaction, DEFAULT_START_OF_NEW_PERIOD)).andReturn(transactionModel);
        control.replay();
        // WHEN
        TransactionModelResponse result = underTest.updateTransaction(context);
        // THEN
        control.verify();
        Assert.assertTrue(result.isSuccessful());
        Assert.assertEquals(TRANSACTION_HAS_BEEN_UPDATED, result.getMessage());
    }

    @Test
    public void testDeleteWhenTransactionCannotBeDeleted() {
        // GIVEN
        TransactionModel transactionModel = createExampleTransactionModelBuilderWithDefaultValues().build();
        TransactionModelRequestContext context = createContext(END, transactionModel);
        Transaction transaction = createExampleTransactionBuilderWithDefaultValues().build();
        prepareForValidation(transactionModel, true);
        EasyMock.expect(transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType())).andReturn(DEFAULT_START_OF_NEW_PERIOD);
        transformer.populateValidationFields(transactionModel, DEFAULT_START_OF_NEW_PERIOD);
        EasyMock.expect(transactionService.isLockedTransaction(transactionModel.getId(), INCOME)).andReturn(NOT_LOCKED);
        EasyMock.expect(transformer.transformToTransaction(transactionModel)).andReturn(transaction);
        EasyMock.expect(transactionService.delete(transaction)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        TransactionModelResponse result = underTest.deleteTransaction(context);
        // THEN
        control.verify();
        Assert.assertFalse(result.isSuccessful());
        Assert.assertEquals(TRANSACTION_CANNOT_BE_DELETED, result.getMessage());
    }

    @Test
    public void testDelete() {
        // GIVEN
        TransactionModel transactionModel = createExampleTransactionModelBuilderWithDefaultValues().build();
        TransactionModelRequestContext context = createContext(END, transactionModel);
        Transaction transaction = createExampleTransactionBuilderWithDefaultValues().build();
        prepareForValidation(transactionModel, true);
        EasyMock.expect(transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType())).andReturn(DEFAULT_START_OF_NEW_PERIOD);
        transformer.populateValidationFields(transactionModel, DEFAULT_START_OF_NEW_PERIOD);
        EasyMock.expect(transactionService.isLockedTransaction(transactionModel.getId(), INCOME)).andReturn(NOT_LOCKED);
        EasyMock.expect(transformer.transformToTransaction(transactionModel)).andReturn(transaction);
        EasyMock.expect(transactionService.delete(transaction)).andReturn(Optional.of(transaction));
        EasyMock.expect(transformer.transformToTransactionModel(transaction, DEFAULT_START_OF_NEW_PERIOD)).andReturn(transactionModel);
        control.replay();
        // WHEN
        TransactionModelResponse result = underTest.deleteTransaction(context);
        // THEN
        control.verify();
        Assert.assertTrue(result.isSuccessful());
        Assert.assertEquals(TRANSACTION_HAS_BEEN_DELETED, result.getMessage());
    }

    private Transaction.Builder createExampleTransactionBuilderWithDefaultValues() {
        return Transaction.builder()
                .withId(DEFAULT_ID)
                .withTitle(DEFAULT_TITLE)
                .withAmount(DEFAULT_AMOUNT)
                .withCurrency(DEFAULT_CURRENCY)
                .withTransactionType(DEFAULT_TYPE)
                .withMainCategory(MainCategory.builder().build())
                .withDate(DEFAULT_DATE);
    }

    private TransactionModel.Builder createExampleTransactionModelBuilderWithDefaultValues() {
        return TransactionModel.builder()
                .withId(DEFAULT_ID)
                .withTitle(ModelStringValue.builder()
                        .withValue(DEFAULT_TITLE)
                        .withMaximumLength(TRANSACTION_TITLE_MAXIMUM_LENGTH)
                        .build())
                .withAmount(ModelAmountValue.builder()
                        .withValue(DEFAULT_AMOUNT)
                        .withPositive(true)
                        .build())
                .withCurrency(ModelStringValue.builder()
                        .withValue(DEFAULT_CURRENCY.name())
                        .withPossibleEnumValues(POSSIBLE_CURRENCIES)
                        .build())
                .withTransactionType(ModelStringValue.builder()
                        .withValue(DEFAULT_TYPE.name())
                        .withPossibleEnumValues(POSSIBLE_TRANSACTION_TYPES)
                        .build())
                .withMainCategory(MAIN_CATEGORY_MODEL)
                .withDate(ModelDateValue.builder()
                        .withValue(DEFAULT_DATE)
                        .withAfter(DEFAULT_START_OF_NEW_PERIOD)
                        .build());
    }

    private TransactionModelRequestContext createContext(final LocalDate end, final TransactionModel transactionModel) {
        TransactionModelRequestContext context = new TransactionModelRequestContext();
        context.setStart(DEFAULT_START_OF_NEW_PERIOD);
        context.setTransactionType(INCOME);
        context.setEnd(end);
        context.setTransactionModel(transactionModel);
        return context;
    }

    private void prepareForValidation(final TransactionModel transactionModel, final boolean validTitle) {
        EasyMock.expect(validator.validate(transactionModel.getTitle(), TITLE_FIELD_NAME)).andReturn(validTitle);
        EasyMock.expect(validator.validate(transactionModel.getAmount(), AMOUNT_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(transactionModel.getCurrency(), CURRENCY_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(transactionModel.getTransactionType(), TYPE_FIELD_NAME)).andReturn(true);
        EasyMock.expect(validator.validate(transactionModel.getDate(), DATE_FIELD_NAME)).andReturn(true);
    }

    private void updateUnderTestProperties() {
        ReflectionTestUtils.setField(underTest, "transactionIsInvalid", "The new transaction is invalid.");
        ReflectionTestUtils.setField(underTest, "transactionHasBeenSaved", "The transaction has been saved.");
        ReflectionTestUtils.setField(underTest, "transactionHasBeenSavedBefore", "The transaction has been saved before.");
        ReflectionTestUtils.setField(underTest, "transactionHasBeenUpdated", "The transaction has been updated.");
        ReflectionTestUtils.setField(underTest, "transactionCannotBeUpdated", "You cannot update this transaction, because it exists.");
        ReflectionTestUtils.setField(underTest, "transactionHasBeenDeleted", "The transaction has been deleted.");
        ReflectionTestUtils.setField(underTest, "transactionCannotBeDeleted", "You cannot delete this transaction.");
    }

}
