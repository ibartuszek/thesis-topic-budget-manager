package hu.elte.bm.authenticationservice.app.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import hu.elte.bm.authenticationservice.app.AbstractAuthenticationServiceApplicationTest;
import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.web.user.UserRequestContext;

public class AbstractUserTest extends AbstractAuthenticationServiceApplicationTest {

    static final String URL_FOR_FIND_BY_ID = "/bm/users/findById";
    static final Long USER_ID = 1L;
    static final Long RESERVED_ID = 1L;
    static final Long INVALID_ID = 4L;
    static final String RESERVED_EMAIL = "exampleEmail@mail.com";
    static final String MASKED_PASSWORD = "********";
    static final String DEFAULT_EMAIL = "exampleEmail3@mail.com";
    static final String DEFAULT_FIRST_NAME = "John";
    static final String DEFAULT_LAST_NAME = "Doe";
    private static final String OAUTH_URL = "/oauth/token";
    private static final String USER_NAME = "exampleEmail@mail.com";
    private static final String USER_PASSWORD = "password";
    private static final String EMPTY_VALUE = "";
    private static final String NOT_MATCHING_EMAIL = "examplMail_mail.com";
    private static final String TOO_SHORT_EMAIL = "em@ma.c";
    private static final String TOO_LONG_EMAIL = "aaaaabbbbbaaaaabbbbbaaaaabbbb@aaaaabbbbb.aaaaabbbbb";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String TOO_SHORT_PASSWORD = "aaaabbb";
    private static final String TOO_LONG_PASSWORD = "aaaabbbbaaaabbbb1";
    private static final String TOO_SHORT_NAME = "a";
    private static final String TOO_LONG_NAME = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";

    private String accessToken;

    @Value("${security.jwt.client-id}")
    private String clientId;

    @Value("${security.jwt.client-secret}")
    private String clientSecret;

    @BeforeClass
    public void setAccessToken() throws Exception {
        accessToken = "Bearer " + obtainAccessToken();
    }

    public String getAccessToken() {
        return accessToken;
    }

    @DataProvider
    Object[][] testDataForValidation() {
        JsonObject nullEmail = createRequestBody("email", null);
        JsonObject emptyEmail = createRequestBody("email", EMPTY_VALUE);
        JsonObject tooShortEmail = createRequestBody("email", TOO_SHORT_EMAIL);
        JsonObject tooLongEmail = createRequestBody("email", TOO_LONG_EMAIL);
        JsonObject notMatchingEmail = createRequestBody("email", NOT_MATCHING_EMAIL);
        JsonObject nullPassword = createRequestBody("password", null);
        JsonObject emptyPassword = createRequestBody("password", EMPTY_VALUE);
        JsonObject tooShortPassword = createRequestBody("password", TOO_SHORT_PASSWORD);
        JsonObject tooLongPassword = createRequestBody("password", TOO_LONG_PASSWORD);
        JsonObject nullFirstName = createRequestBody("firstName", null);
        JsonObject emptyFirstName = createRequestBody("firstName", EMPTY_VALUE);
        JsonObject tooShortFirstName = createRequestBody("firstName", TOO_SHORT_NAME);
        JsonObject tooLongFirstName = createRequestBody("firstName", TOO_LONG_NAME);
        JsonObject nullLastName = createRequestBody("lastName", null);
        JsonObject emptyLastName = createRequestBody("lastName", EMPTY_VALUE);
        JsonObject tooShortLastName = createRequestBody("lastName", TOO_SHORT_NAME);
        JsonObject tooLongLastName = createRequestBody("lastName", TOO_SHORT_NAME);

        return new Object[][]{
                {nullEmail},
                {emptyEmail},
                {tooShortEmail},
                {tooLongEmail},
                {notMatchingEmail},

                {nullPassword},
                {emptyPassword},
                {tooShortPassword},
                {tooLongPassword},

                {nullFirstName},
                {emptyFirstName},
                {tooShortFirstName},
                {tooLongFirstName},

                {nullLastName},
                {emptyLastName},
                {tooShortLastName},
                {tooLongLastName}
        };
    }

    User.Builder createDefaultUserBuilder(final Long id) {
        return User.builder()
                .withId(id)
                .withEmail(DEFAULT_EMAIL)
                .withPassword(DEFAULT_PASSWORD)
                .withFirstName(DEFAULT_FIRST_NAME)
                .withLastName(DEFAULT_LAST_NAME);
    }

    String createRequestBody(final Object object) {
        return new Gson().toJson(object);
    }

    String createRequestBodyWithDefaultUserId(final User user) {
        UserRequestContext requestContext = new UserRequestContext();
        requestContext.setUserId(USER_ID);
        requestContext.setUser(user);
        return new Gson().toJson(requestContext);
    }

    String createRequestBody(final Long userId, final JsonObject user) {
        UserRequestContext requestContext = new UserRequestContext();
        requestContext.setUserId(userId);
        JsonObject requestBody = new Gson().toJsonTree(requestContext).getAsJsonObject();
        requestBody.add("user", user);
        return new Gson().toJson(requestBody);
    }

    private String obtainAccessToken() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", USER_NAME);
        params.add("password", USER_PASSWORD);

        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(OAUTH_URL)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(clientId, clientSecret))
                .accept("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    private JsonObject createRequestBody(final String keyToReplace, final Object newValue) {
        User user = createDefaultUserBuilder(null).build();
        JsonObject jsonObject = new Gson().toJsonTree(user).getAsJsonObject();
        jsonObject.remove(keyToReplace);
        jsonObject.add(keyToReplace, new Gson().toJsonTree(newValue));
        return jsonObject;
    }

}
