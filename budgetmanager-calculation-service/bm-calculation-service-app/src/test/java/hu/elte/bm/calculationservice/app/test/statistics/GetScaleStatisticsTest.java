package hu.elte.bm.calculationservice.app.test.statistics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.transactionservice.TransactionType;

public class GetScaleStatisticsTest extends AbstractStatisticsTest {

    private static final String URL = "/bm/statistics/getCustomStatistics";
    private static final Long SCALE_SCHEMA_ID = 2L;
    private static final Long SCALE_SCHEMA_ID_WITH_MAIN_CATEGORY = 3L;
    private static final Long SCALE_SCHEMA_ID_WITH_SUB_CATEGORY = 4L;

    @Test
    public void testGetScaleStatisticsWhenIncomesAndOutcomesAreEmpty() throws Exception {
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
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("statistics/getScaleStatisticsWithEmptyTransactions.json"),
            getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

    @Test
    public void testGetScaleStatisticsWhenIncomesAreEmpty() throws Exception {
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
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("statistics/getScaleStatisticsWithEmptyIncomes.json"),
            getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

    @Test
    public void testGetScaleStatisticsWhenOutcomesAreEmpty() throws Exception {
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
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("statistics/getScaleStatisticsWithEmptyOutcomes.json"),
            getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

    @Test
    public void testGetScaleStatistics() throws Exception {
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
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("statistics/getScaleStatistics.json"),
            getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

    @Test
    public void testGetScaleStatisticsWithMainCategory() throws Exception {
        // GIVEN
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL)
            .param("userId", USER_ID.toString())
            .param("schemaId", SCALE_SCHEMA_ID_WITH_MAIN_CATEGORY.toString())
            .param("startDate", START.toString())
            .param("endDate", END.toString());
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.INCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_INCOME_MAIN_CATEGORIES);
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.OUTCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_OUTCOME_MAIN_CATEGORIES);
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.INCOME), HttpStatus.OK.value(), FIND_ALL_INCOME_FILE);
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.OUTCOME), HttpStatus.OK.value(), FIND_ALL_OUTCOME_FILE);
        getWireMockService().setUpGetExchangeRates(HttpStatus.OK.value(), GET_EXCHANGE_RATES_FILE);

        // WHEN
        ResultActions resultAction = getMvc().perform(requestBuilder);
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.OK.value(), result.getStatus());
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("statistics/getScaleStatisticsWithMainCategory.json"),
            getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

    @Test
    public void testGetScaleStatisticsWithSubCategory() throws Exception {
        // GIVEN
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL)
            .param("userId", USER_ID.toString())
            .param("schemaId", SCALE_SCHEMA_ID_WITH_SUB_CATEGORY.toString())
            .param("startDate", START.toString())
            .param("endDate", END.toString());
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.INCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_INCOME_MAIN_CATEGORIES);
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.OUTCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_OUTCOME_MAIN_CATEGORIES);
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.INCOME), HttpStatus.OK.value(), FIND_ALL_INCOME_FILE);
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.OUTCOME), HttpStatus.OK.value(), FIND_ALL_OUTCOME_FILE);
        getWireMockService().setUpGetExchangeRates(HttpStatus.OK.value(), GET_EXCHANGE_RATES_FILE);

        // WHEN
        ResultActions resultAction = getMvc().perform(requestBuilder);
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.OK.value(), result.getStatus());
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("statistics/getScaleStatisticsWithSubCategory.json"),
            getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

    @Test
    public void testGetScaleStatisticsWithMainCategoryNotFound() throws Exception {
        // GIVEN
        insertSchemaIntoDb(StatisticsType.SCALE, INVALID_MAIN_CATEGORY_ID);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL)
            .param("userId", USER_ID.toString())
            .param("schemaId", NEW_SCHEMA_ID.toString())
            .param("startDate", START.toString())
            .param("endDate", END.toString());
        getWireMockService()
            .setUpFindAllMainCategoriesResponse(TransactionType.INCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_MAIN_CATEGORIES_WITH_EMPTY_BODY);
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.OUTCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_OUTCOME_MAIN_CATEGORIES);
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.INCOME), HttpStatus.OK.value(), FIND_ALL_INCOME_FILE);
        getWireMockService().setUpFindAllTransactionsResponse(
            createDefaultContext(TransactionType.OUTCOME), HttpStatus.OK.value(), FIND_ALL_OUTCOME_FILE);
        getWireMockService().setUpGetExchangeRates(HttpStatus.OK.value(), GET_EXCHANGE_RATES_FILE);

        // WHEN
        ResultActions resultAction = getMvc().perform(requestBuilder);
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatus());
        Assertions.assertEquals("Main category cannot be found!", result.getContentAsString());
    }

    private RequestBuilder createDefaultMvcRequest() {
        return MockMvcRequestBuilders.get(URL)
            .param("userId", USER_ID.toString())
            .param("schemaId", SCALE_SCHEMA_ID.toString())
            .param("startDate", START.toString())
            .param("endDate", END.toString());
    }

}
