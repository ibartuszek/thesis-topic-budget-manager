package hu.elte.bm.calculationservice.forexclient;

import static hu.elte.bm.transactionservice.Currency.EUR;
import static hu.elte.bm.transactionservice.Currency.HUF;
import static hu.elte.bm.transactionservice.Currency.USD;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.gson.Gson;

import hu.elte.bm.transactionservice.Currency;

@RunWith(MockitoJUnitRunner.class)
class ForexClientTest {

    private static final double USD_EUR_RATE = 0.90878d;
    private static final double EUR_USD_RATE = 1 / USD_EUR_RATE;
    private static final double USD_HUF_RATE = 303.709769d;
    private static final double HUF_USD_RATE = 1 / USD_HUF_RATE;
    private static final double EUR_HUF_RATE = EUR_USD_RATE * USD_HUF_RATE;
    private static final double HUF_EUR_RATE = 1 / EUR_HUF_RATE;
    private static final String RESPONSE_STRING = "{\"rates\":"
            + "{\"USDEUR\":{\"rate\":0.90878,\"timestamp\":1573675086},"
            + "\"USDHUF\":{\"rate\":303.709769,\"timestamp\":1573675086}},"
            + "\"code\":200}";

    @InjectMocks
    private ForexClient underTest;

    @Mock
    private ForexProxy forexProxy;

    @Test
    public void testGetExchangeRates() {
        // GIVEN

        List<Rate> expectedRates = new ArrayList<>();
        expectedRates.add(createRate(USD, EUR, USD_EUR_RATE));
        expectedRates.add(createRate(EUR, USD, EUR_USD_RATE));
        expectedRates.add(createRate(USD, HUF, USD_HUF_RATE));
        expectedRates.add(createRate(HUF, USD, HUF_USD_RATE));
        expectedRates.add(createRate(EUR, HUF, EUR_HUF_RATE));
        expectedRates.add(createRate(HUF, EUR, HUF_EUR_RATE));

        // WHEN
        var result = underTest.getExchangeRates();

        // THEN

    }

    private ForexResponse createExampleForexResponse() {
        return new Gson().fromJson(RESPONSE_STRING, ForexResponse.class);
    }

    private Rate createRate(final Currency originalCurrency, final Currency newCurrency, final double rate) {
        return Rate.builder()
                .withOriginalCurrency(originalCurrency)
                .withNewCurrency(newCurrency)
                .withRate(rate)
                .build();
    }
}