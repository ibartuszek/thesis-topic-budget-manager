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

import hu.elte.bm.calculationservice.app.test.AbstractCalculationServiceApplicationTest;

public class FindAllSchemaTest extends AbstractCalculationServiceApplicationTest {

    private static final String URL = "/bm/statistics/schema/findAll";
    private static final String FIND_ALL_MAIN_CATEGORIES_WITH_INVALID_MAIN_CATEGORY = "findAllOutcomeMainCategoryWithInvalidMainCategory.json";
    private static final Long STANDARD_SCHEMA_ID = 1L;

    @Test
    public void testFindAllWhenStandardSchemaCannotBeFound() throws Exception {
        // GIVEN
        getRepository().deleteById(STANDARD_SCHEMA_ID);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.get(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .param("userId", USER_ID.toString()));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatus());
        Assertions.assertEquals("Standard schema cannot be found!", result.getContentAsString());
    }

    @Test
    public void testFindAllWhenMainCategoryCannotBeFound() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllMainCategoriesResponse(TRANSACTION_TYPE, USER_ID, HttpStatus.OK.value(), FIND_ALL_MAIN_CATEGORIES_WITH_EMPTY_BODY);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.get(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .param("userId", USER_ID.toString()));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatus());
        Assertions.assertEquals("Main category cannot be found!", result.getContentAsString());
    }

    @Test
    public void testFindAllWhenMainCategoryDoesNotHaveSubCategory() throws Exception {
        // GIVEN
        getWireMockService()
            .setUpFindAllMainCategoriesResponse(TRANSACTION_TYPE, USER_ID, HttpStatus.OK.value(), FIND_ALL_MAIN_CATEGORIES_WITH_INVALID_MAIN_CATEGORY);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.get(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .param("userId", USER_ID.toString()));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        Assertions.assertEquals("Sub category cannot be found!", result.getContentAsString());
    }

    @Test
    public void testFindAll() throws Exception {
        // GIVEN
        getWireMockService().setUpFindAllMainCategoriesResponse(TRANSACTION_TYPE, USER_ID, HttpStatus.OK.value(), FIND_ALL_OUTCOME_MAIN_CATEGORIES);

        // WHEN
        ResultActions resultAction = getMvc().perform(MockMvcRequestBuilders.get(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .param("userId", USER_ID.toString()));
        MockHttpServletResponse result = resultAction.andReturn().getResponse();

        // THEN
        Assertions.assertEquals(HttpStatus.OK.value(), result.getStatus());
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile("schema/findAllSchemaOk.json"), getActualResponseFromResult(result), JSONCompareMode.LENIENT);
    }

}
