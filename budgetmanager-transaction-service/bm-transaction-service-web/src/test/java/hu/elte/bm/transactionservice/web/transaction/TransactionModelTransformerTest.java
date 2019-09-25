package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.commonpack.validator.ModelAmountValue;
import hu.elte.bm.commonpack.validator.ModelDateValue;
import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class TransactionModelTransformerTest {

    private static final Integer TRANSACTION_TITLE_MAXIMUM_LENGTH = 50;
    private static final Integer TRANSACTION_DESCRIPTION_MAXIMUM_LENGTH = 100;
    private static final Integer TRANSACTION_MINIMUM_DAYS_TO_ADD_TO_START_DATE = 1;
    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_TITLE = "title";
    private static final Double DEFAULT_AMOUNT = 100.0d;
    private static final Currency DEFAULT_CURRENCY = Currency.EUR;
    private static final TransactionType DEFAULT_TYPE = TransactionType.INCOME;
    private static final LocalDate DEFAULT_DATE = LocalDate.now();
    private static final LocalDate DEFAULT_START_OF_NEW_PERIOD = LocalDate.now().minusDays(1);
    private static final LocalDate DEFAULT_END_DATE = LocalDate.now().plusYears(1);
    private static final String DEFAULT_DESCRIPTION = "desctiption";
    private static final Set<String> POSSIBLE_CURRENCIES = Arrays.stream(Currency.values()).map(Currency::name).collect(Collectors.toSet());
    private static final Set<String> POSSIBLE_TRANSCATION_TYPES = Arrays.stream(TransactionType.values()).map(TransactionType::name)
        .collect(Collectors.toSet());

    private TransactionModelTransformer underTest;

    private IMocksControl control;
    private MainCategoryModelTransformer mainCategoryModelTransformer;
    private SubCategoryModelTransformer subCategoryModelTransformer;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        mainCategoryModelTransformer = control.createMock(MainCategoryModelTransformer.class);
        subCategoryModelTransformer = control.createMock(SubCategoryModelTransformer.class);
        underTest = new TransactionModelTransformer(mainCategoryModelTransformer, subCategoryModelTransformer);
        ReflectionTestUtils.setField(underTest, "transactionTitleMaximumLength", TRANSACTION_TITLE_MAXIMUM_LENGTH);
        ReflectionTestUtils.setField(underTest, "transactionDescriptionMaximumLength", TRANSACTION_DESCRIPTION_MAXIMUM_LENGTH);
        ReflectionTestUtils.setField(underTest, "minimumDaysToAddToStartDate", TRANSACTION_MINIMUM_DAYS_TO_ADD_TO_START_DATE);
    }

    @Test
    public void testTransformToTransactionModelWhenSubCategoryAndEndDateIsAndDescriptionNull() {
        // GIVEN
        Transaction transaction = createExampleTransactionBuilderWithDefaultValues().build();
        MainCategoryModel mainCategoryModel = MainCategoryModel.builder().build();
        EasyMock.expect(mainCategoryModelTransformer.transformToMainCategoryModel(transaction.getMainCategory())).andReturn(mainCategoryModel);
        control.replay();
        // WHEN
        TransactionModel result = underTest.transformToTransactionModel(transaction, DEFAULT_START_OF_NEW_PERIOD);
        // THEN
        control.verify();
        Assert.assertEquals(result.getId(), DEFAULT_ID);
        Assert.assertEquals(result.getTitle().getValue(), DEFAULT_TITLE);
        Assert.assertEquals(result.getTitle().getMaximumLength(), TRANSACTION_TITLE_MAXIMUM_LENGTH);
        Assert.assertEquals(result.getAmount().getValue(), DEFAULT_AMOUNT);
        Assert.assertTrue(result.getAmount().getPositive());
        Assert.assertEquals(result.getCurrency().getValue(), DEFAULT_CURRENCY.name());
        Assert.assertEquals(result.getCurrency().getPossibleEnumValues(), POSSIBLE_CURRENCIES);
        Assert.assertEquals(result.getTransactionType().getValue(), DEFAULT_TYPE.name());
        Assert.assertNotNull(result.getMainCategory());
        Assert.assertNull(result.getSubCategory());
        Assert.assertEquals(result.getDate().getValue(), DEFAULT_DATE);
        Assert.assertEquals(result.getDate().getPossibleFirstDay(), DEFAULT_START_OF_NEW_PERIOD);
        Assert.assertNull(result.getEndDate());
        Assert.assertNull(result.getDescription());
        Assert.assertFalse(result.isMonthly());
        Assert.assertFalse(result.isLocked());
    }

    @Test
    public void testTransformToTransactionModelWhenSubCategoryAndEndDateAndDescriptionIsNotNull() {
        SubCategory subCategory = SubCategory.builder().build();
        SubCategoryModel subCategoryModel = SubCategoryModel.builder().build();
        Transaction transaction = createExampleTransactionBuilderWithDefaultValues()
            .withSubCategory(subCategory)
            .withEndDate(DEFAULT_END_DATE)
            .withMonthly(true)
            .withLocked(true)
            .withDescription(DEFAULT_DESCRIPTION)
            .build();
        MainCategoryModel mainCategoryModel = MainCategoryModel.builder().build();
        EasyMock.expect(mainCategoryModelTransformer.transformToMainCategoryModel(transaction.getMainCategory())).andReturn(mainCategoryModel);
        EasyMock.expect(subCategoryModelTransformer.transformToSubCategoryModel(subCategory)).andReturn(subCategoryModel);
        control.replay();
        // WHEN
        TransactionModel result = underTest.transformToTransactionModel(transaction, DEFAULT_START_OF_NEW_PERIOD);
        // THEN
        control.verify();
        Assert.assertNotNull(result.getSubCategory());
        Assert.assertEquals(result.getEndDate().getValue(), DEFAULT_END_DATE);
        Assert.assertEquals(result.getEndDate().getPossibleFirstDay(), DEFAULT_DATE.plusDays(1));
        Assert.assertEquals(result.getDescription().getValue(), DEFAULT_DESCRIPTION);
        Assert.assertEquals(result.getDescription().getMaximumLength(), TRANSACTION_DESCRIPTION_MAXIMUM_LENGTH);
        Assert.assertTrue(result.isMonthly());
        Assert.assertTrue(result.isLocked());
    }

    @Test
    public void testTransformToTransactionWhenSubCategoryAndEndDateAndDescriptionIsNull() {
        // GIVEN
        TransactionModel transactionModel = createExampleTransactionModelBuilderWithDefaultValues().build();
        MainCategory mainCategory = MainCategory.builder().build();
        EasyMock.expect(mainCategoryModelTransformer.transformToMainCategory(transactionModel.getMainCategory())).andReturn(mainCategory);
        control.replay();
        // WHEN
        Transaction result = underTest.transformToTransaction(transactionModel);
        // THEN
        control.verify();
        Assert.assertEquals(result.getId(), DEFAULT_ID);
        Assert.assertEquals(result.getTitle(), DEFAULT_TITLE);
        Assert.assertEquals(result.getAmount(), DEFAULT_AMOUNT);
        Assert.assertEquals(result.getCurrency(), DEFAULT_CURRENCY);
        Assert.assertEquals(result.getTransactionType(), DEFAULT_TYPE);
        Assert.assertNotNull(result.getMainCategory());
        Assert.assertNull(result.getSubCategory());
        Assert.assertEquals(result.getDate(), DEFAULT_DATE);
        Assert.assertNull(result.getEndDate());
        Assert.assertNull(result.getDescription());
    }

    @Test
    public void testTransformToTransactionWhenSubCategoryAndEndDateAndDescriptionIsNotNull() {
        // GIVEN
        TransactionModel transactionModel = createExampleTransactionModelBuilderWithDefaultValues()
            .withSubCategory(SubCategoryModel.builder().build())
            .withDescription(ModelStringValue.builder().withValue(DEFAULT_DESCRIPTION).build())
            .withEndDate(ModelDateValue.builder().withValue(DEFAULT_END_DATE).build())
            .withMonthly(true)
            .withLocked(true)
            .build();
        MainCategory mainCategory = MainCategory.builder().build();
        SubCategory subCategory = SubCategory.builder().build();
        EasyMock.expect(mainCategoryModelTransformer.transformToMainCategory(transactionModel.getMainCategory())).andReturn(mainCategory);
        EasyMock.expect(subCategoryModelTransformer.transformToSubCategory(transactionModel.getSubCategory())).andReturn(subCategory);
        control.replay();
        // WHEN
        Transaction result = underTest.transformToTransaction(transactionModel);
        // THEN
        control.verify();
        Assert.assertNotNull(result.getSubCategory());
        Assert.assertEquals(result.getEndDate(), DEFAULT_END_DATE);
        Assert.assertEquals(result.getDescription(), DEFAULT_DESCRIPTION);
        Assert.assertTrue(result.isMonthly());
        Assert.assertTrue(result.isLocked());
    }

    private Transaction.Builder createExampleTransactionBuilderWithDefaultValues() {
        return Transaction.builder()
            .withId(DEFAULT_ID)
            .withTitle(DEFAULT_TITLE)
            .withAmount(DEFAULT_AMOUNT)
            .withCurrency(DEFAULT_CURRENCY)
            .withTransactionType(DEFAULT_TYPE)
            .withMainCategory(MainCategory.builder().build())
            .withDate(DEFAULT_DATE);
    }

    private TransactionModel.Builder createExampleTransactionModelBuilderWithDefaultValues() {
        return TransactionModel.builder()
            .withId(DEFAULT_ID)
            .withTitle(ModelStringValue.builder()
                .withValue(DEFAULT_TITLE)
                .withMaximumLength(TRANSACTION_TITLE_MAXIMUM_LENGTH)
                .build())
            .withAmount(ModelAmountValue.builder()
                .withValue(DEFAULT_AMOUNT)
                .withPositive(true)
                .build())
            .withCurrency(ModelStringValue.builder()
                .withValue(DEFAULT_CURRENCY.name())
                .withPossibleEnumValues(POSSIBLE_CURRENCIES)
                .build())
            .withTransactionType(ModelStringValue.builder()
                .withValue(DEFAULT_TYPE.name())
                .withPossibleEnumValues(POSSIBLE_TRANSCATION_TYPES)
                .build())
            .withMainCategory(MainCategoryModel.builder().build())
            .withDate(ModelDateValue.builder()
                .withValue(DEFAULT_DATE)
                .withAfter(DEFAULT_START_OF_NEW_PERIOD)
                .build());
    }

}
