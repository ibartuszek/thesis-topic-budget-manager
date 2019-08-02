package hu.elte.bm.transactionservice.app.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryController;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModelRequestContext;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModelResponse;

public class SubCategoryTest extends AbstractTransactionServiceApplicationTest {

    private static final String NEW_CATEGORY_NAME = "new supplementary category";
    private static final Long EXISTING_INCOME_ID = 1L;
    private static final Long EXISTING_OUTCOME_ID = 4L;
    private static final Long NEW_ID = 6L;
    private static final String RESERVED_CATEGORY_NAME = "supplementary category 1";
    private static final String OTHER_RESERVED_CATEGORY_NAME = "supplementary category 2";
    private static final String INCOME = "INCOME";
    private static final String OUTCOME = "OUTCOME";

    @Autowired
    private SubCategoryController subCategoryController;

    @Test
    public void testSaveCategoryWhenCategoryHasNewName() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(NEW_ID, response.getSubCategoryModel().getId());
        Assert.assertEquals(NEW_CATEGORY_NAME, response.getSubCategoryModel().getName().getValue());
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithDifferentCategory() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(OUTCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(NEW_ID, response.getSubCategoryModel().getId());
        Assert.assertEquals(RESERVED_CATEGORY_NAME, response.getSubCategoryModel().getName().getValue());
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithSameCategory() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.createSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewName() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.updateSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(EXISTING_INCOME_ID, response.getSubCategoryModel().getId());
        Assert.assertEquals(NEW_CATEGORY_NAME, response.getSubCategoryModel().getName().getValue());
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndOtherTypeHasThisName() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withId(EXISTING_OUTCOME_ID)
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(OUTCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.updateSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(EXISTING_OUTCOME_ID, response.getSubCategoryModel().getId());
        Assert.assertEquals(RESERVED_CATEGORY_NAME, response.getSubCategoryModel().getName().getValue());
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndItIsReserved() {
        // GIVEN
        SubCategoryModel subCategoryModelToSave = SubCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(OTHER_RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .build();
        SubCategoryModelRequestContext context = createContext(TransactionType.INCOME, subCategoryModelToSave);
        // WHEN
        ResponseEntity result = subCategoryController.updateSubCategory(context);
        SubCategoryModelResponse response = (SubCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
    }

    private SubCategoryModelRequestContext createContext(final TransactionType type, final SubCategoryModel subCategoryModel) {
        SubCategoryModelRequestContext context = new SubCategoryModelRequestContext();
        context.setTransactionType(TransactionType.INCOME);
        context.setSubCategoryModel(subCategoryModel);
        return context;
    }

}
