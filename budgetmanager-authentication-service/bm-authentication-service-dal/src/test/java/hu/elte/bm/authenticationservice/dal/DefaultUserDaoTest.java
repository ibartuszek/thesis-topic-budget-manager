package hu.elte.bm.authenticationservice.dal;

import java.util.Optional;

import org.easymock.Capture;
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
    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";

    private DefaultUserDao underTest;
    private IMocksControl control;
    private UserRepository userRepository;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        userRepository = control.createMock(UserRepository.class);
        underTest = new DefaultUserDao(new UserEntityTransformer(), userRepository);
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
        User expectedUser = createExampleUserBuilder().build();
        UserEntity userEntityFromRepository = createExampleUserEntity();
        EasyMock.expect(userRepository.findById(ID)).andReturn(Optional.of(userEntityFromRepository));
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
        User expectedUser = createExampleUserBuilder().build();
        UserEntity userEntityFromRepository = createExampleUserEntity();
        EasyMock.expect(userRepository.findByEmail(EMAIL)).andReturn(Optional.of(userEntityFromRepository));
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
        User userToSave = createExampleUserBuilder().withId(null).build();
        UserEntity userEntityFromRepository = createExampleUserEntity();
        User expectedUser = createExampleUserBuilder().build();
        Capture<UserEntity> userEntityCapture = Capture.newInstance();
        EasyMock.expect(userRepository.save(EasyMock.capture(userEntityCapture))).andReturn(userEntityFromRepository);
        control.replay();
        // WHEN
        Optional<User> result = underTest.registerUser(userToSave);
        // THEN
        control.verify();
        Assert.assertTrue(result.isPresent());
        Assert.assertNull(userEntityCapture.getValue().getId());
        Assert.assertEquals(userEntityCapture.getValue().getEmail(), EMAIL);
        Assert.assertEquals(userEntityCapture.getValue().getPassword(), PASSWORD);
        Assert.assertEquals(userEntityCapture.getValue().getFirstName(), FIRST_NAME);
        Assert.assertEquals(userEntityCapture.getValue().getLastName(), LAST_NAME);
        Assert.assertEquals(result.get(), expectedUser);
    }

    @Test
    public void testDeleteUser() {
        // GIVEN
        User userToDelete = createExampleUserBuilder().build();
        User expectedUser = createExampleUserBuilder().build();
        Capture<UserEntity> userEntityCapture = Capture.newInstance();
        userRepository.delete(EasyMock.capture(userEntityCapture));
        control.replay();
        // WHEN
        Optional<User> result = underTest.deleteUser(userToDelete);
        // THEN
        control.verify();
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(userEntityCapture.getValue().getId(), ID);
        Assert.assertEquals(userEntityCapture.getValue().getEmail(), EMAIL);
        Assert.assertEquals(userEntityCapture.getValue().getPassword(), PASSWORD);
        Assert.assertEquals(userEntityCapture.getValue().getFirstName(), FIRST_NAME);
        Assert.assertEquals(userEntityCapture.getValue().getLastName(), LAST_NAME);
        Assert.assertEquals(result.get(), expectedUser);
    }

    private User.Builder createExampleUserBuilder() {
        return User.builder()
            .withId(ID)
            .withEmail(EMAIL)
            .withPassword(PASSWORD)
            .withFirstName(FIRST_NAME)
            .withLastName(LAST_NAME);
    }

    private UserEntity createExampleUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(ID);
        userEntity.setEmail(EMAIL);
        userEntity.setPassword(PASSWORD);
        userEntity.setFirstName(FIRST_NAME);
        userEntity.setLastName(LAST_NAME);
        return userEntity;
    }

}
