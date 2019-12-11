package hu.elte.bm.authenticationservice.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import hu.elte.bm.authenticationservice.service.UserService;

@Component
public class UserIdValidator implements ConstraintValidator<ValidUserId, Long> {

    private final UserService userService;

    public UserIdValidator(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(ValidUserId validUserId) {
    }

    @Override
    public boolean isValid(final Long userId, final ConstraintValidatorContext constraintValidatorContext) {
        boolean result = false;
        Object userPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userId != null && userPrincipal instanceof String) {
            Long userIdFromRepository = userService.findUserByEmail((String) userPrincipal).getId();
            result = userId.equals(userIdFromRepository);
        }
        return result;
    }

}
