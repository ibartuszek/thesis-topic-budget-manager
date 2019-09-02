package hu.elte.bm.authenticationservice.web.validation;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.domain.UserService;

@Component
public class UserIdValidator implements ConstraintValidator<ValidUserId, Long> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ValidUserId validUserId) {
    }

    @Override
    public boolean isValid(final Long userId, final ConstraintValidatorContext constraintValidatorContext) {
        boolean result = false;
        Object userPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userId != null && userPrincipal instanceof String) {
            Long userIdFromRepository = getUserIdFromRepository((String) userPrincipal);
            result = userId.equals(userIdFromRepository);
        }
        return result;
    }

    private Long getUserIdFromRepository(final String userPrincipal) {
        Optional<User> user = userService.findUserByEmail(userPrincipal);
        return user.map(User::getId).orElse(null);
    }

}
