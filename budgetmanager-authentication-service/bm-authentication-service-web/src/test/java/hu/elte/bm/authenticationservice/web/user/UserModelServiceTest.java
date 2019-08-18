package hu.elte.bm.authenticationservice.web.user;

import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.domain.UserException;
import hu.elte.bm.authenticationservice.domain.UserService;
import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.commonpack.validator.ModelValidator;

public class UserModelServiceTest {

    private static final Long ID = 1L;
    private static final Long INVALID_ID = 2L;
    private static final String EMAIL_VALUE = "email@email.com";
    private static final String PASSWORD_VALUE = "password";
    private static final String ENCRYPTED_PASSWORD_VALUE = "encrypted password";
    private static final String NEW_PASSWORD_VALUE = "new password";
    private static final String NEW_ENCRYPTED_PASSWORD_VALUE = "new encrypted password";
    private static final String FIRST_NAME_VALUE = "firstName";
    private static final String LAST_NAME_VALUE = "lastName";
    private static final ModelStringValue EMAIL = ModelStringValue.builder().withValue(EMAIL_VALUE).build();
    private static final ModelStringValue PASSWORD = ModelStringValue.builder().withValue(PASSWORD_VALUE).build();
    private static final ModelStringValue FIRST_NAME = ModelStringValue.builder().withValue(FIRST_NAME_VALUE).build();
    private static final ModelStringValue LAST_NAME = ModelStringValue.builder().withValue(LAST_NAME_VALUE).build();

    private static final String USER_CANNOT_BE_FOUND = "User cannot be found in the repository!";
    private static final String USER_IS_INVALID = "User is invalid!";
    private static final String USER_HAS_BEEN_REGISTERED = "User registration is completed!";
    private static final String USER_HAS_BEEN_UPDATED = "User data has been updated!";
    private static final String USER_HAS_BEEN_DELETED = "User data has been deleted!";
    private static final String USER_CANNOT_BE_DELETED = "User cannot be deleted!";
    private static final String USER_EMAIL_IS_RESERVED = "This email has been used already!";
    private static final String MASKED_PASSWORD_VALUE = "********";
    private static final String PASSWORD_FIELD_NAME = "Password";
    private static final String EMAIL_FIELD_NAME = "Email";
    private static final String FIRST_NAME_FIELD_NAME = "First name";
    private static final String LAST_NAME_FIELD_NAME = "Last name";
    private static final boolean VALID = true;
    private static final boolean INVALID = false;

    private UserModelService underTest;
    private IMocksControl control;
    private BCryptPasswordEncoder encoder;
    private ModelValidator validator;
    private UserService userService;
    private UserModelTransformer transformer;

    @DataProvider(name = "dataForRegisterUser")
    public static Object[][] dataProvider() {
        return new Object[][] {
            { null, PASSWORD, FIRST_NAME, LAST_NAME },
            { EMAIL, null, FIRST_NAME, LAST_NAME },
            { EMAIL, PASSWORD, null, LAST_NAME },
            { EMAIL, PASSWORD, FIRST_NAME, null }
        };
    }

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        encoder = control.createMock(BCryptPasswordEncoder.class);
        validator = control.createMock(ModelValidator.class);
        userService = control.createMock(UserService.class);
        transformer = control.createMock(UserModelTransformer.class);
        underTest = new UserModelService(encoder, validator, userService, transformer);

