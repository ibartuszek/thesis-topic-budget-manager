package hu.elte.bm.transactionservice.service.database;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

public class TransactionDaoProxyTest {

    private static final LocalDate START = LocalDate.now().minusDays(30);
    private static final LocalDate END = LocalDate.now();
    private static final Long USER_ID = 1L;
    private static final TransactionContext INCOME_CONTEXT = TransactionContext.builder()
        .withTransactionType(TransactionType.INCOME)
        .withUserId(USER_ID)
        .build();
    private static final TransactionContext OUTCOME_CONTEXT = TransactionContext.builder()
        .withTransactionType(TransactionType.OUTCOME)
        .withUserId(USER_ID)
        .build();
    private static final String TITLE = "Transaction";
    private static final double AMOUNT = 1.0;
    private static final Currency CURRENCY = Currency.EUR;
    private static final LocalDate TRANSACTION_DATE = LocalDate.now().minusDays(15);
    private static final Long ID = 1L;
    private static final String CATEGORY_NAME = "Category";

    private TransactionDaoProxy underTest;

    private IMocksControl control;
    private IncomeDao incomeDao;
    private OutcomeDao outcomeDao;

    @BeforeClass
    public void setup() {
        control = EasyMock.createControl();
        incomeDao = control.createMock(IncomeDao.class);
        outcomeDao = control.createMock(OutcomeDao.class);
        underTest = new TransactionDaoProxy(incomeDao, outcomeDao);
    }

    @BeforeMethod
    public void beforeMethod() {
        control.reset();
    }

    @AfterMethod
    public void afterMethod() {
        control.reset();
    }

    @Test
    public void testGetTransactionListWhenTypeIsIncome() {
        // GIVEN
        List<Transaction> expectedList = List.of(createExampleIncome());
        EasyMock.expect(incomeDao.findAll(START, END, USER_ID)).andReturn(expectedList);
        control.replay();
        // WHEN
        List<Transaction> result = underTest.getTransactionList(START, END, INCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, expectedList);
    }

    @Test
    public void testGetTransactionListWhenTypeIsOutcome() {
        // GIVEN
        List<Transaction> expectedList = List.of(createExampleOutcome());
        EasyMock.expect(outcomeDao.findAll(START, END, USER_ID)).andReturn(expectedList);
        control.replay();
        // WHEN
        List<Transaction> result = underTest.getTransactionList(START, END, OUTCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, expectedList);
    }

    @Test
    public void testGetTransactionListBothTypes() {
        // GIVEN
        List<Transaction> expectedList = List.of(createExampleIncome(), createExampleOutcome());
        List<Transaction> incomeList = new ArrayList<>();
        incomeList.add(createExampleIncome());
        EasyMock.expect(incomeDao.findAll(START, END, USER_ID)).andReturn(incomeList);
        EasyMock.expect(outcomeDao.findAll(START, END, USER_ID)).andReturn(List.of(createExampleOutcome()));
        control.replay();
        // WHEN
        List<Transaction> result = underTest.getTransactionListBothTypes(START, END, USER_ID);
        // THEN
        Assert.assertEquals(result, expectedList);
    }

    @Test
    public void testFindByIdWhenContextIsIncome() {
        // GIVEN
        Optional<Transaction> transaction = Optional.ofNullable(createExampleIncome());
        EasyMock.expect(incomeDao.findById(ID, USER_ID)).andReturn(transaction);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.findById(ID, INCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, transaction);
    }

    @Test
    public void testFindByIdWhenContextIsOutcome() {
        // GIVEN
        Optional<Transaction> transaction = Optional.ofNullable(createExampleOutcome());
        EasyMock.expect(outcomeDao.findById(ID, USER_ID)).andReturn(transaction);
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.findById(ID, OUTCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, transaction);
    }

