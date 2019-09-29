package hu.elte.bm.transactionservice.app.test.subcategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import org.testng.annotations.DataProvider;

import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryRequestContext;

public class AbstractSubCategoryTest extends AbstractTransactionServiceApplicationTest {

    static final String THE_CATEGORY_HAS_BEEN_SAVED = "The category has been saved.";
    static final String THE_CATEGORY_HAS_BEEN_UPDATED = "The category has been updated.";
    static final String NEW_CATEGORY_NAME = "new supplementary category";
    static final Long EXISTING_INCOME_ID = 1L;
    static final Long EXISTING_OUTCOME_ID = 4L;
    static final Long NEW_ID = 8L;
    static final Long INVALID_ID = 99L;
    static final String RESERVED_CATEGORY_NAME = "supplementary category 1";
    static final String OTHER_RESERVED_CATEGORY_NAME = "supplementary category 2";

    private static final String EMPTY_NAME = "";
    private static final String TOO_LONG_NAME = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";

    @DataProvider
    public Object[][] dataForSubCategoryValidation() {
        SubCategory categoryWithNullName = createSubCategoryBuilder(null, NEW_CATEGORY_NAME, INCOME)
            .withName(null).build();
        SubCategory categoryWithEmptyName = createSubCategoryBuilder(null, EMPTY_NAME, INCOME).build();
        SubCategory categoryWithTooLongName = createSubCategoryBuilder(null, TOO_LONG_NAME, INCOME).build();
        SubCategory categoryWithNullType = createSubCategoryBuilder(null, NEW_CATEGORY_NAME, INCOME)
            .withTransactionType(null).build();

        return new Object[][] {
            { categoryWithNullName, "Name cannot be empty!" },
            { categoryWithEmptyName, "Name cannot be empty!" },
            { categoryWithTooLongName, "Name must be shorter than 50 characters!" },
            { categoryWithNullType, "Type cannot be null!" },
        };
    }

    @DataProvider
    public Object[][] dataForContextValidation() {
        SubCategoryRequestContext contextWithoutUserId = new SubCategoryRequestContext();
        contextWithoutUserId.setUserId(null);
        contextWithoutUserId.setTransactionType(INCOME);
        SubCategoryRequestContext contextWithoutTransactionType = new SubCategoryRequestContext();
        contextWithoutTransactionType.setUserId(USER_ID);
        contextWithoutTransactionType.setTransactionType(null);

        return new Object[][] {
            { contextWithoutUserId, "User id cannot be null!" },
            { contextWithoutTransactionType, "Category type cannot be null!" }
        };
    }

    SubCategoryRequestContext createContext(final TransactionType type, final SubCategory subCategory) {
        SubCategoryRequestContext context = new SubCategoryRequestContext();
        context.setTransactionType(type);
        context.setSubCategory(subCategory);
        context.setUserId(USER_ID);
        return context;
    }

}
