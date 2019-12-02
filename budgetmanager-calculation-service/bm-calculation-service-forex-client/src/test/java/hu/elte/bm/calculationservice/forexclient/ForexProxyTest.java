package hu.elte.bm.calculationservice.forexclient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class ForexProxyTest {

    private static final String BASE_URL = "http://freeforexapi.com/api/live";
    private static final String PAIRS = "USDEUR,USDHUF";
    private static final String CALLED_URL = BASE_URL + "?pairs=" + PAIRS;
    private static final int EXPECTED_CODE = 200;
    private static final double USD_EUR_RATE = 0.90878d;
    private static final int USD_EUR_TIMESTAMP = 1573675086;
    private static final double USD_HUF_RATE = 303.709769d;
    private static final int USD_HUF_TIMESTAMP = 1573675086;
    private static final double FALL_BACK_USD_EUR_RATE = 1.0d;
    private static final double FALL_BACK_USD_HUF_RATE = 2.0d;
    private static final String EXPECTED_RESPONSE_STRING = "{\"rates\":"
        + "{\"USDEUR\":{\"rate\":0.90878,\"timestamp\":1573675086},"
        + "\"USDHUF\":{\"rate\":303.709769,\"timestamp\":1573675086}},"
        + "\"code\":200}";
    private static final String BLACKLIST_RESPONSE_STRING = "{\n"
        + "  \"rates\": null,\n"
        + "  \"code\": 200\n"
        + "}";

    @InjectMocks
    private ForexProxy underTest;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(underTest, "baseUrl", BASE_URL);
        ReflectionTestUtils.setField(underTest, "usdeur", 1.00d);
        ReflectionTestUtils.setField(underTest, "usdhuf", 2.00d);
    }

    @Test
    public void testGetForexResponseWhenServerNotRespond() {
        // GIVEN
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.REQUEST_TIMEOUT);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, String.class)).thenReturn(responseEntity);
        // WHEN
        Assertions.assertThrows(ForexClientException.class, () -> underTest.getForexResponse());
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, String.class);
    }

    @Test
    public void testGetForexResponseWhenServerSendsEmptyString() {
        // GIVEN
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, String.class)).thenReturn(responseEntity);
        // WHEN
        Assertions.assertThrows(ForexClientException.class, () -> underTest.getForexResponse());
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, String.class);
    }

    @Test
    public void testGetForexResponseWhenServerSendsAList() {
        // GIVEN
        ForexResponse expected = createExpectedForexResponse();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(EXPECTED_RESPONSE_STRING, HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, String.class)).thenReturn(responseEntity);
        // WHEN
        var result = underTest.getForexResponse();
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, String.class);
        Assertions.assertEquals(expected.getCode(), result.getCode());
        Assertions.assertEquals(expected.getRates().getUSDEUR(), result.getRates().getUSDEUR());
        Assertions.assertEquals(expected.getRates().getUSDHUF(), result.getRates().getUSDHUF());
    }

    @Test
    public void testGetForexResponseWhenServerIsBlackListed() {
        // GIVEN
        ForexResponse expected = createExpectedFallBackForexResponse();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(BLACKLIST_RESPONSE_STRING, HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, String.class)).thenReturn(responseEntity);
        // WHEN
        var result = underTest.getForexResponse();
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, String.class);
        Assertions.assertEquals(expected.getCode(), result.getCode());
        Assertions.assertEquals(expected.getRates().getUSDEUR(), result.getRates().getUSDEUR());
        Assertions.assertEquals(expected.getRates().getUSDHUF(), result.getRates().getUSDHUF());
    }

    private ForexResponse createExpectedForexResponse() {
        ForexResponse expected = new ForexResponse();
        expected.setCode(EXPECTED_CODE);
        ForexRates expectedRates = new ForexRates();
        expected.setRates(expectedRates);
        ForexRate expectedUsdEur = ForexRate.builder()
            .withRate(USD_EUR_RATE)
            .withTimestamp(USD_EUR_TIMESTAMP)
            .build();
        expectedRates.setUSDEUR(expectedUsdEur);
        ForexRate expectedUsdHuf = ForexRate.builder()
            .withRate(USD_HUF_RATE)
            .withTimestamp(USD_HUF_TIMESTAMP)
            .build();
        expectedRates.setUSDHUF(expectedUsdHuf);
        return expected;
    }

    private ForexResponse createExpectedFallBackForexResponse() {
        ForexResponse expected = new ForexResponse();
        expected.setCode(EXPECTED_CODE);
        ForexRates expectedRates = new ForexRates();
        expected.setRates(expectedRates);
        ForexRate expectedUsdEur = ForexRate.builder()
            .withRate(FALL_BACK_USD_EUR_RATE)
            .build();
        expectedRates.setUSDEUR(expectedUsdEur);
        ForexRate expectedUsdHuf = ForexRate.builder()
            .withRate(FALL_BACK_USD_HUF_RATE)
            .build();
        expectedRates.setUSDHUF(expectedUsdHuf);
        return expected;
    }
}
