package hu.elte.bm.calculationservice.app.test.statistics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.transactionservice.TransactionType;

public class GetSumStatisticsTest extends AbstractStatisticsTest {

    private static final String URL = "/bm/statistics/getCustomStatistics";
    private static final Long SUM_SCHEMA_ID = 5L;
    private static final Long SUM_SCHEMA_ID_WITH_SUB_CATEGORY = 6L;

    @Test
    public void testGetSumStatisticsWhenIncomesAndOutcomesAreEmpty() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.INCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_INCOME_MAIN_CATEGORIES);
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.OUTCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_OUTCOME_MAIN_CATEGORIES);
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
        assertExpectedJsonFileWithDates("statistics/getSumStatisticsWithEmptyTransactions.json", result.getContentAsString());
    }

    @Test
    public void testGetSumStatisticsWhenIncomesAreEmpty() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.INCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_INCOME_MAIN_CATEGORIES);
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.OUTCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_OUTCOME_MAIN_CATEGORIES);
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
        assertExpectedJsonFileWithDates("statistics/getSumStatisticsWithEmptyIncomes.json", result.getContentAsString());
    }

    @Test
    public void testGetSumStatisticsWhenOutcomesAreEmpty() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.INCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_INCOME_MAIN_CATEGORIES);
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.OUTCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_OUTCOME_MAIN_CATEGORIES);
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
        assertExpectedJsonFileWithDates("statistics/getSumStatisticsWithEmptyOutcomes.json", result.getContentAsString());
    }

    @Test
    public void testGetSumStatistics() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.INCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_INCOME_MAIN_CATEGORIES);
        getWireMockService().setUpFindAllMainCategoriesResponse(TransactionType.OUTCOME, USER_ID, HttpStatus.OK.value(), FIND_ALL_OUTCOME_MAIN_CATEGORIES);
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
        assertExpectedJsonFileWithDates("statistics/getSumStatistics.json", result.getContentAsString());
    }

    @Test
    public void testGetSumStatisticsWithSubCategory() throws Exception {
        // GIVEN
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL)
            .param("userId", USER_ID.toString())
            .param("schemaId", SUM_SCHEMA_ID_WITH_SUB_CATEGORY.toString())
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
        assertExpectedJsonFileWithDates("statistics/getSumStatisticsWithSubCategory.json", result.getContentAsString());
    }

    @Test
    public void testGetSumStatisticsWithoutMainCategory() throws Exception {
        // GIVEN
        insertSchemaIntoDb(StatisticsType.SUM, null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL)
            .param("userId", USER_ID.toString())
            .param("schemaId", NEW_SCHEMA_ID.toString())
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
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getStatus());
        Assertions.assertEquals("Something went wrong!", result.getContentAsString());
    }

    @Test
    public void testGetSumStatisticsWithMainCategoryNotFound() throws Exception {
        // GIVEN
        insertSchemaIntoDb(StatisticsType.SUM, INVALID_MAIN_CATEGORY_ID);
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
            .param("schemaId", SUM_SCHEMA_ID.toString())
            .param("startDate", START.toString())
            .param("endDate", END.toString());
    }

}
