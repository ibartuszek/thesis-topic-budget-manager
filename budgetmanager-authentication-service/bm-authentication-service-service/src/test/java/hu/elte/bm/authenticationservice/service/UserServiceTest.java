package hu.elte.bm.authenticationservice.service;

import java.util.Optional;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.authenticationservice.dal.BlackListDao;
import hu.elte.bm.authenticationservice.dal.UserDao;
import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.domain.UserCannotBeFoundException;
import hu.elte.bm.authenticationservice.domain.UserEmailIsReservedException;

public class UserServiceTest {

    private static final Long INVALID_ID = 2L;
    private static final Long VALID_ID = 1L;
    private static final String DEFAULT_EMAIL = "email";
    private static final String RESERVED_EMAIL = "invalid email";
    private static final String NEW_EMAIL = "new email";
    private static final String DEFAULT_FIRST_NAME = "first name";
    private static final String NEW_FIRST_NAME = "new first name";
    private static final String DEFAULT_LAST_NAME = "last name";
    private static final String NEW_LAST_NAME = "new last name";
    private static final String NEW_PASSWORD = "new password";
    private static final String ENCODED_NEW_PASSWORD = "encoded password";
    private static final String MASKED_PASSWORD = "masked password";
    private static final String INVALID_TOKEN = "invalid token";

    private UserService underTest;

    private IMocksControl control;
    private UserDao userDao;
    private BlackListDao blackListDao;
    private BCryptPasswordEncoder encoder;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        userDao = control.createMock(UserDao.class);
        blackListDao = control.createMock(BlackListDao.class);
        encoder = control.createMock(BCryptPasswordEncoder.class);
        underTest = new UserService(userDao, blackListDao, encoder);
        ReflectionTestUtils.setField(underTest, "maskedPasswordValue", MASKED_PASSWORD);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindByIdWhenIdIsNull() {
        // GIVEN
        // WHEN
        underTest.findUserById(null);
        // THEN
    }

