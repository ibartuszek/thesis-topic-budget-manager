package hu.elte.bm.transactionservice.domain.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
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
    private DefaultTransactionService underTest;
    private IMocksControl control;
    private DatabaseProxy databaseProxy;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        databaseProxy = control.createMock(DatabaseProxy.class);
        underTest = new DefaultTransactionService(databaseProxy);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindAllTransactionWhenStartIsNull() {
        // GIVEN
        // WHEN
        underTest.findAllTransaction(null, LocalDate.now(), INCOME);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindAllTransactionWhenEndIsNull() {
        // GIVEN
        // WHEN
        underTest.findAllTransaction(LocalDate.now(), null, INCOME);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindAllTransactionWhenTypeIsNull() {
        // GIVEN
        // WHEN
        underTest.findAllTransaction(LocalDate.now(), LocalDate.now(), null);
        // THEN
    }

    @Test
    public void testFindAllTransactionWhenDatabaseSendsEmptyList() {
        // GIVEN
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, INCOME)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findAllTransaction(start, end, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFindAllTransactionWhenDatabaseSendsAList() {
        // GIVEN
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        List<Transaction> expectedTransactionList = createExampleTransActionList();
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, INCOME)).andReturn(expectedTransactionList);
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findAllTransaction(start, end, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(expectedTransactionList, result);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveWhenTransactionIsNull() {
        // GIVEN
        // WHEN
        underTest.save(null);
        // THEN
    }

    @Test
    public void testSaveWhenMainCategoryOfTransactionHasNotIdShouldIsValidReturnFalse() {
        // GIVEN
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        List<Transaction> listForCreatingEndOfLastPeriod = createExampleListForEndOfLastPeriod();
        MainCategory mainCategory = MainCategory.builder()
            .withId(null)
            .withName(CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(new HashSet<>())
            .build();
        Transaction transaction = createExampleTransactionBuilder()
            .withId(null)
            .withMainCategory(mainCategory)
            .build();
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, INCOME)).andReturn(listForCreatingEndOfLastPeriod);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.save(transaction);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSaveWhenMainCategoryOfTransactionHasInvalidSubCategoryShouldIsValidReturnFalse() {
        // GIVEN
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        List<Transaction> listForCreatingEndOfLastPeriod = createExampleListForEndOfLastPeriod();
        Set<SubCategory> subCategorySet = new HashSet<>();
        subCategorySet.add(SubCategory.builder()
            .withName(CATEGORY_NAME).
                withTransactionType(INCOME)
            .build());
        MainCategory mainCategory = MainCategory.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(subCategorySet)
            .build();
        Transaction transaction = createExampleTransactionBuilder()
            .withId(null)
            .withMainCategory(mainCategory)
            .build();
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, INCOME)).andReturn(listForCreatingEndOfLastPeriod);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.save(transaction);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSaveWhenDateOfTransactionIsBeforeEndOfLastPeriodShouldIsValidReturnFalse() {
        // GIVEN
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        List<Transaction> listForCreatingEndOfLastPeriod = createExampleListForEndOfLastPeriod();
        Transaction transaction = createExampleTransactionBuilder()
            .withId(null)
            .withDate(LocalDate.now().minusDays(DAYS_TO_SUBTRACT))
            .build();
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, INCOME)).andReturn(listForCreatingEndOfLastPeriod);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.save(transaction);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSaveWhenThereIsOneTransactionWithSameNameAndDate() {
        // GIVEN
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        List<Transaction> listForCreatingEndOfLastPeriod = createExampleListForEndOfLastPeriod();
        Transaction transactionWithSameTitle = createExampleTransactionBuilder()
            .withId(EXPECTED_ID)
            .build();
        List<Transaction> listOfTransactionsWithSameTitle = new ArrayList<>();
        listOfTransactionsWithSameTitle.add(transactionWithSameTitle);
        Transaction transaction = createExampleTransactionBuilder()
            .withId(null)
            .build();
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, INCOME)).andReturn(listForCreatingEndOfLastPeriod);
        EasyMock.expect(databaseProxy.findTransactionByTitle(transaction.getTitle(), INCOME)).andReturn(listOfTransactionsWithSameTitle);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.save(transaction);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSave() {
        // GIVEN
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        List<Transaction> listForCreatingEndOfLastPeriod = createExampleListForEndOfLastPeriod();
        Transaction transactionWithSameTitle = createExampleTransactionBuilder()
            .withId(EXPECTED_ID)
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
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, INCOME)).andReturn(listForCreatingEndOfLastPeriod);
        EasyMock.expect(databaseProxy.findTransactionByTitle(transaction.getTitle(), INCOME)).andReturn(listOfTransactionsWithSameTitle);
        EasyMock.expect(databaseProxy.saveTransaction(transaction)).andReturn(expectedTransAction);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.save(transaction);
        // THEN
        control.verify();
        Assert.assertEquals(expectedTransAction, result);
    }

    @Test
    public void testUpdateWhenTransactionIsLocked() {
        // GIVEN
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        List<Transaction> listForCreatingEndOfLastPeriod = createExampleListForEndOfLastPeriod();
        Transaction transaction = createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build();
        Optional<Transaction> transactionFromRepository = Optional.ofNullable(createExampleTransactionBuilder()
            .withLocked(true)
            .build());
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, INCOME)).andReturn(listForCreatingEndOfLastPeriod);
        EasyMock.expect(databaseProxy.findTransactionById(transaction.getId())).andReturn(transactionFromRepository);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.update(transaction);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdate() {
        // GIVEN
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        List<Transaction> listForCreatingEndOfLastPeriod = createExampleListForEndOfLastPeriod();
        Transaction transactionWithSameTitle = createExampleTransactionBuilder()
            .withId(EXPECTED_ID)
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
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, INCOME)).andReturn(listForCreatingEndOfLastPeriod);
        EasyMock.expect(databaseProxy.findTransactionById(transaction.getId())).andReturn(transactionFromRepository);
        EasyMock.expect(databaseProxy.findTransactionByTitle(transaction.getTitle(), INCOME)).andReturn(listOfTransactionsWithSameTitle);
        EasyMock.expect(databaseProxy.updateTransaction(transaction)).andReturn(expectedTransaction);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.update(transaction);
        // THEN
        control.verify();
        Assert.assertEquals(expectedTransaction, result);
    }

    @Test
    public void testUpdateWhenTypeHasChanged() {
        // GIVEN
        LocalDate start = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate end = LocalDate.now();
        List<Transaction> listForCreatingEndOfLastPeriod = createExampleListForEndOfLastPeriod();
        Transaction transaction = createExampleTransactionBuilder()
            .withTitle(NEW_TITLE)
            .build();
        Optional<Transaction> transactionFromRepository = Optional.ofNullable(createExampleTransactionBuilder()
            .withTransactionType(OUTCOME)
            .build());
        EasyMock.expect(databaseProxy.findAllTransaction(start, end, INCOME)).andReturn(listForCreatingEndOfLastPeriod);
        EasyMock.expect(databaseProxy.findTransactionById(transaction.getId())).andReturn(transactionFromRepository);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.update(transaction);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    private List<Transaction> createExampleListForEndOfLastPeriod() {
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(createExampleTransactionBuilder()
            .withId(EXPECTED_ID)
            .withLocked(true)
            .withDate(LocalDate.now().minusDays(DAYS_TO_SUBTRACT))
            .build());
        transactionList.add(createExampleTransactionBuilder()
            .withId(ID_2)
            .withLocked(true)
            .withDate(LocalDate.now().minusDays(DAYS_TO_SUBTRACT - 1))
            .build());
        transactionList.add(createExampleTransactionBuilder()
            .withId(ID_3)
            .withLocked(false)
            .withDate(LocalDate.now().minusDays(DAYS_TO_SUBTRACT - 2))
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
            .withMainCategory(MainCategory.builder()
                .withId(CATEGORY_ID)
                .withName(CATEGORY_NAME)
                .withTransactionType(INCOME)
                .withSubCategorySet(new HashSet<>())
                .build()
            )
            .withDate(LocalDate.now());
    }

}
