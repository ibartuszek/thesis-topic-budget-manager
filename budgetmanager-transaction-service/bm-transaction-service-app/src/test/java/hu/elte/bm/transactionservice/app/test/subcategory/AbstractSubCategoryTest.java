package hu.elte.bm.transactionservice.app.test.subcategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;

import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryController;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryRequestContext;

public class AbstractSubCategoryTest extends AbstractTransactionServiceApplicationTest {

    static final String THE_NEW_CATEGORY_IS_INVALID = "The new category is invalid.";
    static final String THE_CATEGORY_HAS_BEEN_SAVED = "The category has been saved.";
    static final String THE_CATEGORY_HAS_BEEN_UPDATED = "The category has been updated.";
    static final String NEW_CATEGORY_NAME = "new supplementary category";
    static final Long EXISTING_INCOME_ID = 1L;
    static final Long EXISTING_OUTCOME_ID = 4L;
    static final Long NEW_ID = 8L;
    static final Long INVALID_ID = 99L;
    static final String RESERVED_CATEGORY_NAME = "supplementary category 1";
    static final String OTHER_RESERVED_CATEGORY_NAME = "supplementary category 2";

    private static final String VALIDATED_FIELD_VALUE_CANNOT_BE_NULL = "Validated field value cannot be null!";
    private static final String EMPTY_NAME = "";
    private static final String TOO_LONG_NAME = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";
    private static final String INVALID_TYPE = "INCOME1";

    @Autowired
    private SubCategoryController subCategoryController;

    SubCategoryController getSubCategoryController() {
        return subCategoryController;
    }

    @DataProvider
    public Object[][] dataForMainCategoryModelValidation() {
        SubCategoryModel categoryWithNullName = createSubCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME)
            .withName(null).build();
        SubCategoryModel categoryWithNullNameValue = createSubCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME)
            .withName(ModelStringValue.builder().build()).build();
        SubCategoryModel categoryWithEmptyName = createSubCategoryModelBuilder(null, EMPTY_NAME, INCOME).build();
        SubCategoryModel categoryWithTooLongName = createSubCategoryModelBuilder(null, TOO_LONG_NAME, INCOME).build();
        SubCategoryModel categoryWithNullType = createSubCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME)
            .withTransactionType(null).build();
        SubCategoryModel categoryWithNullTypeValue = createSubCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME)
            .withTransactionType(ModelStringValue.builder().build()).build();
        SubCategoryModel categoryWithEmptyTypeValue = createSubCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME)
            .withTransactionType(ModelStringValue.builder().withValue(EMPTY_NAME).build()).build();
        SubCategoryModel categoryWithInvalidTypeValue = createSubCategoryModelBuilder(null, NEW_CATEGORY_NAME, INCOME)
            .withTransactionType(ModelStringValue.builder().withValue(INVALID_TYPE).build()).build();

        return new Object[][] {
            { categoryWithNullName, THE_NEW_CATEGORY_IS_INVALID, null },
            { categoryWithNullNameValue, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { categoryWithEmptyName, THE_NEW_CATEGORY_IS_INVALID, "Name cannot be empty!" },
            { categoryWithTooLongName, THE_NEW_CATEGORY_IS_INVALID, "Name cannot be longer than 50!" },
            { categoryWithNullType, THE_NEW_CATEGORY_IS_INVALID, null },
            { categoryWithNullTypeValue, VALIDATED_FIELD_VALUE_CANNOT_BE_NULL, null },
            { categoryWithEmptyTypeValue, THE_NEW_CATEGORY_IS_INVALID, "Type cannot be empty!" },
            { categoryWithInvalidTypeValue, THE_NEW_CATEGORY_IS_INVALID, "Type must be one of them: [INCOME, OUTCOME]!" },
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

    SubCategoryRequestContext createContext(final TransactionType type, final SubCategoryModel subCategoryModel) {
        SubCategoryRequestContext context = new SubCategoryRequestContext();
        context.setTransactionType(type);
        context.setSubCategory(subCategoryModel);
        context.setUserId(USER_ID);
        return context;
    }

}
