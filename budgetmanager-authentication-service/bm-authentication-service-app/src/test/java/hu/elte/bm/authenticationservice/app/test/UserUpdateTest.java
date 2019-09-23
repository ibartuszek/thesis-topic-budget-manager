package hu.elte.bm.authenticationservice.app.test;

import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import hu.elte.bm.authenticationservice.domain.User;

public class UserUpdateTest extends AbstractUserTest {

    private static final String URL = "/bm/users/update";

    private static final String OTHER_RESERVED_EMAIL = "exampleEmail2@mail.com";
    private static final String NEW_EMAIL = "exampleEmail4@mail.com";
    private static final String NEW_FIRST_NAME = "Johny";
    private static final String NEW_LAST_NAME = "Do";
    private static final String NEW_PASSWORD = "password1";

    @Test
    @WithMockUser
    public void testUpdateWhenIdIsNull() throws Exception {
        // GIVEN
        User user = createDefaultUserBuilder(null).build();

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getAccessToken())
            .content(createRequestBodyWithDefaultUserId(user)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("User's id cannot be null!"));
    }

    @Test(dataProvider = "testDataForValidation")
    public void testUpdateWhenValidationFails(final JsonObject user, final String responseErrorMessage) throws Exception {
        // GIVEN
        user.add("id", new Gson().toJsonTree(USER_ID));

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getAccessToken())
            .content(createRequestBody(USER_ID, user)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(responseErrorMessage));
    }

    @Test
    public void testUpdateWhenUserCannotBeFound() throws Exception {
        // GIVEN
        User user = createDefaultUserBuilder(INVALID_ID)
            .withEmail(NEW_EMAIL)
            .build();
        JsonObject jsonObject = new Gson().toJsonTree(user).getAsJsonObject();

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getAccessToken())
            .content(createRequestBody(USER_ID, jsonObject)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("User cannot be found in the repository!"));
    }

    @Test
    public void testUpdateWhenEmailIsReserved() throws Exception {
        // GIVEN
        User user = createDefaultUserBuilder(RESERVED_ID)
            .withEmail(OTHER_RESERVED_EMAIL)
            .build();
        JsonObject jsonObject = new Gson().toJsonTree(user).getAsJsonObject();

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getAccessToken())
            .content(createRequestBody(USER_ID, jsonObject)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(MockMvcResultMatchers.content().string("This email has been used already!"));
    }

    @Test
    public void testUpdateWhenEmailIsUnchanged() throws Exception {
        // GIVEN
        int id = Math.toIntExact(RESERVED_ID);
        User user = createDefaultUserBuilder(RESERVED_ID)
            .withEmail(RESERVED_EMAIL)
            .withFirstName(NEW_FIRST_NAME)
            .withLastName(NEW_LAST_NAME)
            .build();
        JsonObject jsonObject = new Gson().toJsonTree(user).getAsJsonObject();
        MultiValueMap<String, String> paramsForFindById = new LinkedMultiValueMap<>();
        paramsForFindById.add("id", id + "");

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getAccessToken())
            .content(createRequestBody(USER_ID, jsonObject)));
        ResultActions resultForFindById = getMvc().perform(MockMvcRequestBuilders.get(URL_FOR_FIND_BY_ID)
            .header("Authorization", getAccessToken())
            .params(paramsForFindById));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("User data has been updated!")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.successful", Matchers.is(true)));
        resultForFindById.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.id", Matchers.is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(RESERVED_EMAIL)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.password", Matchers.is(MASKED_PASSWORD)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.firstName", Matchers.is(NEW_FIRST_NAME)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.lastName", Matchers.is(NEW_LAST_NAME)));
    }

    @Test
    public void testUpdate() throws Exception {
        // GIVEN
        int id = Math.toIntExact(RESERVED_ID);
        User user = createDefaultUserBuilder(RESERVED_ID)
            .withEmail(NEW_EMAIL)
            .withPassword(NEW_PASSWORD)
            .withFirstName(NEW_FIRST_NAME)
            .withLastName(NEW_LAST_NAME)
            .build();
        JsonObject jsonObject = new Gson().toJsonTree(user).getAsJsonObject();
        MultiValueMap<String, String> paramsForFindById = new LinkedMultiValueMap<>();
        paramsForFindById.add("id", id + "");

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getAccessToken())
            .content(createRequestBody(USER_ID, jsonObject)));
        ResultActions resultForFindById = getMvc().perform(MockMvcRequestBuilders.get(URL_FOR_FIND_BY_ID)
            .header("Authorization", getAccessToken())
            .params(paramsForFindById));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("User data has been updated!")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.successful", Matchers.is(true)));
        resultForFindById.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.id", Matchers.is(id)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(NEW_EMAIL)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.password", Matchers.is(MASKED_PASSWORD)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.firstName", Matchers.is(NEW_FIRST_NAME)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.lastName", Matchers.is(NEW_LAST_NAME)));
    }

}
