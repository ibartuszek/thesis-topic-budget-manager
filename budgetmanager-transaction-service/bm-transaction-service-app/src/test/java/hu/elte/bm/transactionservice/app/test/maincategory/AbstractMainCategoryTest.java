package hu.elte.bm.transactionservice.app.test.maincategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;

import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryController;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModelRequestContext;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;

public abstract class AbstractMainCategoryTest extends AbstractTransactionServiceApplicationTest {

    static final Long EXISTING_INCOME_ID = 1L;
    static final Long EXISTING_OUTCOME_ID = 4L;
    static final Long NEW_ID = 8L;
    static final Long INVALID_ID = 99L;
    static final String NEW_CATEGORY_NAME = "new main category";
    static final String RESERVED_CATEGORY_NAME = "main category 1";
    static final String OTHER_RESERVED_CATEGORY_NAME = "main category 2";
    static final String THE_NEW_CATEGORY_IS_INVALID = "The new category is invalid.";
    static final String THE_CATEGORY_HAS_BEEN_SAVED = "The category has been saved.";

    private static final String EMPTY_NAME = "";
    private static final String TOO_LONG_NAME = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";
    private static final String INVALID_TYPE = "OUTCOME1";
    private static final String VALIDATED_FIELD_VALUE_CANNOT_BE_NULL = "Validated field value cannot be null!";

    @Autowired
    private MainCategoryController mainCategoryController;

    MainCategoryController getMainCategoryController() {
        return mainCategoryController;
    }

    @DataProvider
    public Object[][] dataForMainCategoryModelValidation() {
        MainCategoryModel categoryWithNullName = createMainCategoryModelBuilder(null, null, INCOME, new HashSet<>())
            .withName(null).build();
        MainCategoryModel categoryWithNullNameValue = createMainCategoryModelBuilder(null, null, INCOME, new HashSet<>()).build();
        MainCategoryModel categoryWithEmptyName = createMainCategoryModelBuilder(null, EMPTY_NAME, INCOME, new HashSet<>()).build();
        MainCategoryModel categoryWithTooLongName = createMainCategoryModelBuilder(null, TOO_LONG_NAME, INCOME, new HashSet<>()).build();
        MainCategoryModel categoryWithNullType = createMainCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME, new HashSet<>())
            .withTransactionType(null).build();
        MainCategoryModel categoryWithNullTypeValue = createMainCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME, new HashSet<>())
            .withTransactionType(ModelStringValue.builder().build()).build();
        MainCategoryModel categoryWithEmptyTypeValue = createMainCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME, new HashSet<>())
            .withTransactionType(ModelStringValue.builder().withValue(EMPTY_NAME).build()).build();
        MainCategoryModel categoryWithInvalidTypeValue = createMainCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME, new HashSet<>())
            .withTransactionType(ModelStringValue.builder().withValue(INVALID_TYPE).build()).build();

        return new Object[][] {
            { categoryWithNullName, THE_NEW_CATEGORY_IS_INVALID, null },
            { categoryWithNullNameValue, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { categoryWithEmptyName, THE_NEW_CATEGORY_IS_INVALID, "Name cannot be empty!" },
            { categoryWithTooLongName, THE_NEW_CATEGORY_IS_INVALID, "Name cannot be longer than 50!" },
            { categoryWithNullType, THE_NEW_CATEGORY_IS_INVALID, null },
            { categoryWithNullTypeValue, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { categoryWithEmptyTypeValue, THE_NEW_CATEGORY_IS_INVALID, "Type cannot be empty!" },
            { categoryWithInvalidTypeValue, THE_NEW_CATEGORY_IS_INVALID, "Type must be one of them: [INCOME, OUTCOME]!" }
        };
    }

    @DataProvider
    public Object[][] dataForContextValidation() {
        MainCategoryModelRequestContext contextWithoutUserId = new MainCategoryModelRequestContext();
        contextWithoutUserId.setUserId(null);
        contextWithoutUserId.setTransactionType(INCOME);
        MainCategoryModelRequestContext contextWithoutTransactionType = new MainCategoryModelRequestContext();
        contextWithoutTransactionType.setUserId(USER_ID);
        contextWithoutTransactionType.setTransactionType(null);

        return new Object[][] {
            { contextWithoutUserId, "User id cannot be null!" },
            { contextWithoutTransactionType, "Category type cannot be null!" }
        };
    }

    Set<SubCategoryModel> createSubCategoryModelSet() {
        Set<SubCategoryModel> subCategorySet = new HashSet<>();
        subCategorySet.add(createSubCategoryModel(1L, "supplementary category 1", INCOME));
        subCategorySet.add(createSubCategoryModel(2L, "supplementary category 2", INCOME));
        return subCategorySet;
    }

    MainCategoryModelRequestContext createContext(final TransactionType type, final MainCategoryModel mainCategoryModel) {
        MainCategoryModelRequestContext context = new MainCategoryModelRequestContext();
        context.setTransactionType(type);
        context.setMainCategoryModel(mainCategoryModel);
        context.setUserId(USER_ID);
        return context;
    }

}
