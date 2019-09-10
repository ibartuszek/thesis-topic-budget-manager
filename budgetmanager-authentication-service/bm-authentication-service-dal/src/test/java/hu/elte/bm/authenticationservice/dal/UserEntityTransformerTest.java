package hu.elte.bm.authenticationservice.dal;

import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.authenticationservice.domain.User;

public class UserEntityTransformerTest {

    private static final Long ID = 1L;
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";
    private static final String PASSWORD = "password";
    private static final String MASKED_PASSWORD = "********";
    private UserEntityTransformer underTest;

    @BeforeMethod
    public void setup() {
        underTest = new UserEntityTransformer();
        ReflectionTestUtils.setField(underTest, "maskedPasswordValue", MASKED_PASSWORD);
    }

    @Test
    public void testTransformToUser() {
        // GIVEN
        UserEntity userEntity = createExampleUserEntity();
        // WHEN
        User result = underTest.transformToUserWithMaskedPassword(userEntity);
        // THEN
        Assert.assertEquals(result.getId(), ID);
        Assert.assertEquals(result.getEmail(), EMAIL);
        Assert.assertEquals(result.getPassword(), MASKED_PASSWORD);
        Assert.assertEquals(result.getFirstName(), FIRST_NAME);
        Assert.assertEquals(result.getLastName(), LAST_NAME);
    }

    @Test
    public void testTransformToUserEntity() {
        // GIVEN
        User user = createExampleUser();
        // WHEN
        UserEntity result = underTest.transformToUserEntity(user);
        // THEN
        Assert.assertEquals(result.getId(), ID);
        Assert.assertEquals(result.getEmail(), EMAIL);
        Assert.assertEquals(result.getPassword(), PASSWORD);
        Assert.assertEquals(result.getFirstName(), FIRST_NAME);
        Assert.assertEquals(result.getLastName(), LAST_NAME);
    }

    private User createExampleUser() {
        return User.builder()
                .withId(ID)
                .withEmail(EMAIL)
                .withPassword(PASSWORD)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .build();
    }

    private UserEntity createExampleUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(FIRST_NAME);
        userEntity.setLastName(LAST_NAME);
        userEntity.setEmail(EMAIL);
        userEntity.setId(ID);
        userEntity.setPassword(PASSWORD);
        return userEntity;
    }

}
