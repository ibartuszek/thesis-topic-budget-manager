package hu.elte.bm.transactionservice.app.test.maincategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.DataProvider;

import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryRequestContext;

public abstract class AbstractMainCategoryTest extends AbstractTransactionServiceApplicationTest {

    static final Long EXISTING_INCOME_ID = 1L;
    static final Long EXISTING_OUTCOME_ID = 4L;
    static final Long NEW_ID = 8L;
    static final Long INVALID_ID = 99L;
    static final String NEW_CATEGORY_NAME = "new main category";
    static final String RESERVED_CATEGORY_NAME = "main category 1";
    static final String OTHER_RESERVED_CATEGORY_NAME = "main category 2";
    static final String THE_CATEGORY_HAS_BEEN_SAVED = "The category has been saved.";

    private static final String EMPTY_NAME = "";
    private static final String TOO_LONG_NAME = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";

    @DataProvider
    public Object[][] dataForMainCategoryModelValidation() {
        MainCategory categoryWithNullName = createMainCategoryBuilder(null, null, INCOME, new HashSet<>())
            .withName(null).build();
        MainCategory categoryWithEmptyName = createMainCategoryBuilder(null, EMPTY_NAME, INCOME, new HashSet<>()).build();
        MainCategory categoryWithTooLongName = createMainCategoryBuilder(null, TOO_LONG_NAME, INCOME, new HashSet<>()).build();
        MainCategory categoryWithNullType = createMainCategoryBuilder(null, NEW_CATEGORY_NAME, INCOME, new HashSet<>())
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
        MainCategoryRequestContext contextWithoutUserId = new MainCategoryRequestContext();
        contextWithoutUserId.setUserId(null);
        contextWithoutUserId.setTransactionType(INCOME);
        MainCategoryRequestContext contextWithoutTransactionType = new MainCategoryRequestContext();
        contextWithoutTransactionType.setUserId(USER_ID);
        contextWithoutTransactionType.setTransactionType(null);

        return new Object[][] {
            { contextWithoutUserId, "User id cannot be null!" },
            { contextWithoutTransactionType, "Category type cannot be null!" }
        };
    }

    Set<SubCategory> createSubCategoryModelSet() {
        Set<SubCategory> subCategorySet = new HashSet<>();
        subCategorySet.add(createSubCategory(1L, "supplementary category 1", INCOME));
        subCategorySet.add(createSubCategory(2L, "supplementary category 2", INCOME));
        return subCategorySet;
    }

    MainCategoryRequestContext createContext(final TransactionType type, final MainCategory mainCategory) {
        MainCategoryRequestContext context = new MainCategoryRequestContext();
        context.setTransactionType(type);
        context.setMainCategory(mainCategory);
        context.setUserId(USER_ID);
        return context;
    }

}
