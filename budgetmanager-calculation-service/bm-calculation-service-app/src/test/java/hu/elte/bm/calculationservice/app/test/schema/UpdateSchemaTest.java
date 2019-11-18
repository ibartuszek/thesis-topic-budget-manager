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

import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.calculationservice.web.schema.StatisticsSchemaRequestContext;

public class UpdateSchemaTest extends AbstractSchemaTest {

    private static final String URL = "/bm/statistics/schema/update";

    @Test
    public void testUpdateWhenUserIdIsNull() throws Exception {
        // GIVEN
        StatisticsSchemaRequestContext context = createContext(null, createSchemaBuilderWithDefaultValues().build());

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("User id cannot be null!", result.getContentAsString());
    }

    @Test
    public void testUpdateWhenSchemaIsNull() throws Exception {
        // GIVEN
        StatisticsSchemaRequestContext context = createContext(USER_ID, null);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("Schema cannot be null!", result.getContentAsString());
    }

    @Test
    public void testUpdateWhenSchemaIdIsNull() throws Exception {
        // GIVEN
        StatisticsSchema schema = createSchemaBuilderWithDefaultValues()
            .withId(null)
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("Schema's id cannot be null!", result.getContentAsString());
    }

    @ParameterizedTest
    @MethodSource("createSchemaValidationParams")
    public void testUpdateWhenSchemaIsNotValid(final StatisticsSchema.Builder schemaBuilder, final int statusCode, final String exceptionMessage)
        throws Exception {
        // GIVEN
        StatisticsSchema schema = schemaBuilder.build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(statusCode, result.getStatus());
        Assertions.assertEquals(exceptionMessage, result.getContentAsString());
    }

    @Test
    public void testUpdateWhenSchemaIsStandard() throws Exception {
        // GIVEN
        StatisticsSchema schema = createExampleBuilderForUpdate()
            .withType(StatisticsType.STANDARD)
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("Standard schema cannot be modified!", result.getContentAsString());
    }

    @Test
    public void testUpdateWhenSchemaCannotBeFound() throws Exception {
        // GIVEN
        StatisticsSchema schema = createSchemaBuilderWithDefaultValues()
            .withId(INVALID_SCHEMA_ID)
            .withTitle(MODIFIED_TITLE)
            .withMainCategory(createDefaultMainCategoryBuilder().build())
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatus());
        Assertions.assertEquals("Schema cannot be found!", result.getContentAsString());
    }

    @Test
    public void testUpdateWhenSchemaDoesNotHaveAnyChange() throws Exception {
        // GIVEN
        StatisticsSchema schema = createExampleBuilderForUpdate()
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("Schema has no changes!", result.getContentAsString());
    }

    @Test
    public void testUpdateWhenSchemaTitleIsReserved() throws Exception {
        // GIVEN
        StatisticsSchema schema = createExampleBuilderForUpdate()
            .withTitle(RESERVED_SCHEMA_TITLE)
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), result.getStatus());
        Assertions.assertEquals("Schema's title is reserved!", result.getContentAsString());
    }

    @ParameterizedTest
    @MethodSource("createSchemaValidationParamsWithCategories")
    public void testUpdateWhenSchemaHasInvalidCategories(final StatisticsSchema.Builder schemaBuilder, final int statusCode, final String exceptionMessage)
        throws Exception {
        // GIVEN
        StatisticsSchema schema = schemaBuilder.build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);
        getWireMockService().setUpFindAllMainCategoriesResponse(TRANSACTION_TYPE, USER_ID, HttpStatus.OK.value(), FIND_ALL_OUTCOME_MAIN_CATEGORIES);
        getWireMockService().setUpFindAllSubCategoriesResponse(TRANSACTION_TYPE, USER_ID, HttpStatus.OK.value(), FIND_ALL_SUB_CATEGORIES_RESULT_BODY);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(statusCode, result.getStatus());
        Assertions.assertEquals(exceptionMessage, result.getContentAsString());
    }

    @Test
    public void testUpdate() throws Exception {
        // GIVEN
        StatisticsSchema schema = createExampleBuilderForUpdate()
            .withTitle(MODIFIED_TITLE)
            .withMainCategory(createDefaultMainCategoryBuilder().build())
            .withSubCategory(createDefaultSubCategoryBuilder().build())
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);
        getWireMockService().setUpFindAllMainCategoriesResponse(TRANSACTION_TYPE, USER_ID, HttpStatus.OK.value(), FIND_ALL_OUTCOME_MAIN_CATEGORIES);
        getWireMockService().setUpFindAllSubCategoriesResponse(TRANSACTION_TYPE, USER_ID, HttpStatus.OK.value(), FIND_ALL_SUB_CATEGORIES_RESULT_BODY);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.OK.value(), result.getStatus());
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("schema/updateSchemaOk.json"), getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

}
