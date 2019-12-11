package hu.elte.bm.calculationservice.service.statistics.transactionprovider;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.elte.bm.calculationservice.service.statistics.AbstractCalculatorTest;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

class MonthlyTransactionProviderTest extends AbstractCalculatorTest {

    private static final LocalDate START = LocalDate.of(2019, 1, 1);
    private static final LocalDate END_ONE_MONTHS_AFTER = START.plusMonths(1);
    private static final LocalDate END_TWO_MONTHS_AFTER = START.plusMonths(2);
    private static final LocalDate BEFORE_START = START.minusDays(5);
    private static final LocalDate MODIFIED_BEFORE_START = BEFORE_START.plusMonths(1);
    private static final LocalDate OTHER_MODIFIED_BEFORE_START = BEFORE_START.plusMonths(2);
    private static final LocalDate AFTER_START = START.plusDays(5);
    private static final LocalDate MODIFIED_AFTER_START = AFTER_START.plusMonths(1);
    private static final LocalDate BEFORE_END_ONE = END_ONE_MONTHS_AFTER.minusDays(5);
    private static final LocalDate BEFORE_END_TWO = END_TWO_MONTHS_AFTER.minusDays(5);
    private static final LocalDate AFTER_END_ONE = END_ONE_MONTHS_AFTER.plusDays(5);
    private static final LocalDate AFTER_END_TWO = END_TWO_MONTHS_AFTER.plusDays(5);
    private static final Long ID_1 = 3L;
    private static final Long ID_2 = 4L;
    private static final Long ID_3 = 5L;
    private static final Long ID_4 = 6L;
    private static final boolean MONTHLY = true;
    private static final boolean NOT_MONTHLY = false;

    private MonthlyTransactionProvider underTest = new MonthlyTransactionProvider();

