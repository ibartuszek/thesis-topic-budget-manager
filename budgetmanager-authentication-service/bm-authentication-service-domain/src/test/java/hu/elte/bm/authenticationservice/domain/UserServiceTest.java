package hu.elte.bm.authenticationservice.domain;

import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserServiceTest {

    private static final Long INVALID_ID = 2L;
    private static final Long VALID_ID = 1L;
    private static final String INVALID_EMAIL = "invalid email";
    private static final String VALID_EMAIL = "email";
    private static final String FIRST_NAME = "first name";
    private static final String NEW_FIRST_NAME = "new first name";
    private static final String LAST_NAME = "last name";
    private static final String PASSWORD = "password";

    private UserService underTest;

    private IMocksControl control;
    private UserDao userDao;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        userDao = control.createMock(UserDao.class);
        underTest = new UserService(userDao);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindByIdWhenIdIsNull() {
        // GIVEN
        // WHEN
        underTest.findUserById(null);
        // THEN
    }

    @Test
    public void testFindByIdWhenUserCannotBeFound() {
        // GIVEN
        EasyMock.expect(userDao.findById(INVALID_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<User> result = underTest.findUserById(INVALID_ID);
        // THEN
        control.verify();
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testFindByIdWhenUserIsPresent() {
        // GIVEN
        User user = createExampleUserBuilder().build();
        EasyMock.expect(userDao.findById(VALID_ID)).andReturn(Optional.of(user));
        control.replay();
        // WHEN
        Optional<User> result = underTest.findUserById(VALID_ID);
        // THEN
        control.verify();
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), user);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindByEmailWhenIdIsNull() {
        // GIVEN
        // WHEN
        underTest.findUserByEmail(null);
        // THEN
    }

    @Test
    public void testFindByEmailWhenUserCannotBeFound() {
        // GIVEN
        EasyMock.expect(userDao.findByEmail(INVALID_EMAIL)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<User> result = underTest.findUserByEmail(INVALID_EMAIL);
        // THEN
        control.verify();
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testFindByEmailWhenUserIsPresent() {
        // GIVEN
        User user = createExampleUserBuilder().build();
        EasyMock.expect(userDao.findByEmail(VALID_EMAIL)).andReturn(Optional.of(user));
        control.replay();
        // WHEN
        Optional<User> result = underTest.findUserByEmail(VALID_EMAIL);
        // THEN
        control.verify();
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), user);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRegisterUserWhenUserIsNull() {
        // GIVEN
        // WHEN
        underTest.registerUser(null);
        // THEN
    }

    @Test
    public void testRegisterUserWhenUserIsExisted() {
        // GIVEN
        User userToSave = createExampleUserBuilder().withId(null).build();
        User userFromRepository = createExampleUserBuilder().build();
        EasyMock.expect(userDao.findByEmail(VALID_EMAIL)).andReturn(Optional.of(userFromRepository));
        control.replay();
        // WHEN
        Optional<User> result = underTest.registerUser(userToSave);
        // THEN
        control.verify();
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testRegisterUser() {
        // GIVEN
        User userToSave = createExampleUserBuilder().withId(null).build();
        User userFromRepository = createExampleUserBuilder().build();
        EasyMock.expect(userDao.findByEmail(VALID_EMAIL)).andReturn(Optional.empty());
        EasyMock.expect(userDao.registerUser(userFromRepository)).andReturn(Optional.of(userFromRepository));
        control.replay();
        // WHEN
        Optional<User> result = underTest.registerUser(userToSave);
        // THEN
        control.verify();
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getId(), VALID_ID);
        Assert.assertEquals(result.get(), userFromRepository);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenUserIsNull() {
        // GIVEN
        // WHEN
        underTest.updateUser(null);
        // THEN
    }

    @Test(expectedExceptions = UserException.class)
    public void testUpdateWhenUserCannotBeFound() {
        // GIVEN
        User userToUpdate = createExampleUserBuilder().withId(INVALID_ID).build();
        EasyMock.expect(userDao.findById(INVALID_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        try {
            underTest.updateUser(userToUpdate);
        } finally {
            control.verify();
        }
        // THEN
    }

    @Test
    public void testUpdateWhenUserCannotBeUpdated() {
        // GIVEN
        User userToUpdate = createExampleUserBuilder().withEmail(INVALID_EMAIL).build();
        User originalUser = createExampleUserBuilder().build();
        User otherUserWithSameEmail = createExampleUserBuilder().withId(INVALID_ID).withEmail(INVALID_EMAIL).build();
        EasyMock.expect(userDao.findById(VALID_ID)).andReturn(Optional.of(originalUser));
        EasyMock.expect(userDao.findByEmail(INVALID_EMAIL)).andReturn(Optional.of(otherUserWithSameEmail));
        control.replay();
        // WHEN
        Optional<User> result = underTest.updateUser(userToUpdate);
        // THEN
        control.verify();
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateWhenUserNotModifiedHisOrHerEmail() {
        // GIVEN
        User userToUpdate = createExampleUserBuilder().withEmail(VALID_EMAIL).withFirstName(NEW_FIRST_NAME).build();
        User originalUser = createExampleUserBuilder().build();
        EasyMock.expect(userDao.findById(VALID_ID)).andReturn(Optional.of(originalUser));
        EasyMock.expect(userDao.findByEmail(VALID_EMAIL)).andReturn(Optional.of(originalUser));
        EasyMock.expect(userDao.updateUser(userToUpdate)).andReturn(Optional.of(userToUpdate));
        control.replay();
        // WHEN
        Optional<User> result = underTest.updateUser(userToUpdate);
        // THEN
        control.verify();
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), userToUpdate);
    }

    @Test
    public void testUpdate() {
        // GIVEN
        User userToUpdate = createExampleUserBuilder().withEmail(INVALID_EMAIL).build();
        User originalUser = createExampleUserBuilder().build();
        EasyMock.expect(userDao.findById(VALID_ID)).andReturn(Optional.of(originalUser));
        EasyMock.expect(userDao.findByEmail(INVALID_EMAIL)).andReturn(Optional.empty());
        EasyMock.expect(userDao.updateUser(userToUpdate)).andReturn(Optional.of(userToUpdate));
        control.replay();
        // WHEN
        Optional<User> result = underTest.updateUser(userToUpdate);
        // THEN
        control.verify();
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), userToUpdate);
    }

    @Test
    public void testDelete() {
        // GIVEN
        User userToDelete = createExampleUserBuilder().withEmail(INVALID_EMAIL).build();
        User originalUSer = createExampleUserBuilder().build();
        EasyMock.expect(userDao.findById(VALID_ID)).andReturn(Optional.of(originalUSer));
        EasyMock.expect(userDao.deleteUser(userToDelete)).andReturn(Optional.of(userToDelete));
        control.replay();
        // WHEN
        Optional<User> result = underTest.deleteUser(userToDelete);
        // THEN
        control.verify();
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), userToDelete);
    }

    private User.Builder createExampleUserBuilder() {
        return User.builder()
            .withId(VALID_ID)
            .withEmail(VALID_EMAIL)
            .withPassword(PASSWORD)
            .withFirstName(FIRST_NAME)
            .withLastName(LAST_NAME);
    }

}
