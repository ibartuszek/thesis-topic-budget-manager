package hu.elte.bm.calculationservice.service.statistics.transactionprovider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.elte.bm.calculationservice.forexclient.ForexClient;
import hu.elte.bm.calculationservice.forexclient.Rate;
import hu.elte.bm.calculationservice.transactionserviceclient.TransactionServiceFacade;
import hu.elte.bm.transactionservice.Coordinate;
import hu.elte.bm.transactionservice.Currency;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@ExtendWith(MockitoExtension.class)
public class TransactionProviderTest {

    private static final long USER_ID = 1L;

    private static final Currency ORIGINAL_CURRENCY = Currency.EUR;
    private static final Currency NEW_CURRENCY = Currency.HUF;
    private static final Currency OTHER_CURRENCY = Currency.USD;
    private static final double ORIGINAL_AMOUNT = 100.0d;
    private static final double NEW_AMOUNT = 300.0d;
    private static final double EXCHANGE_RATE = 3.0d;
    private static final double OTHER_EXCHANGE_RATE = 4.0d;

    private static final long TRANSACTION_ID = 1L;
    private static final String TRANSACTION_TITLE = "Title";
    private static final TransactionType TRANSACTION_TYPE = TransactionType.OUTCOME;
    private static final LocalDate TRANSACTION_DATE = LocalDate.now().minusDays(30);
    private static final boolean MONTHLY_TRANSACTION = true;
    private static final LocalDate TRANSACTION_END_DATE = LocalDate.now();
    private static final String TRANSACTION_DESCRIPTION = "Description";
    private static final boolean LOCKED_TRANSACTION = true;
    private static final long PICTURE_ID = 4L;
    private static final double LATITUDE = 123.123d;
    private static final double LONGITUDE = 234.234d;

    private static final long MAIN_CATEGORY_ID = 2L;
    private static final String MAIN_CATEGORY_NAME = "Main category name";
    private static final long SUB_CATEGORY_ID = 3L;
    private static final String SUB_CATEGORY_NAME = "Sub category name";

    @InjectMocks
    private TransactionProvider underTest;

    @Mock
    private TransactionServiceFacade transactionServiceFacade;

    @Mock
    private ForexClient forexClient;

    @Mock
    private TransactionCurrencyExchanger exchanger;

    @Mock
    private MonthlyTransactionProvider monthlyTransactionProvider;

    @Test
    public void testGetTransactions() {
        // GIVEN
        Transaction originalTransaction = createExampleTransaction(ORIGINAL_CURRENCY, ORIGINAL_AMOUNT);
        Transaction expectedTransaction = createExampleTransaction(NEW_CURRENCY, NEW_AMOUNT);
        List<Rate> exchangeRates = createExchangeRates();
        Mockito.when(transactionServiceFacade.getTransactions(TRANSACTION_TYPE, USER_ID, TRANSACTION_DATE, TRANSACTION_END_DATE))
            .thenReturn(List.of(originalTransaction));
        Mockito.when(forexClient.getExchangeRates()).thenReturn(exchangeRates);
        Mockito.when(exchanger.exchangeCurrency(originalTransaction, NEW_CURRENCY, exchangeRates)).thenReturn(expectedTransaction);
        Mockito.when(monthlyTransactionProvider.provide(List.of(expectedTransaction), TRANSACTION_DATE, TRANSACTION_END_DATE))
                .thenReturn(List.of(expectedTransaction));
        // WHEN
        var result = underTest.getTransactions(TRANSACTION_TYPE, createContext());
        // THEN
        Mockito.verify(transactionServiceFacade).getTransactions(TRANSACTION_TYPE, USER_ID, TRANSACTION_DATE, TRANSACTION_END_DATE);
        Mockito.verify(forexClient).getExchangeRates();
        Mockito.verify(exchanger).exchangeCurrency(originalTransaction, NEW_CURRENCY, exchangeRates);
        Mockito.verify(monthlyTransactionProvider).provide(List.of(expectedTransaction), TRANSACTION_DATE, TRANSACTION_END_DATE);
        Assertions.assertEquals(List.of(expectedTransaction), result);
    }

    private TransactionProviderContext createContext() {
        return TransactionProviderContext.builder()
            .withUserId(USER_ID)
            .withCurrency(NEW_CURRENCY)
            .withStart(TRANSACTION_DATE)
            .withEnd(TRANSACTION_END_DATE)
            .build();
    }

    private Transaction createExampleTransaction(final Currency currency, final double amount) {
        SubCategory subCategory = SubCategory.builder()
            .withId(SUB_CATEGORY_ID)
            .withName(SUB_CATEGORY_NAME)
            .withTransactionType(TRANSACTION_TYPE)
            .build();
        MainCategory mainCategory = MainCategory.builder()
            .withId(MAIN_CATEGORY_ID)
            .withName(MAIN_CATEGORY_NAME)
            .withTransactionType(TRANSACTION_TYPE)
            .withSubCategorySet(Set.of(subCategory))
            .build();
        Coordinate coordinate = Coordinate.builder()
            .withLatitude(LATITUDE)
            .withLongitude(LONGITUDE)
            .build();
        return Transaction.builder()
            .withId(TRANSACTION_ID)
            .withTitle(TRANSACTION_TITLE)
            .withTransactionType(TRANSACTION_TYPE)
            .withAmount(amount)
            .withCurrency(currency)
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory)
            .withDate(TRANSACTION_DATE)
            .withMonthly(MONTHLY_TRANSACTION)
            .withEndDate(TRANSACTION_END_DATE)
            .withDescription(TRANSACTION_DESCRIPTION)
            .withLocked(LOCKED_TRANSACTION)
            .withPictureId(PICTURE_ID)
            .withCoordinate(coordinate)
            .build();
    }

    private List<Rate> createExchangeRates() {
        List<Rate> exchangeRates = new ArrayList<>();
        exchangeRates.add(createRate(ORIGINAL_CURRENCY, OTHER_CURRENCY, OTHER_EXCHANGE_RATE));
        exchangeRates.add(createRate(NEW_CURRENCY, OTHER_CURRENCY, OTHER_EXCHANGE_RATE));
        exchangeRates.add(createRate(ORIGINAL_CURRENCY, NEW_CURRENCY, EXCHANGE_RATE));
        return exchangeRates;
    }

    private Rate createRate(final Currency originalCurrency, final Currency newCurrency, final double rate) {
        return Rate.builder()
            .withOriginalCurrency(originalCurrency)
            .withNewCurrency(newCurrency)
            .withRate(rate)
            .build();
    }

}
