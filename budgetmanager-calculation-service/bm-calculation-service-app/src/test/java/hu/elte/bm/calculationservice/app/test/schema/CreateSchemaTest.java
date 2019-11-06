package hu.elte.bm.calculationservice.app.test.schema;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.web.schema.StatisticsSchemaRequestContext;

public class CreateSchemaTest extends AbstractSchemaTest {

    private static final String URL = "/bm/statistics/schema/create";

    @Test
    public void testCreateWhenUserIdIsNull() throws Exception {
        // GIVEN
        StatisticsSchemaRequestContext context = createContext(null, createSchemaBuilderWithDefaultValues().build());

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("User id cannot be null!", result.getContentAsString());
    }

    @Test
    public void testCreateWhenSchemaIsNull() throws Exception {
        // GIVEN
        StatisticsSchemaRequestContext context = createContext(USER_ID, null);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("Schema cannot be null!", result.getContentAsString());
    }

    @ParameterizedTest
    @MethodSource("createSchemaValidationParams")
    public void testCreateWhenSchemaIsNotValid(final StatisticsSchema.Builder schemaBuilder, final int statusCode, final String exceptionMessage)
        throws Exception {
        // GIVEN
        StatisticsSchema schema = schemaBuilder.withId(null)
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(statusCode, result.getStatus());
        Assertions.assertEquals(exceptionMessage, result.getContentAsString());
    }

    @Test
    public void testCreateWhenSchemaTitleIsReserved() throws Exception {
        // GIVEN
        StatisticsSchema schema = createSchemaBuilderWithDefaultValues()
            .withId(null)
            .withTitle(RESERVED_SCHEMA_TITLE)
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), result.getStatus());
        Assertions.assertEquals("Schema's title is reserved!", result.getContentAsString());
    }

    @ParameterizedTest
    @MethodSource("createSchemaValidationParamsWithCategories")
    public void testCreateWhenSchemaHasInvalidCategories(final StatisticsSchema.Builder schemaBuilder, final int statusCode, final String exceptionMessage)
        throws Exception {
        // GIVEN
        StatisticsSchema schema = schemaBuilder.withId(null)
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);
        getWireMockService().setUpFindAllMainCategoriesResponse(TRANSACTION_TYPE, USER_ID, HttpStatus.OK.value(), FIND_ALL_MAIN_CATEGORIES_RESULT_BODY);
        getWireMockService().setUpFindAllSubCategoriesResponse(TRANSACTION_TYPE, USER_ID, HttpStatus.OK.value(), FIND_ALL_SUB_CATEGORIES_RESULT_BODY);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(statusCode, result.getStatus());
        Assertions.assertEquals(exceptionMessage, result.getContentAsString());
    }

    @Test
    public void testCreate() throws Exception {
        // GIVEN
        StatisticsSchema schema = createSchemaBuilderWithDefaultValues()
            .withId(null)
            .withMainCategory(createDefaultMainCategoryBuilder().build())
            .withSubCategory(createDefaultSubCategoryBuilder().build())
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);
        getWireMockService().setUpFindAllMainCategoriesResponse(TRANSACTION_TYPE, USER_ID, HttpStatus.OK.value(), FIND_ALL_MAIN_CATEGORIES_RESULT_BODY);
        getWireMockService().setUpFindAllSubCategoriesResponse(TRANSACTION_TYPE, USER_ID, HttpStatus.OK.value(), FIND_ALL_SUB_CATEGORIES_RESULT_BODY);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.OK.value(), result.getStatus());
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("createSchemaOk.json"), getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

}
