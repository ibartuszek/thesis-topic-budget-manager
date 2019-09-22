package hu.elte.bm.authenticationservice.app.test;

import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import hu.elte.bm.authenticationservice.app.AbstractAuthenticationServiceApplicationTest;
import hu.elte.bm.authenticationservice.domain.User;

public class UserRegisterTest extends AbstractAuthenticationServiceApplicationTest {

    private static final String URL = "/bm/users/register";

    private static final Long USER_ID = 3L;
    private static final Long EXPECTED_ID = 3L;
    private static final Long RESERVED_ID = 1L;
    private static final Long INVALID_ID = 4L;
    private static final String EMPTY_VALUE = "";
    private static final String DEFAULT_EMAIL = "exampleEmail3@mail.com";
    private static final String RESERVED_EMAIL = "exampleEmail@mail.com";
    private static final String OTHER_RESERVED_EMAIL = "exampleEmail2@mail.com";
    private static final String NEW_EMAIL = "exampleEmail4@mail.com";
    private static final String EMAIL_CANNOT_BE_NULL = "Email is compulsory";
    private static final String NOT_MATCHING_EMAIL = "exampleEmail_mail.com";
    private static final String TOO_SHORT_EMAIL = "em@ma.c";
    private static final String TOO_LONG_EMAIL = "aaaaabbbbbaaaaabbbbbaaaaabbbb@aaaaabbbbb.aaaaabbbbb";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String NEW_PASSWORD = "password1";
    private static final String MASKED_PASSWORD = "********";
    private static final String TOO_SHORT_PASSWORD = "aaaabbb";
    private static final String TOO_LONG_PASSWORD = "aaaabbbbaaaabbbb1";
    private static final String DEFAULT_FIRST_NAME = "John";
    private static final String NEW_FIRST_NAME = "Johny";
    private static final String DEFAULT_LAST_NAME = "Doe";
    private static final String NEW_LAST_NAME = "Do";
    private static final String TOO_SHORT_NAME = "a";
    private static final String TOO_LONG_NAME = "aaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbbaaaaabbbbb1";
    //    private static final ModelStringValue EMAIL = ModelStringValue.builder().withValue(DEFAULT_EMAIL).build();
    //    private static final ModelStringValue PASSWORD = ModelStringValue.builder().withValue(DEFAULT_PASSWORD).build();
    //    private static final ModelStringValue FIRST_NAME = ModelStringValue.builder().withValue(DEFAULT_FIRST_NAME).build();
    //    private static final ModelStringValue LAST_NAME = ModelStringValue.builder().withValue(DEFAULT_LAST_NAME).build();

    @Test
    public void testRegisterWhenIdIsNotNull() throws Exception {
        // GIVEN
        User user = createDefaultUserBuilder(RESERVED_ID).build();

        // WHEN
        ResultActions result = mvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            // .content(createBody()));
            .content(createRequestBody(user)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("New user's id cannot be null!"));
    }