    @Test(expectedExceptions = UserCannotBeFoundException.class)
    public void testFindByIdWhenUserCannotBeFound() {
        // GIVEN
        EasyMock.expect(userDao.findById(INVALID_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        try {
            underTest.findUserById(INVALID_ID);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testFindByIdWhenUserIsPresent() {
        // GIVEN
        User user = createExampleUserBuilder().build();
        EasyMock.expect(userDao.findById(VALID_ID)).andReturn(Optional.of(user));
        control.replay();
        // WHEN
        User result = underTest.findUserById(VALID_ID);
        // THEN
        control.verify();
        Assert.assertEquals(result, user);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindByEmailWhenIdIsNull() {
        // GIVEN
        // WHEN
        underTest.findUserByEmail(null);
        // THEN
    }

    @Test(expectedExceptions = UserCannotBeFoundException.class)
    public void testFindByEmailWhenUserCannotBeFound() {
        // GIVEN
        EasyMock.expect(userDao.findByEmail(RESERVED_EMAIL)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        try {
            underTest.findUserByEmail(RESERVED_EMAIL);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testFindByEmailWhenUserIsPresent() {
        // GIVEN
        User user = createExampleUserBuilder().build();
        EasyMock.expect(userDao.findByEmail(DEFAULT_EMAIL)).andReturn(Optional.of(user));
        control.replay();
        // WHEN
        User result = underTest.findUserByEmail(DEFAULT_EMAIL);
        // THEN
        control.verify();
        Assert.assertEquals(result, user);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRegisterUserWhenUserIsNull() {
        // GIVEN
        // WHEN
        underTest.registerUser(null);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRegisterUserWhenUserIdIsNotNull() {
        // GIVEN
        User userToSave = createExampleUserBuilder()
                .withId(VALID_ID)
                .build();
        // WHEN
        underTest.registerUser(userToSave);
        // THEN
    }

    @Test(expectedExceptions = UserEmailIsReservedException.class)
    public void testRegisterUserWhenUserIsExisted() {
        // GIVEN
        User userToSave = createExampleUserBuilder()
                .withId(null)
                .withEmail(RESERVED_EMAIL)
                .build();
        User userFromRepository = createExampleUserBuilder()
                .withEmail(RESERVED_EMAIL)
                .build();
        EasyMock.expect(userDao.findByEmail(RESERVED_EMAIL)).andReturn(Optional.of(userFromRepository));
        control.replay();
        // WHEN
        try {
            underTest.registerUser(userToSave);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testRegisterUser() {
        // GIVEN
        User userToSave = createExampleUserBuilder()
                .withId(null)
                .build();
        User userToSaveWithEncodedPassword = createExampleUserBuilder()
                .withId(null)
                .withPassword(ENCODED_NEW_PASSWORD)
                .build();
        User userFromRepository = createExampleUserBuilder()
                .withPassword(MASKED_PASSWORD)
                .build();
        EasyMock.expect(userDao.findByEmail(DEFAULT_EMAIL)).andReturn(Optional.empty());
        EasyMock.expect(encoder.encode(userToSave.getPassword())).andReturn(ENCODED_NEW_PASSWORD);
        EasyMock.expect(userDao.registerUser(userToSaveWithEncodedPassword)).andReturn(userFromRepository);
        control.replay();
        // WHEN
        User result = underTest.registerUser(userToSave);
        // THEN
        control.verify();
        Assert.assertEquals(result.getId(), VALID_ID);
        Assert.assertEquals(result.getPassword(), MASKED_PASSWORD);
        Assert.assertEquals(result, userFromRepository);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenUserIsNull() {
        // GIVEN
        // WHEN
        underTest.updateUser(null);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateWhenUserIdIsNull() {
        // GIVEN
        User userToUpdate = createExampleUserBuilder()
                .withId(null)
                .build();
        // WHEN
        underTest.updateUser(userToUpdate);
        // THEN
    }

    @Test(expectedExceptions = UserCannotBeFoundException.class)
    public void testUpdateWhenUserCannotBeFound() {
        // GIVEN
        User userToUpdate = createExampleUserBuilder()
                .withId(INVALID_ID)
                .build();
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

    @Test(expectedExceptions = UserEmailIsReservedException.class)
    public void testUpdateWhenEmailIsReserved() {
        // GIVEN
        User userToUpdate = createExampleUserBuilder()
                .withEmail(RESERVED_EMAIL)
                .build();
        User originalUser = createExampleUserBuilder()
                .build();
        User otherUserWithSameEmail = createExampleUserBuilder()
                .withId(INVALID_ID)
                .withEmail(RESERVED_EMAIL)
                .build();
        EasyMock.expect(userDao.findById(VALID_ID)).andReturn(Optional.of(originalUser));
        EasyMock.expect(userDao.findByEmail(RESERVED_EMAIL)).andReturn(Optional.of(otherUserWithSameEmail));
        control.replay();
        // WHEN
        try {
            underTest.updateUser(userToUpdate);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testUpdateWhenUserNotModifiedHisOrHerEmailAndPassword() {
        // GIVEN
        User userToUpdate = createExampleUserBuilder()
                .withPassword(MASKED_PASSWORD)
                .withFirstName(NEW_FIRST_NAME)
                .withFirstName(NEW_LAST_NAME)
                .build();
        User originalUser = createExampleUserBuilder()
                .build();
        User expectedUser = createExampleUserBuilder()
                .withPassword(MASKED_PASSWORD)
                .withFirstName(NEW_FIRST_NAME)
                .withFirstName(NEW_LAST_NAME)
                .build();
        EasyMock.expect(userDao.findById(VALID_ID)).andReturn(Optional.of(originalUser));
        EasyMock.expect(userDao.updateUser(userToUpdate)).andReturn(expectedUser);
        control.replay();
        // WHEN
        User result = underTest.updateUser(userToUpdate);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedUser);
    }

    @Test
    public void testUpdateWhenUserModifiedHisOrHerEmail() {
        // GIVEN
        User userToUpdate = createExampleUserBuilder()
                .withEmail(NEW_EMAIL)
                .withPassword(MASKED_PASSWORD)
                .build();
        User originalUser = createExampleUserBuilder()
                .build();
        User expectedUser = createExampleUserBuilder()
                .withEmail(NEW_EMAIL)
                .withPassword(MASKED_PASSWORD)
                .build();
        EasyMock.expect(userDao.findById(VALID_ID)).andReturn(Optional.of(originalUser));
        EasyMock.expect(userDao.findByEmail(NEW_EMAIL)).andReturn(Optional.empty());
        EasyMock.expect(userDao.updateUser(userToUpdate)).andReturn(expectedUser);
        control.replay();
        // WHEN
        User result = underTest.updateUser(userToUpdate);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedUser);
    }

    @Test
    public void testUpdateWhenUserModifiedHisOrHerPassword() {
        // GIVEN
        User userToUpdate = createExampleUserBuilder()
                .withPassword(NEW_PASSWORD)
                .build();
        User originalUser = createExampleUserBuilder()
                .build();
        User expectedUser = createExampleUserBuilder()
                .withPassword(MASKED_PASSWORD)
                .build();
        Capture<User> userCapture = EasyMock.newCapture();
        EasyMock.expect(userDao.findById(VALID_ID)).andReturn(Optional.of(originalUser));
        EasyMock.expect(encoder.encode(NEW_PASSWORD)).andReturn(ENCODED_NEW_PASSWORD);
        EasyMock.expect(userDao.updateUser(EasyMock.capture(userCapture))).andReturn(expectedUser);
        control.replay();
        // WHEN
        User result = underTest.updateUser(userToUpdate);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedUser);
        Assert.assertEquals(userCapture.getValue().getPassword(), ENCODED_NEW_PASSWORD);
        Assert.assertEquals(result.getPassword(), MASKED_PASSWORD);
    }

    @Test
    public void testDelete() {
        // GIVEN
        User userToDelete = createExampleUserBuilder()
                .build();
        User originalUSer = createExampleUserBuilder().build();
        EasyMock.expect(userDao.findById(VALID_ID)).andReturn(Optional.of(originalUSer));
        EasyMock.expect(userDao.deleteUser(userToDelete)).andReturn(userToDelete);
        control.replay();
        // WHEN
        User result = underTest.deleteUser(userToDelete);
        // THEN
        control.verify();
        Assert.assertEquals(result, userToDelete);
    }

    @Test
    public void testSaveTokenIntoBlackList() {
        // GIVEN
        EasyMock.expect(blackListDao.saveToken(VALID_ID, INVALID_TOKEN)).andReturn(INVALID_TOKEN);
        control.replay();
        // WHEN
        String result = underTest.saveTokenIntoBlackList(VALID_ID, INVALID_TOKEN);
        // THEN
        control.verify();
        Assert.assertEquals(result, INVALID_TOKEN);
    }

    private User.Builder createExampleUserBuilder() {
        return User.builder()
                .withId(VALID_ID)
                .withEmail(DEFAULT_EMAIL)
                .withPassword(NEW_PASSWORD)
                .withFirstName(DEFAULT_FIRST_NAME)
                .withLastName(DEFAULT_LAST_NAME);
    }

}
