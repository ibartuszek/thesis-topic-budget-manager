package hu.elte.bm.transactionservice.dal.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntity;
import hu.elte.bm.transactionservice.dal.transaction.transactionEntityContext.TransactionEntityContext;
import hu.elte.bm.transactionservice.dal.transaction.transactionEntityContext.TransactionEntityContextFactory;
import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;

public class DefaultIncomeDaoTest {

    private static final LocalDate START = LocalDate.now().minusDays(5);
    private static final LocalDate END = LocalDate.now().minusDays(5);
    private static final long EXPECTED_ID = 1L;
    private static final String EXPECTED_TITLE = "title";
    private static final double EXPECTED_AMOUNT = 100.0d;
    private static final Currency EXPECTED_CURRENCY = Currency.EUR;
    private static final Date EXPECTED_DATE = Date.valueOf(LocalDate.now().minusDays(1));
    private static final long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "category";
    private static final Long USER_ID = 1L;

    private DefaultIncomeDao underTest;

    private IMocksControl control;
    private IncomeRepository incomeRepository;
    private TransactionEntityContextFactory contextFactory;
    private TransactionEntityTransformer transformer;

    @BeforeClass
    public void setup() {
        control = EasyMock.createControl();
        incomeRepository = control.createMock(IncomeRepository.class);
        contextFactory = control.createMock(TransactionEntityContextFactory.class);
        transformer = control.createMock(TransactionEntityTransformer.class);
        underTest = new DefaultIncomeDao(incomeRepository, contextFactory, transformer);
    }

    @BeforeMethod
    public void reset() {
        control.reset();
    }

    @AfterMethod
    public void verify() {
        control.verify();
    }