        ReflectionTestUtils.setField(underTest, "userCannotBeFound", USER_CANNOT_BE_FOUND);
        ReflectionTestUtils.setField(underTest, "userIsInvalid", USER_IS_INVALID);
        ReflectionTestUtils.setField(underTest, "userHasBeenRegistered", USER_HAS_BEEN_REGISTERED);
        ReflectionTestUtils.setField(underTest, "userHasBeenUpdated", USER_HAS_BEEN_UPDATED);
        ReflectionTestUtils.setField(underTest, "userHasBeenDeleted", USER_HAS_BEEN_DELETED);
        ReflectionTestUtils.setField(underTest, "userCannotBeDeleted", USER_CANNOT_BE_DELETED);
        ReflectionTestUtils.setField(underTest, "userEmailIsReserved", USER_EMAIL_IS_RESERVED);
        ReflectionTestUtils.setField(underTest, "maskedPasswordValue", MASKED_PASSWORD_VALUE);
    }

    @Test
    public void testFindByIdWhenUserCannotBeFoundInTheRepository() {
        // GIVEN
        EasyMock.expect(userService.findUserById(INVALID_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        UserModelResponse result = underTest.findById(INVALID_ID);
        // THEN
        control.verify();
        Assert.assertNull(result.getUserModel());
        Assert.assertFalse(result.isSuccessful());
        Assert.assertEquals(result.getMessage(), USER_CANNOT_BE_FOUND);
    }

    @Test
    public void testFindById() {
        // GIVEN
        User userFromRepository = createDefaultUserBuilder().build();
        UserModel userModel = createDefaultUserModelBuilder().build();
        EasyMock.expect(userService.findUserById(ID)).andReturn(Optional.of(userFromRepository));
        EasyMock.expect(transformer.transformToUserModel(userFromRepository, MASKED_PASSWORD_VALUE)).andReturn(userModel);
        control.replay();
        // WHEN
        UserModelResponse result = underTest.findById(ID);
        // THEN
        control.verify();
        Assert.assertNotNull(result.getUserModel());
        Assert.assertTrue(result.isSuccessful());
        Assert.assertNull(result.getMessage());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRegisterWhenIdIsNotNull() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder().build();
        // WHEN
        underTest.registerUser(userModel);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataForRegisterUser")
    public void testRegisterWhenPreValidationFails(final ModelStringValue email, final ModelStringValue password,
        final ModelStringValue firstName, final ModelStringValue lastName) {
        // GIVEN
        UserModel userModel = UserModel.builder()
            .withId(null)
            .withEmail(email)
            .withPassword(password)
            .withFirstName(firstName)
            .withLastName(lastName)
            .build();
        // WHEN
        underTest.registerUser(userModel);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRegisterWhenValidatorThrowsException() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder()
            .withId(null)
            .withEmail(ModelStringValue.builder().withValue(null).build())
            .build();
        transformer.setValidationFields(userModel);
        EasyMock.expect(validator.validate(userModel.getPassword(), PASSWORD_FIELD_NAME)).andThrow(new IllegalArgumentException());
        control.replay();
        // WHEN
        try {
            underTest.registerUser(userModel);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testRegisterWhenValidatorReturnsFalse() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder().withId(null).build();
        transformer.setValidationFields(userModel);
        EasyMock.expect(validator.validate(userModel.getPassword(), PASSWORD_FIELD_NAME)).andReturn(VALID);
        setValidatorAnswers(userModel, VALID, VALID, INVALID);
        control.replay();
        // WHEN
        UserModelResponse result = underTest.registerUser(userModel);
        // THEN
        control.verify();
        Assert.assertFalse(result.isSuccessful());
        Assert.assertEquals(result.getMessage(), USER_IS_INVALID);
    }

    @Test
    public void testRegisterWhenOtherUserHasThisEmail() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder().withId(null).build();
        User user = createDefaultUserBuilder().withId(null).build();
        transformer.setValidationFields(userModel);
        EasyMock.expect(validator.validate(userModel.getPassword(), PASSWORD_FIELD_NAME)).andReturn(VALID);
        setValidatorAnswers(userModel, VALID, VALID, VALID);
        EasyMock.expect(encoder.encode(PASSWORD_VALUE)).andReturn(ENCRYPTED_PASSWORD_VALUE);
        EasyMock.expect(transformer.transformToUser(userModel, ENCRYPTED_PASSWORD_VALUE)).andReturn(user);
        EasyMock.expect(userService.registerUser(user)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        UserModelResponse result = underTest.registerUser(userModel);
        // THEN
        control.verify();
        Assert.assertFalse(result.isSuccessful());
        Assert.assertEquals(result.getMessage(), USER_EMAIL_IS_RESERVED);
    }

    @Test
    public void testRegister() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder().withId(null).build();
        User user = createDefaultUserBuilder().withId(null).build();
        User savedUser = createDefaultUserBuilder().withId(ID).build();
        UserModel savedUserModel = createDefaultUserModelBuilder()
            .withId(ID)
            .withPassword(ModelStringValue.builder().withValue(MASKED_PASSWORD_VALUE).build())
            .build();
        transformer.setValidationFields(userModel);
        EasyMock.expect(validator.validate(userModel.getPassword(), PASSWORD_FIELD_NAME)).andReturn(VALID);
        setValidatorAnswers(userModel, VALID, VALID, VALID);
        EasyMock.expect(encoder.encode(PASSWORD_VALUE)).andReturn(ENCRYPTED_PASSWORD_VALUE);
        EasyMock.expect(transformer.transformToUser(userModel, ENCRYPTED_PASSWORD_VALUE)).andReturn(user);
        EasyMock.expect(userService.registerUser(user)).andReturn(Optional.of(savedUser));
        EasyMock.expect(transformer.transformToUserModel(savedUser, MASKED_PASSWORD_VALUE)).andReturn(savedUserModel);
        control.replay();
        // WHEN
        UserModelResponse result = underTest.registerUser(userModel);
        // THEN
        control.verify();
        Assert.assertTrue(result.isSuccessful());
        Assert.assertEquals(result.getMessage(), USER_HAS_BEEN_REGISTERED);
        Assert.assertEquals(result.getUserModel().getPassword().getValue(), MASKED_PASSWORD_VALUE);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenIdIsNull() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder().withId(null).build();
        // WHEN
        underTest.updateUser(userModel);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataForRegisterUser")
    public void testUpdateWhenPreValidationFails(final ModelStringValue email, final ModelStringValue password,
        final ModelStringValue firstName, final ModelStringValue lastName) {
        // GIVEN
        UserModel userModel = UserModel.builder()
            .withId(ID)
            .withEmail(email)
            .withPassword(password)
            .withFirstName(firstName)
            .withLastName(lastName)
            .build();
        // WHEN
        underTest.updateUser(userModel);
        // THEN
    }

    @Test
    public void testUpdateWhenValidationReturnsWithFalse() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder().build();
        transformer.setValidationFields(userModel);
        EasyMock.expect(validator.validate(userModel.getPassword(), PASSWORD_FIELD_NAME)).andReturn(VALID);
        setValidatorAnswers(userModel, VALID, VALID, INVALID);
        control.replay();
        // WHEN
        UserModelResponse result = underTest.updateUser(userModel);
        // THEN
        control.verify();
        Assert.assertFalse(result.isSuccessful());
        Assert.assertEquals(result.getMessage(), USER_IS_INVALID);
    }

    @Test(expectedExceptions = UserException.class)
    public void testUpdateWhenOriginalUserCannotBeFound() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder().build();
        User userForException = createDefaultUserBuilder().withPassword(MASKED_PASSWORD_VALUE).build();
        transformer.setValidationFields(userModel);
        EasyMock.expect(validator.validate(userModel.getPassword(), PASSWORD_FIELD_NAME)).andReturn(VALID);
        setValidatorAnswers(userModel, VALID, VALID, VALID);
        EasyMock.expect(userService.findUserById(ID)).andReturn(Optional.empty());
        EasyMock.expect(transformer.transformToUser(userModel, MASKED_PASSWORD_VALUE)).andReturn(userForException);
        control.replay();
        // WHEN
        try {
            underTest.updateUser(userModel);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testUpdateWhenPasswordNotChanged() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder()
            .withPassword(ModelStringValue.builder().withValue(MASKED_PASSWORD_VALUE).build())
            .build();
        User originalUser = createDefaultUserBuilder().build();
        User savableUser = createDefaultUserBuilder().withPassword(MASKED_PASSWORD_VALUE).build();
        transformer.setValidationFields(userModel);
        setValidatorAnswers(userModel, VALID, VALID, VALID);
        EasyMock.expect(userService.findUserById(ID)).andReturn(Optional.of(originalUser));
        EasyMock.expect(transformer.transformToUser(userModel, ENCRYPTED_PASSWORD_VALUE)).andReturn(savableUser);
        EasyMock.expect(userService.updateUser(savableUser)).andReturn(Optional.of(savableUser));
        EasyMock.expect(transformer.transformToUserModel(savableUser, MASKED_PASSWORD_VALUE)).andReturn(userModel);
        control.replay();
        // WHEN
        UserModelResponse result = underTest.updateUser(userModel);
        // THEN
        control.verify();
        Assert.assertTrue(result.isSuccessful());
        Assert.assertEquals(result.getMessage(), USER_HAS_BEEN_UPDATED);
        Assert.assertEquals(result.getUserModel().getPassword().getValue(), MASKED_PASSWORD_VALUE);
    }

    @Test
    public void testUpdateWhenPasswordIsChanged() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder()
            .withPassword(ModelStringValue.builder().withValue(NEW_PASSWORD_VALUE).build())
            .build();
        User originalUser = createDefaultUserBuilder().build();
        User savableUser = createDefaultUserBuilder().withPassword(NEW_ENCRYPTED_PASSWORD_VALUE).build();
        UserModel userModelResponse = createDefaultUserModelBuilder()
            .withPassword(ModelStringValue.builder().withValue(MASKED_PASSWORD_VALUE).build())
            .build();
        transformer.setValidationFields(userModel);
        EasyMock.expect(validator.validate(userModel.getPassword(), PASSWORD_FIELD_NAME)).andReturn(VALID);
        setValidatorAnswers(userModel, VALID, VALID, VALID);
        EasyMock.expect(userService.findUserById(ID)).andReturn(Optional.of(originalUser));
        EasyMock.expect(encoder.encode(NEW_PASSWORD_VALUE)).andReturn(NEW_ENCRYPTED_PASSWORD_VALUE);
        EasyMock.expect(transformer.transformToUser(userModel, NEW_ENCRYPTED_PASSWORD_VALUE)).andReturn(savableUser);
        EasyMock.expect(userService.updateUser(savableUser)).andReturn(Optional.of(savableUser));
        EasyMock.expect(transformer.transformToUserModel(savableUser, MASKED_PASSWORD_VALUE)).andReturn(userModelResponse);
        control.replay();
        // WHEN
        UserModelResponse result = underTest.updateUser(userModel);
        // THEN
        control.verify();
        Assert.assertTrue(result.isSuccessful());
        Assert.assertEquals(result.getMessage(), USER_HAS_BEEN_UPDATED);
        Assert.assertEquals(result.getUserModel().getPassword().getValue(), MASKED_PASSWORD_VALUE);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDeleteWhenIdIsNull() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder().withId(null).build();
        // WHEN
        underTest.deleteUser(userModel);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataForRegisterUser")
    public void testDeleteWhenPreValidationFails(final ModelStringValue email, final ModelStringValue password,
        final ModelStringValue firstName, final ModelStringValue lastName) {
        // GIVEN
        UserModel userModel = UserModel.builder()
            .withId(ID)
            .withEmail(email)
            .withPassword(password)
            .withFirstName(firstName)
            .withLastName(lastName)
            .build();
        // WHEN
        underTest.deleteUser(userModel);
        // THEN
    }

    @Test
    public void testDelete() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder()
            .withPassword(ModelStringValue.builder().withValue(MASKED_PASSWORD_VALUE).build())
            .build();
        User originalUser = createDefaultUserBuilder().build();
        User deletableUser = createDefaultUserBuilder().withPassword(MASKED_PASSWORD_VALUE).build();
        transformer.setValidationFields(userModel);
        setValidatorAnswers(userModel, VALID, VALID, VALID);
        EasyMock.expect(userService.findUserById(ID)).andReturn(Optional.of(originalUser));
        EasyMock.expect(transformer.transformToUser(userModel, ENCRYPTED_PASSWORD_VALUE)).andReturn(deletableUser);
        EasyMock.expect(userService.deleteUser(deletableUser)).andReturn(Optional.of(deletableUser));
        EasyMock.expect(transformer.transformToUserModel(deletableUser, MASKED_PASSWORD_VALUE)).andReturn(userModel);
        control.replay();
        // WHEN
        UserModelResponse result = underTest.deleteUser(userModel);
        // THEN
        Assert.assertTrue(result.isSuccessful());
        Assert.assertEquals(result.getMessage(), USER_HAS_BEEN_DELETED);
        Assert.assertEquals(result.getUserModel().getPassword().getValue(), MASKED_PASSWORD_VALUE);
    }

    private User.Builder createDefaultUserBuilder() {
        return User.builder()
            .withId(ID)
            .withEmail(EMAIL_VALUE)
            .withPassword(ENCRYPTED_PASSWORD_VALUE)
            .withFirstName(FIRST_NAME_VALUE)
            .withLastName(LAST_NAME_VALUE);
    }

    private UserModel.Builder createDefaultUserModelBuilder() {
        return UserModel.builder()
            .withId(ID)
            .withEmail(EMAIL)
            .withPassword(PASSWORD)
            .withFirstName(FIRST_NAME)
            .withLastName(LAST_NAME);
    }

    private void setValidatorAnswers(final UserModel userModel, final boolean email, final boolean firstName, final boolean lastName) {
        EasyMock.expect(validator.validate(userModel.getEmail(), EMAIL_FIELD_NAME)).andReturn(email);
        EasyMock.expect(validator.validate(userModel.getFirstName(), FIRST_NAME_FIELD_NAME)).andReturn(firstName);
        EasyMock.expect(validator.validate(userModel.getLastName(), LAST_NAME_FIELD_NAME)).andReturn(lastName);
    }

}
