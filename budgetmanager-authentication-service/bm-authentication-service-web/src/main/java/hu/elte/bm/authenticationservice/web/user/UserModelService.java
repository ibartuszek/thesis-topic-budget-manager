package hu.elte.bm.authenticationservice.web.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.domain.UserException;
import hu.elte.bm.authenticationservice.domain.UserService;
import hu.elte.bm.commonpack.validator.ModelValidator;

@Service
public class UserModelService {

    private final BCryptPasswordEncoder encoder;
    private final ModelValidator validator;
    private final UserService userService;
    private final UserModelTransformer transformer;

    @Value("${user.user_cannot_be_found:User cannot be found in the repository!}")
    private String userCannotBeFound;

    @Value("${user.user_is_invalid:User is invalid!}")
    private String userIsInvalid;

    @Value("${user.user_has_been_registered:User registration is completed!}")
    private String userHasBeenRegistered;

    @Value("${user.user_has_been_updated:User data has been updated!}")
    private String userHasBeenUpdated;

    @Value("${user.user_has_been_deleted:User has been deleted!}")
    private String userHasBeenDeleted;

    @Value("${user.user_cannot_be_deleted:User cannot be deleted!}")
    private String userCannotBeDeleted;

    @Value("${user.user_email_is_reserved:This email has been used already!}")
    private String userEmailIsReserved;

    @Value("${user.password.masked_value:********}")
    private String maskedPasswordValue;

    UserModelService(final BCryptPasswordEncoder encoder, final ModelValidator validator,
        final UserService userService, final UserModelTransformer transformer) {
        this.encoder = encoder;
        this.validator = validator;
        this.userService = userService;
        this.transformer = transformer;
    }

    UserModelResponse findById(final Long id) {
        Optional<UserModel> userModel = userService.findUserById(id)
            .map(user -> transformer.transformToUserModel(user, maskedPasswordValue));
        UserModelResponse result = new UserModelResponse();
        if (userModel.isEmpty()) {
            result.setMessage(userCannotBeFound);
        } else {
            result.setSuccessful(true);
            result.setUserModel(userModel.get());
        }
        return result;
    }

    UserModelResponse registerUser(final UserModel userModel) {
        preValidateSavableUser(userModel);
        UserModelResponse result = createResponseWithDefaultValues(userModel);
        if (isValid(result.getUserModel())) {
            User savableUser = transformer.transformToUser(result.getUserModel(), encoder.encode(result.getUserModel().getPassword().getValue()));
            Optional<User> registeredUser = userService.registerUser(savableUser);
            updateResponse(registeredUser, result, userHasBeenRegistered, userEmailIsReserved);
        } else {
            result.setMessage(userIsInvalid);
        }
        return result;
    }

    UserModelResponse updateUser(final UserModel userModel) {
        preValidateUpdatableUser(userModel);
        UserModelResponse result = createResponseWithDefaultValues(userModel);
        if (isValid(result.getUserModel())) {
            Optional<User> savedUser = userService.updateUser(createUpdatableUser(userModel));
            updateResponse(savedUser, result, userHasBeenUpdated, userEmailIsReserved);
        } else {
            result.setMessage(userIsInvalid);
        }
        return result;
    }

    UserModelResponse deleteUser(final UserModel userModel) {
        preValidateUpdatableUser(userModel);
        UserModelResponse result = createResponseWithDefaultValues(userModel);
        if (isValid(result.getUserModel())) {
            Optional<User> deletedUser = userService.deleteUser(createUpdatableUser(userModel));
            updateResponse(deletedUser, result, userHasBeenDeleted, userCannotBeDeleted);
        } else {
            result.setMessage(userIsInvalid);
        }
        return result;
    }

    private void preValidateSavableUser(final UserModel userModel) {
        if (userModel.getId() != null) {
            throw new IllegalArgumentException(userIsInvalid);
        }
        validateUserModelFields(userModel);
    }

    private void preValidateUpdatableUser(final UserModel userModel) {
        Assert.notNull(userModel.getId(), userIsInvalid);
        validateUserModelFields(userModel);
    }

    private void validateUserModelFields(final UserModel userModel) {
        Assert.notNull(userModel.getEmail(), userIsInvalid);
        Assert.notNull(userModel.getFirstName(), userIsInvalid);
        Assert.notNull(userModel.getLastName(), userIsInvalid);
        Assert.notNull(userModel.getPassword(), userIsInvalid);
    }

    private UserModelResponse createResponseWithDefaultValues(final UserModel userModel) {
        transformer.setValidationFields(userModel);
        UserModelResponse result = new UserModelResponse();
        result.setUserModel(userModel);
        return result;
    }

    private boolean isValid(final UserModel userModel) {
        boolean password = true;
        if (!maskedPasswordValue.equals(userModel.getPassword().getValue())) {
            password = validator.validate(userModel.getPassword(), "Password");
        }
        boolean email = validator.validate(userModel.getEmail(), "Email");
        boolean firstName = validator.validate(userModel.getFirstName(), "First name");
        boolean lastName = validator.validate(userModel.getLastName(), "Last name");
        return password && email && firstName && lastName;
    }

    private User createUpdatableUser(final UserModel userModel) {
        User result;
        Optional<User> originalUser = userService.findUserById(userModel.getId());
        if (originalUser.isEmpty()) {
            throw new UserException(userCannotBeFound, transformer.transformToUser(userModel, maskedPasswordValue));
        } else if (maskedPasswordValue.equals(userModel.getPassword().getValue())) {
            result = transformer.transformToUser(userModel, originalUser.get().getPassword());
        } else {
            result = transformer.transformToUser(userModel, encoder.encode(userModel.getPassword().getValue()));
        }
        return result;
    }

    private void updateResponse(final Optional<User> user,
        final UserModelResponse response, final String successMessage, final String unSuccessMessage) {
        if (user.isPresent()) {
            response.setUserModel(transformer.transformToUserModel(user.get(), maskedPasswordValue));
            response.setSuccessful(true);
            response.setMessage(successMessage);
        } else {
            response.setMessage(unSuccessMessage);
        }
    }
}
