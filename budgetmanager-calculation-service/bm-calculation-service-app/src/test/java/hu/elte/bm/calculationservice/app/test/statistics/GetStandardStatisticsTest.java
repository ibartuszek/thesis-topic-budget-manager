package hu.elte.bm.calculationservice.app.test.statistics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import hu.elte.bm.transactionservice.TransactionType;

public class GetStandardStatisticsTest extends AbstractStatisticsTest {

    private static final String URL = "/bm/statistics/getStandardStatistics";
    private static final long STANDARD_SCHEMA_ID = 1L;

    @ParameterizedTest
    @MethodSource("getStandardStatisticsParametersForValidation")
    public void testGetStandardStatisticsWhenRequestParametersAreNotValid(final String userId, final String start, final String end,
        final String exceptionMessage) throws Exception {
        // GIVEN

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.get(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .param("userId", userId)
            .param("startDate", start)
            .param("endDate", end));
        MvcResult result = resultAction.andReturn();
        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        Assertions.assertEquals(exceptionMessage, result.getResolvedException().getMessage());
    }

    @Test
    public void testGetStandardStatisticsWhenSchemaNotFound() throws Exception {
        // GIVEN
        getRepository().deleteById(STANDARD_SCHEMA_ID);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.get(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .param("userId", USER_ID.toString())
            .param("startDate", START.toString())
            .param("endDate", END.toString()));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatus());
        Assertions.assertEquals("Standard schema cannot be found!", result.getContentAsString());
    }

    @Test
    public void testGetStandardStatisticsWhenTransactionServiceUnavailable() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllTransactionsResponse(createDefaultContext(TransactionType.INCOME), HttpStatus.SERVICE_UNAVAILABLE.value(), null);

        // WHEN
        ResultActions resultAction = getMvc().perform(createDefaultMvcRequest());
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), result.getStatus());
        Assertions.assertEquals("Service Unavailable", result.getContentAsString());
    }

    @Test
    public void testGetStandardStatisticsWhenForexClientIsUnavailable() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllTransactionsResponse(createDefaultContext(TransactionType.INCOME), HttpStatus.OK.value(), FIND_ALL_INCOME_FILE);
        getWireMockService().setUpFindAllTransactionsResponse(createDefaultContext(TransactionType.OUTCOME), HttpStatus.OK.value(), FIND_ALL_OUTCOME_FILE);
        getWireMockService().setUpGetExchangeRates(HttpStatus.SERVICE_UNAVAILABLE.value(), null);

        // WHEN
        ResultActions resultAction = getMvc().perform(createDefaultMvcRequest());
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), result.getStatus());
        Assertions.assertEquals("Service Unavailable", result.getContentAsString());
    }

    @Test
    public void testGetStandardStatisticsWhenIncomesAndOutcomesAreEmpty() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.INCOME), HttpStatus.OK.value(), FIND_ALL_TRANSACTION_WITH_EMPTY_BODY);
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.OUTCOME), HttpStatus.OK.value(), FIND_ALL_TRANSACTION_WITH_EMPTY_BODY);
        getWireMockService().setUpGetExchangeRates(HttpStatus.OK.value(), GET_EXCHANGE_RATES_FILE);

        // WHEN
        ResultActions resultAction = getMvc().perform(createDefaultMvcRequest());
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.OK.value(), result.getStatus());
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("statistics/getStandardStatisticsWithEmptyTransactions.json"),
            getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

    @Test
    public void testGetStandardStatisticsWhenIncomesAreEmpty() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.INCOME), HttpStatus.OK.value(), FIND_ALL_TRANSACTION_WITH_EMPTY_BODY);
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.OUTCOME), HttpStatus.OK.value(), FIND_ALL_OUTCOME_FILE);
        getWireMockService().setUpGetExchangeRates(HttpStatus.OK.value(), GET_EXCHANGE_RATES_FILE);

        // WHEN
        ResultActions resultAction = getMvc().perform(createDefaultMvcRequest());
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.OK.value(), result.getStatus());
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("statistics/getStandardStatisticsWithEmptyIncomes.json"),
            getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

    @Test
    public void testGetStandardStatisticsWhenOutcomesAreEmpty() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.INCOME), HttpStatus.OK.value(), FIND_ALL_INCOME_FILE);
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.OUTCOME), HttpStatus.OK.value(), FIND_ALL_TRANSACTION_WITH_EMPTY_BODY);
        getWireMockService().setUpGetExchangeRates(HttpStatus.OK.value(), GET_EXCHANGE_RATES_FILE);

        // WHEN
        ResultActions resultAction = getMvc().perform(createDefaultMvcRequest());
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.OK.value(), result.getStatus());
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("statistics/getStandardStatisticsWithEmptyOutcomes.json"),
            getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

    @Test
    public void testGetStandardStatistics() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.INCOME), HttpStatus.OK.value(), FIND_ALL_INCOME_FILE);
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.OUTCOME), HttpStatus.OK.value(), FIND_ALL_OUTCOME_FILE);
        getWireMockService().setUpGetExchangeRates(HttpStatus.OK.value(), GET_EXCHANGE_RATES_FILE);
        // WHEN
        ResultActions resultAction = getMvc().perform(createDefaultMvcRequest());
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.OK.value(), result.getStatus());
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("statistics/getStandardStatistics.json"),
            getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

    private RequestBuilder createDefaultMvcRequest() {
        return MockMvcRequestBuilders.get(URL)
            .param("userId", USER_ID.toString())
            .param("startDate", START.toString())
            .param("endDate", END.toString());
    }

}
