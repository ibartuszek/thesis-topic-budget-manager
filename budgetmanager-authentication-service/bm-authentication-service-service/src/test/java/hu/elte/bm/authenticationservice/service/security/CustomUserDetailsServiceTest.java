package hu.elte.bm.authenticationservice.service.security;

import java.util.Collections;
import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.security.core.userdetails.UserDetails;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.authenticationservice.dal.UserDao;
import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.domain.UserCannotBeFoundException;

public class CustomUserDetailsServiceTest {

    private static final long ID = 1L;
    private static final String USER_NAME = "user name";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";

    private CustomUserDetailsService underTest;

    private IMocksControl control;
    private UserDao userDao;

    @BeforeMethod
    private void setup() {
        control = EasyMock.createControl();
        userDao = control.createMock(UserDao.class);
        underTest = new CustomUserDetailsService(userDao);
    }

    @Test(expectedExceptions = UserCannotBeFoundException.class)
    public void tesLoadUserByUserNameWhenUserCannotBeFound() {
        // GIVEN
        EasyMock.expect(userDao.findByEmailWithPassword(USER_NAME)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        try {
            underTest.loadUserByUsername(USER_NAME);
        } finally {
            // THEN
            control.verify();
        }
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
        control.verify();
        Assert.assertEquals(result.getUsername(), USER_NAME);
        Assert.assertEquals(result.getPassword(), PASSWORD);
        Assert.assertEquals(result.getAuthorities(), Collections.emptyList());
    }

}
