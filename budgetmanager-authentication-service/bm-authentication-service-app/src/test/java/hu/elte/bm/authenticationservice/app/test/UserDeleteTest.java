package hu.elte.bm.authenticationservice.app.test;

import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import hu.elte.bm.authenticationservice.domain.User;

public class UserDeleteTest extends AbstractUserTest {

    private static final String URL = "/bm/users/delete";

    @Test
    public void testDeleteWhenIdIsNull() throws Exception {
        // GIVEN
        User user = createDefaultUserBuilder(null).build();

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.delete(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getAccessToken())
            .content(createRequestBodyWithDefaultUserId(user)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("User's id cannot be null!"));
    }

    @Test
    public void testDeleteWhenUserCannotBeFound() throws Exception {
        // GIVEN
        User user = createDefaultUserBuilder(INVALID_ID)
            .build();
        JsonObject jsonObject = new Gson().toJsonTree(user).getAsJsonObject();

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.delete(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getAccessToken())
            .content(createRequestBody(USER_ID, jsonObject)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("User cannot be found in the repository!"));
    }

    @Test
    public void testDelete() throws Exception {
        // GIVEN
        User user = createDefaultUserBuilder(RESERVED_ID).build();
        JsonObject jsonObject = new Gson().toJsonTree(user).getAsJsonObject();
        MultiValueMap<String, String> paramsForFindById = new LinkedMultiValueMap<>();
        paramsForFindById.add("id", RESERVED_ID.toString());

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.delete(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getAccessToken())
            .content(createRequestBody(USER_ID, jsonObject)));
        ResultActions resultForFindById = getMvc().perform(MockMvcRequestBuilders.get(URL_FOR_FIND_BY_ID)
            .header("Authorization", getAccessToken())
            .params(paramsForFindById));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("User has been deleted!")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.successful", Matchers.is(true)));

        resultForFindById.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
