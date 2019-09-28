package hu.elte.bm.transactionservice.service.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.database.TransactionDaoProxy;

public class TransactionDateValidatorTest {

    private static final long EXPECTED_ID = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private static final String EXPECTED_TITLE = "title";
    private static final double AMOUNT = 10.0d;
    private static final Currency CURRENCY = Currency.EUR;
    private static final long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "category";
    private static final long DAYS_TO_SUBTRACT = 30L;
    private static final LocalDate EXPECTED_LAST_DATE = LocalDate.now().minusDays(DAYS_TO_SUBTRACT - 5);
    private static final LocalDate AFTER_EXPECTED_LAST_DATE = LocalDate.now().minusDays(DAYS_TO_SUBTRACT - 10);
    private static final Long USER_ID = 1L;
    private static final LocalDate START = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
    private static final LocalDate END = LocalDate.now();
    private static final LocalDate TRANSACTION_DATE = START.plusDays(10);
    private static final LocalDate TRANSACTION_END_DATE = TRANSACTION_DATE.minusDays(5);
    private static final boolean NOT_MONTHLY = false;
    private static final boolean MONTHLY = true;

    private TransactionDateValidator underTest;
    private IMocksControl control;
    private TransactionDaoProxy transactionDaoProxy;

    @BeforeClass
    public void setup() {
        control = EasyMock.createControl();
        transactionDaoProxy = control.createMock(TransactionDaoProxy.class);
        underTest = new TransactionDateValidator(transactionDaoProxy);
        ReflectionTestUtils.setField(underTest, "daysToSubtract", DAYS_TO_SUBTRACT);
    }

    @BeforeMethod
    public void reset() {
        control.reset();
    }

    @AfterMethod
    public void verify() {
        control.verify();
    }

    @Test
    public void testGetTheFirstDateOfTheNewPeriodWhenRepositoryReturnsWithEmptyList() {
        // GIVEN
        TransactionContext context = createTransactionContext();
        EasyMock.expect(transactionDaoProxy.getTransactionListBothTypes(START, END, USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        LocalDate result = underTest.getTheFirstDateOfTheNewPeriod(context);
        // THEN
        Assert.assertEquals(result, START);
    }

    @Test
    public void testGetTheFirstDateOfTheNewPeriodWhenThereCannotBeFoundALockedDate() {
        // GIVEN
        TransactionContext context = createTransactionContext();
        List<Transaction> transactionList = List.of(
            createExampleTransactionBuilder().withDate(LocalDate.now()).build(),
            createExampleTransactionBuilder().withDate(LocalDate.now().minusDays(DAYS_TO_SUBTRACT)).build()
        );
        EasyMock.expect(transactionDaoProxy.getTransactionListBothTypes(START, END, USER_ID)).andReturn(transactionList);
        control.replay();
        // WHEN
        LocalDate result = underTest.getTheFirstDateOfTheNewPeriod(context);
        // THEN
        Assert.assertEquals(result, START);
    }

    @Test
    public void testGetTheFirstDateOfTheNewPeriodWhenThereCanBeFoundALockedDate() {
        // GIVEN
        TransactionContext context = createTransactionContext();
        List<Transaction> transactionList = createExampleListForEndOfLastPeriodWithCalls();
        EasyMock.expect(transactionDaoProxy.getTransactionListBothTypes(START, END, USER_ID)).andReturn(transactionList);
        control.replay();
        // WHEN
        LocalDate result = underTest.getTheFirstDateOfTheNewPeriod(context);
        // THEN
        Assert.assertEquals(result, EXPECTED_LAST_DATE.plusDays(1));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateWhenDateIsBeforeTheFirstPossibleDate() {
        // GIVEN
        TransactionContext context = createTransactionContext();
        LocalDate transactionDate = START.minusDays(1);
        Transaction transaction = createExampleTransactionBuilder()
            .withDate(transactionDate)
            .build();
        EasyMock.expect(transactionDaoProxy.getTransactionListBothTypes(START, END, USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        underTest.validate(transaction, context);
        // THEN
    }

    @Test
    public void testValidateWhenDateIsValidAndEndDateIsNull() {
        // GIVEN
        TransactionContext context = createTransactionContext();
        LocalDate transactionDate = END.minusDays(1);
        Transaction transaction = createExampleTransactionBuilder()
            .withDate(transactionDate)
            .withEndDate(null)
            .build();
        EasyMock.expect(transactionDaoProxy.getTransactionListBothTypes(START, END, USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        underTest.validate(transaction, context);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateWhenTransactionHasEndDateButNotMonthly() {
        // GIVEN
        TransactionContext context = createTransactionContext();
        Transaction transaction = createExampleTransactionBuilder()
            .withDate(TRANSACTION_DATE)
            .withEndDate(TRANSACTION_END_DATE)
            .withMonthly(NOT_MONTHLY)
            .build();
        EasyMock.expect(transactionDaoProxy.getTransactionListBothTypes(START, END, USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        underTest.validate(transaction, context);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateWhenTransactionEndDateAfterItsDate() {
        // GIVEN
        TransactionContext context = createTransactionContext();
        LocalDate transactionEndDate = TRANSACTION_DATE.plusDays(1);
        Transaction transaction = createExampleTransactionBuilder()
            .withDate(TRANSACTION_DATE)
            .withEndDate(transactionEndDate)
            .withMonthly(MONTHLY)
            .build();
        EasyMock.expect(transactionDaoProxy.getTransactionListBothTypes(START, END, USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        underTest.validate(transaction, context);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateWhenTransactionDateAndEndDateIsTheSame() {
        // GIVEN
        TransactionContext context = createTransactionContext();
        Transaction transaction = createExampleTransactionBuilder()
            .withDate(TRANSACTION_DATE)
            .withEndDate(TRANSACTION_DATE)
            .withMonthly(MONTHLY)
            .build();
        EasyMock.expect(transactionDaoProxy.getTransactionListBothTypes(START, END, USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        underTest.validate(transaction, context);
        // THEN
    }

    @Test
    public void testValidateWhenTransactionHasEndDate() {
        // GIVEN
        TransactionContext context = createTransactionContext();
        Transaction transaction = createExampleTransactionBuilder()
            .withDate(TRANSACTION_DATE)
            .withEndDate(TRANSACTION_END_DATE)
            .withMonthly(MONTHLY)
            .build();
        EasyMock.expect(transactionDaoProxy.getTransactionListBothTypes(START, END, USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        underTest.validate(transaction, context);
        // THEN
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

    private TransactionContext createTransactionContext() {
        return TransactionContext.builder()
            .withTransactionType(TransactionType.INCOME)
            .withUserId(TransactionDateValidatorTest.USER_ID)
            .build();
    }

}
