package hu.elte.bm.authenticationservice.app.test;

import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import hu.elte.bm.authenticationservice.domain.User;

public class UserRegisterTest extends AbstractUserTest {

    private static final String URL = "/bm/users/register";
    private static final Long EXPECTED_ID = 3L;

    @Test
    public void testRegisterWhenIdIsNotNull() throws Exception {
        // GIVEN
        User user = createDefaultUserBuilder(RESERVED_ID).build();

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(user)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("New user's id cannot be null!"));
    }

    @Test(dataProvider = "testDataForValidation")
    public void testRegisterWhenValidationFails(final JsonObject user) throws Exception {
        // GIVEN

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(user)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testRegisterWhenEmailIsReserved() throws Exception {
        // GIVEN
        User user = createDefaultUserBuilder(null)
            .withEmail(RESERVED_EMAIL)
            .build();

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(user)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(MockMvcResultMatchers.content().string("This email has been used already!"));
    }

    @Test
    public void testRegister() throws Exception {
        // GIVEN
        int expectedId = Math.toIntExact(EXPECTED_ID);
        User user = createDefaultUserBuilder(null).build();
        MultiValueMap<String, String> paramsForFindById = new LinkedMultiValueMap<>();
        paramsForFindById.add("id", expectedId + "");

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(user)));
        ResultActions resultForFindById = getMvc().perform(MockMvcRequestBuilders.get(URL_FOR_FIND_BY_ID)
            .header("Authorization", getAccessToken())
            .params(paramsForFindById));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("User registration is completed!")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.successful", Matchers.is(true)));

        resultForFindById.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.id", Matchers.is(expectedId)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(DEFAULT_EMAIL)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.password", Matchers.is(MASKED_PASSWORD)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.firstName", Matchers.is(DEFAULT_FIRST_NAME)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.lastName", Matchers.is(DEFAULT_LAST_NAME)));
    }

}
