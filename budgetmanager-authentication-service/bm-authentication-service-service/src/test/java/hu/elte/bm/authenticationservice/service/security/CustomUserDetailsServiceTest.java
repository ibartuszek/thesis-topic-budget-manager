package hu.elte.bm.authenticationservice.service.security;

import java.util.Collections;
import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.security.core.userdetails.UserDetails;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.authenticationservice.dal.UserDao;
import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.domain.exceptions.UserNotFoundException;

public class CustomUserDetailsServiceTest {

    private static final long ID = 1L;
    private static final String USER_NAME = "user name";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";

    private CustomUserDetailsService underTest;

    private IMocksControl control;
    private UserDao userDao;

    @BeforeClass
    private void setup() {
        control = EasyMock.createControl();
        userDao = control.createMock(UserDao.class);
        underTest = new CustomUserDetailsService(userDao);
    }

    @BeforeMethod
    public void reset() {
        control.reset();
    }

    @AfterMethod
    public void verify() {
        control.verify();
    }

    @Test(expectedExceptions = UserNotFoundException.class)
    public void tesLoadUserByUserNameWhenUserCannotBeFound() {
        // GIVEN
        EasyMock.expect(userDao.findByEmailWithPassword(USER_NAME)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        underTest.loadUserByUsername(USER_NAME);
        // THEN
    }

    @Test
    public void tesLoadUserByUserNameWhenUserCanBeFound() {
        // GIVEN
        User userFromRepository = User.builder()
                .withId(ID)
                .withEmail(USER_NAME)
                .withPassword(PASSWORD)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .build();
        EasyMock.expect(userDao.findByEmailWithPassword(USER_NAME)).andReturn(Optional.of(userFromRepository));
        control.replay();
        // WHEN
        UserDetails result = underTest.loadUserByUsername(USER_NAME);
        // THEN
        Assert.assertEquals(result.getUsername(), USER_NAME);
        Assert.assertEquals(result.getPassword(), PASSWORD);
        Assert.assertEquals(result.getAuthorities(), Collections.emptyList());
    }

}