    @Test
    public void testFindAllWhenRepositoryReturnsWithEmptyList() {
        // GIVEN
        EasyMock.expect(incomeRepository.findAll(Date.valueOf(START), Date.valueOf(END), USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findAll(START, END, USER_ID);
        // THEN
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void testFindAll() {
        // GIVEN
        List<IncomeEntity> incomeEntityList = createExampleIncomeEntityList();
        List<Transaction> expectedTransactionList = List.of(createTransactionBuilderWithDefaultValues().build());
        EasyMock.expect(incomeRepository.findAll(Date.valueOf(START), Date.valueOf(END), USER_ID)).andReturn(incomeEntityList);
        EasyMock.expect(transformer.transformToTransaction(incomeEntityList.get(0))).andReturn(expectedTransactionList.get(0));
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findAll(START, END, USER_ID);
        // THEN
        Assert.assertEquals(result, expectedTransactionList);
    }

    @Test
    public void testFindByIdWhenCannotBeFound() {
        // GIVEN
        EasyMock.expect(incomeRepository.findByIdAndUserId(EXPECTED_ID, USER_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.findById(EXPECTED_ID, USER_ID);
        // THEN
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testFindById() {
        // GIVEN
        Optional<IncomeEntity> incomeEntityFromRepository = Optional.ofNullable(createExampleIncomeEntity());
        Optional<Transaction> expectedResult = Optional.of(createTransactionBuilderWithDefaultValues().build());
        EasyMock.expect(incomeRepository.findByIdAndUserId(EXPECTED_ID, USER_ID)).andReturn(incomeEntityFromRepository);
        EasyMock.expect(transformer.transformToTransaction(incomeEntityFromRepository.get())).andReturn(expectedResult.get());
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.findById(EXPECTED_ID, USER_ID);
        // THEN
        Assert.assertEquals(result, expectedResult);
    }

    @Test
    public void testFindByTitleWhenRepositoryReturnsWithEmptyList() {
        // GIVEN
        List<IncomeEntity> incomeEntityList = createExampleIncomeEntityList();
        List<Transaction> expectedTransactionList = List.of(createTransactionBuilderWithDefaultValues().build());
        EasyMock.expect(incomeRepository.findByTitleAndUserId(EXPECTED_TITLE, USER_ID)).andReturn(incomeEntityList);
        EasyMock.expect(transformer.transformToTransaction(incomeEntityList.get(0))).andReturn(expectedTransactionList.get(0));
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findByTitle(EXPECTED_TITLE, USER_ID);
        // THEN
        Assert.assertEquals(result, expectedTransactionList);
    }

    @Test
    public void testFindByTitle() {
        // GIVEN
        EasyMock.expect(incomeRepository.findByTitleAndUserId(EXPECTED_TITLE, USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findByTitle(EXPECTED_TITLE, USER_ID);
        // THEN
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void voidTestSave() {
        // GIVEN
        Transaction transactionToSave = createTransactionBuilderWithDefaultValues()
            .withId(null)
            .withSubCategory(createExampleSubCategory())
            .build();
        IncomeEntity transformedEntity = createExampleIncomeEntity();
        MainCategoryEntity mainCategoryEntity = transformedEntity.getMainCategoryEntity();
        SubCategoryEntity subCategoryEntity = createExampleSubCategoryEntity();
        transformedEntity.setId(null);
        transformedEntity.setSubCategoryEntity(subCategoryEntity);
        IncomeEntity responseEntity = createExampleIncomeEntity();
        Transaction expectedTransaction = createTransactionBuilderWithDefaultValues().build();
        TransactionEntityContext context = TransactionEntityContext.builder()
            .withTransaction(transactionToSave)
            .withMainCategoryEntity(mainCategoryEntity)
            .withSubCategoryEntity(subCategoryEntity)
            .withUserId(USER_ID)
            .build();
        EasyMock.expect(contextFactory.create(transactionToSave, USER_ID)).andReturn(context);
        EasyMock.expect(transformer.transformToIncomeEntity(context)).andReturn(transformedEntity);
        EasyMock.expect(incomeRepository.save(transformedEntity)).andReturn(responseEntity);
        EasyMock.expect(transformer.transformToTransaction(responseEntity)).andReturn(expectedTransaction);
        control.replay();
        // WHEN
        Transaction result = underTest.save(transactionToSave, USER_ID);
        // THEN
        Assert.assertEquals(result, expectedTransaction);
    }

    @Test
    public void voidTestDelete() {
        // GIVEN
        Transaction transactionToDelete = createTransactionBuilderWithDefaultValues().build();
        IncomeEntity transformedEntity = createExampleIncomeEntity();
        Transaction expectedTransaction = createTransactionBuilderWithDefaultValues().build();
        MainCategoryEntity mainCategoryEntity = transformedEntity.getMainCategoryEntity();
        TransactionEntityContext context = TransactionEntityContext.builder()
            .withTransaction(transactionToDelete)
            .withMainCategoryEntity(mainCategoryEntity)
            .withUserId(USER_ID)
            .build();
        EasyMock.expect(contextFactory.create(transactionToDelete, USER_ID)).andReturn(context);
        EasyMock.expect(transformer.transformToIncomeEntity(context)).andReturn(transformedEntity);
        incomeRepository.delete(transformedEntity);
        control.replay();
        // WHEN
        Transaction result = underTest.delete(transactionToDelete, USER_ID);
        // THEN
        Assert.assertEquals(result, expectedTransaction);
    }

    private List<IncomeEntity> createExampleIncomeEntityList() {
        List<IncomeEntity> incomeEntityList = new ArrayList<>();
        incomeEntityList.add(createExampleIncomeEntity());
        return incomeEntityList;
    }

    private IncomeEntity createExampleIncomeEntity() {
        IncomeEntity incomeEntity = new IncomeEntity();
        incomeEntity.setId(EXPECTED_ID);
        incomeEntity.setTitle(EXPECTED_TITLE);
        incomeEntity.setAmount(EXPECTED_AMOUNT);
        incomeEntity.setCurrency(EXPECTED_CURRENCY);
        incomeEntity.setDate(EXPECTED_DATE);
        incomeEntity.setMainCategoryEntity(createExampleMainCategoryEntity());
        return incomeEntity;
    }

    private MainCategoryEntity createExampleMainCategoryEntity() {
        return MainCategoryEntity.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategoryEntitySet(new HashSet<>())
            .withUserId(USER_ID)
            .build();
    }

    private SubCategoryEntity createExampleSubCategoryEntity() {
        return SubCategoryEntity.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withUserId(USER_ID)
            .build();
    }

    private Transaction.Builder createTransactionBuilderWithDefaultValues() {
        return Transaction.builder()
            .withId(EXPECTED_ID)
            .withTitle(EXPECTED_TITLE)
            .withTransactionType(INCOME)
            .withAmount(EXPECTED_AMOUNT)
            .withCurrency(EXPECTED_CURRENCY)
            .withMainCategory(createMainCategoryBuilderWithDefaultValues().build())
            .withDate(EXPECTED_DATE.toLocalDate());
    }

    private MainCategory.Builder createMainCategoryBuilderWithDefaultValues() {
        return MainCategory.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(new HashSet<>());
    }

    private SubCategory createExampleSubCategory() {
        return SubCategory.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(INCOME)
            .build();
    }

}
