package hu.elte.bm.transactionservice.dal.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.sql.Date;
import java.util.HashSet;

import org.dozer.DozerBeanMapper;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntityTransformer;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntityTransformer;
import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;

public class TransactionEntityTransformerTest {

    private static final Long EXPECTED_ID = 1L;
    private static final String EXPECTED_TITLE = "title";
    private static final double EXPECTED_AMOUNT = 100.0d;
    private static final Currency EXPECTED_CURRENCY = Currency.EUR;
    private static final Date EXPECTED_DATE = Date.valueOf("1999-01-01");
    private static final Date EXPECTED_END_DATE = Date.valueOf("1999-02-01");
    private static final boolean MONTHLY = true;
    private static final String DESCRIPTION = "description";
    private static final boolean LOCKED = true;
    private static final long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "category";

    private TransactionEntityTransformer underTest;

    private IMocksControl control;
    private MainCategoryEntityTransformer mainCategoryEntityTransformer;
    private SubCategoryEntityTransformer subCategoryEntityTransformer;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        mainCategoryEntityTransformer = control.createMock(MainCategoryEntityTransformer.class);
        subCategoryEntityTransformer = control.createMock(SubCategoryEntityTransformer.class);
        underTest = new TransactionEntityTransformer(mainCategoryEntityTransformer, subCategoryEntityTransformer, new DozerBeanMapper());
    }

    @Test
    public void testTransFormToTransaction() {
        // GIVEN
        IncomeEntity incomeEntity = createExampleIncomeEntity();
        EasyMock.expect(mainCategoryEntityTransformer.transformToMainCategory(EasyMock.isA(MainCategoryEntity.class)))
            .andReturn(createExampleMainCategory());
        control.replay();
        // WHEN
        Transaction result = underTest.transformToTransaction(incomeEntity);
        // THEN
        control.verify();
        Assert.assertEquals(EXPECTED_ID, result.getId());
        Assert.assertEquals(EXPECTED_TITLE, result.getTitle());
        Assert.assertEquals(EXPECTED_AMOUNT, result.getAmount());
        Assert.assertEquals(EXPECTED_CURRENCY, result.getCurrency());
        Assert.assertNotNull(result.getMainCategory());
        Assert.assertNull(result.getSubCategory());
        Assert.assertEquals(EXPECTED_DATE.toLocalDate(), result.getDate());
        Assert.assertEquals(EXPECTED_END_DATE.toLocalDate(), result.getEndDate());
        Assert.assertEquals(MONTHLY, result.isMonthly());
        Assert.assertEquals(DESCRIPTION, result.getDescription());
        Assert.assertEquals(LOCKED, result.isLocked());
        Assert.assertEquals(INCOME, result.getTransactionType());
    }

    @Test
    public void testTransFormToTransactionWhenItHasSubCategory() {
        // GIVEN
        IncomeEntity incomeEntity = createExampleIncomeEntity();
        incomeEntity.setSubCategoryEntity(createExampleSubCategoryEntity());

        EasyMock.expect(mainCategoryEntityTransformer.transformToMainCategory(EasyMock.isA(MainCategoryEntity.class)))
            .andReturn(createExampleMainCategory());
        EasyMock.expect(subCategoryEntityTransformer.transformToSubCategory(EasyMock.isA(SubCategoryEntity.class)))
            .andReturn(createExampleSubCategory());
        control.replay();
        // WHEN
        Transaction result = underTest.transformToTransaction(incomeEntity);
        // THEN
        control.verify();
        Assert.assertEquals(EXPECTED_ID, result.getId());
        Assert.assertEquals(EXPECTED_TITLE, result.getTitle());
        Assert.assertEquals(EXPECTED_AMOUNT, result.getAmount());
        Assert.assertEquals(EXPECTED_CURRENCY, result.getCurrency());
        Assert.assertNotNull(result.getMainCategory());
        Assert.assertNotNull(result.getSubCategory());
        Assert.assertEquals(EXPECTED_DATE.toLocalDate(), result.getDate());
        Assert.assertEquals(EXPECTED_END_DATE.toLocalDate(), result.getEndDate());
        Assert.assertEquals(MONTHLY, result.isMonthly());
        Assert.assertEquals(DESCRIPTION, result.getDescription());
        Assert.assertEquals(LOCKED, result.isLocked());
        Assert.assertEquals(INCOME, result.getTransactionType());
    }

    @Test
    public void testTransFormToIncomeEntity() {
        // GIVEN
        Transaction transaction = createExampleTransactionBuilderWithoutSubCategory().build();
        MainCategoryEntity mainCategoryEntity = createExampleMainCategoryEntity();
        control.replay();
        // WHEN
        IncomeEntity result = underTest.transformToIncomeEntity(transaction, mainCategoryEntity, null);
        // THEN
        control.verify();
        Assert.assertEquals(EXPECTED_ID, result.getId());
        Assert.assertEquals(EXPECTED_TITLE, result.getTitle());
        Assert.assertEquals(EXPECTED_AMOUNT, result.getAmount());
        Assert.assertEquals(EXPECTED_CURRENCY, result.getCurrency());
        Assert.assertNotNull(result.getMainCategoryEntity());
        Assert.assertNull(result.getSubCategoryEntity());
        Assert.assertEquals(EXPECTED_DATE, result.getDate());
        Assert.assertEquals(EXPECTED_END_DATE, result.getEndDate());
        Assert.assertEquals(MONTHLY, result.isMonthly());
        Assert.assertEquals(DESCRIPTION, result.getDescription());
        Assert.assertEquals(LOCKED, result.isLocked());
    }

    @Test
    public void testTransFormToIncomeEntityWhenItHasSubCategory() {
        // GIVEN
        Transaction transaction = createExampleTransactionBuilderWithoutSubCategory()
            .withSubCategory(createExampleSubCategory())
            .build();
        MainCategoryEntity mainCategoryEntity = createExampleMainCategoryEntity();
        SubCategoryEntity subCategoryEntity = createExampleSubCategoryEntity();
        control.replay();
        // WHEN
        IncomeEntity result = underTest.transformToIncomeEntity(transaction, mainCategoryEntity, subCategoryEntity);
        // THEN
        control.verify();
        Assert.assertEquals(EXPECTED_ID, result.getId());
        Assert.assertEquals(EXPECTED_TITLE, result.getTitle());
        Assert.assertEquals(EXPECTED_AMOUNT, result.getAmount());
        Assert.assertEquals(EXPECTED_CURRENCY, result.getCurrency());
        Assert.assertNotNull(result.getMainCategoryEntity());
        Assert.assertNotNull(result.getSubCategoryEntity());
        Assert.assertEquals(EXPECTED_DATE, result.getDate());
        Assert.assertEquals(EXPECTED_END_DATE, result.getEndDate());
        Assert.assertEquals(MONTHLY, result.isMonthly());
        Assert.assertEquals(DESCRIPTION, result.getDescription());
        Assert.assertEquals(LOCKED, result.isLocked());
    }

    private IncomeEntity createExampleIncomeEntity() {
        IncomeEntity incomeEntity = new IncomeEntity();
        incomeEntity.setId(EXPECTED_ID);
        incomeEntity.setTitle(EXPECTED_TITLE);
        incomeEntity.setAmount(EXPECTED_AMOUNT);
        incomeEntity.setCurrency(EXPECTED_CURRENCY);
        incomeEntity.setMainCategoryEntity(createExampleMainCategoryEntity());
        incomeEntity.setDate(EXPECTED_DATE);
        incomeEntity.setEndDate(EXPECTED_END_DATE);
        incomeEntity.setMonthly(MONTHLY);
        incomeEntity.setDescription(DESCRIPTION);
        incomeEntity.setLocked(LOCKED);
        return incomeEntity;
    }

    private MainCategoryEntity createExampleMainCategoryEntity() {
        return new MainCategoryEntity(CATEGORY_ID, CATEGORY_NAME, INCOME, new HashSet<>());
    }

    private SubCategoryEntity createExampleSubCategoryEntity() {
        return new SubCategoryEntity(CATEGORY_ID, CATEGORY_NAME, INCOME);
    }

    private MainCategory createExampleMainCategory() {
        return MainCategory.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(new HashSet<>())
            .build();
    }

    private SubCategory createExampleSubCategory() {
        return SubCategory.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(INCOME)
            .build();
    }

    private Transaction.Builder createExampleTransactionBuilderWithoutSubCategory() {
        return Transaction.builder()
            .withId(EXPECTED_ID)
            .withTitle(EXPECTED_TITLE)
            .withTransactionType(INCOME)
            .withAmount(EXPECTED_AMOUNT)
            .withCurrency(EXPECTED_CURRENCY)
            .withMainCategory(createExampleMainCategory())
            .withDate(EXPECTED_DATE.toLocalDate())
            .withEndDate(EXPECTED_END_DATE.toLocalDate())
            .withMonthly(MONTHLY)
            .withDescription(DESCRIPTION)
            .withLocked(LOCKED);
    }

}