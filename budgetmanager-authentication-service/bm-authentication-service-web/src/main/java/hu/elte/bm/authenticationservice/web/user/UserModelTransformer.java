package hu.elte.bm.authenticationservice.web.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.commonpack.validator.ModelStringValue;

@Component
public class UserModelTransformer {

    @Value("${user.email.minimum_length:8}")
    private Integer emailMinimumLength;

    @Value("${user.email.maximum_length:50}")
    private Integer emailMaximumLength;

    @Value("${user.email.regexp:^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$}")
    private String emailRegexp;

    @Value("${user.password.minimum_length:8}")
    private Integer passwordMinimumLength;

    @Value("${user.password.maximum_length:16}")
    private Integer passwordMaximumLength;

    @Value("${user.first_name.minimum_length:2}")
    private Integer firstNameMinimumLength;

    @Value("${user.first_name.maximum_length:50}")
    private Integer firstNameMaximumLength;

    @Value("${user.last_name.minimum_length:2}")
    private Integer lastNameMinimumLength;

    @Value("${user.last_name.maximum_length:50}")
    private Integer lastNameMaximumLength;

    public UserModel transformToUserModel(final User user, final String passwordValue) {
        ModelStringValue email = ModelStringValue.builder()
            .withValue(user.getEmail())
            .build();
        ModelStringValue firstName = ModelStringValue.builder()
            .withValue(user.getFirstName())
            .build();
        ModelStringValue lastName = ModelStringValue.builder()
            .withValue(user.getLastName())
            .build();
        ModelStringValue password = ModelStringValue.builder()
            .withValue(passwordValue)
            .build();
        UserModel userModel = UserModel.builder()
            .withId(user.getId())
            .withEmail(email)
            .withPassword(password)
            .withFirstName(firstName)
            .withLastName(lastName)
            .build();
        setValidationFields(userModel);
        return userModel;
    }

    public User transformToUser(final UserModel userModel, final String password) {
        String email = userModel.getEmail().getValue();
        String firstName = userModel.getFirstName().getValue();
        String lastName = userModel.getLastName().getValue();
        return User.builder()
            .withId(userModel.getId())
            .withEmail(email)
            .withPassword(password)
            .withFirstName(firstName)
            .withLastName(lastName)
            .build();
    }

    void setValidationFields(final UserModel userModel) {
        userModel.getEmail().setMinimumLength(emailMinimumLength);
        userModel.getEmail().setMaximumLength(emailMaximumLength);
        userModel.getEmail().setRegexp(emailRegexp);
        userModel.getPassword().setMinimumLength(passwordMinimumLength);
        userModel.getPassword().setMaximumLength(passwordMaximumLength);
        userModel.getFirstName().setMinimumLength(firstNameMinimumLength);
        userModel.getFirstName().setMaximumLength(firstNameMaximumLength);
        userModel.getLastName().setMinimumLength(lastNameMinimumLength);
        userModel.getLastName().setMaximumLength(lastNameMaximumLength);
    }
}
