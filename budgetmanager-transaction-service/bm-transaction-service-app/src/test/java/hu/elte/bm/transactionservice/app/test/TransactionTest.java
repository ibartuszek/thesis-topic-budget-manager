package hu.elte.bm.transactionservice.app.test;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.DefaultTransactionService;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionException;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class TransactionTest extends AbstractTransactionServiceApplicationTest {

    private static final Long EXPECTED_ID = 9L;
    private static final Long RESERVED_ID = 5L;
    private static final Long INVALID_ID = 99L;
    private static final Long LOCKED_ID = 1L;
    private static final String EXPECTED_TITLE = "title";
    private static final String RESERVED_TITLE = "income 1";
    private static final String RESERVED_TITLE_2 = "income 2";
    private static final double EXPECTED_AMOUNT = 100.0d;
    private static final double RESERVED_AMOUNT = 1000.0d;
    private static final Currency EXPECTED_CURRENCY = Currency.EUR;
    private static final LocalDate EXPECTED_DATE = LocalDate.now();
    private static final LocalDate RESERVED_DATE = LocalDate.now().minusDays(5);
    private static final LocalDate BEFORE_THE_DEADLINE_DATE = LocalDate.now().minusDays(15);
    private static final long EXISTING_MAIN_CATEGORY_ID_1 = 1L;
    private static final long EXISTING_MAIN_CATEGORY_ID_2 = 2L;
    private static final String EXISTING_MAIN_CATEGORY_NAME_1 = "main category 1";
    private static final String EXISTING_MAIN_CATEGORY_NAME_2 = "main category 2";
    private static final long EXISTING_SUB_CATEGORY_ID_1 = 1L;
    private static final long EXISTING_SUB_CATEGORY_ID_2 = 2L;
    @Autowired
    private DefaultTransactionService transactionService;

    @Test(expectedExceptions = TransactionException.class)
    public void testSaveWhenMainCategoryIdIsNull() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(null, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategory subCategory = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategory.getSubCategorySet().add(subCategory);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        Transaction transaction = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(null)
            .withSubCategory(subCategory)
            .build();
        // WHEN
        Optional<Transaction> result = transactionService.save(transaction);
        // THEN
    }

    @Test(expectedExceptions = TransactionException.class)
    public void testSaveWhenOneOfTheSubCategorySetsSubCategoryIdIsNull() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategory subCategory = createSubCategory(null, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategory.getSubCategorySet().add(subCategory);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        Transaction transaction = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(null)
            .withSubCategory(subCategory)
            .build();
        // WHEN
        transactionService.save(transaction);
        // THEN
    }

    @Test(expectedExceptions = TransactionException.class)
    public void testSaveWhenTheDateIsBeforeTheEndOfLastPeriod() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategory subCategory = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategory.getSubCategorySet().add(subCategory);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        Transaction transaction = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(null)
            .withSubCategory(subCategory)
            .withDate(BEFORE_THE_DEADLINE_DATE)
            .build();
        // WHEN
        transactionService.save(transaction);
        // THEN
    }

    @Test
    public void testSaveWhenThereIsOneWithSameDate() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        Transaction transaction = createTransactionBuilderWithDefaultValues(mainCategory).withId(null).build();
        // WHEN
        Optional<Transaction> result = transactionService.save(transaction);
        // THEN
        Assert.assertEquals(EXPECTED_ID, result.get().getId());
    }

    @Test
    public void testSaveWithoutSubCategory() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        Transaction transaction = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(null)
            .withTitle(RESERVED_TITLE)
            .withDate(RESERVED_DATE)
            .build();
        // WHEN
        Optional<Transaction> result = transactionService.save(transaction);
        // THEN
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSave() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategory subCategory = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategory.getSubCategorySet().add(subCategory);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        Transaction transaction = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(null)
            .withSubCategory(subCategory)
            .build();
        // WHEN
        Optional<Transaction> result = transactionService.save(transaction);
        // THEN
        Assert.assertEquals(EXPECTED_ID, result.get().getId());
    }

    @Test(expectedExceptions = TransactionException.class)
    public void testUpdateWhenTransactionCannotBeFound() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategory subCategory = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategory.getSubCategorySet().add(subCategory);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        Transaction transaction = createTransactionBuilderWithForUpdateValues(mainCategory)
            .withId(INVALID_ID)
            .withTitle(EXPECTED_TITLE)
            .build();
        // WHEN
        Optional<Transaction> result = transactionService.update(transaction);
        // THEN
        Assert.assertEquals(RESERVED_ID, result.get().getId());
    }

    @Test(expectedExceptions = TransactionException.class)
    public void testUpdateWhenTransactionIsLocked() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategory subCategory = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategory.getSubCategorySet().add(subCategory);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        Transaction transaction = createTransactionBuilderWithForUpdateValues(mainCategory)
            .withId(LOCKED_ID)
            .withTitle(EXPECTED_TITLE)
            .build();
        // WHEN
        Optional<Transaction> result = transactionService.update(transaction);
        // THEN
        Assert.assertEquals(RESERVED_ID, result.get().getId());
    }

    @Test(expectedExceptions = TransactionException.class)
    public void testUpdateWhenTransactionTypeChanged() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategory subCategory = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategory.getSubCategorySet().add(subCategory);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        Transaction transaction = createTransactionBuilderWithForUpdateValues(mainCategory)
            .withTitle(EXPECTED_TITLE)
            .withTransactionType(OUTCOME)
            .build();
        // WHEN
        Optional<Transaction> result = transactionService.update(transaction);
        // THEN
        Assert.assertEquals(RESERVED_ID, result.get().getId());
    }

    @Test
    public void testUpdateWhenThereIsOneTransactionWithSameDateAndName() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategory subCategory = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategory.getSubCategorySet().add(subCategory);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        Transaction transaction = createTransactionBuilderWithForUpdateValues(mainCategory)
            .withTitle(RESERVED_TITLE_2)
            .withDate(RESERVED_DATE)
            .build();
        // WHEN
        Optional<Transaction> result = transactionService.update(transaction);
        // THEN
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdate() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategory subCategory = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategory.getSubCategorySet().add(subCategory);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        Transaction transaction = createTransactionBuilderWithForUpdateValues(mainCategory)
            .withTitle(EXPECTED_TITLE)
            .build();
        // WHEN
        Optional<Transaction> result = transactionService.update(transaction);
        // THEN
        Assert.assertEquals(RESERVED_ID, result.get().getId());
    }

    @Test
    public void testDelete() {
        // GIVEN
        MainCategory mainCategory = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategory subCategory = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategory.getSubCategorySet().add(subCategory);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        Transaction transaction = createTransactionBuilderWithForUpdateValues(mainCategory).build();
        // WHEN
        boolean result = transactionService.delete(transaction);
        // THEN
        Assert.assertTrue(result);
    }

    private Transaction.Builder createTransactionBuilderWithDefaultValues(final MainCategory mainCategory) {
        return Transaction.builder()
            .withId(EXPECTED_ID)
            .withTitle(EXPECTED_TITLE)
            .withTransactionType(INCOME)
            .withAmount(EXPECTED_AMOUNT)
            .withCurrency(EXPECTED_CURRENCY)
            .withDate(EXPECTED_DATE)
            .withMainCategory(mainCategory);
    }

    private Transaction.Builder createTransactionBuilderWithForUpdateValues(final MainCategory mainCategory) {
        return Transaction.builder()
            .withId(RESERVED_ID)
            .withTitle(RESERVED_TITLE)
            .withTransactionType(INCOME)
            .withAmount(RESERVED_AMOUNT)
            .withCurrency(EXPECTED_CURRENCY)
            .withDate(RESERVED_DATE)
            .withMainCategory(mainCategory);
    }

    private MainCategory createMainCategoryWithoutSubCategories(final Long id, final String name, final TransactionType type) {
        return MainCategory.builder()
            .withId(id)
            .withName(name)
            .withTransactionType(type)
            .withSubCategorySet(new HashSet<>())
            .build();
    }

    private SubCategory createSubCategory(final Long id, final String name, final TransactionType type) {
        return SubCategory.builder()
            .withId(id)
            .withName(name)
            .withTransactionType(type)
            .build();
    }
}
