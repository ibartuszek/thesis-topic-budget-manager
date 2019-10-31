package hu.elte.bm.transactionservice.service.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.exceptions.transaction.IllegalTransactionException;
import hu.elte.bm.transactionservice.domain.exceptions.transaction.TransactionConflictException;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.database.TransactionDaoProxy;

public class TransactionServiceTest {

    private static final long EXPECTED_ID = 1L;
    private static final long ID_2 = 2L;
    private static final Long NEW_ID = 5L;
    private static final String EXPECTED_TITLE = "title";
    private static final double AMOUNT = 10.0d;
    private static final Currency CURRENCY = Currency.EUR;
    private static final long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "category";
    private static final long DAYS_TO_SUBTRACT = 30L;
    private static final LocalDate EXPECTED_DATE = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
    private static final String NEW_TITLE = "new Title";
    private static final Long USER_ID = 1L;
    private static final LocalDate START = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);

    private TransactionService underTest;
    private IMocksControl control;
    private TransactionDaoProxy transactionDaoProxy;
    private TransactionDateValidator dateValidator;

    @BeforeClass
    public void setup() {
        control = EasyMock.createControl();
        transactionDaoProxy = control.createMock(TransactionDaoProxy.class);
        dateValidator = control.createMock(TransactionDateValidator.class);
        underTest = new TransactionService(transactionDaoProxy, dateValidator);
    }

    @BeforeMethod
    public void reset() {
        control.reset();
    }

    @AfterMethod
    public void verify() {
        control.verify();
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "testDataForFindAllValidation")
    public void testFindAllTransactionWhenStartIsNull(final LocalDate start, final TransactionType type, final Long userId) {
        // GIVEN
        control.replay();
        // WHEN
        underTest.getTransactionList(start, null, createTransactionContext(type, userId));
        // THEN
    }

    @DataProvider
    public Object[][] testDataForFindAllValidation() {
        return new Object[][] {
            { null, INCOME, USER_ID },
            { START, null, USER_ID },
            { START, INCOME, null },
        };
    }

    @Test
    public void testFindAllTransactionWhenDatabaseSendsEmptyList() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        EasyMock.expect(transactionDaoProxy.getTransactionList(START, LocalDate.now(), context)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<Transaction> result = underTest.getTransactionList(START, null, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void testFindAllTransactionWhenDatabaseSendsAList() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        List<Transaction> expectedTransactionList = createExampleTransActionList();
        EasyMock.expect(transactionDaoProxy.getTransactionList(START, LocalDate.now(), context)).andReturn(expectedTransactionList);
        control.replay();
        // WHEN
        List<Transaction> result = underTest.getTransactionList(START, null, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedTransactionList);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "testDateForInputParameterValidation")
    public void testSaveWhenAnInputParameterIsNull(final Transaction transaction, final TransactionType type, final Long userId) {
        // GIVEN
        control.replay();
        // WHEN
        underTest.save(transaction, createTransactionContext(type, userId));
        // THEN
    }

    @DataProvider
    public Object[][] testDateForInputParameterValidation() {
        Transaction transaction = createExampleTransactionBuilder().build();
        return new Object[][] {
            { null, INCOME, USER_ID },
            { transaction, null, USER_ID },
            { transaction, INCOME, null }
        };
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenTransactionIdIsNotNull() {
        // GIVEN
        Transaction transaction = createExampleTransactionBuilder().withId(NEW_ID).build();
        control.replay();
        // WHEN
        underTest.save(transaction, createTransactionContext(INCOME, USER_ID));
        // THEN
    }

    @Test(expectedExceptions = IllegalTransactionException.class, dataProvider = "testDataForFieldValidation")
    public void testSaveWhenFieldValidationFails(final Transaction.Builder builder) {
        // GIVEN
        Transaction transaction = builder.withId(null).build();
        control.replay();
        // WHEN
        underTest.save(transaction, createTransactionContext(INCOME, USER_ID));
        // THEN
    }

    @DataProvider
    public Object[][] testDataForFieldValidation() {
        Transaction.Builder builderWithMainCategoryWithoutId = Transaction.builder()
            .withMainCategory(createExampleMainCategoryBuilderWithDefaultValues().withId(null).build());

        MainCategory mainCategoryWithSubCategoryWithoutId = createExampleMainCategoryBuilderWithDefaultValues().build();
        mainCategoryWithSubCategoryWithoutId.getSubCategorySet().add(createExampleSubCategoryWithNullId());
        Transaction.Builder builderWithMainCategoryWithSubcategoryWithoutId = Transaction.builder()
            .withMainCategory(mainCategoryWithSubCategoryWithoutId);

        Transaction.Builder builderWithSubCategoryWithoutId = createExampleTransactionBuilder()
            .withSubCategory(createExampleSubCategoryWithNullId());

        return new Object[][] {
            { builderWithMainCategoryWithoutId },
            { builderWithMainCategoryWithSubcategoryWithoutId },
            { builderWithSubCategoryWithoutId }
        };
    }

    @Test(expectedExceptions = TransactionConflictException.class)
    public void testSaveWhenTransactionHasSavedAlready() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        List<Transaction> listOfTransactionsWithSameTitle = List.of(createExampleTransactionBuilder().build());
        Transaction transaction = createExampleTransactionBuilder()
            .withId(null)
            .build();
        dateValidator.validate(transaction, context);
        EasyMock.expect(transactionDaoProxy.findByTitle(transaction.getTitle(), context)).andReturn(listOfTransactionsWithSameTitle);
        control.replay();
        // WHEN
        underTest.save(transaction, context);
        // THEN
    }

    @Test
    public void testSave() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transactionWithSameTitle = createExampleTransactionBuilder()
            .withDate(LocalDate.now().minusDays(1))
            .build();
        List<Transaction> listOfTransactionsWithSameTitle = List.of(transactionWithSameTitle);
        Transaction transaction = createExampleTransactionBuilder()
            .withId(null)
            .build();
        Transaction expectedTransAction = createExampleTransactionBuilder()
            .withId(NEW_ID)
            .build();
        dateValidator.validate(transaction, context);
        EasyMock.expect(transactionDaoProxy.findByTitle(transaction.getTitle(), context)).andReturn(listOfTransactionsWithSameTitle);
        EasyMock.expect(transactionDaoProxy.save(transaction, context)).andReturn(expectedTransAction);
        control.replay();
        // WHEN
        Transaction result = underTest.save(transaction, context);
        // THEN
        Assert.assertEquals(result, expectedTransAction);
        Assert.assertEquals(result.getId(), NEW_ID);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "testDateForInputParameterValidation")
    public void testUpdateWhenAnInputParameterIsNull(final Transaction transaction, final TransactionType type, final Long userId) {
        // GIVEN
        control.replay();
        // WHEN
        underTest.update(transaction, createTransactionContext(type, userId));
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenTransactionIdIsNull() {
        // GIVEN
        Transaction transaction = createExampleTransactionBuilder()
            .withId(null)
            .build();
        control.replay();
        // WHEN
        underTest.update(transaction, createTransactionContext(INCOME, USER_ID));
        // THEN
    }

    @Test(expectedExceptions = IllegalTransactionException.class, dataProvider = "testDataForFieldValidation")
    public void testUpdateWhenValidationFails(final Transaction.Builder builder) {
        // GIVEN
        Transaction transaction = builder
            .withId(EXPECTED_ID)
            .build();
        control.replay();
        // WHEN
        underTest.update(transaction, createTransactionContext(INCOME, USER_ID));
        // THEN
    }

    @Test(expectedExceptions = IllegalTransactionException.class)
    public void testUpdateWhenOriginalTransactionCannotBeFound() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transaction = createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build();
        dateValidator.validate(transaction, context);
        EasyMock.expect(transactionDaoProxy.findById(transaction.getId(), context)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        underTest.update(transaction, context);
        // THEN
    }

    @Test(expectedExceptions = IllegalTransactionException.class)
    public void testUpdateWhenOriginalTransactionIsLocked() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transaction = createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build();
        Optional<Transaction> originalTransaction = Optional.ofNullable(createExampleTransactionBuilder()
            .withLocked(true)
            .build());
        dateValidator.validate(transaction, context);
        EasyMock.expect(transactionDaoProxy.findById(transaction.getId(), context))
            .andReturn(originalTransaction);
        control.replay();
        // WHEN
        underTest.update(transaction, context);
        // THEN
    }

    @Test(expectedExceptions = IllegalTransactionException.class)
    public void testUpdateWhenTransactionTypeHasChanged() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transaction = createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build();
        Optional<Transaction> transactionFromRepository = Optional.ofNullable(createExampleTransactionBuilder()
            .withTransactionType(OUTCOME)
            .build());
        dateValidator.validate(transaction, context);
        EasyMock.expect(transactionDaoProxy.findById(transaction.getId(), context))
            .andReturn(transactionFromRepository);
        control.replay();
        // WHEN
        underTest.update(transaction, context);
        // THEN
    }

    @Test(expectedExceptions = IllegalTransactionException.class)
    public void testUpdateWhenTransactionDoesNotHaveAnyChange() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Optional<Transaction> transactionFromRepository = Optional.ofNullable(createExampleTransactionBuilder()
            .build());
        Transaction transaction = createExampleTransactionBuilder()
            .build();
        dateValidator.validate(transaction, context);
        EasyMock.expect(transactionDaoProxy.findById(transaction.getId(), context)).andReturn(transactionFromRepository);
        control.replay();
        // WHEN
        underTest.update(transaction, context);
        // THEN
    }

    @Test
    public void testUpdate() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transactionWithSameTitle = createExampleTransactionBuilder()
            .withId(ID_2)
            .withTitle(NEW_TITLE)
            .withDate(LocalDate.now().minusDays(1))
            .build();
        List<Transaction> listOfTransactionsWithSameTitle = List.of(transactionWithSameTitle);
        Transaction transaction = createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build();
        Optional<Transaction> transactionFromRepository = Optional.ofNullable(createExampleTransactionBuilder().build());
        Transaction expectedTransaction = createExampleTransactionBuilder()
            .withTitle(EXPECTED_TITLE)
            .build();
        dateValidator.validate(transaction, context);
        EasyMock.expect(transactionDaoProxy.findById(transaction.getId(), context)).andReturn(transactionFromRepository);
        EasyMock.expect(transactionDaoProxy.findByTitle(transaction.getTitle(), context)).andReturn(listOfTransactionsWithSameTitle);
        EasyMock.expect(transactionDaoProxy.update(transaction, context)).andReturn(expectedTransaction);
        control.replay();
        // WHEN
        Transaction result = underTest.update(transaction, context);
        // THEN
        Assert.assertEquals(result, expectedTransaction);
    }

    @Test
    public void testDelete() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transaction = createExampleTransactionBuilder().build();
        Optional<Transaction> transactionFromRepository = Optional.ofNullable(createExampleTransactionBuilder().build());
        Transaction expectedTransaction = createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build();
        EasyMock.expect(transactionDaoProxy.findById(transaction.getId(), context)).andReturn(transactionFromRepository);
        EasyMock.expect(transactionDaoProxy.delete(transaction, context)).andReturn(expectedTransaction);
        control.replay();
        // WHEN
        Transaction result = underTest.delete(transaction, context);
        // THEN
        Assert.assertEquals(result, expectedTransaction);
    }

    @Test
    public void testGetTheFirstDateOfTheNewPeriod() {
        // GIVEN
        EasyMock.expect(dateValidator.getTheFirstDateOfTheNewPeriod(USER_ID)).andReturn(EXPECTED_DATE);
        control.replay();
        // WHEN
        LocalDate result = underTest.getTheFirstDateOfTheNewPeriod(USER_ID);
        // THEN
        Assert.assertEquals(result, EXPECTED_DATE);
    }

    private List<Transaction> createExampleTransActionList() {
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(createExampleTransactionBuilder().build());
        return transactionList;
    }

    private Transaction.Builder createExampleTransactionBuilder() {
        return Transaction.builder()
            .withId(EXPECTED_ID)
            .withTitle(EXPECTED_TITLE)
            .withAmount(AMOUNT)
            .withCurrency(CURRENCY)
            .withTransactionType(INCOME)
            .withMainCategory(createExampleMainCategoryBuilderWithDefaultValues().build())
            .withDate(LocalDate.now());
    }

    private MainCategory.Builder createExampleMainCategoryBuilderWithDefaultValues() {
        return MainCategory.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(new HashSet<>());
    }

    private SubCategory createExampleSubCategoryWithNullId() {
        return SubCategory.builder()
            .withId(null)
            .withName(CATEGORY_NAME)
            .withTransactionType(TransactionType.INCOME)
            .build();
    }

    private TransactionContext createTransactionContext(final TransactionType type, final Long userId) {
        return TransactionContext.builder()
            .withTransactionType(type)
            .withUserId(userId)
            .build();
    }

}
