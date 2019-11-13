package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetailsElement;
import hu.elte.bm.calculationservice.schema.ChartType;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.transactionservice.Currency;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

class AbstractBudgetCalculatorTest {

    protected static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.OUTCOME;
    protected static final Currency DEFAULT_CURRENCY = Currency.EUR;
    protected static final Double ZERO = 0.0d;
    protected static final double EXPECTED_SUM_AMOUNT = 55.0d;
    protected static final String SAVINGS_LABEL = "Savings";
    protected static final String TOTAL_INCOMES_LABEL = "Total incomes";
    protected static final String TOTAL_EXPENSES_LABEL = "Total expenses";
    protected static final double DEFAULT_TRANSACTION_AMOUNT = 10.0d;
    protected static final double OTHER_TRANSACTION_AMOUNT = 20.0d;
    protected static final double ANOTHER_TRANSACTION_AMOUNT = 25.0d;
    protected static final String DEFAULT_MAIN_CATEGORY_NAME = "Main category";
    protected static final String OTHER_MAIN_CATEGORY_NAME = "Other main category";
    protected static final String ANOTHER_MAIN_CATEGORY_NAME = "Another main category";
    protected static final String DEFAULT_SUB_CATEGORY_NAME = "Supplementary category";
    protected static final String OTHER_SUB_CATEGORY_NAME = "Other supplementary category";

    private static final Long DEFAULT_TRANSACTION_ID = 1L;
    private static final Long OTHER_TRANSACTION_ID = 2L;
    private static final Long ANOTHER_TRANSACTION_ID = 3L;
    private static final String DEFAULT_TRANSACTION_TITLE = "Transation";
    private static final String OTHER_TRANSACTION_TITLE = "Other transation";
    private static final String ANOTHER_TRANSACTION_TITLE = "Another transation";
    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.now().minusDays(3);
    private static final LocalDate OTHER_TRANSACTION_DATE = LocalDate.now().minusDays(2);
    private static final LocalDate ANOTHER_TRANSACTION_DATE = LocalDate.now().minusDays(1);

    private static final Long DEFAULT_MAIN_CATEGORY_ID = 1L;
    private static final Long OTHER_MAIN_CATEGORY_ID = 2L;
    private static final Long ANOTHER_MAIN_CATEGORY_ID = 3L;
    private static final Long DEFAULT_SUB_CATEGORY_ID = 1L;
    private static final Long OTHER_SUB_CATEGORY_ID = 2L;

    private static final Long DEFAULT_SCHEMA_ID = 1L;
    private static final String DEFAULT_SCHEMA_TITLE = "Schema";
    private static final ChartType DEFAULT_SCHEMA_CHART_TYPE = ChartType.BAR;

    protected List<Transaction> createExampleTransactionList(final TransactionType type, final Currency currency) {
        SubCategory defaultSubCategory = createExampleSubCategoryBuilder(type)
            .build();
        SubCategory otherSubCategory = createExampleSubCategoryBuilder(type)
            .withId(OTHER_SUB_CATEGORY_ID)
            .withName(OTHER_SUB_CATEGORY_NAME)
            .build();
        MainCategory defaultMainCategory = createExampleMainCategoryBuilder(type)
            .build();
        defaultMainCategory.getSubCategorySet().add(defaultSubCategory);
        defaultMainCategory.getSubCategorySet().add(otherSubCategory);
        MainCategory otherMainCategory = createExampleMainCategoryBuilder(type)
            .withId(OTHER_MAIN_CATEGORY_ID)
            .withName(OTHER_MAIN_CATEGORY_NAME)
            .build();
        otherMainCategory.getSubCategorySet().add(otherSubCategory);
        otherMainCategory.getSubCategorySet().add(defaultSubCategory);
        MainCategory anotherMainCategory = createExampleMainCategoryBuilder(type)
            .withId(ANOTHER_MAIN_CATEGORY_ID)
            .withName(ANOTHER_MAIN_CATEGORY_NAME)
            .build();

        Transaction defaultTransaction = createExampleTransactionBuilder(type, currency)
            .withMainCategory(defaultMainCategory)
            .build();
        Transaction otherTransaction = createExampleTransactionBuilder(type, currency)
            .withId(OTHER_TRANSACTION_ID)
            .withTitle(OTHER_TRANSACTION_TITLE)
            .withAmount(OTHER_TRANSACTION_AMOUNT)
            .withDate(OTHER_TRANSACTION_DATE)
            .withMainCategory(defaultMainCategory)
            .withSubCategory(defaultSubCategory)
            .build();
        Transaction anotherTransaction = createExampleTransactionBuilder(type, currency)
            .withId(ANOTHER_TRANSACTION_ID)
            .withTitle(ANOTHER_TRANSACTION_TITLE)
            .withAmount(ANOTHER_TRANSACTION_AMOUNT)
            .withDate(ANOTHER_TRANSACTION_DATE)
            .withMainCategory(anotherMainCategory)
            .build();
        return List.of(defaultTransaction, otherTransaction, anotherTransaction);
    }

    protected Transaction.Builder createExampleTransactionBuilder(final TransactionType type, final Currency currency) {
        return Transaction.builder()
            .withId(DEFAULT_TRANSACTION_ID)
            .withTitle(DEFAULT_TRANSACTION_TITLE)
            .withAmount(DEFAULT_TRANSACTION_AMOUNT)
            .withCurrency(currency)
            .withTransactionType(type)
            .withDate(DEFAULT_TRANSACTION_DATE);
    }

    protected MainCategory.Builder createExampleMainCategoryBuilder(final TransactionType type) {
        return MainCategory.builder()
            .withId(DEFAULT_MAIN_CATEGORY_ID)
            .withName(DEFAULT_MAIN_CATEGORY_NAME)
            .withTransactionType(type)
            .withSubCategorySet(new HashSet<>());
    }

    protected SubCategory.Builder createExampleSubCategoryBuilder(final TransactionType type) {
        return SubCategory.builder()
            .withId(DEFAULT_SUB_CATEGORY_ID)
            .withName(DEFAULT_SUB_CATEGORY_NAME)
            .withTransactionType(type);
    }

    protected BudgetDetailsElement createBudgetDetailsElement(final double amount, final String label) {
        return BudgetDetailsElement.builder()
            .withAmount(amount)
            .withLabel(label)
            .build();
    }

    protected StatisticsSchema.Builder createExampleSchemaBuilder(final StatisticsType type, final Currency currency) {
        return StatisticsSchema.builder()
            .withId(DEFAULT_SCHEMA_ID)
            .withTitle(DEFAULT_SCHEMA_TITLE)
            .withType(type)
            .withChartType(DEFAULT_SCHEMA_CHART_TYPE)
            .withCurrency(currency);
    }

    protected List<Transaction> createExampleOutcomeList() {
        List<Transaction> outcomeList = new ArrayList<>();
        List<Transaction> fullList = createExampleTransactionList(TransactionType.OUTCOME, DEFAULT_CURRENCY);
        outcomeList.add(fullList.get(0));
        outcomeList.add(fullList.get(1));
        return outcomeList;
    }

}
