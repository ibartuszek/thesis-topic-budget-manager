package hu.elte.bm.authenticationservice.dal;

import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.authenticationservice.domain.User;

public class DefaultUserDaoTest {

    private static final Long ID = 1L;
    private static final Long INVALID_ID = 2L;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String MASKED_PASSWORD = "masked password";
    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";

    private DefaultUserDao underTest;
    private IMocksControl control;
    private UserRepository userRepository;
    private UserEntityTransformer userEntityTransformer;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        userRepository = control.createMock(UserRepository.class);
        userEntityTransformer = control.createMock(UserEntityTransformer.class);
        underTest = new DefaultUserDao(userEntityTransformer, userRepository);
    }

    @Test
    public void testFindByIdWhenUserCannotBeFound() {
        // GIVEN
        EasyMock.expect(userRepository.findById(INVALID_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<User> result = underTest.findById(INVALID_ID);
        // THEN
        control.verify();
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testFindById() {
        // GIVEN
        User expectedUser = createExampleUserBuilder()
                .withPassword(MASKED_PASSWORD)
                .build();
        UserEntity userEntityFromRepository = createExampleUserEntity();
        EasyMock.expect(userRepository.findById(ID)).andReturn(Optional.of(userEntityFromRepository));
        EasyMock.expect(userEntityTransformer.transformToUserWithMaskedPassword(userEntityFromRepository)).andReturn(expectedUser);
        control.replay();
        // WHEN
        Optional<User> result = underTest.findById(ID);
        // THEN
        control.verify();
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), expectedUser);
    }

    @Test
    public void testFindByEmailWhenUserCannotBeFound() {
        // GIVEN
        EasyMock.expect(userRepository.findByEmail(EMAIL)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<User> result = underTest.findByEmail(EMAIL);
        // THEN
        control.verify();
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testFindByEmail() {
        // GIVEN
        User expectedUser = createExampleUserBuilder()
                .withPassword(MASKED_PASSWORD)
                .build();
        UserEntity userEntityFromRepository = createExampleUserEntity();
        EasyMock.expect(userRepository.findByEmail(EMAIL)).andReturn(Optional.of(userEntityFromRepository));
        EasyMock.expect(userEntityTransformer.transformToUserWithMaskedPassword(userEntityFromRepository)).andReturn(expectedUser);
        control.replay();
        // WHEN
        Optional<User> result = underTest.findByEmail(EMAIL);
        // THEN
        control.verify();
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), expectedUser);
    }

    @Test
    public void testRegisterUser() {
        // GIVEN
        User userToSave = createExampleUserBuilder()
                .withId(null)
                .build();
        User expectedUser = createExampleUserBuilder()
                .withPassword(MASKED_PASSWORD)
                .build();
        UserEntity userEntityToSave = createExampleUserEntityWithOutId();
        UserEntity userEntityFromRepository = createExampleUserEntity();
        EasyMock.expect(userEntityTransformer.transformToUserEntity(userToSave)).andReturn(userEntityToSave);
        EasyMock.expect(userRepository.save(userEntityToSave)).andReturn(userEntityFromRepository);
        EasyMock.expect(userEntityTransformer.transformToUserWithMaskedPassword(userEntityFromRepository)).andReturn(expectedUser);
        control.replay();
        // WHEN
        User result = underTest.registerUser(userToSave);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedUser);
        Assert.assertEquals(result.getPassword(), MASKED_PASSWORD);
    }

    @Test
    public void testDeleteUser() {
        // GIVEN
        User userToDelete = createExampleUserBuilder().build();
        User expectedUser = createExampleUserBuilder()
                .withPassword(MASKED_PASSWORD)
                .build();
        UserEntity userEntityToDelete = createExampleUserEntityWithOutId();
        EasyMock.expect(userEntityTransformer.transformToUserEntity(userToDelete)).andReturn(userEntityToDelete);
        userRepository.delete(userEntityToDelete);
        EasyMock.expect(userEntityTransformer.transformToUserWithMaskedPassword(userEntityToDelete)).andReturn(expectedUser);
        control.replay();
        // WHEN
        User result = underTest.deleteUser(userToDelete);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedUser);
        Assert.assertEquals(result.getPassword(), MASKED_PASSWORD);
    }

    private User.Builder createExampleUserBuilder() {
        return User.builder()
                .withId(ID)
                .withEmail(EMAIL)
                .withPassword(PASSWORD)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME);
    }

    private UserEntity createExampleUserEntityWithOutId() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(EMAIL);
        userEntity.setPassword(PASSWORD);
        userEntity.setFirstName(FIRST_NAME);
        userEntity.setLastName(LAST_NAME);
        return userEntity;
    }

    private UserEntity createExampleUserEntity() {
        UserEntity userEntity = createExampleUserEntityWithOutId();
        userEntity.setId(ID);
        return userEntity;
    }

}
