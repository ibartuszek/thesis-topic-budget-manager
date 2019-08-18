package hu.elte.bm.authenticationservice.web.user;

import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.commonpack.validator.ModelStringValue;

public class UserModelTransformerTest {

    private static final Long ID = 1L;
    private static final String EMAIL_VALUE = "email@email.com";
    private static final String PASSWORD_VALUE = "password";
    private static final String ENCRYPTED_PASSWORD_VALUE = "encrypted_password";
    private static final String MASKED_PASSWORD_VALUE = "masked_password";
    private static final String FIRST_NAME_VALUE = "firstName";
    private static final String LAST_NAME_VALUE = "lastName";
    private static final ModelStringValue EMAIL = ModelStringValue.builder().withValue(EMAIL_VALUE).build();
    private static final ModelStringValue PASSWORD = ModelStringValue.builder().withValue(PASSWORD_VALUE).build();
    private static final ModelStringValue FIRST_NAME = ModelStringValue.builder().withValue(FIRST_NAME_VALUE).build();
    private static final ModelStringValue LAST_NAME = ModelStringValue.builder().withValue(LAST_NAME_VALUE).build();
    private static final Integer EMAIL_MINIMUM_LENGTH = 8;
    private static final Integer EMAIL_MAXIMUM_LENGTH = 20;
    private static final String EMAIL_REGEXP = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private static final Integer PASSWORD_MINIMUM_LENGTH = 8;
    private static final Integer PASSWORD_MAXIMUM_LENGTH = 16;
    private static final Integer FIRST_NAME_MINIMUM_LENGTH = 2;
    private static final Integer FIRST_NAME_MAXIMUM_LENGTH = 20;
    private static final Integer LAST_NAME_MINIMUM_LENGTH = 2;
    private static final Integer LAST_NAME_MAXIMUM_LENGTH = 20;

    private UserModelTransformer underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new UserModelTransformer();

        ReflectionTestUtils.setField(underTest, "emailMinimumLength", EMAIL_MINIMUM_LENGTH);
        ReflectionTestUtils.setField(underTest, "emailMaximumLength", EMAIL_MAXIMUM_LENGTH);
        ReflectionTestUtils.setField(underTest, "emailRegexp", EMAIL_REGEXP);
        ReflectionTestUtils.setField(underTest, "passwordMinimumLength", PASSWORD_MINIMUM_LENGTH);
        ReflectionTestUtils.setField(underTest, "passwordMaximumLength", PASSWORD_MAXIMUM_LENGTH);
        ReflectionTestUtils.setField(underTest, "firstNameMinimumLength", FIRST_NAME_MINIMUM_LENGTH);
        ReflectionTestUtils.setField(underTest, "firstNameMaximumLength", FIRST_NAME_MAXIMUM_LENGTH);
        ReflectionTestUtils.setField(underTest, "lastNameMinimumLength", LAST_NAME_MINIMUM_LENGTH);
        ReflectionTestUtils.setField(underTest, "lastNameMaximumLength", LAST_NAME_MAXIMUM_LENGTH);
    }

    @Test
    public void testTransformToUserModel() {
        // GIVEN
        User user = createDefaultUserBuilder().build();
        // WHEN
        UserModel result = underTest.transformToUserModel(user, MASKED_PASSWORD_VALUE);
        // THEN
        Assert.assertEquals(result.getId(), ID);
        Assert.assertEquals(result.getEmail().getValue(), EMAIL_VALUE);
        Assert.assertEquals(result.getEmail().getMinimumLength(), EMAIL_MINIMUM_LENGTH);
        Assert.assertEquals(result.getEmail().getMaximumLength(), EMAIL_MAXIMUM_LENGTH);
        Assert.assertEquals(result.getEmail().getRegexp(), EMAIL_REGEXP);
        Assert.assertNull(result.getEmail().getErrorMessage());
        Assert.assertEquals(result.getPassword().getValue(), MASKED_PASSWORD_VALUE);
        Assert.assertEquals(result.getPassword().getMinimumLength(), PASSWORD_MINIMUM_LENGTH);
        Assert.assertEquals(result.getPassword().getMaximumLength(), PASSWORD_MAXIMUM_LENGTH);
        Assert.assertNull(result.getPassword().getErrorMessage());
        Assert.assertEquals(result.getFirstName().getValue(), FIRST_NAME_VALUE);
        Assert.assertEquals(result.getFirstName().getMinimumLength(), FIRST_NAME_MINIMUM_LENGTH);
        Assert.assertEquals(result.getFirstName().getMaximumLength(), FIRST_NAME_MAXIMUM_LENGTH);
        Assert.assertNull(result.getFirstName().getErrorMessage());
        Assert.assertEquals(result.getLastName().getValue(), LAST_NAME_VALUE);
        Assert.assertEquals(result.getLastName().getMinimumLength(), LAST_NAME_MINIMUM_LENGTH);
        Assert.assertEquals(result.getLastName().getMaximumLength(), LAST_NAME_MAXIMUM_LENGTH);
        Assert.assertNull(result.getLastName().getErrorMessage());
    }

    @Test
    public void testTransformToUser() {
        // GIVEN
        UserModel userModel = createDefaultUserModelBuilder().build();
        // WHEN
        User result = underTest.transformToUser(userModel, ENCRYPTED_PASSWORD_VALUE);
        // THEN
        Assert.assertEquals(result.getId(), ID);
        Assert.assertEquals(result.getEmail(), EMAIL_VALUE);
        Assert.assertEquals(result.getPassword(), ENCRYPTED_PASSWORD_VALUE);
        Assert.assertEquals(result.getFirstName(), FIRST_NAME_VALUE);
        Assert.assertEquals(result.getLastName(), LAST_NAME_VALUE);
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

}
