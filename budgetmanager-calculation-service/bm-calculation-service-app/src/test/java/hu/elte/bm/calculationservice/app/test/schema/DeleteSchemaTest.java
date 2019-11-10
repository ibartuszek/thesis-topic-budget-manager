package hu.elte.bm.calculationservice.app.test.schema;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.web.schema.StatisticsSchemaRequestContext;

public class DeleteSchemaTest extends AbstractSchemaTest {

    private static final String URL = "/bm/statistics/schema/delete";

    @Test
    public void testDeleteWhenUserIdIsNull() throws Exception {
        // GIVEN
        StatisticsSchemaRequestContext context = createContext(null, createSchemaBuilderWithDefaultValues().build());

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.delete(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("User id cannot be null!", result.getContentAsString());
    }

    @Test
    public void testDeleteWhenSchemaIsNull() throws Exception {
        // GIVEN
        StatisticsSchemaRequestContext context = createContext(USER_ID, null);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.delete(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("Schema cannot be null!", result.getContentAsString());
    }

    @Test
    public void testDeleteWhenSchemaIdIsNull() throws Exception {
        // GIVEN
        StatisticsSchema schema = createSchemaBuilderWithDefaultValues()
            .withId(null)
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.delete(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("Schema's id cannot be null!", result.getContentAsString());
    }

    @Test
    public void testDeleteWhenSchemaCannotBeFound() throws Exception {
        // GIVEN
        StatisticsSchema schema = createSchemaBuilderWithDefaultValues()
            .withId(INVALID_SCHEMA_ID)
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.delete(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatus());
        Assertions.assertEquals("Schema cannot be found!", result.getContentAsString());
    }

    @Test
    public void testDeleteWhenSchemaHasBeenModified() throws Exception {
        // GIVEN
        StatisticsSchema schema = createExampleBuilderForUpdate()
            .withTitle(MODIFIED_TITLE)
            .build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.delete(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("Schema cannot be changed during deleting!", result.getContentAsString());
    }

    @Test
    public void testDelete() throws Exception {
        // GIVEN
        StatisticsSchema schema = createExampleBuilderForUpdate().build();
        StatisticsSchemaRequestContext context = createContext(USER_ID, schema);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.delete(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.OK.value(), result.getStatus());
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("schema/deleteSchemaOk.json"), getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

}
