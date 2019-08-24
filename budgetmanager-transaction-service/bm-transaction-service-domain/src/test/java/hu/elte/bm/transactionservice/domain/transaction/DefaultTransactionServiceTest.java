package hu.elte.bm.transactionservice.domain.transaction;

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
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;

public class DefaultTransactionServiceTest {

    private static final long EXPECTED_ID = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private static final Long NEW_ID = 5L;
    private static final String EXPECTED_TITLE = "title";
    private static final double AMOUNT = 10.0d;
    private static final Currency CURRENCY = Currency.EUR;
    private static final long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "category";
    private static final long DAYS_TO_SUBTRACT = 30L;
    private static final String NEW_TITLE = "new Title";
    private static final LocalDate EXPECTED_LAST_DATE = LocalDate.now().minusDays(DAYS_TO_SUBTRACT - 5);
    private static final LocalDate AFTER_EXPECTED_LAST_DATE = LocalDate.now().minusDays(DAYS_TO_SUBTRACT - 10);
    private static final Long USER_ID = 1L;
    private static final LocalDate START = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);

    private DefaultTransactionService underTest;
    private IMocksControl control;
    private DatabaseProxy databaseProxy;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        databaseProxy = control.createMock(DatabaseProxy.class);
        underTest = new DefaultTransactionService(databaseProxy);
        ReflectionTestUtils.setField(underTest, "cannotBeNullExceptionMessage", "{0} cannot be null!");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "testDataForFindAllValidation")
    public void testFindAllTransactionWhenStartIsNull(final LocalDate start, final TransactionType type, final Long userId) {
        // GIVEN
        // WHEN
        underTest.findAllTransaction(start, null, createTransactionContext(type, userId));
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
        EasyMock.expect(databaseProxy.findAllTransaction(START, LocalDate.now(), context)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findAllTransaction(START, null, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void testFindAllTransactionWhenDatabaseSendsAList() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        List<Transaction> expectedTransactionList = createExampleTransActionList();
        EasyMock.expect(databaseProxy.findAllTransaction(START, LocalDate.now(), context)).andReturn(expectedTransactionList);
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findAllTransaction(START, null, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedTransactionList);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "testDataForPreValidation")
    public void testSaveWhenValidationFails(final Transaction transaction, final TransactionType type, final Long userId) {
        // GIVEN
        // WHEN
        underTest.save(transaction, createTransactionContext(type, userId));
        // THEN
    }

    @DataProvider
    public Object[][] testDataForPreValidation() {
        Transaction transaction = createExampleTransactionBuilder().build();
        return new Object[][] {
            { null, INCOME, USER_ID },
            { transaction, null, USER_ID },
            { transaction, INCOME, null }
        };
    }

    @Test(expectedExceptions = TransactionException.class, dataProvider = "testDataForValidation")
    public void testSaveWhenValidationFails(final Transaction.Builder builder) {
        // GIVEN
        Transaction transaction = builder.withId(null).build();
        // WHEN
        underTest.save(transaction, createTransactionContext(INCOME, USER_ID));
        // THEN
    }

    @DataProvider
    public Object[][] testDataForValidation() {
        Transaction.Builder builderWithMainCategoryWithoutId = Transaction.builder()
            .withMainCategory(createExampleMainCategoryBuilderWithDefaultValues().withId(null).build());

        MainCategory mainCategoryWithSubCategoryWithoutId = createExampleMainCategoryBuilderWithDefaultValues().build();
        mainCategoryWithSubCategoryWithoutId.getSubCategorySet().add(createExampleSubCategory(null, CATEGORY_NAME, INCOME));
        Transaction.Builder builderWithMainCategoryWithSubcategoryWithoutId = Transaction.builder()
            .withMainCategory(mainCategoryWithSubCategoryWithoutId);

        Transaction.Builder builderWithSubCategoryWithoutId = createExampleTransactionBuilder()
            .withSubCategory(createExampleSubCategory(null, CATEGORY_NAME, INCOME));

        return new Object[][] {
            { builderWithMainCategoryWithoutId },
            { builderWithMainCategoryWithSubcategoryWithoutId },
            { builderWithSubCategoryWithoutId }
        };
    }

    @Test
    public void testSaveWhenThereIsOneTransactionWithSameNameAndDate() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        List<Transaction> listOfTransactionsWithSameTitle = new ArrayList<>();
        listOfTransactionsWithSameTitle.add(createExampleTransactionBuilder().build());
        Transaction transaction = createExampleTransactionBuilder()
            .withId(null)
            .build();
        EasyMock.expect(databaseProxy.findTransactionByTitle(transaction.getTitle(), context)).andReturn(listOfTransactionsWithSameTitle);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.save(transaction, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testSave() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transactionWithSameTitle = createExampleTransactionBuilder()
            .withDate(LocalDate.now().minusDays(1))
            .build();
        List<Transaction> listOfTransactionsWithSameTitle = new ArrayList<>();
        listOfTransactionsWithSameTitle.add(transactionWithSameTitle);
        Transaction transaction = createExampleTransactionBuilder()
            .withId(null)
            .build();
        Optional<Transaction> expectedTransAction = Optional.of(createExampleTransactionBuilder()
            .withId(NEW_ID)
            .build());
        EasyMock.expect(databaseProxy.findTransactionByTitle(transaction.getTitle(), context)).andReturn(listOfTransactionsWithSameTitle);
        EasyMock.expect(databaseProxy.saveTransaction(transaction, context)).andReturn(expectedTransAction);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.save(transaction, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedTransAction);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "testDataForPreValidation")
    public void testUpdateWhenValidationFails(final Transaction transaction, final TransactionType type, final Long userId) {
        // GIVEN
        // WHEN
        underTest.update(transaction, createTransactionContext(type, userId));
        // THEN
    }

    @Test(expectedExceptions = TransactionException.class, dataProvider = "testDataForValidation")
    public void testUpdateWhenValidationFails(final Transaction.Builder builder) {
        // GIVEN
        Transaction transaction = builder.build();
        // WHEN
        underTest.update(transaction, createTransactionContext(INCOME, USER_ID));
        // THEN
    }

    @Test(expectedExceptions = TransactionException.class)
    public void testUpdateWhenOriginalTransactionCannotBeFound() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transaction = createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build();
        EasyMock.expect(databaseProxy.findTransactionById(transaction.getId(), context)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        try {
            underTest.update(transaction, context);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test(expectedExceptions = TransactionException.class)
    public void testUpdateWhenOriginalTransactionIsLocked() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transaction = createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build();
        Optional<Transaction> originalTransaction = Optional.ofNullable(createExampleTransactionBuilder()
            .withLocked(true)
            .build());
        EasyMock.expect(databaseProxy.findTransactionById(transaction.getId(), context))
            .andReturn(originalTransaction);
        control.replay();
        // WHEN
        try {
            underTest.update(transaction, context);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test(expectedExceptions = TransactionException.class)
    public void testUpdateWhenTransactionTypeHasChanged() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transaction = createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build();
        Optional<Transaction> transactionFromRepository = Optional.ofNullable(createExampleTransactionBuilder()
            .withTransactionType(OUTCOME)
            .build());
        EasyMock.expect(databaseProxy.findTransactionById(transaction.getId(), context))
            .andReturn(transactionFromRepository);
        control.replay();
        // WHEN
        try {
            underTest.update(transaction, context);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testUpdate() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transactionWithSameTitle = createExampleTransactionBuilder()
            .withId(ID_2)
            .withDate(LocalDate.now().minusDays(1))
            .build();
        List<Transaction> listOfTransactionsWithSameTitle = new ArrayList<>();
        listOfTransactionsWithSameTitle.add(transactionWithSameTitle);
        Transaction transaction = createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build();
        Optional<Transaction> transactionFromRepository = Optional.ofNullable(createExampleTransactionBuilder().build());
        Optional<Transaction> expectedTransaction = Optional.ofNullable(createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build());
        EasyMock.expect(databaseProxy.findTransactionById(transaction.getId(), context))
            .andReturn(transactionFromRepository);
        EasyMock.expect(databaseProxy.findTransactionByTitle(transaction.getTitle(), context)).andReturn(listOfTransactionsWithSameTitle);
        EasyMock.expect(databaseProxy.updateTransaction(transaction, context)).andReturn(expectedTransaction);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.update(transaction, context);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedTransaction);
    }

    @Test
    public void testDelete() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        Transaction transaction = createExampleTransactionBuilder().build();
        Optional<Transaction> transactionFromRepository = Optional.ofNullable(createExampleTransactionBuilder().build());
        EasyMock.expect(databaseProxy.findTransactionById(transaction.getId(), context))
            .andReturn(transactionFromRepository);
        databaseProxy.deleteTransaction(transaction, context);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.delete(transaction, context);
        // THEN
        control.verify();
        Assert.assertEquals(result.get(), transaction);
    }

    @Test
    public void testGetTheFirstDateOfTheNewPeriodWhenRepositoryReturnsWithEmptyList() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        List<Transaction> transactionList = Collections.emptyList();
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, context)).andReturn(transactionList);
        control.replay();
        // WHEN
        LocalDate result = underTest.getTheFirstDateOfTheNewPeriod(context);
        // THEN
        control.verify();
        Assert.assertEquals(result, start);
    }

    @Test
    public void testGetTheFirstDateOfTheNewPeriodWhenThereCannotBeFoundALockedDate() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(createExampleTransactionBuilder().withDate(LocalDate.now()).build());
        transactionList.add(createExampleTransactionBuilder().withDate(LocalDate.now().minusDays(DAYS_TO_SUBTRACT)).build());
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, context)).andReturn(transactionList);
        control.replay();
        // WHEN
        LocalDate result = underTest.getTheFirstDateOfTheNewPeriod(context);
        // THEN
        control.verify();
        Assert.assertEquals(result, start);
    }

    @Test
    public void testGetTheFirstDateOfTheNewPeriodWhenThereCanBeFoundALockedDate() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME, USER_ID);
        List<Transaction> transactionList = createExampleListForEndOfLastPeriodWithCalls();
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, context)).andReturn(transactionList);
        control.replay();
        // WHEN
        LocalDate result = underTest.getTheFirstDateOfTheNewPeriod(context);
        // THEN
        control.verify();
        Assert.assertEquals(result, EXPECTED_LAST_DATE.plusDays(1));
    }

    private List<Transaction> createExampleListForEndOfLastPeriodWithCalls() {
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(createExampleTransactionBuilder()
            .withId(EXPECTED_ID)
            .withLocked(true)
            .withDate(LocalDate.now().minusDays(DAYS_TO_SUBTRACT))
            .build());
        transactionList.add(createExampleTransactionBuilder()
            .withId(ID_2)
            .withLocked(true)
            .withDate(EXPECTED_LAST_DATE)
            .build());
        transactionList.add(createExampleTransactionBuilder()
            .withId(ID_3)
            .withDate(AFTER_EXPECTED_LAST_DATE)
            .build());
        return transactionList;
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

    private SubCategory createExampleSubCategory(final Long id, final String name, final TransactionType type) {
        return SubCategory.builder()
            .withId(id)
            .withName(name)
            .withTransactionType(type)
            .build();
    }

    private TransactionContext createTransactionContext(final TransactionType type, final Long userId) {
        return TransactionContext.builder()
            .withTransactionType(type)
            .withUserId(userId)
            .build();
    }

}