    @Test
    public void testFindByTitleWhenContextIsIncome() {
        // GIVEN
        List<Transaction> transaction = List.of(createExampleIncome());
        EasyMock.expect(incomeDao.findByTitle(TITLE, USER_ID)).andReturn(transaction);
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findByTitle(TITLE, INCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, transaction);
    }

    @Test
    public void testFindByTitleWhenContextIsOutcome() {
        // GIVEN
        List<Transaction> transaction = List.of(createExampleOutcome());
        EasyMock.expect(outcomeDao.findByTitle(TITLE, USER_ID)).andReturn(transaction);
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findByTitle(TITLE, OUTCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, transaction);
    }

    @Test
    public void testSaveWhenContextIsIncome() {
        // GIVEN
        Transaction transaction = createExampleIncome();
        EasyMock.expect(incomeDao.save(transaction, USER_ID)).andReturn(transaction);
        control.replay();
        // WHEN
        Transaction result = underTest.save(transaction, INCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, transaction);
    }

    @Test
    public void testSaveWhenContextIsOutcome() {
        // GIVEN
        Transaction transaction = createExampleOutcome();
        EasyMock.expect(outcomeDao.save(transaction, USER_ID)).andReturn(transaction);
        control.replay();
        // WHEN
        Transaction result = underTest.save(transaction, OUTCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, transaction);
    }

    @Test
    public void testUpdateWhenContextIsIncome() {
        // GIVEN
        Transaction transaction = createExampleIncome();
        EasyMock.expect(incomeDao.update(transaction, USER_ID)).andReturn(transaction);
        control.replay();
        // WHEN
        Transaction result = underTest.update(transaction, INCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, transaction);
    }

    @Test
    public void testUpdateWhenContextIsOutcome() {
        // GIVEN
        Transaction transaction = createExampleOutcome();
        EasyMock.expect(outcomeDao.update(transaction, USER_ID)).andReturn(transaction);
        control.replay();
        // WHEN
        Transaction result = underTest.update(transaction, OUTCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, transaction);
    }

    @Test
    public void testDeleteWhenContextIsIncome() {
        // GIVEN
        Transaction transaction = createExampleIncome();
        EasyMock.expect(incomeDao.delete(transaction, USER_ID)).andReturn(transaction);
        control.replay();
        // WHEN
        Transaction result = underTest.delete(transaction, INCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, transaction);
    }

    @Test
    public void testDeleteWhenContextIsOutcome() {
        // GIVEN
        Transaction transaction = createExampleOutcome();
        EasyMock.expect(outcomeDao.delete(transaction, USER_ID)).andReturn(transaction);
        control.replay();
        // WHEN
        Transaction result = underTest.delete(transaction, OUTCOME_CONTEXT);
        // THEN
        Assert.assertEquals(result, transaction);
    }

    private Transaction createExampleIncome() {
        return createExampleTransactionBuilder()
            .withMainCategory(createExampleIncomeMainCategory())
            .withTransactionType(TransactionType.INCOME)
            .build();
    }

    private Transaction createExampleOutcome() {
        return createExampleTransactionBuilder()
            .withMainCategory(createExampleOutcomeMainCategory())
            .withTransactionType(TransactionType.OUTCOME)
            .build();
    }

    private Transaction.Builder createExampleTransactionBuilder() {
        return Transaction.builder()
            .withId(ID)
            .withTitle(TITLE)
            .withAmount(AMOUNT)
            .withCurrency(CURRENCY)
            .withDate(TRANSACTION_DATE);
    }

    private MainCategory createExampleIncomeMainCategory() {
        return createExampleMainCategoryBuilder()
            .withTransactionType(TransactionType.INCOME)
            .build();
    }

    private MainCategory createExampleOutcomeMainCategory() {
        return createExampleMainCategoryBuilder()
            .withTransactionType(TransactionType.OUTCOME)
            .build();
    }

    private MainCategory.Builder createExampleMainCategoryBuilder() {
        return MainCategory.builder()
            .withId(ID)
            .withName(CATEGORY_NAME)
            .withSubCategorySet(Set.of());
    }

}
