package hu.elte.bm.authenticationservice.web.filter;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;

import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.domain.UserService;
import hu.elte.bm.authenticationservice.web.user.UserIdException;

public class UserIdFilter implements Filter {

    private static final String USER_ID = "userId";
    private static final String USER_ID_CANNOT_BE_CONVERTED = "'{0}' cannot be converted into an Id!'";
    private static final String USER_ID_NOT_MATCHES = "'{0}' from request is not match with:'{1}' from repository!";

    private UserService userService;

    public UserIdFilter(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        Object userPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = servletRequest.getParameter(USER_ID);
        if (userId != null && userPrincipal instanceof String) {
            Long userIdFromRequest = convertUserId(userId);
            Long userIdFromRepository = getUserIdFromRepository((String) userPrincipal);
            validateUserId(userId, userIdFromRequest, userIdFromRepository);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Long convertUserId(final String userId) {
        Long userIdFromRequest;
        try {
            userIdFromRequest = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new UserIdException(MessageFormat.format(USER_ID_CANNOT_BE_CONVERTED, userId, e), userId);
        }
        return userIdFromRequest;
    }

    private Long getUserIdFromRepository(final String userPrincipal) {
        Optional<User> user = userService.findUserByEmail(userPrincipal);
        return user.map(User::getId).orElse(null);
    }

    private void validateUserId(final String userId, final Long userIdFromRequest, final Long userIdFromRepository) {
        if (!userIdFromRequest.equals(userIdFromRepository)) {
            throw new UserIdException(MessageFormat.format(USER_ID_NOT_MATCHES, userId, userIdFromRepository), userId);
        }
    }

}
