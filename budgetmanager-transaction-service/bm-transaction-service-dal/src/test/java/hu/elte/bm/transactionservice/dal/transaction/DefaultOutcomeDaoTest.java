package hu.elte.bm.transactionservice.dal.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

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

public class DefaultOutcomeDaoTest {

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

    private DefaultOutcomeDao underTest;

    private IMocksControl control;
    private OutcomeRepository outcomeRepository;
    private TransactionEntityContextFactory contextFactory;
    private TransactionEntityTransformer transformer;

    @BeforeClass
    public void setup() {
        control = EasyMock.createControl();
        outcomeRepository = control.createMock(OutcomeRepository.class);
        contextFactory = control.createMock(TransactionEntityContextFactory.class);
        transformer = control.createMock(TransactionEntityTransformer.class);
        underTest = new DefaultOutcomeDao(outcomeRepository, contextFactory, transformer);
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
        EasyMock.expect(outcomeRepository.findAll(Date.valueOf(START), Date.valueOf(END), USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findAll(START, END, USER_ID);
        // THEN
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void testFindAll() {
        // GIVEN
        List<OutcomeEntity> outcomeEntityList = createExampleOutcomeEntityList();
        List<Transaction> expectedTransactionList = new ArrayList<>();
        expectedTransactionList.add(createTransactionBuilderWithDefaultValues().build());
        EasyMock.expect(outcomeRepository.findAll(Date.valueOf(START), Date.valueOf(END), USER_ID)).andReturn(outcomeEntityList);
        EasyMock.expect(transformer.transformToTransaction(outcomeEntityList.get(0))).andReturn(expectedTransactionList.get(0));
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findAll(START, END, USER_ID);
        // THEN
        Assert.assertEquals(result, expectedTransactionList);
    }

    @Test
    public void testFindByIdWhenCannotBeFound() {
        // GIVEN
        EasyMock.expect(outcomeRepository.findByIdAndUserId(EXPECTED_ID, USER_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.findById(EXPECTED_ID, USER_ID);
        // THEN
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testFindById() {
        // GIVEN
        Optional<OutcomeEntity> outcomeEntityFromRepository = Optional.ofNullable(createExampleOutcomeEntity());
        Optional<Transaction> expectedResult = Optional.of(createTransactionBuilderWithDefaultValues().build());
        EasyMock.expect(outcomeRepository.findByIdAndUserId(EXPECTED_ID, USER_ID)).andReturn(outcomeEntityFromRepository);
        EasyMock.expect(transformer.transformToTransaction(outcomeEntityFromRepository.get())).andReturn(expectedResult.get());
        control.replay();
        // WHEN
        Optional<Transaction> result = underTest.findById(EXPECTED_ID, USER_ID);
        // THEN
        Assert.assertEquals(result, expectedResult);
    }

    @Test
    public void testFindByTitleWhenRepositoryReturnsWithEmptyList() {
        // GIVEN
        List<OutcomeEntity> outcomeEntityList = createExampleOutcomeEntityList();
        List<Transaction> expectedTransactionList = new ArrayList<>();
        expectedTransactionList.add(createTransactionBuilderWithDefaultValues().build());
        EasyMock.expect(outcomeRepository.findByTitleAndUserId(EXPECTED_TITLE, USER_ID)).andReturn(outcomeEntityList);
        EasyMock.expect(transformer.transformToTransaction(outcomeEntityList.get(0))).andReturn(expectedTransactionList.get(0));
        control.replay();
        // WHEN
        List<Transaction> result = underTest.findByTitle(EXPECTED_TITLE, USER_ID);
        // THEN
        Assert.assertEquals(result, expectedTransactionList);
    }

    @Test
    public void testFindByTitle() {
        // GIVEN
        EasyMock.expect(outcomeRepository.findByTitleAndUserId(EXPECTED_TITLE, USER_ID)).andReturn(Collections.emptyList());
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
        OutcomeEntity transformedEntity = createExampleOutcomeEntity();
        MainCategoryEntity mainCategoryEntity = transformedEntity.getMainCategoryEntity();
        SubCategoryEntity subCategoryEntity = createExampleSubCategoryEntity();
        transformedEntity.setId(null);
        transformedEntity.setSubCategoryEntity(subCategoryEntity);
        OutcomeEntity responseEntity = createExampleOutcomeEntity();
        Transaction expectedTransaction = createTransactionBuilderWithDefaultValues().build();
        TransactionEntityContext context = TransactionEntityContext.builder()
            .withTransaction(transactionToSave)
            .withUserId(USER_ID)
            .withMainCategoryEntity(mainCategoryEntity)
            .withSubCategoryEntity(subCategoryEntity)
            .build();
        EasyMock.expect(contextFactory.create(transactionToSave, USER_ID)).andReturn(context);
        EasyMock.expect(transformer.transformToOutcomeEntity(context)).andReturn(transformedEntity);
        EasyMock.expect(outcomeRepository.save(transformedEntity)).andReturn(responseEntity);
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
        OutcomeEntity transformedEntity = createExampleOutcomeEntity();
        MainCategoryEntity mainCategoryEntity = transformedEntity.getMainCategoryEntity();
        Transaction expectedTransaction = createTransactionBuilderWithDefaultValues().build();
        TransactionEntityContext context = TransactionEntityContext.builder()
            .withTransaction(transactionToDelete)
            .withUserId(USER_ID)
            .withMainCategoryEntity(mainCategoryEntity)
            .build();
        EasyMock.expect(contextFactory.create(transactionToDelete, USER_ID)).andReturn(context);
        EasyMock.expect(transformer.transformToOutcomeEntity(context)).andReturn(transformedEntity);
        outcomeRepository.delete(transformedEntity);
        control.replay();
        // WHEN
        Transaction result = underTest.delete(transactionToDelete, USER_ID);
        // THEN
        Assert.assertEquals(result, expectedTransaction);
    }

    private List<OutcomeEntity> createExampleOutcomeEntityList() {
        List<OutcomeEntity> outcomeEntityList = new ArrayList<>();
        outcomeEntityList.add(createExampleOutcomeEntity());
        return outcomeEntityList;
    }

    private OutcomeEntity createExampleOutcomeEntity() {
        OutcomeEntity outcomeEntity = new OutcomeEntity();
        outcomeEntity.setId(EXPECTED_ID);
        outcomeEntity.setTitle(EXPECTED_TITLE);
        outcomeEntity.setAmount(EXPECTED_AMOUNT);
        outcomeEntity.setCurrency(EXPECTED_CURRENCY);
        outcomeEntity.setDate(EXPECTED_DATE);
        outcomeEntity.setMainCategoryEntity(createExampleMainCategoryEntity());
        return outcomeEntity;
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
            .withTransactionType(OUTCOME)
            .withAmount(EXPECTED_AMOUNT)
            .withCurrency(EXPECTED_CURRENCY)
            .withMainCategory(createMainCategoryBuilderWithDefaultValues().build())
            .withDate(EXPECTED_DATE.toLocalDate());
    }

    private MainCategory.Builder createMainCategoryBuilderWithDefaultValues() {
        return MainCategory.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(OUTCOME)
            .withSubCategorySet(new HashSet<>());
    }

    private SubCategory createExampleSubCategory() {
        return SubCategory.builder()
            .withId(CATEGORY_ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(OUTCOME)
            .build();
    }

}