    @Test(dataProvider = "testDataForValidation")
    public void testRegisterWhenValidationFails(final String user, final String responseErrorMessage) throws Exception {

        // WHEN
        ResultActions result = mvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(user));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(responseErrorMessage));

        //            Assert.assertFalse(response.isSuccessful());
        //            Assert.assertEquals(response.getMessage(), responseErrorMessage);
        //            if (response.getUser().getEmail() != null && response.getUser().getEmail().getErrorMessage() != null) {
        //                Assert.assertEquals(response.getUserModel().getEmail().getErrorMessage(), fieldErrorMessage);
        //            }
        //            if (response.getUserModel().getPassword() != null && response.getUserModel().getPassword().getErrorMessage() != null) {
        //                Assert.assertEquals(response.getUserModel().getPassword().getErrorMessage(), fieldErrorMessage);
        //            }
        //            if (response.getUserModel().getLastName() != null && response.getUserModel().getLastName().getErrorMessage() != null) {
        //                Assert.assertEquals(response.getUserModel().getLastName().getErrorMessage(), fieldErrorMessage);
        //            }
        //            if (response.getUserModel().getFirstName() != null && response.getUserModel().getFirstName().getErrorMessage() != null) {
        //                Assert.assertEquals(response.getUserModel().getFirstName().getErrorMessage(), fieldErrorMessage);
        //            }
    }

    @DataProvider
    private Object[][] testDataForValidation() {
        String nullEmail = createRequestBody("email");
        // "\"id\": null,\"email\": \"new_example@example.com\",\"password\": \"password\",\"firstName\": \"Johny\",\"lastName\": \"Doe\"";

        //            UserModel emailWithNullValue = createDefaultUserBuilder(null)
        //                .withEmail(ModelStringValue.builder().withValue(null).build()).build();
        //            UserModel emailWithEmptyValue = createDefaultUserBuilder(null)
        //                .withEmail(ModelStringValue.builder().withValue(EMPTY_VALUE).build()).build();
        //            UserModel emailWithTooShortValue = createDefaultUserBuilder(null)
        //                .withEmail(ModelStringValue.builder().withValue(TOO_SHORT_EMAIL).build()).build();
        //            UserModel emailWithTooLongValue = createDefaultUserBuilder(null)
        //                .withEmail(ModelStringValue.builder().withValue(TOO_LONG_EMAIL).build()).build();
        //            UserModel emailWithNotMatchesValue = createDefaultUserBuilder(null)
        //                .withEmail(ModelStringValue.builder().withValue(NOT_MATCHING_EMAIL).build()).build();
        //            UserModel nullPassword = createDefaultUserBuilder(null)
        //                .withPassword(null).build();
        //            UserModel passwordWithNullValue = createDefaultUserBuilder(null)
        //                .withPassword(ModelStringValue.builder().withValue(null).build()).build();
        //            UserModel passwordWithEmptyValue = createDefaultUserBuilder(null)
        //                .withPassword(ModelStringValue.builder().withValue(EMPTY_VALUE).build()).build();
        //            UserModel passwordWithTooShortValue = createDefaultUserBuilder(null)
        //                .withPassword(ModelStringValue.builder().withValue(TOO_SHORT_PASSWORD).build()).build();
        //            UserModel passwordWithTooLongValue = createDefaultUserBuilder(null)
        //                .withPassword(ModelStringValue.builder().withValue(TOO_LONG_PASSWORD).build()).build();
        //            UserModel nullFirstName = createDefaultUserBuilder(null)
        //                .withFirstName(null).build();
        //            UserModel firstNameWithNullValue = createDefaultUserBuilder(null)
        //                .withFirstName(ModelStringValue.builder().withValue(null).build()).build();
        //            UserModel firstNameWithEmptyValue = createDefaultUserBuilder(null)
        //                .withFirstName(ModelStringValue.builder().withValue(EMPTY_VALUE).build()).build();
        //            UserModel firstNameWithTooShortValue = createDefaultUserBuilder(null)
        //                .withFirstName(ModelStringValue.builder().withValue(TOO_SHORT_NAME).build()).build();
        //            UserModel firstNameWithTooLongValue = createDefaultUserBuilder(null)
        //                .withFirstName(ModelStringValue.builder().withValue(TOO_LONG_NAME).build()).build();
        //            UserModel nullLastName = createDefaultUserBuilder(null)
        //                .withLastName(null).build();
        //            UserModel lastNameWithNullValue = createDefaultUserBuilder(null)
        //                .withLastName(ModelStringValue.builder().withValue(null).build()).build();
        //            UserModel lastNameWithEmptyValue = createDefaultUserBuilder(null)
        //                .withLastName(ModelStringValue.builder().withValue(EMPTY_VALUE).build()).build();
        //            UserModel lastNameWithTooShortValue = createDefaultUserBuilder(null)
        //                .withLastName(ModelStringValue.builder().withValue(TOO_SHORT_NAME).build()).build();
        //            UserModel lastNameWithTooLongValue = createDefaultUserBuilder(null)
        //                .withLastName(ModelStringValue.builder().withValue(TOO_LONG_NAME).build()).build();

        return new Object[][] {
            { nullEmail, EMAIL_CANNOT_BE_NULL },
            //                { emailWithNullValue, null, "Validated field value cannot be null!" },
            //                { emailWithEmptyValue, "Email cannot be empty!", USER_IS_INVALID },
            //                { emailWithTooShortValue, "Email cannot be shorter than 8!", USER_IS_INVALID },
            //                { emailWithTooLongValue, "Email cannot be longer than 50!", USER_IS_INVALID },
            //                { emailWithNotMatchesValue, "Email must be match with ^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$!", USER_IS_INVALID },
            //                { nullPassword, null, USER_IS_INVALID },
            //                { passwordWithNullValue, null, "Validated field value cannot be null!" },
            //                { passwordWithEmptyValue, "Password cannot be empty!", USER_IS_INVALID },
            //                { passwordWithTooShortValue, "Password cannot be shorter than 8!", USER_IS_INVALID },
            //                { passwordWithTooLongValue, "Password cannot be longer than 16!", USER_IS_INVALID },
            //                { nullFirstName, null, USER_IS_INVALID },
            //                { firstNameWithNullValue, null, "Validated field value cannot be null!" },
            //                { firstNameWithEmptyValue, "First name cannot be empty!", USER_IS_INVALID },
            //                { firstNameWithTooShortValue, "First name cannot be shorter than 2!", USER_IS_INVALID },
            //                { firstNameWithTooLongValue, "First name cannot be longer than 50!", USER_IS_INVALID },
            //                { nullLastName, null, USER_IS_INVALID },
            //                { lastNameWithNullValue, null, "Validated field value cannot be null!" },
            //                { lastNameWithEmptyValue, "Last name cannot be empty!", USER_IS_INVALID },
            //                { lastNameWithTooShortValue, "Last name cannot be shorter than 2!", USER_IS_INVALID },
            //                { lastNameWithTooLongValue, "Last name cannot be longer than 50!", USER_IS_INVALID }
        };
    }

    @Test
    public void testRegisterWhenEmailIsReserved() throws Exception {
        // GIVEN
        User user = createDefaultUserBuilder(null)
            .withEmail(RESERVED_EMAIL)
            .build();

        // WHEN
        ResultActions result = mvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(user)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(MockMvcResultMatchers.content().string("This email has been used already!"));
    }

    @Test
    public void testRegister() throws Exception {
        // GIVEN
        User user = createDefaultUserBuilder(null).build();

        // WHEN
        ResultActions result = mvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(user)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("User registration is completed!")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.successful", Matchers.is(true)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.id", Matchers.is(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user.password", Matchers.is("********")));
    }
/*
    @Test
    public void testUpdateWhenIdIsNull() {
        // GIVEN
        User user = createDefaultUserBuilder(null).build();
        UserRequestContext context = createRequestBody(user);
        // WHEN
        UserResponse result = underTest.updateUser(context);
        // THEN
        Assert.assertFalse(result.isSuccessful());
        Assert.assertEquals(result.getMessage(), USER_IS_INVALID);
    }
*/
    /*
        @Test(dataProvider = "testDataForValidation")
        public void testUpdateWhenValidationFails(final UserModel userModel, final String fieldErrorMessage, final String responseErrorMessage) {
            // GIVEN
            userModel.setId(RESERVED_ID);
            UserRequestContext context = createRequestBody(userModel);
            // WHEN
            ResponseEntity result = underTest.updateUser(context);
            UserResponse response = (UserResponse) result.getBody();
            // THEN
            Assert.assertFalse(response.isSuccessful());
            Assert.assertEquals(response.getMessage(), responseErrorMessage);
            if (response.getUserModel().getEmail() != null && response.getUserModel().getEmail().getErrorMessage() != null) {
                Assert.assertEquals(response.getUserModel().getEmail().getErrorMessage(), fieldErrorMessage);
            }
            if (response.getUserModel().getPassword() != null && response.getUserModel().getPassword().getErrorMessage() != null) {
                Assert.assertEquals(response.getUserModel().getPassword().getErrorMessage(), fieldErrorMessage);
            }
            if (response.getUserModel().getLastName() != null && response.getUserModel().getLastName().getErrorMessage() != null) {
                Assert.assertEquals(response.getUserModel().getLastName().getErrorMessage(), fieldErrorMessage);
            }
            if (response.getUserModel().getFirstName() != null && response.getUserModel().getFirstName().getErrorMessage() != null) {
                Assert.assertEquals(response.getUserModel().getFirstName().getErrorMessage(), fieldErrorMessage);
            }
        }
    */
/*
    @Test
    public void testUpdateWhenUserCannotBeFound() {
        // GIVEN
        User user = createDefaultUserBuilder(INVALID_ID)
            .withEmail(NEW_EMAIL)
            .build();
        UserRequestContext context = createRequestBody(user);
        // WHEN
        ResponseEntity result = underTest.updateUser(context);
        UserResponse response = (UserResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "User cannot be found in the repository!");
        Assert.assertEquals(response.getUser().getId(), INVALID_ID);
    }

    @Test
    public void testUpdateWhenEmailIsReserved() {
        // GIVEN
        User user = createDefaultUserBuilder(RESERVED_ID)
            .withEmail(OTHER_RESERVED_EMAIL)
            .build();
        UserRequestContext context = createRequestBody(user);
        // WHEN
        ResponseEntity result = underTest.updateUser(context);
        UserResponse response = (UserResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "This email has been used already!");
        Assert.assertEquals(response.getUser().getId(), RESERVED_ID);
    }

    @Test
    public void testUpdateWhenEmailIsUnchanged() {
        // GIVEN
        User user = createDefaultUserBuilder(RESERVED_ID)
            .withEmail(RESERVED_EMAIL)
            .withFirstName(NEW_FIRST_NAME)
            .withLastName(NEW_LAST_NAME)
            .build();
        UserRequestContext context = createRequestBody(user);
        // WHEN
        ResponseEntity result = underTest.updateUser(context);
        UserResponse response = (UserResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "User data has been updated!");
        Assert.assertEquals(response.getUser().getId(), RESERVED_ID);
        Assert.assertEquals(response.getUser().getEmail(), RESERVED_EMAIL);
        Assert.assertEquals(response.getUser().getPassword(), MASKED_PASSWORD);
        Assert.assertEquals(response.getUser().getFirstName(), NEW_FIRST_NAME);
        Assert.assertEquals(response.getUser().getLastName(), NEW_LAST_NAME);
    }

    @Test
    public void testUpdate() {
        // GIVEN
        User user = createDefaultUserBuilder(RESERVED_ID)
            .withEmail(NEW_EMAIL)
            .withPassword(NEW_PASSWORD)
            .withFirstName(NEW_FIRST_NAME)
            .withLastName(NEW_LAST_NAME)
            .build();
        UserRequestContext context = createRequestBody(user);
        // WHEN
        ResponseEntity result = underTest.updateUser(context);
        UserResponse response = (UserResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "User data has been updated!");
        Assert.assertEquals(response.getUser().getId(), RESERVED_ID);
        Assert.assertEquals(response.getUser().getEmail(), NEW_EMAIL);
        Assert.assertEquals(response.getUser().getPassword(), MASKED_PASSWORD);
        Assert.assertEquals(response.getUser().getFirstName(), NEW_FIRST_NAME);
        Assert.assertEquals(response.getUser().getLastName(), NEW_LAST_NAME);
    }

    @Test
    public void testDeleteWhenIdIsNull() {
        // GIVEN
        User user = createDefaultUserBuilder(null).build();
        UserRequestContext context = createRequestBody(user);
        // WHEN
        ResponseEntity result = underTest.deleteUser(context);
        UserResponse response = (UserResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), USER_IS_INVALID);
    }

    /*
        @Test(dataProvider = "testDataForValidation")
        public void testDeleteWhenValidationFails(final UserModel userModel, final String fieldErrorMessage, final String responseErrorMessage) {
            // GIVEN
            userModel.setId(RESERVED_ID);
            UserRequestContext context = createRequestBody(userModel);
            // WHEN
            ResponseEntity result = underTest.deleteUser(context);
            UserResponse response = (UserResponse) result.getBody();
            // THEN
            Assert.assertFalse(response.isSuccessful());
            Assert.assertEquals(response.getMessage(), responseErrorMessage);
            if (response.getUserModel().getEmail() != null && response.getUserModel().getEmail().getErrorMessage() != null) {
                Assert.assertEquals(response.getUserModel().getEmail().getErrorMessage(), fieldErrorMessage);
            }
            if (response.getUserModel().getPassword() != null && response.getUserModel().getPassword().getErrorMessage() != null) {
                Assert.assertEquals(response.getUserModel().getPassword().getErrorMessage(), fieldErrorMessage);
            }
            if (response.getUserModel().getLastName() != null && response.getUserModel().getLastName().getErrorMessage() != null) {
                Assert.assertEquals(response.getUserModel().getLastName().getErrorMessage(), fieldErrorMessage);
            }
            if (response.getUserModel().getFirstName() != null && response.getUserModel().getFirstName().getErrorMessage() != null) {
                Assert.assertEquals(response.getUserModel().getFirstName().getErrorMessage(), fieldErrorMessage);
            }
        }
    */
/*
    @Test
    public void testDeleteWhenUserCannotBeFound() {
        // GIVEN
        User user = createDefaultUserBuilder(INVALID_ID).build();
        UserRequestContext context = createRequestBody(user);
        // WHEN
        ResponseEntity result = underTest.deleteUser(context);
        UserResponse response = (UserResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "User cannot be found in the repository!");
        Assert.assertEquals(response.getUser().getId(), INVALID_ID);
    }

    @Test
    public void testDelete() {
        // GIVEN
        User user = createDefaultUserBuilder(RESERVED_ID).build();
        UserRequestContext context = createRequestBody(user);
        // WHEN
        ResponseEntity result = underTest.deleteUser(context);
        UserResponse response = (UserResponse) result.getBody();
        ResponseEntity resultForFind = underTest.findUserById(RESERVED_ID);
        UserResponse responseForFind = (UserResponse) resultForFind.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "User has been deleted!");
        Assert.assertFalse(responseForFind.isSuccessful());
        Assert.assertEquals(responseForFind.getMessage(), "User cannot be found in the repository!");
    }
    */

    private User.Builder createDefaultUserBuilder(final Long id) {
        return User.builder()
            .withId(id)
            .withEmail(DEFAULT_EMAIL)
            .withPassword(DEFAULT_PASSWORD)
            .withFirstName(DEFAULT_FIRST_NAME)
            .withLastName(DEFAULT_LAST_NAME);
    }

    private String createRequestBody(final User user) {
        return new Gson().toJson(user);
    }

    private String createRequestBody(final String keyToReplace, final Object newValue) {
        User test = createDefaultUserBuilder(null).build();
        JsonObject jsonObject = new Gson().toJsonTree(test).getAsJsonObject();
        jsonObject.remove(keyToReplace);
        jsonObject.add(keyToReplace, new Gson().toJsonTree(newValue));
        return new Gson().toJson(jsonObject);
    }

    private String createRequestBody(final String keyToRemove) {
        User test = createDefaultUserBuilder(null).build();
        JsonObject jsonObject = new Gson().toJsonTree(test).getAsJsonObject();
        jsonObject.remove(keyToRemove);
        return new Gson().toJson(jsonObject);
    }

}
