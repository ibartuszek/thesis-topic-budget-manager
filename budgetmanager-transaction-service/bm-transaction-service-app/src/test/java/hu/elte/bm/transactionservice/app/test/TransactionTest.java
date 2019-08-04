package hu.elte.bm.transactionservice.app.test;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.time.LocalDate;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelAmountValue;
import hu.elte.bm.transactionservice.web.common.ModelDateValue;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionController;
import hu.elte.bm.transactionservice.web.transaction.TransactionModel;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelRequestContext;
import hu.elte.bm.transactionservice.web.transaction.TransactionModelResponse;

public class TransactionTest extends AbstractTransactionServiceApplicationTest {

    private static final Long EXPECTED_ID = 9L;
    private static final Long RESERVED_ID = 5L;
    private static final Long INVALID_ID = 99L;
    private static final Long LOCKED_ID = 1L;
    private static final String EXPECTED_TITLE = "title";
    private static final String RESERVED_TITLE = "income 1";
    private static final String RESERVED_TITLE_2 = "income 2";
    private static final double EXPECTED_AMOUNT = 100.0d;
    private static final double RESERVED_AMOUNT = 1000.0d;
    private static final Currency EXPECTED_CURRENCY = Currency.EUR;
    private static final LocalDate EXPECTED_DATE = LocalDate.now();
    private static final LocalDate RESERVED_DATE = LocalDate.now().minusDays(5);
    private static final LocalDate BEFORE_THE_DEADLINE_DATE = LocalDate.now().minusDays(15);
    private static final long EXISTING_MAIN_CATEGORY_ID_1 = 1L;
    private static final long EXISTING_MAIN_CATEGORY_ID_2 = 2L;
    private static final String EXISTING_MAIN_CATEGORY_NAME_1 = "main category 1";
    private static final String EXISTING_MAIN_CATEGORY_NAME_2 = "main category 2";
    private static final long EXISTING_SUB_CATEGORY_ID_1 = 1L;
    private static final long EXISTING_SUB_CATEGORY_ID_2 = 2L;

    @Autowired
    private TransactionController transactionController;

    // TransactionException
    @Test
    public void testSaveWhenMainCategoryIdIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(null, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withSubCategory(subCategoryModel)
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("The Id of mainCategory cannot be null!", response.getMessage());
    }

    // TransactionException
    @Test
    public void testSaveWhenOneOfTheSubCategorySetsSubCategoryIdIsNull() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(null, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withSubCategory(subCategoryModel)
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("The Id of subCategory cannot be null!", response.getMessage());
    }

    // TransactionException
    @Test
    public void testSaveWhenTheDateIsBeforeTheEndOfLastPeriod() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withSubCategory(subCategoryModel)
            .withDate(ModelDateValue.builder().withValue(BEFORE_THE_DEADLINE_DATE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("The new transaction is invalid.", response.getMessage());
    }

    @Test
    public void testSaveWhenThereIsOneWithSameDate() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals("The transaction has been saved.", response.getMessage());
        Assert.assertEquals(EXPECTED_ID, response.getTransactionModel().getId());
    }

