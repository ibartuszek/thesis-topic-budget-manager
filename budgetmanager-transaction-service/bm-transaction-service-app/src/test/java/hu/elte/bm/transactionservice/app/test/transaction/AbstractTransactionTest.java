package hu.elte.bm.transactionservice.app.test.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.time.LocalDate;

import org.testng.annotations.DataProvider;

import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.Coordinate;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.transaction.TransactionRequestContext;

public abstract class AbstractTransactionTest extends AbstractTransactionServiceApplicationTest {

    static final Long EXPECTED_ID = 10L;
    static final Long EXPECTED_OUTCOME_ID = 1L;
    static final Long RESERVED_ID = 5L;
    static final Long INVALID_ID = 99L;
    static final Long LOCKED_ID = 1L;
    static final String EXPECTED_TITLE = "title";
    static final String RESERVED_TITLE = "income 1";
    static final String RESERVED_TITLE_2 = "income 2";
    static final Double EXPECTED_AMOUNT = 100.0d;
    static final Currency EXPECTED_CURRENCY = Currency.EUR;
    static final LocalDate EXPECTED_DATE = LocalDate.now();
    static final LocalDate RESERVED_DATE = LocalDate.now().minusDays(5L);
    static final LocalDate EXPECTED_END_DATE = LocalDate.now().plusDays(1L);
    static final String EXPECTED_DESCRIPTION = "description";
    static final Long EXISTING_MAIN_CATEGORY_ID_2 = 2L;
    static final String EXISTING_MAIN_CATEGORY_NAME_2 = "main category 2";
    static final String THE_TRANSACTION_HAS_BEEN_SAVED = "The transaction has been saved.";
    static final Long EXISTING_MAIN_CATEGORY_ID_1 = 1L;
    static final Long EXISTING_OUTCOME_MAIN_CATEGORY_ID_1 = 4L;
    static final Double LATITUDE = 1.0;
    static final Double LONGITUDE = -1.0;
    static final Coordinate COORDINATE = Coordinate.builder()
        .withLatitude(LATITUDE)
        .withLongitude(LONGITUDE)
        .build();
    static final Long PICTURE_ID = 1L;

    private static final String EMPTY_STRING = "";
    private static final String TOO_LONG_TITLE = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";
    private static final double ZERO = 0.0d;
    private static final double RESERVED_AMOUNT = 1000.0d;
    private static final LocalDate BEFORE_THE_DEADLINE_DATE = LocalDate.now().minusDays(15L);
    private static final LocalDate FIRST_DATE_OF_THE_PERIOD = LocalDate.now().minusDays(9L);
    private static final String TOO_LONG_DESCRIPTION = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";
    private static final String EXISTING_MAIN_CATEGORY_NAME_1 = "main category 1";
    private static final long EXISTING_SUB_CATEGORY_ID_1 = 1L;
    private static final long EXISTING_SUB_CATEGORY_ID_2 = 2L;
    private static final long EXISTING_OUTCOME_SUB_CATEGORY_ID_1 = 4L;
    private static final long EXISTING_OUTCOME_SUB_CATEGORY_ID_2 = 5L;
    private static final String EXISTING_SUB_CATEGORY_NAME_1 = "supplementary category 1";
    private static final String EXISTING_SUB_CATEGORY_NAME_2 = "supplementary category 2";
    private static final boolean MONTHLY = true;
    private static final boolean NOT_MONTHLY = false;

