package hu.elte.bm.transactionservice.app.test.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.text.MessageFormat;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;

import hu.elte.bm.commonpack.validator.ModelAmountValue;
import hu.elte.bm.commonpack.validator.ModelDateValue;
import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionController;
import hu.elte.bm.transactionservice.web.transaction.TransactionModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelRequestContext;

public abstract class AbstractTransactionTest extends AbstractTransactionServiceApplicationTest {

    static final Long EXPECTED_ID = 10L;
    static final Long RESERVED_ID = 5L;
    static final Long INVALID_ID = 99L;
    static final Long LOCKED_ID = 1L;
    static final String EXPECTED_TITLE = "title";
    static final String RESERVED_TITLE = "income 1";
    static final String RESERVED_TITLE_2 = "income 2";
    static final LocalDate RESERVED_DATE = LocalDate.now().minusDays(5L);
    static final LocalDate EXPECTED_END_DATE = LocalDate.now().plusDays(1L);
    static final String EXPECTED_DESCRIPTION = "description";
    static final long EXISTING_MAIN_CATEGORY_ID_2 = 2L;
    static final String EXISTING_MAIN_CATEGORY_NAME_2 = "main category 2";
    static final String THE_NEW_TRANSACTION_IS_INVALID = "The new transaction is invalid.";
    static final String THE_TRANSACTION_HAS_BEEN_SAVED = "The transaction has been saved.";
    static final String ORIGINAL_TRANSACTION_CANNOT_BE_FOUND_IN_THE_REPOSITORY = "Original transaction cannot be found in the repository!";

    private static final String EMPTY_STRING = "";
    private static final String TOO_LONG_TITLE = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";
    private static final double ZERO = 0.0d;
    private static final double EXPECTED_AMOUNT = 100.0d;
    private static final double RESERVED_AMOUNT = 1000.0d;
    private static final String INVALID_CURRENCY = "EURO";
    private static final Currency EXPECTED_CURRENCY = Currency.EUR;
    private static final String INVALID_TYPE = "INCOME1";
    private static final LocalDate EXPECTED_DATE = LocalDate.now();
    private static final LocalDate BEFORE_THE_DEADLINE_DATE = LocalDate.now().minusDays(15L);
    private static final LocalDate FIRST_DATE_OF_THE_PERIOD = LocalDate.now().minusDays(9L);
    private static final String TOO_LONG_DESCRIPTION = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";
    private static final long EXISTING_MAIN_CATEGORY_ID_1 = 1L;
    private static final String EXISTING_MAIN_CATEGORY_NAME_1 = "main category 1";
    private static final long EXISTING_SUB_CATEGORY_ID_1 = 1L;
    private static final long EXISTING_SUB_CATEGORY_ID_2 = 2L;
    private static final String VALIDATED_FIELD_VALUE_CANNOT_BE_NULL = "Validated field value cannot be null!";
    private static final String THE_ID_OF_SUB_CATEGORY_CANNOT_BE_NULL = "The Id of subCategory cannot be null!";
    private static final String EXISTING_SUB_CATEGORY_NAME_1 = "supplementary category 1";
    private static final String EXISTING_SUB_CATEGORY_NAME_2 = "supplementary category 2";

    @Autowired
    private TransactionController transactionController;

    TransactionController getTransactionController() {
        return transactionController;
    }