    @Test
    public void testSaveWithoutSubCategory() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withTitle(ModelStringValue.builder().withValue(RESERVED_TITLE).build())
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("The transaction has been saved before.", response.getMessage());
    }

    @Test
    public void testSave() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionModelToSave = createTransactionBuilderWithDefaultValues(mainCategoryModel)
            .withId(null)
            .withSubCategory(subCategoryModel)
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionModelToSave);
        // WHEN
        ResponseEntity<Object> result = transactionController.createTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals("The transaction has been saved.", response.getMessage());
        Assert.assertEquals(EXPECTED_ID, response.getTransactionModel().getId());
    }

    // TransactionException
    @Test
    public void testUpdateWhenTransactionCannotBeFound() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionToUpdate = createTransactionBuilderWithForUpdateValues(mainCategoryModel)
            .withId(INVALID_ID)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = transactionController.updateTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("The new transaction is invalid.", response.getMessage());
    }

    // TransactionException
    @Test
    public void testUpdateWhenTransactionIsLocked() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionToUpdate = createTransactionBuilderWithForUpdateValues(mainCategoryModel)
            .withId(LOCKED_ID)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = transactionController.updateTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("The new transaction is invalid.", response.getMessage());
    }

    // TransactionException
    @Test
    public void testUpdateWhenTransactionTypeChanged() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionToUpdate = createTransactionBuilderWithForUpdateValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .withTransactionType(ModelStringValue.builder().withValue(TransactionType.OUTCOME.name()).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = transactionController.updateTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("The new transaction is invalid.", response.getMessage());
    }

    @Test
    public void testUpdateWhenThereIsOneTransactionWithSameDateAndName() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionToUpdate = createTransactionBuilderWithForUpdateValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(RESERVED_TITLE_2).build())
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = transactionController.updateTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals("You cannot update this transaction, because it exists.", response.getMessage());
    }

    @Test
    public void testUpdate() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionToUpdate = createTransactionBuilderWithForUpdateValues(mainCategoryModel)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToUpdate);
        // WHEN
        ResponseEntity<Object> result = transactionController.updateTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals("The transaction has been updated.", response.getMessage());
    }

    @Test
    public void testDelete() {
        // GIVEN
        MainCategoryModel mainCategoryModel = createMainCategoryWithoutSubCategories(EXISTING_MAIN_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        SubCategoryModel subCategoryModel = createSubCategory(EXISTING_SUB_CATEGORY_ID_1, EXISTING_MAIN_CATEGORY_NAME_1, INCOME);
        mainCategoryModel.getSubCategoryModelSet().add(subCategoryModel);
        mainCategoryModel.getSubCategoryModelSet().add(createSubCategory(EXISTING_SUB_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME));
        TransactionModel transactionToDelete = createTransactionBuilderWithForUpdateValues(mainCategoryModel).build();
        TransactionModelRequestContext context = createContext(TransactionType.INCOME, transactionToDelete);
        // WHEN
        ResponseEntity<Object> result = transactionController.deleteTransaction(context);
        TransactionModelResponse response = (TransactionModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals("The transaction has been deleted.", response.getMessage());
    }

    private TransactionModelRequestContext createContext(final TransactionType type, final TransactionModel transactionModel) {
        TransactionModelRequestContext context = new TransactionModelRequestContext();
        context.setTransactionType(type);
        context.setTransactionModel(transactionModel);
        return context;
    }

    private TransactionModel.Builder createTransactionBuilderWithDefaultValues(final MainCategoryModel mainCategoryModel) {
        return TransactionModel.builder()
            .withId(EXPECTED_ID)
            .withTitle(ModelStringValue.builder().withValue(EXPECTED_TITLE).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME.name()).build())
            .withAmount(ModelAmountValue.builder().withValue(EXPECTED_AMOUNT).build())
            .withCurrency(ModelStringValue.builder().withValue(EXPECTED_CURRENCY.name()).build())
            .withDate(ModelDateValue.builder().withValue(EXPECTED_DATE).build())
            .withMainCategory(mainCategoryModel);
    }

    private TransactionModel.Builder createTransactionBuilderWithForUpdateValues(final MainCategoryModel mainCategoryModel) {
        return TransactionModel.builder()
            .withId(RESERVED_ID)
            .withTitle(ModelStringValue.builder().withValue(RESERVED_TITLE).build())
            .withTransactionType(ModelStringValue.builder().withValue(INCOME.name()).build())
            .withAmount(ModelAmountValue.builder().withValue(RESERVED_AMOUNT).build())
            .withCurrency(ModelStringValue.builder().withValue(EXPECTED_CURRENCY.name()).build())
            .withDate(ModelDateValue.builder().withValue(RESERVED_DATE).build())
            .withMainCategory(mainCategoryModel);
    }

    private MainCategoryModel createMainCategoryWithoutSubCategories(final Long id, final String name, final TransactionType type) {
        return MainCategoryModel.builder()
            .withId(id)
            .withName(ModelStringValue.builder().withValue(name).build())
            .withTransactionType(ModelStringValue.builder().withValue(type.name()).build())
            .withSubCategoryModelSet(new HashSet<>())
            .build();
    }

    private SubCategoryModel createSubCategory(final Long id, final String name, final TransactionType type) {
        return SubCategoryModel.builder()
            .withId(id)
            .withName(ModelStringValue.builder().withValue(name).build())
            .withTransactionType(ModelStringValue.builder().withValue(type.name()).build())
            .build();
    }
}
