package hu.elte.bm.calculationservice.app.test.statistics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import hu.elte.bm.transactionservice.TransactionType;

public class GetCustomStatisticsTest extends AbstractStatisticsTest {

    private static final String URL = "/bm/statistics/getCustomStatistics";

    @ParameterizedTest
    @MethodSource("getCustomStatisticsParametersForValidation")
    public void testGetCustomStatisticsWhenRequestParametersAreNotValid(final String userId, final String schemaId, final String start, final String end,
        final String exceptionMessage) throws Exception {
        // GIVEN

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.get(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .param("userId", userId)
            .param("schemaId", schemaId)
            .param("startDate", start)
            .param("endDate", end));
        MvcResult result = resultAction.andReturn();
        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        Assertions.assertEquals(exceptionMessage, result.getResolvedException().getMessage());
    }

    @Test
    public void testGetCustomStatisticsWhenSchemaNotFound() throws Exception {
        // GIVEN
        getRepository().deleteById(CUSTOM_SCHEMA_ID);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.get(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .param("userId", USER_ID.toString())
            .param("schemaId", CUSTOM_SCHEMA_ID.toString())
            .param("startDate", START.toString())
            .param("endDate", END.toString()));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatus());
        Assertions.assertEquals("Schema cannot be found!", result.getContentAsString());
    }

    @Test
    public void testGetCustomStatisticsWhenTransactionServiceUnavailable() throws Exception {
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
    public void testGetCustomStatisticsWhenForexClientIsUnavailable() throws Exception {
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

    private RequestBuilder createDefaultMvcRequest() {
        return MockMvcRequestBuilders.get(URL)
            .param("userId", USER_ID.toString())
            .param("schemaId", CUSTOM_SCHEMA_ID.toString())
            .param("startDate", START.toString())
            .param("endDate", END.toString());
    }

}