    @DataProvider
    public Object[][] dataForTransactionModelValidation() {
        MainCategory mainCategory = createDefaultMainCategory();
        MainCategory mainCategoryWithNullId = createMainCategory(null, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        MainCategory mainCategoryWithInvalidSubCategory = createMainCategory(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategory subCategoryWithNullId = createSubCategory(null, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        mainCategoryWithInvalidSubCategory.getSubCategorySet().add(subCategoryWithNullId);

        Transaction.Builder withNullTitle = createTransactionBuilderWithDefaultValues(mainCategory)
            .withTitle(null);
        Transaction.Builder withEmptyTitle = createTransactionBuilderWithDefaultValues(mainCategory)
            .withTitle(EMPTY_STRING);
        Transaction.Builder withTooLongTitle = createTransactionBuilderWithDefaultValues(mainCategory)
            .withTitle(TOO_LONG_TITLE);

        Transaction.Builder withNotPositiveAmount = createTransactionBuilderWithDefaultValues(mainCategory)
            .withAmount(ZERO);

        Transaction.Builder withNullCurrency = createTransactionBuilderWithDefaultValues(mainCategory)
            .withCurrency(null);

        Transaction.Builder witNullType = createTransactionBuilderWithDefaultValues(mainCategory)
            .withTransactionType(null);

        Transaction.Builder witNullMainCategory = createTransactionBuilderWithDefaultValues(null);
        Transaction.Builder witNullMainCategoryId = createTransactionBuilderWithDefaultValues(mainCategoryWithNullId);
        Transaction.Builder witNullMainCategoryWithInvalidSubCategory = createTransactionBuilderWithDefaultValues(mainCategoryWithInvalidSubCategory);

        Transaction.Builder witInvalidSubCategory = createTransactionBuilderWithDefaultValues(mainCategory)
            .withSubCategory(subCategoryWithNullId);

        Transaction.Builder witNullDate = createTransactionBuilderWithDefaultValues(mainCategory)
            .withDate(null);
        Transaction.Builder witDateBeforeTheDeadLine = createTransactionBuilderWithDefaultValues(mainCategory)
            .withDate(BEFORE_THE_DEADLINE_DATE);

        Transaction.Builder witEndDateBeforeDate = createTransactionBuilderWithDefaultValues(mainCategory)
            .withMonthly(MONTHLY)
            .withEndDate(EXPECTED_DATE);

        Transaction.Builder witEndDateButNotMonthly = createTransactionBuilderWithDefaultValues(mainCategory)
            .withMonthly(NOT_MONTHLY)
            .withEndDate(FIRST_DATE_OF_THE_PERIOD);

        Transaction.Builder withEmptyDescription = createTransactionBuilderWithDefaultValues(mainCategory)
            .withDescription(EMPTY_STRING);
        Transaction.Builder withTooLongDescription = createTransactionBuilderWithDefaultValues(mainCategory)
            .withDescription(TOO_LONG_DESCRIPTION);

        return new Object[][] {
            { withNullTitle, "Title cannot be empty!" },
            { withEmptyTitle, "Title cannot be empty!" },
            { withTooLongTitle, "Title must be shorter than 50 characters!" },
            { withNotPositiveAmount, "Amount must be positive!" },
            { withNullCurrency, "Currency cannot be null!" },
            { witNullType, "Type cannot be null!" },
            { witNullMainCategory, "Main category cannot be null!" },
            { witNullMainCategoryId, "The Id of mainCategory cannot be null!" },
            { witNullMainCategoryWithInvalidSubCategory, "The Id of subCategory cannot be null!" },
            { witInvalidSubCategory, "The Id of subCategory cannot be null!" },
            { witNullDate, "Date cannot be null!" },
            { witDateBeforeTheDeadLine, "The date of transaction cannot be before the beginning of the period!" },
            { witEndDateBeforeDate, "End of the periodical transaction cannot be before its start!" },
            { witEndDateButNotMonthly, "Only periodical transactions have end date!" },
            { withEmptyDescription, "Description must be shorter than 100 characters!" },
            { withTooLongDescription, "Description must be shorter than 100 characters!" }

        };
    }

    @DataProvider
    public Object[][] dataForContextValidation() {
        TransactionRequestContext contextWithoutUserId = new TransactionRequestContext();
        contextWithoutUserId.setUserId(null);
        contextWithoutUserId.setTransactionType(INCOME);
        TransactionRequestContext contextWithoutTransactionType = new TransactionRequestContext();
        contextWithoutTransactionType.setUserId(USER_ID);
        contextWithoutTransactionType.setTransactionType(null);

        return new Object[][] {
            { contextWithoutUserId, "User id cannot be null!" },
            { contextWithoutTransactionType, "Category type cannot be null!" }
        };
    }

    TransactionRequestContext createContext(final TransactionType type, final Transaction transaction) {
        TransactionRequestContext context = new TransactionRequestContext();
        context.setTransactionType(type);
        context.setTransaction(transaction);
        context.setUserId(USER_ID);
        return context;
    }

    Transaction.Builder createTransactionBuilderWithDefaultValues(final MainCategory mainCategory) {
        return Transaction.builder()
            .withId(EXPECTED_ID)
            .withTitle(EXPECTED_TITLE)
            .withTransactionType(INCOME)
            .withAmount(EXPECTED_AMOUNT)
            .withCurrency(EXPECTED_CURRENCY)
            .withDate(EXPECTED_DATE)
            .withMainCategory(mainCategory);
    }

    Transaction.Builder createTransactionBuilderWithValuesForUpdate(final MainCategory mainCategory) {
        return Transaction.builder()
            .withId(RESERVED_ID)
            .withTitle(RESERVED_TITLE)
            .withTransactionType(INCOME)
            .withAmount(RESERVED_AMOUNT)
            .withCurrency(EXPECTED_CURRENCY)
            .withDate(RESERVED_DATE)
            .withMainCategory(mainCategory);
    }

    MainCategory createDefaultMainCategory() {
        MainCategory mainCategory = createMainCategory(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_SUB_CATEGORY_NAME_1, INCOME));
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_SUB_CATEGORY_NAME_2, INCOME));
        return mainCategory;
    }

    MainCategory createDefaultMainCategoryForOutcome() {
        MainCategory mainCategory = createMainCategory(EXISTING_OUTCOME_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, OUTCOME);
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_OUTCOME_SUB_CATEGORY_ID_1, EXISTING_SUB_CATEGORY_NAME_1, OUTCOME));
        mainCategory.getSubCategorySet().add(createSubCategory(EXISTING_OUTCOME_SUB_CATEGORY_ID_2, EXISTING_SUB_CATEGORY_NAME_2, OUTCOME));
        return mainCategory;
    }

}
