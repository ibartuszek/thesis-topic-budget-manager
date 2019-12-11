package hu.elte.bm.transactionservice.dal.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

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
import hu.elte.bm.transactionservice.dal.transaction.transactionEntityContext.TransactionEntityContext;
import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.Coordinate;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

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
    private static final Long USER_ID = 1L;
    private static final Double LONGITUDE = 1.00;
    private static final Double LATITUDE = -1.00;
    private static final Coordinate COORDINATE = Coordinate.builder()
        .withLatitude(LATITUDE)
        .withLongitude(LONGITUDE)
        .build();

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
        IncomeEntity incomeEntity = createIncomeExampleEntity();
        EasyMock.expect(mainCategoryEntityTransformer.transformToMainCategory(EasyMock.isA(MainCategoryEntity.class)))
            .andReturn(createExampleMainCategory(INCOME));
        control.replay();
        // WHEN
        Transaction result = underTest.transformToTransaction(incomeEntity);
        // THEN
        control.verify();
        Assert.assertEquals(result.getId(), EXPECTED_ID);
        Assert.assertEquals(result.getTitle(), EXPECTED_TITLE);
        Assert.assertEquals(result.getAmount(), EXPECTED_AMOUNT);
        Assert.assertEquals(result.getCurrency(), EXPECTED_CURRENCY);
        Assert.assertNotNull(result.getMainCategory());
        Assert.assertNull(result.getSubCategory());
        Assert.assertEquals(result.getDate(), EXPECTED_DATE.toLocalDate());
        Assert.assertEquals(result.getEndDate(), EXPECTED_END_DATE.toLocalDate());
        Assert.assertEquals(result.isMonthly(), MONTHLY);
        Assert.assertEquals(result.getDescription(), DESCRIPTION);
        Assert.assertEquals(result.isLocked(), LOCKED);
        Assert.assertEquals(result.getTransactionType(), INCOME);
    }

    @Test
    public void testTransFormToTransactionWhenItHasSubCategoryAndCoordinate() {
        // GIVEN
        OutcomeEntity outcomeEntity = createOutcomeExampleEntity(LONGITUDE, LATITUDE);
        outcomeEntity.setSubCategoryEntity(createExampleSubCategoryEntity(OUTCOME));
        EasyMock.expect(mainCategoryEntityTransformer.transformToMainCategory(EasyMock.isA(MainCategoryEntity.class)))
            .andReturn(createExampleMainCategory(OUTCOME));
        EasyMock.expect(subCategoryEntityTransformer.transformToSubCategory(EasyMock.isA(SubCategoryEntity.class)))
            .andReturn(createExampleSubCategory(OUTCOME));
        control.replay();
        // WHEN
        Transaction result = underTest.transformToTransaction(outcomeEntity);
        // THEN
        control.verify();
        Assert.assertEquals(result.getId(), EXPECTED_ID);
        Assert.assertEquals(result.getTitle(), EXPECTED_TITLE);
        Assert.assertEquals(result.getAmount(), EXPECTED_AMOUNT);
        Assert.assertEquals(result.getCurrency(), EXPECTED_CURRENCY);
        Assert.assertNotNull(result.getMainCategory());
        Assert.assertNotNull(result.getSubCategory());
        Assert.assertEquals(result.getDate(), EXPECTED_DATE.toLocalDate());
        Assert.assertEquals(result.getEndDate(), EXPECTED_END_DATE.toLocalDate());
        Assert.assertEquals(result.isMonthly(), MONTHLY);
        Assert.assertEquals(result.getDescription(), DESCRIPTION);
        Assert.assertEquals(result.isLocked(), LOCKED);
        Assert.assertEquals(result.getTransactionType(), OUTCOME);
        Assert.assertEquals(result.getCoordinate(), COORDINATE);
    }

    @Test
    public void testTransFormToIncomeEntity() {
        // GIVEN
        Transaction transaction = createExampleTransactionBuilderWithoutSubCategory(INCOME)
            .build();
        MainCategoryEntity mainCategoryEntity = createExampleMainCategoryEntity(INCOME);
        TransactionEntityContext transactionEntityContext = TransactionEntityContext.builder()
            .withTransaction(transaction)
            .withUserId(USER_ID)
            .withMainCategoryEntity(mainCategoryEntity)
            .build();
        control.replay();
        // WHEN
        IncomeEntity result = underTest.transformToIncomeEntity(transactionEntityContext);
        // THEN
        control.verify();
        Assert.assertEquals(result.getId(), EXPECTED_ID);
        Assert.assertEquals(result.getTitle(), EXPECTED_TITLE);
        Assert.assertEquals(result.getAmount(), EXPECTED_AMOUNT);
        Assert.assertEquals(result.getCurrency(), EXPECTED_CURRENCY);
        Assert.assertNotNull(result.getMainCategoryEntity());
        Assert.assertNull(result.getSubCategoryEntity());
        Assert.assertEquals(result.getDate(), EXPECTED_DATE);
        Assert.assertEquals(result.getEndDate(), EXPECTED_END_DATE);
        Assert.assertEquals(result.isMonthly(), MONTHLY);
        Assert.assertEquals(result.getDescription(), DESCRIPTION);
        Assert.assertEquals(result.isLocked(), LOCKED);
    }

    @Test
    public void testTransFormToOutcomeEntityWhenItHasSubCategoryAndItHasCoordinate() {
        // GIVEN
        Transaction transaction = createExampleTransactionBuilderWithoutSubCategory(OUTCOME)
            .withSubCategory(createExampleSubCategory(OUTCOME))
            .withCoordinate(COORDINATE)
            .build();
        MainCategoryEntity mainCategoryEntity = createExampleMainCategoryEntity(OUTCOME);
        SubCategoryEntity subCategoryEntity = createExampleSubCategoryEntity(OUTCOME);
        TransactionEntityContext transactionEntityContext = TransactionEntityContext.builder()
            .withTransaction(transaction)
            .withUserId(USER_ID)
            .withMainCategoryEntity(mainCategoryEntity)
            .withSubCategoryEntity(subCategoryEntity)
            .build();
        control.replay();
        // WHEN
        OutcomeEntity result = underTest.transformToOutcomeEntity(transactionEntityContext);
        // THEN
        control.verify();
        Assert.assertEquals(result.getId(), EXPECTED_ID);
        Assert.assertEquals(result.getTitle(), EXPECTED_TITLE);
        Assert.assertEquals(result.getAmount(), EXPECTED_AMOUNT);
        Assert.assertEquals(result.getCurrency(), EXPECTED_CURRENCY);
        Assert.assertNotNull(result.getMainCategoryEntity());
        Assert.assertNotNull(result.getSubCategoryEntity());
        Assert.assertEquals(result.getDate(), EXPECTED_DATE);
        Assert.assertEquals(result.getEndDate(), EXPECTED_END_DATE);
        Assert.assertEquals(result.isMonthly(), MONTHLY);
        Assert.assertEquals(result.getDescription(), DESCRIPTION);
        Assert.assertEquals(result.isLocked(), LOCKED);
        Assert.assertEquals(result.getLatitude(), COORDINATE.getLatitude());
        Assert.assertEquals(result.getLongitude(), COORDINATE.getLongitude());
    }

    private IncomeEntity createIncomeExampleEntity() {
        IncomeEntity incomeEntity = new IncomeEntity();
        incomeEntity.setId(EXPECTED_ID);
        incomeEntity.setTitle(EXPECTED_TITLE);
        incomeEntity.setAmount(EXPECTED_AMOUNT);
        incomeEntity.setCurrency(EXPECTED_CURRENCY);
        incomeEntity.setMainCategoryEntity(createExampleMainCategoryEntity(INCOME));
        incomeEntity.setDate(EXPECTED_DATE);
        incomeEntity.setEndDate(EXPECTED_END_DATE);
        incomeEntity.setMonthly(MONTHLY);
        incomeEntity.setDescription(DESCRIPTION);
        incomeEntity.setLocked(LOCKED);
        return incomeEntity;
    }

    private OutcomeEntity createOutcomeExampleEntity(final Double longitude, final Double latitude) {
        OutcomeEntity outcomeEntity = new OutcomeEntity();
        outcomeEntity.setId(EXPECTED_ID);
        outcomeEntity.setTitle(EXPECTED_TITLE);
        outcomeEntity.setAmount(EXPECTED_AMOUNT);
        outcomeEntity.setCurrency(EXPECTED_CURRENCY);
        outcomeEntity.setMainCategoryEntity(createExampleMainCategoryEntity(OUTCOME));
        outcomeEntity.setDate(EXPECTED_DATE);
        outcomeEntity.setEndDate(EXPECTED_END_DATE);
        outcomeEntity.setMonthly(MONTHLY);
        outcomeEntity.setDescription(DESCRIPTION);
        outcomeEntity.setLocked(LOCKED);
        outcomeEntity.setLongitude(longitude);
        outcomeEntity.setLatitude(latitude);
        return outcomeEntity;
    }

    private MainCategoryEntity createExampleMainCategoryEntity(final TransactionType type) {
        return MainCategoryEntity.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(type)
            .withSubCategoryEntitySet(new HashSet<>())
            .withUserId(USER_ID)
            .build();
    }

    private SubCategoryEntity createExampleSubCategoryEntity(final TransactionType type) {
        return SubCategoryEntity.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(type)
            .withUserId(USER_ID)
            .build();
    }

    private MainCategory createExampleMainCategory(final TransactionType type) {
        return MainCategory.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(type)
            .withSubCategorySet(new HashSet<>())
            .build();
    }

    private SubCategory createExampleSubCategory(final TransactionType type) {
        return SubCategory.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(type)
            .build();
    }

    private Transaction.Builder createExampleTransactionBuilderWithoutSubCategory(final TransactionType type) {
        return Transaction.builder()
            .withId(EXPECTED_ID)
            .withTitle(EXPECTED_TITLE)
            .withTransactionType(type)
            .withAmount(EXPECTED_AMOUNT)
            .withCurrency(EXPECTED_CURRENCY)
            .withMainCategory(createExampleMainCategory(type))
            .withDate(EXPECTED_DATE.toLocalDate())
            .withEndDate(EXPECTED_END_DATE.toLocalDate())
            .withMonthly(MONTHLY)
            .withDescription(DESCRIPTION)
            .withLocked(LOCKED);
    }

}
