package hu.elte.bm.transactionservice.app.test.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.time.LocalDate;
import java.util.HashSet;

import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelAmountValue;
import hu.elte.bm.transactionservice.web.common.ModelDateValue;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelRequestContext;

public abstract class AbstactTransactionTest extends AbstractTransactionServiceApplicationTest {

    static final Long EXPECTED_ID = 9L;
    static final Long RESERVED_ID = 5L;
    static final Long INVALID_ID = 99L;
    static final Long LOCKED_ID = 1L;
    static final String EMPTY_STRING = "";
    static final String TOO_LONG_TITLE = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";
    static final String EXPECTED_TITLE = "title";
    static final String RESERVED_TITLE = "income 1";
    static final String RESERVED_TITLE_2 = "income 2";
    static final double ZERO = 0.0d;
    static final double EXPECTED_AMOUNT = 100.0d;
    static final double RESERVED_AMOUNT = 1000.0d;
    static final String INVALID_CURRENCY = "EURO";
    static final Currency EXPECTED_CURRENCY = Currency.EUR;
    static final String INVALID_TYPE = "INCOME1";
    static final LocalDate EXPECTED_DATE = LocalDate.now();
    static final LocalDate RESERVED_DATE = LocalDate.now().minusDays(5L);
    static final LocalDate BEFORE_THE_DEADLINE_DATE = LocalDate.now().minusDays(15L);
    static final LocalDate FIRST_DATE_OF_THE_PERIOD = LocalDate.now().minusDays(9L);
    static final LocalDate EXPECTED_END_DATE = LocalDate.now().plusDays(1L);
    static final String TOO_LONG_DESCRIPTION = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";
    static final String EXPECTED_DESCRIPTION = "description";
    static final long EXISTING_MAIN_CATEGORY_ID_1 = 1L;
    static final long EXISTING_MAIN_CATEGORY_ID_2 = 2L;
    static final String EXISTING_MAIN_CATEGORY_NAME_1 = "main category 1";
    static final String EXISTING_MAIN_CATEGORY_NAME_2 = "main category 2";
    static final long EXISTING_SUB_CATEGORY_ID_1 = 1L;
    static final long EXISTING_SUB_CATEGORY_ID_2 = 2L;

    TransactionModelRequestContext createContext(final TransactionType type, final TransactionModel transactionModel) {
        TransactionModelRequestContext context = new TransactionModelRequestContext();
        context.setTransactionType(type);
        context.setTransactionModel(transactionModel);
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

    TransactionModel.Builder createTransactionBuilderWithForUpdateValues(final MainCategoryModel mainCategoryModel) {
        return TransactionModel.builder()
                .withId(RESERVED_ID)
                .withTitle(ModelStringValue.builder().withValue(RESERVED_TITLE).build())
                .withTransactionType(ModelStringValue.builder().withValue(INCOME.name()).build())
                .withAmount(ModelAmountValue.builder().withValue(RESERVED_AMOUNT).build())
                .withCurrency(ModelStringValue.builder().withValue(EXPECTED_CURRENCY.name()).build())
                .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
                .withMainCategory(mainCategoryModel);
    }

    MainCategoryModel createMainCategoryWithoutSubCategories(final Long id, final String name, final TransactionType type) {
        return MainCategoryModel.builder()
                .withId(id)
                .withName(ModelStringValue.builder().withValue(name).build())
                .withTransactionType(ModelStringValue.builder().withValue(type.name()).build())
                .withSubCategoryModelSet(new HashSet<>())
                .build();
    }

    SubCategoryModel createSubCategory(final Long id, final String name, final TransactionType type) {
        return SubCategoryModel.builder()
                .withId(id)
                .withName(ModelStringValue.builder().withValue(name).build())
                .withTransactionType(ModelStringValue.builder().withValue(type.name()).build())
                .build();
    }

}
