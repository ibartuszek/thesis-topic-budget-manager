package hu.elte.bm.authenticationservice.web.validation;

import javax.validation.ConstraintValidatorContext;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.After;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.service.UserService;

public class UserIdValidatorTest {

    private static final long ID = 1L;
    private static final long INVALID_ID = 2L;
    private static final String EMAIL = "email";

    private UserIdValidator underTest;

    private IMocksControl control;
    private UserService userService;
    private Authentication authentication;
    private SecurityContext securityContext;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeClass
    private void setup() {
        control = EasyMock.createControl();
        securityContext = control.createMock(SecurityContext.class);
        authentication = control.createMock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        userService = control.createMock(UserService.class);
        constraintValidatorContext = control.createMock(ConstraintValidatorContext.class);
        underTest = new UserIdValidator(userService);
    }

    @BeforeMethod
    private void beforeMethod() {
        EasyMock.reset(securityContext, authentication, userService);
    }

    @AfterMethod
    private void tearDown() {
        control.verify();
    }

    @Test
    public void testIsValidWhenUserNameNotMatches() {
        // GIVEN
        User userFromRepository = createUser();
        EasyMock.expect(securityContext.getAuthentication()).andReturn(authentication);
        EasyMock.expect(authentication.getPrincipal()).andReturn(EMAIL);
        EasyMock.expect(userService.findUserByEmail(EMAIL)).andReturn(userFromRepository);
        control.replay();
        // WHEN
        boolean result = underTest.isValid(INVALID_ID, constraintValidatorContext);
        // THEN
        Assert.assertFalse(result);
    }

    @Test
    public void testIsValidWhenUserNameMatches() {
        // GIVEN
        User userFromRepository = createUser();
        EasyMock.expect(securityContext.getAuthentication()).andReturn(authentication);
        EasyMock.expect(authentication.getPrincipal()).andReturn(EMAIL);
        EasyMock.expect(userService.findUserByEmail(EMAIL)).andReturn(userFromRepository);
        control.replay();
        // WHEN
        boolean result = underTest.isValid(ID, constraintValidatorContext);
        // THEN
        Assert.assertTrue(result);
    }

    private User createUser() {
        return User.builder()
                .withId(ID)
                .withEmail(EMAIL)
                .build();
    }

}
