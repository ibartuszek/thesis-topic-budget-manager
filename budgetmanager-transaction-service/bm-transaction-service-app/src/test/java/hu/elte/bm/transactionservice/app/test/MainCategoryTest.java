package hu.elte.bm.transactionservice.app.test;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryController;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModelRequestContext;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModelResponse;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;

public class MainCategoryTest extends AbstractTransactionServiceApplicationTest {

    private static final Long EXISTING_INCOME_ID = 1L;
    private static final Long EXISTING_OUTCOME_ID = 4L;
    private static final Long NEW_ID = 6L;
    private static final String NEW_CATEGORY_NAME = "new main category";
    private static final String RESERVED_CATEGORY_NAME = "main category 1";
    private static final String OTHER_RESERVED_CATEGORY_NAME = "main category 2";
    private static final String INCOME = "INCOME";
    private static final String OUTCOME = "OUTCOME";

    @Autowired
    private MainCategoryController mainCategoryController;

    @Test
    public void testSaveCategoryWhenCategoryHasNewName() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(NEW_ID, response.getMainCategoryModel().getId());
        Assert.assertEquals(NEW_CATEGORY_NAME, response.getMainCategoryModel().getName().getValue());
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithDifferentCategory() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(OUTCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(NEW_ID, response.getMainCategoryModel().getId());
        Assert.assertEquals(RESERVED_CATEGORY_NAME, response.getMainCategoryModel().getName().getValue());
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithSameCategory() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
    }

    // MainCategoryException
    @Test
    public void testSaveCategoryWhenCategoryHasNewNameButThereIsOneSubCategoryWithoutId() {
        // GIVEN
        MainCategoryModel mainCategoryToSave = MainCategoryModel.builder()
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        mainCategoryToSave.getSubCategoryModelSet()
            .add(createSubCategoryModel(null, "illegal supplementary category", INCOME));
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);
        // WHEN
        ResponseEntity result = mainCategoryController.createMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewName() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(EXISTING_INCOME_ID, response.getMainCategoryModel().getId());
        Assert.assertEquals(NEW_CATEGORY_NAME, response.getMainCategoryModel().getName().getValue());
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndOtherTypeHasThisName() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
            .withId(EXISTING_OUTCOME_ID)
            .withName(ModelStringValue.builder().withValue(RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(OUTCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(EXISTING_OUTCOME_ID, response.getMainCategoryModel().getId());
        Assert.assertEquals(RESERVED_CATEGORY_NAME, response.getMainCategoryModel().getName().getValue());
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndItIsReserved() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(OTHER_RESERVED_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
    }

    // MainCategoryException
    @Test
    public void testUpdateCategoryWhenCategoryHasNewNameButThereIsOneSubCategoryWithoutId() {
        // GIVEN
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(createSubCategoryModelSet())
            .build();
        mainCategoryToUpdate.getSubCategoryModelSet()
            .add(createSubCategoryModel(null, "illegal supplementary category", INCOME));
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
    }

    // MainCategoryException
    @Test
    public void testUpdateCategoryWhenCategoryHasNewNameButItHasLessSubCategory() {
        // GIVEN
        Set<SubCategoryModel> modifiedSet = new HashSet<>();
        modifiedSet.add(createSubCategoryModel(1L, "supplementary category 1", INCOME));
        MainCategoryModel mainCategoryToUpdate = MainCategoryModel.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(ModelStringValue.builder().withValue(NEW_CATEGORY_NAME).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME).build())
            .withSubCategoryModelSet(modifiedSet)
            .build();
        MainCategoryModelRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);
        // WHEN
        ResponseEntity result = mainCategoryController.updateMainCategory(context);
        MainCategoryModelResponse response = (MainCategoryModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
    }

    private Set<SubCategoryModel> createSubCategoryModelSet() {
        Set<SubCategoryModel> subCategorySet = new HashSet<>();
        subCategorySet.add(createSubCategoryModel(1L, "supplementary category 1", INCOME));
        subCategorySet.add(createSubCategoryModel(2L, "supplementary category 2", INCOME));
        return subCategorySet;
    }

    private SubCategoryModel createSubCategoryModel(final Long id, final String name, final String type) {
        ModelStringValue nameValue = ModelStringValue.builder()
            .withValue(name)
            .build();
        ModelStringValue typeValue = ModelStringValue.builder()
            .withValue(type)
            .build();
        return SubCategoryModel.builder()
            .withId(id)
            .withName(nameValue)
            .withTransactionType(typeValue)
            .build();
    }

    private MainCategoryModelRequestContext createContext(final TransactionType type, final MainCategoryModel mainCategoryModel) {
        MainCategoryModelRequestContext context = new MainCategoryModelRequestContext();
        context.setTransactionType(TransactionType.INCOME);
        context.setMainCategoryModel(mainCategoryModel);
        return context;
    }
}
