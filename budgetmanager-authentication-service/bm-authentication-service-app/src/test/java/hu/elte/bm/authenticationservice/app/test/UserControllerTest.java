package hu.elte.bm.authenticationservice.app.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import hu.elte.bm.authenticationservice.app.AbstractAuthenticationServiceApplicationTest;
import hu.elte.bm.authenticationservice.web.user.UserController;
import hu.elte.bm.authenticationservice.web.user.UserModel;
import hu.elte.bm.authenticationservice.web.user.UserModelRequestContext;
import hu.elte.bm.authenticationservice.web.user.UserModelResponse;
import hu.elte.bm.commonpack.validator.ModelStringValue;

public class UserControllerTest extends AbstractAuthenticationServiceApplicationTest {

    private static final Long EXPECTED_ID = 3L;
    private static final Long RESERVED_ID = 1L;
    private static final Long INVALID_ID = 4L;
    private static final String EMPTY_VALUE = "";
    private static final String DEFAULT_EMAIL = "exampleEmail3@mail.com";
    private static final String RESERVED_EMAIL = "exampleEmail@mail.com";
    private static final String OTHER_RESERVED_EMAIL = "exampleEmail2@mail.com";
    private static final String NEW_EMAIL = "exampleEmail4@mail.com";
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
    private static final ModelStringValue EMAIL = ModelStringValue.builder().withValue(DEFAULT_EMAIL).build();
    private static final ModelStringValue PASSWORD = ModelStringValue.builder().withValue(DEFAULT_PASSWORD).build();
    private static final ModelStringValue FIRST_NAME = ModelStringValue.builder().withValue(DEFAULT_FIRST_NAME).build();
    private static final ModelStringValue LAST_NAME = ModelStringValue.builder().withValue(DEFAULT_LAST_NAME).build();
    private static final String USER_IS_INVALID = "User is invalid!";

    @Autowired
    private UserController underTest;