    @Test
    public void testProvideWhenThereIsOneNotMonthlyTransaction() {
        // GIVEN
        Transaction defaultTransaction = createExampleMonthlyTransactionBuilder(DEFAULT_TRANSACTION_DATE)
                .withMonthly(NOT_MONTHLY)
                .build();
        List<Transaction> transactionList = List.of(defaultTransaction);
        List<Transaction> expected = Collections.emptyList();

        // THEN
        var result = underTest.provide(transactionList, START, END_TWO_MONTHS_AFTER);

        // WHEN
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testProvideWhenThereIsOneTransactionBeforeStartWithoutEndDate() {
        // GIVEN
        Transaction.Builder monthlyBeforeStart = createExampleMonthlyTransactionBuilder(BEFORE_START)
                .withId(ID_1);
        List<Transaction> transactionList = List.of(monthlyBeforeStart.build());
        List<Transaction> expected = List.of(
                monthlyBeforeStart.withDate(MODIFIED_BEFORE_START).build(),
                monthlyBeforeStart.withDate(OTHER_MODIFIED_BEFORE_START).build());

        // THEN
        var result = underTest.provide(transactionList, START, END_TWO_MONTHS_AFTER);

        // WHEN
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testProvideWhenTransactionsDateBeforeStartAndHasEndDate() {
        // GIVEN
        Transaction.Builder monthlyBeforeStartAfterEndOne = createExampleMonthlyTransactionBuilder(BEFORE_START)
                .withId(ID_1)
                .withEndDate(AFTER_END_ONE);
        Transaction.Builder monthlyBeforeStartAfterEndTwo = createExampleMonthlyTransactionBuilder(BEFORE_START)
                .withId(ID_2)
                .withEndDate(AFTER_END_TWO);
        Transaction.Builder monthlyBeforeStartBeforeEndOne = createExampleMonthlyTransactionBuilder(BEFORE_START)
                .withId(ID_3)
                .withEndDate(BEFORE_END_ONE);
        Transaction.Builder monthlyBeforeStartBeforeEnd = createExampleMonthlyTransactionBuilder(BEFORE_START)
                .withId(ID_4)
                .withEndDate(BEFORE_END_TWO);

        List<Transaction> transactionList = List.of(monthlyBeforeStartAfterEndOne.build(), monthlyBeforeStartAfterEndTwo.build(),
                monthlyBeforeStartBeforeEndOne.build(), monthlyBeforeStartBeforeEnd.build());
        List<Transaction> expected = List.of(
                monthlyBeforeStartAfterEndOne.withDate(MODIFIED_BEFORE_START).build(),
                monthlyBeforeStartAfterEndTwo.withDate(MODIFIED_BEFORE_START).build(),
                monthlyBeforeStartAfterEndTwo.withDate(OTHER_MODIFIED_BEFORE_START).build(),
                monthlyBeforeStartBeforeEndOne.withDate(MODIFIED_BEFORE_START).build()
        );

        // THEN
        var result = underTest.provide(transactionList, START, END_TWO_MONTHS_AFTER);

        // WHEN
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testProvideWhenThereIsOneTransactionAfterStartWithoutEndDate() {
        // GIVEN
        Transaction.Builder monthlyBeforeStart = createExampleMonthlyTransactionBuilder(AFTER_START)
                .withId(ID_1);
        List<Transaction> transactionList = List.of(monthlyBeforeStart.build());
        List<Transaction> expected = List.of(
                monthlyBeforeStart.withDate(AFTER_START).build(),
                monthlyBeforeStart.withDate(MODIFIED_AFTER_START).build());

        // THEN
        var result = underTest.provide(transactionList, START, END_TWO_MONTHS_AFTER);

        // WHEN
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testProvideWhenTransactionsDateAfterStartAndHasEndDate() {
        // GIVEN
        Transaction.Builder monthlyBeforeStartAfterEndOne = createExampleMonthlyTransactionBuilder(BEFORE_START)
                .withId(ID_1)
                .withEndDate(AFTER_END_ONE);
        Transaction.Builder monthlyBeforeStartAfterEndTwo = createExampleMonthlyTransactionBuilder(BEFORE_START)
                .withId(ID_2)
                .withEndDate(AFTER_END_TWO);
        Transaction.Builder monthlyBeforeStartBeforeEndOne = createExampleMonthlyTransactionBuilder(BEFORE_START)
                .withId(ID_3)
                .withEndDate(BEFORE_END_ONE);
        Transaction.Builder monthlyBeforeStartBeforeEnd = createExampleMonthlyTransactionBuilder(BEFORE_START)
                .withId(ID_4)
                .withEndDate(BEFORE_END_TWO);

        List<Transaction> transactionList = List.of(monthlyBeforeStartAfterEndOne.build(), monthlyBeforeStartAfterEndTwo.build(),
                monthlyBeforeStartBeforeEndOne.build(), monthlyBeforeStartBeforeEnd.build());
        List<Transaction> expected = List.of(
                monthlyBeforeStartAfterEndOne.withDate(MODIFIED_BEFORE_START).build(),
                monthlyBeforeStartAfterEndTwo.withDate(MODIFIED_BEFORE_START).build(),
                monthlyBeforeStartAfterEndTwo.withDate(OTHER_MODIFIED_BEFORE_START).build(),
                monthlyBeforeStartBeforeEndOne.withDate(MODIFIED_BEFORE_START).build()
        );

        // THEN
        var result = underTest.provide(transactionList, START, END_TWO_MONTHS_AFTER);

        // WHEN
        Assertions.assertEquals(expected, result);
    }

    private Transaction.Builder createExampleMonthlyTransactionBuilder(final LocalDate date) {
        MainCategory mainCategory = createExampleMainCategoryBuilder(TransactionType.OUTCOME).build();
        return createExampleTransactionBuilder(TransactionType.OUTCOME, DEFAULT_CURRENCY)
                .withDate(date)
                .withMonthly(MONTHLY)
                .withMainCategory(mainCategory);
    }

}
