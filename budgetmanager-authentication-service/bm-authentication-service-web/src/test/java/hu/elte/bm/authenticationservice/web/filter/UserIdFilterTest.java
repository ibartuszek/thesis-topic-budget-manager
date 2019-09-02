package hu.elte.bm.authenticationservice.web.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.apache.struts.mock.MockHttpServletResponse;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.domain.UserService;
import hu.elte.bm.authenticationservice.web.user.UserIdException;

public class UserIdFilterTest {

    private static final String EMAIL = "example@example.com";
    private static final String INVALID_EMAIL = "invalid email";
    private static final Long USER_ID = 1L;
    private static final Long OTHER_USER_ID = 2L;
    private static final String INVALID_USER_ID = "Peter";
    private MockHttpServletRequest request;
    private MockHttpServletResponse response = new MockHttpServletResponse();

    private UserIdFilter underTest;

    private IMocksControl control;
    private Authentication authentication;
    private SecurityContext securityContext;
    private FilterChain filterChain;
    private UserService userService;

    @BeforeMethod
    private void setUp() {
        request = new MockHttpServletRequest();
        control = EasyMock.createControl();
        authentication = control.createMock(Authentication.class);
        securityContext = control.createMock(SecurityContext.class);
        filterChain = control.createMock(FilterChain.class);
        userService = control.createMock(UserService.class);
        underTest = new UserIdFilter(userService);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test(expectedExceptions = UserIdException.class)
    public void testWhenUserCannotBeFoundInRepository() throws IOException, ServletException {
        // GIVEN
        request.addParameter("userId", USER_ID.toString());
        EasyMock.expect(securityContext.getAuthentication()).andReturn(authentication);
        EasyMock.expect(authentication.getPrincipal()).andReturn(INVALID_EMAIL);
        EasyMock.expect(userService.findUserByEmail(INVALID_EMAIL)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        try {
            underTest.doFilter(request, response, filterChain);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test(expectedExceptions = UserIdException.class)
    public void testWhenUserIdCannotBeConverted() throws IOException, ServletException {
        // GIVEN
        request.addParameter("userId", INVALID_USER_ID);
        EasyMock.expect(securityContext.getAuthentication()).andReturn(authentication);
        EasyMock.expect(authentication.getPrincipal()).andReturn(EMAIL);
        control.replay();
        // WHEN
        try {
            underTest.doFilter(request, response, filterChain);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test(expectedExceptions = UserIdException.class)
    public void testWhenUserIdIsDifferent() throws IOException, ServletException {
        // GIVEN
        request.addParameter("userId", OTHER_USER_ID.toString());
        EasyMock.expect(securityContext.getAuthentication()).andReturn(authentication);
        EasyMock.expect(authentication.getPrincipal()).andReturn(EMAIL);
        EasyMock.expect(userService.findUserByEmail(EMAIL)).andReturn(Optional.of(createExampleUserWithDefaultValues()));
        control.replay();
        // WHEN
        try {
            underTest.doFilter(request, response, filterChain);
        } finally {
            // THEN
            control.verify();
        }
    }

    @Test
    public void testWhenUserIdMatchesAndUserIdIsFromQueryString() throws IOException, ServletException {
        // GIVEN
        request.addParameter("userId", USER_ID.toString());
        EasyMock.expect(securityContext.getAuthentication()).andReturn(authentication);
        EasyMock.expect(authentication.getPrincipal()).andReturn(EMAIL);
        EasyMock.expect(userService.findUserByEmail(EMAIL)).andReturn(Optional.of(createExampleUserWithDefaultValues()));
        filterChain.doFilter(request, response);
        control.replay();
        // WHEN
        underTest.doFilter(request, response, filterChain);
        // THEN
        control.verify();
    }

    private User createExampleUserWithDefaultValues() {
        return User.builder()
                .withId(USER_ID)
                .withEmail(EMAIL)
                .build();
    }

}