    @Test
    public void testRegisterWhenIdIsNotNull() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder(RESERVED_ID).build();
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.registerUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), USER_IS_INVALID);
    }

    @Test(dataProvider = "testDataForValidation")
    public void testRegisterWhenValidationFails(final UserModel userModel, final String fieldErrorMessage, final String responseErrorMessage) {
        // GIVEN
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.registerUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
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

    @DataProvider
    private Object[][] testDataForValidation() {
        UserModel nullEmail = createDefaultUserModelBuilder(null)
            .withEmail(null).build();
        UserModel emailWithNullValue = createDefaultUserModelBuilder(null)
            .withEmail(ModelStringValue.builder().withValue(null).build()).build();
        UserModel emailWithEmptyValue = createDefaultUserModelBuilder(null)
            .withEmail(ModelStringValue.builder().withValue(EMPTY_VALUE).build()).build();
        UserModel emailWithTooShortValue = createDefaultUserModelBuilder(null)
            .withEmail(ModelStringValue.builder().withValue(TOO_SHORT_EMAIL).build()).build();
        UserModel emailWithTooLongValue = createDefaultUserModelBuilder(null)
            .withEmail(ModelStringValue.builder().withValue(TOO_LONG_EMAIL).build()).build();
        UserModel emailWithNotMatchesValue = createDefaultUserModelBuilder(null)
            .withEmail(ModelStringValue.builder().withValue(NOT_MATCHING_EMAIL).build()).build();
        UserModel nullPassword = createDefaultUserModelBuilder(null)
            .withPassword(null).build();
        UserModel passwordWithNullValue = createDefaultUserModelBuilder(null)
            .withPassword(ModelStringValue.builder().withValue(null).build()).build();
        UserModel passwordWithEmptyValue = createDefaultUserModelBuilder(null)
            .withPassword(ModelStringValue.builder().withValue(EMPTY_VALUE).build()).build();
        UserModel passwordWithTooShortValue = createDefaultUserModelBuilder(null)
            .withPassword(ModelStringValue.builder().withValue(TOO_SHORT_PASSWORD).build()).build();
        UserModel passwordWithTooLongValue = createDefaultUserModelBuilder(null)
            .withPassword(ModelStringValue.builder().withValue(TOO_LONG_PASSWORD).build()).build();
        UserModel nullFirstName = createDefaultUserModelBuilder(null)
            .withFirstName(null).build();
        UserModel firstNameWithNullValue = createDefaultUserModelBuilder(null)
            .withFirstName(ModelStringValue.builder().withValue(null).build()).build();
        UserModel firstNameWithEmptyValue = createDefaultUserModelBuilder(null)
            .withFirstName(ModelStringValue.builder().withValue(EMPTY_VALUE).build()).build();
        UserModel firstNameWithTooShortValue = createDefaultUserModelBuilder(null)
            .withFirstName(ModelStringValue.builder().withValue(TOO_SHORT_NAME).build()).build();
        UserModel firstNameWithTooLongValue = createDefaultUserModelBuilder(null)
            .withFirstName(ModelStringValue.builder().withValue(TOO_LONG_NAME).build()).build();
        UserModel nullLastName = createDefaultUserModelBuilder(null)
            .withLastName(null).build();
        UserModel lastNameWithNullValue = createDefaultUserModelBuilder(null)
            .withLastName(ModelStringValue.builder().withValue(null).build()).build();
        UserModel lastNameWithEmptyValue = createDefaultUserModelBuilder(null)
            .withLastName(ModelStringValue.builder().withValue(EMPTY_VALUE).build()).build();
        UserModel lastNameWithTooShortValue = createDefaultUserModelBuilder(null)
            .withLastName(ModelStringValue.builder().withValue(TOO_SHORT_NAME).build()).build();
        UserModel lastNameWithTooLongValue = createDefaultUserModelBuilder(null)
            .withLastName(ModelStringValue.builder().withValue(TOO_LONG_NAME).build()).build();

        return new Object[][] {
            { nullEmail, null, USER_IS_INVALID },
            { emailWithNullValue, null, "Validated field value cannot be null!" },
            { emailWithEmptyValue, "Email cannot be empty!", USER_IS_INVALID },
            { emailWithTooShortValue, "Email cannot be shorter than 8!", USER_IS_INVALID },
            { emailWithTooLongValue, "Email cannot be longer than 50!", USER_IS_INVALID },
            { emailWithNotMatchesValue, "Email must be match with ^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$!", USER_IS_INVALID },
            { nullPassword, null, USER_IS_INVALID },
            { passwordWithNullValue, null, "Validated field value cannot be null!" },
            { passwordWithEmptyValue, "Password cannot be empty!", USER_IS_INVALID },
            { passwordWithTooShortValue, "Password cannot be shorter than 8!", USER_IS_INVALID },
            { passwordWithTooLongValue, "Password cannot be longer than 16!", USER_IS_INVALID },
            { nullFirstName, null, USER_IS_INVALID },
            { firstNameWithNullValue, null, "Validated field value cannot be null!" },
            { firstNameWithEmptyValue, "First name cannot be empty!", USER_IS_INVALID },
            { firstNameWithTooShortValue, "First name cannot be shorter than 2!", USER_IS_INVALID },
            { firstNameWithTooLongValue, "First name cannot be longer than 50!", USER_IS_INVALID },
            { nullLastName, null, USER_IS_INVALID },
            { lastNameWithNullValue, null, "Validated field value cannot be null!" },
            { lastNameWithEmptyValue, "Last name cannot be empty!", USER_IS_INVALID },
            { lastNameWithTooShortValue, "Last name cannot be shorter than 2!", USER_IS_INVALID },
            { lastNameWithTooLongValue, "Last name cannot be longer than 50!", USER_IS_INVALID }
        };
    }

    @Test
    public void testRegisterWhenEmailIsReserved() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder(null)
            .withEmail(ModelStringValue.builder().withValue(RESERVED_EMAIL).build())
            .build();
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.registerUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "This email has been used already!");
        Assert.assertNull(response.getUserModel().getId());
    }

    @Test
    public void testRegister() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder(null).build();
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.registerUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "User registration is completed!");
        Assert.assertEquals(response.getUserModel().getId(), EXPECTED_ID);
        Assert.assertEquals(response.getUserModel().getPassword().getValue(), MASKED_PASSWORD);
    }

    @Test
    public void testUpdateWhenIdIsNull() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder(null).build();
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.updateUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), USER_IS_INVALID);
    }

    @Test(dataProvider = "testDataForValidation")
    public void testUpdateWhenValidationFails(final UserModel userModel, final String fieldErrorMessage, final String responseErrorMessage) {
        // GIVEN
        userModel.setId(RESERVED_ID);
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.updateUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
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

    @Test
    public void testUpdateWhenUserCannotBeFound() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder(INVALID_ID)
            .withEmail(ModelStringValue.builder().withValue(NEW_EMAIL).build())
            .build();
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.updateUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "User cannot be found in the repository!");
        Assert.assertEquals(response.getUserModel().getId(), INVALID_ID);
    }

    @Test
    public void testUpdateWhenEmailIsReserved() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder(RESERVED_ID)
            .withEmail(ModelStringValue.builder().withValue(OTHER_RESERVED_EMAIL).build())
            .build();
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.updateUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "This email has been used already!");
        Assert.assertEquals(response.getUserModel().getId(), RESERVED_ID);
    }

    @Test
    public void testUpdateWhenEmailIsUnchanged() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder(RESERVED_ID)
            .withEmail(ModelStringValue.builder().withValue(RESERVED_EMAIL).build())
            .withFirstName(ModelStringValue.builder().withValue(NEW_FIRST_NAME).build())
            .withLastName(ModelStringValue.builder().withValue(NEW_LAST_NAME).build())
            .build();
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.updateUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "User data has been updated!");
        Assert.assertEquals(response.getUserModel().getId(), RESERVED_ID);
        Assert.assertEquals(response.getUserModel().getEmail().getValue(), RESERVED_EMAIL);
        Assert.assertEquals(response.getUserModel().getPassword().getValue(), MASKED_PASSWORD);
        Assert.assertEquals(response.getUserModel().getFirstName().getValue(), NEW_FIRST_NAME);
        Assert.assertEquals(response.getUserModel().getLastName().getValue(), NEW_LAST_NAME);
    }

    @Test
    public void testUpdate() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder(RESERVED_ID)
            .withEmail(ModelStringValue.builder().withValue(NEW_EMAIL).build())
            .withPassword(ModelStringValue.builder().withValue(NEW_PASSWORD).build())
            .withFirstName(ModelStringValue.builder().withValue(NEW_FIRST_NAME).build())
            .withLastName(ModelStringValue.builder().withValue(NEW_LAST_NAME).build())
            .build();
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.updateUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "User data has been updated!");
        Assert.assertEquals(response.getUserModel().getId(), RESERVED_ID);
        Assert.assertEquals(response.getUserModel().getEmail().getValue(), NEW_EMAIL);
        Assert.assertEquals(response.getUserModel().getPassword().getValue(), MASKED_PASSWORD);
        Assert.assertEquals(response.getUserModel().getFirstName().getValue(), NEW_FIRST_NAME);
        Assert.assertEquals(response.getUserModel().getLastName().getValue(), NEW_LAST_NAME);
    }

    @Test
    public void testDeleteWhenIdIsNull() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder(null).build();
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.deleteUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), USER_IS_INVALID);
    }

    @Test(dataProvider = "testDataForValidation")
    public void testDeleteWhenValidationFails(final UserModel userModel, final String fieldErrorMessage, final String responseErrorMessage) {
        // GIVEN
        userModel.setId(RESERVED_ID);
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.deleteUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
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

    @Test
    public void testDeleteWhenUserCannotBeFound() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder(INVALID_ID).build();
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.deleteUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
        // THEN
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "User cannot be found in the repository!");
        Assert.assertEquals(response.getUserModel().getId(), INVALID_ID);
    }

    @Test
    public void testDelete() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder(RESERVED_ID).build();
        UserModelRequestContext context = createContext(userModel);
        // WHEN
        ResponseEntity result = underTest.deleteUser(context);
        UserModelResponse response = (UserModelResponse) result.getBody();
        ResponseEntity resultForFind = underTest.findUserById(RESERVED_ID);
        UserModelResponse responseForFind = (UserModelResponse) resultForFind.getBody();
        // THEN
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.getMessage(), "User has been deleted!");
        Assert.assertFalse(responseForFind.isSuccessful());
        Assert.assertEquals(responseForFind.getMessage(), "User cannot be found in the repository!");
    }

    private UserModel.Builder createDefaultUserModelBuilder(final Long id) {
        return UserModel.builder()
            .withId(id)
            .withEmail(EMAIL)
            .withPassword(PASSWORD)
            .withFirstName(FIRST_NAME)
            .withLastName(LAST_NAME);
    }

    private UserModelRequestContext createContext(final UserModel userModel) {
        UserModelRequestContext context = new UserModelRequestContext();
        context.setUserModel(userModel);
        return context;
    }

}