    @DataProvider
    public Object[][] dataForTransactionModelValidationOfTitle() {
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel modelWithNullTitle = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTitle(null).build();
        TransactionModel modelWithNullValueTitle = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().build()).build();
        TransactionModel modelWithEmptyTitle = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(EMPTY_STRING).build()).build();
        TransactionModel modelWithTooLongTitle = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(TOO_LONG_TITLE).build()).build();

        return new Object[][] {
            { modelWithNullTitle, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWithNullValueTitle, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWithEmptyTitle, THE_NEW_TRANSACTION_IS_INVALID, "Title cannot be empty!" },
            { modelWithTooLongTitle, THE_NEW_TRANSACTION_IS_INVALID, "Title cannot be longer than 50!" },
        };
    }

    @DataProvider
    public Object[][] dataForTransactionModelValidationOfAmount() {
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();

        TransactionModel modelWithNullAmount = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withAmount(null).build();
        TransactionModel modelWithNullValueAmount = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withAmount(ModelAmountValue.builder().build()).build();
        TransactionModel modelWithNotPositiveAmount = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withAmount(ModelAmountValue.builder().withValue(ZERO).build()).build();

        return new Object[][] {
            { modelWithNullAmount, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWithNullValueAmount, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWithNotPositiveAmount, THE_NEW_TRANSACTION_IS_INVALID, "Amount must be positive number!" },
        };
    }

    @DataProvider
    public Object[][] dataForTransactionModelValidationOfCurrency() {
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel modelWithNullCurrency = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withCurrency(null).build();
        TransactionModel modelWithNullValueCurrency = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withCurrency(ModelStringValue.builder().build()).build();
        TransactionModel modelWithInvalidCurrency = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withCurrency(ModelStringValue.builder().withValue(INVALID_CURRENCY).build()).build();

        return new Object[][] {
            { modelWithNullCurrency, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWithNullValueCurrency, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWithInvalidCurrency, THE_NEW_TRANSACTION_IS_INVALID, "Currency must be one of them: [EUR, USD, HUF]!" },
        };
    }

    @DataProvider
    public Object[][] dataForTransactionModelValidationOfType() {
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel modelWitNullType = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTransactionType(null).build();
        TransactionModel modelWitNullValueType = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTransactionType(ModelStringValue.builder().build()).build();
        TransactionModel modelWitInvalidType = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTransactionType(ModelStringValue.builder().withValue(INVALID_TYPE).build()).build();

        return new Object[][] {
            { modelWitNullType, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWitNullValueType, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWitInvalidType, THE_NEW_TRANSACTION_IS_INVALID, "Type must be one of them: [INCOME, OUTCOME]!" },
        };
    }

    @DataProvider
    public Object[][] dataForTransactionModelValidationOfCategories() {
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();

        MainCategoryModel mainCategoryModelWithNullId = createMainCategoryModel(null, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModelWithNullId.getSubCategoryModelSet().add(createSubCategoryModel(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));

        MainCategoryModel mainCategoryModelWithInvalidSubCategory = createMainCategoryModel(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryWithNullId = createSubCategoryModel(null, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        mainCategoryModelWithInvalidSubCategory.getSubCategoryModelSet().add(subCategoryWithNullId);

        TransactionModel modelWitNullMainCategory = createTransactionBuilderWithDefaultValues(null).build();
        TransactionModel modelWitNullMainCategoryId = createTransactionBuilderWithDefaultValues(mainCategoryModelWithNullId).build();
        TransactionModel modelWitNullMainCategoryWithInvalidSubCategory = createTransactionBuilderWithDefaultValues(mainCategoryModelWithInvalidSubCategory)
            .build();

        TransactionModel modelWitInvalidSubCategory = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withSubCategory(subCategoryWithNullId).build();

        return new Object[][] {
            { modelWitNullMainCategory, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWitNullMainCategoryId, "The Id of mainCategory cannot be null!", null },
            { modelWitNullMainCategoryWithInvalidSubCategory, THE_ID_OF_SUB_CATEGORY_CANNOT_BE_NULL, null },

            { modelWitInvalidSubCategory, THE_ID_OF_SUB_CATEGORY_CANNOT_BE_NULL, null },
        };
    }

    @DataProvider
    public Object[][] dataForTransactionModelValidationOfDates() {
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel modelWitNullDate = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDate(null).build();
        TransactionModel modelWitNullValueDate = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDate(ModelDateValue.builder().build()).build();
        TransactionModel modelWitDateBeforeTheDeadLine = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDate(ModelDateValue.builder().withValue(BEFORE_THE_DEADLINE_DATE).build()).build();

        TransactionModel modelWitNullValueEndDate = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withEndDate(ModelDateValue.builder().build()).build();
        TransactionModel modelWitEndDateBeforeDate = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withEndDate(ModelDateValue.builder().withValue(EXPECTED_DATE).build()).build();

        return new Object[][] {
            { modelWitNullDate, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWitNullValueDate, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWitDateBeforeTheDeadLine, THE_NEW_TRANSACTION_IS_INVALID,
                MessageFormat.format("Date cannot be before {0}!", FIRST_DATE_OF_THE_PERIOD) },

            { modelWitNullValueEndDate, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWitEndDateBeforeDate, THE_NEW_TRANSACTION_IS_INVALID,
                MessageFormat.format("End date cannot be before {0}!", EXPECTED_DATE.plusDays(1)) },
        };
    }

    @DataProvider
    public Object[][] dataForTransactionModelValidationOfDescription() {
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();
        TransactionModel modelWithNullValueDescription = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDescription(ModelStringValue.builder().build()).build();
        TransactionModel modelWithEmptyDescription = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDescription(ModelStringValue.builder().withValue(EMPTY_STRING).build()).build();
        TransactionModel modelWithTooLongDescription = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDescription(ModelStringValue.builder().withValue(TOO_LONG_DESCRIPTION).build()).build();

        return new Object[][] {
            { modelWithNullValueDescription, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWithEmptyDescription, THE_NEW_TRANSACTION_IS_INVALID, "Description cannot be empty!" },
            { modelWithTooLongDescription, THE_NEW_TRANSACTION_IS_INVALID, "Description cannot be longer than 100!" },
        };
    }
    /*
    @DataProvider
    public Object[][] dataForTransactionModelValidation() {
        MainCategoryModel mainCategoryModel = createDefaultMainCategory();

        MainCategoryModel mainCategoryModelWithNullId = createMainCategoryModel(null, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModelWithNullId.getSubCategoryModelSet().add(createSubCategoryModel(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));

        MainCategoryModel mainCategoryModelWithInvalidSubCategory = createMainCategoryModel(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryWithNullId = createSubCategoryModel(null, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        mainCategoryModelWithInvalidSubCategory.getSubCategoryModelSet().add(subCategoryWithNullId);

        TransactionModel modelWithNullTitle = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTitle(null).build();
        TransactionModel modelWithNullValueTitle = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().build()).build();
        TransactionModel modelWithEmptyTitle = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(EMPTY_STRING).build()).build();
        TransactionModel modelWithTooLongTitle = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(TOO_LONG_TITLE).build()).build();

        TransactionModel modelWithNullAmount = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withAmount(null).build();
        TransactionModel modelWithNullValueAmount = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withAmount(ModelAmountValue.builder().build()).build();
        TransactionModel modelWithNotPositiveAmount = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withAmount(ModelAmountValue.builder().withValue(ZERO).build()).build();

        TransactionModel modelWithNullCurrency = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withCurrency(null).build();
        TransactionModel modelWithNullValueCurrency = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withCurrency(ModelStringValue.builder().build()).build();
        TransactionModel modelWithInvalidCurrency = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withCurrency(ModelStringValue.builder().withValue(INVALID_CURRENCY).build()).build();

        TransactionModel modelWitNullType = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTransactionType(null).build();
        TransactionModel modelWitNullValueType = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTransactionType(ModelStringValue.builder().build()).build();
        TransactionModel modelWitInvalidType = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withTransactionType(ModelStringValue.builder().withValue(INVALID_TYPE).build()).build();

        TransactionModel modelWitNullMainCategory = createTransactionBuilderWithDefaultValues(null).build();
        TransactionModel modelWitNullMainCategoryId = createTransactionBuilderWithDefaultValues(mainCategoryModelWithNullId).build();
        TransactionModel modelWitNullMainCategoryWithInvalidSubCategory = createTransactionBuilderWithDefaultValues(mainCategoryModelWithInvalidSubCategory)
            .build();

        TransactionModel modelWitInvalidSubCategory = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withSubCategory(subCategoryWithNullId).build();

        TransactionModel modelWitNullDate = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDate(null).build();
        TransactionModel modelWitNullValueDate = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDate(ModelDateValue.builder().build()).build();
        TransactionModel modelWitDateBeforeTheDeadLine = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDate(ModelDateValue.builder().withValue(BEFORE_THE_DEADLINE_DATE).build()).build();

        TransactionModel modelWitNullValueEndDate = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withEndDate(ModelDateValue.builder().build()).build();
        TransactionModel modelWitEndDateBeforeDate = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withEndDate(ModelDateValue.builder().withValue(EXPECTED_DATE).build()).build();

        TransactionModel modelWithNullValueDescription = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDescription(ModelStringValue.builder().build()).build();
        TransactionModel modelWithEmptyDescription = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDescription(ModelStringValue.builder().withValue(EMPTY_STRING).build()).build();
        TransactionModel modelWithTooLongDescription = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withDescription(ModelStringValue.builder().withValue(TOO_LONG_DESCRIPTION).build()).build();

        return new Object[][] {
            { modelWithNullTitle, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWithNullValueTitle, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWithEmptyTitle, THE_NEW_TRANSACTION_IS_INVALID, "Title cannot be empty!" },
            { modelWithTooLongTitle, THE_NEW_TRANSACTION_IS_INVALID, "Title cannot be longer than 50!" },

            { modelWithNullAmount, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWithNullValueAmount, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWithNotPositiveAmount, THE_NEW_TRANSACTION_IS_INVALID, "Amount must be positive number!" },

            { modelWithNullCurrency, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWithNullValueCurrency, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWithInvalidCurrency, THE_NEW_TRANSACTION_IS_INVALID, "Currency must be one of them: [EUR, USD, HUF]!" },

            { modelWitNullType, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWitNullValueType, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWitInvalidType, THE_NEW_TRANSACTION_IS_INVALID, "Type must be one of them: [INCOME, OUTCOME]!" },

            { modelWitNullMainCategory, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWitNullMainCategoryId, "The Id of mainCategory cannot be null!", null },
            { modelWitNullMainCategoryWithInvalidSubCategory, THE_ID_OF_SUB_CATEGORY_CANNOT_BE_NULL, null },

            { modelWitInvalidSubCategory, THE_ID_OF_SUB_CATEGORY_CANNOT_BE_NULL, null },

            { modelWitNullDate, THE_NEW_TRANSACTION_IS_INVALID, null },
            { modelWitNullValueDate, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWitDateBeforeTheDeadLine, THE_NEW_TRANSACTION_IS_INVALID,
                MessageFormat.format("Date cannot be before {0}!", FIRST_DATE_OF_THE_PERIOD) },

            { modelWitNullValueEndDate, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWitEndDateBeforeDate, THE_NEW_TRANSACTION_IS_INVALID,
                MessageFormat.format("End date cannot be before {0}!", EXPECTED_DATE.plusDays(1)) },

            { modelWithNullValueDescription, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { modelWithEmptyDescription, THE_NEW_TRANSACTION_IS_INVALID, "Description cannot be empty!" },
            { modelWithTooLongDescription, THE_NEW_TRANSACTION_IS_INVALID, "Description cannot be longer than 100!" },
        };
    }
    */

    @DataProvider
    public Object[][] dataForContextValidation() {
        TransactionModelRequestContext contextWithoutUserId = new TransactionModelRequestContext();
        contextWithoutUserId.setUserId(null);
        contextWithoutUserId.setTransactionType(INCOME);
        TransactionModelRequestContext contextWithoutTransactionType = new TransactionModelRequestContext();
        contextWithoutTransactionType.setUserId(USER_ID);
        contextWithoutTransactionType.setTransactionType(null);

        return new Object[][] {
            { contextWithoutUserId, "User id cannot be null!" },
            { contextWithoutTransactionType, "Category type cannot be null!" }
        };
    }

    TransactionModelRequestContext createContext(final TransactionType type, final TransactionModel transactionModel) {
        TransactionModelRequestContext context = new TransactionModelRequestContext();
        context.setTransactionType(type);
        context.setTransactionModel(transactionModel);
        context.setUserId(USER_ID);
        return context;
    }

    TransactionModel.Builder createTransactionBuilderWithDefaultValues(final MainCategoryModel mainCategoryModel) {
        return TransactionModel.builder()
            .withId(EXPECTED_ID)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME.name()).build())
            .withAmount(ModelAmountValue.builder().withValue(EXPECTED_AMOUNT).build())
            .withCurrency(ModelStringValue.builder().withValue(EXPECTED_CURRENCY.name()).build())
            .withDate(ModelDateValue.builder().withValue(EXPECTED_DATE).build())
            .withMainCategory(mainCategoryModel);
    }

    TransactionModel.Builder createTransactionBuilderWithValuesForUpdate(final MainCategoryModel mainCategoryModel) {
        return TransactionModel.builder()
            .withId(RESERVED_ID)
            .withTitle(ModelStringValue.builder().withValue(RESERVED_TITLE).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME.name()).build())
            .withAmount(ModelAmountValue.builder().withValue(RESERVED_AMOUNT).build())
            .withCurrency(ModelStringValue.builder().withValue(EXPECTED_CURRENCY.name()).build())
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .withMainCategory(mainCategoryModel);
    }

    MainCategoryModel createDefaultMainCategory() {
        MainCategoryModel mainCategoryModel = createMainCategoryModel(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategoryModel(EXISTING_SUB_CATEGORY_ID_1, EXISTING_SUB_CATEGORY_NAME_1, INCOME));
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategoryModel(EXISTING_SUB_CATEGORY_ID_2, EXISTING_SUB_CATEGORY_NAME_2, INCOME));
        return mainCategoryModel;
    }

}
