package hu.elte.bm.authenticationservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.authenticationservice.dal.BlackListDao;
import hu.elte.bm.authenticationservice.dal.UserDao;
import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.domain.UserCannotBeFoundException;
import hu.elte.bm.authenticationservice.domain.UserEmailIsReservedException;

@Service
public class UserService {

    private final UserDao userDao;
    private final BlackListDao blackListDao;
    private final BCryptPasswordEncoder encoder;

    @Value("${user.id_cannot_be_null:User's id cannot be null!}")
    private String userIdCannotBeNul;

    @Value("${user.email_cannot_be_null:User's email cannot be null!}")
    private String userEmailCannotBeNull;

    @Value("${user.user_cannot_be_null:User cannot be null!}")
    private String userCannotBeNull;

    @Value("${user.id_must_be_null:New user's id cannot be null!}")
    private String userIdMustBeNull;

    @Value("${user.password.masked_value:********}")
    private String maskedPasswordValue;

    @Value("${user.user_cannot_be_found:User cannot be found in the repository!}")
    private String userCannotBeFound;

    @Value("${user.user_email_is_reserved:This email has been used already!}")
    private String userEmailIsReserved;

    UserService(final UserDao userDao, final BlackListDao blackListDao, final BCryptPasswordEncoder encoder) {
        this.userDao = userDao;
        this.blackListDao = blackListDao;
        this.encoder = encoder;
    }

    public User findUserById(final Long id) {
        Assert.notNull(id, userIdCannotBeNul);
        Optional<User> userFromRepository = userDao.findById(id);
        validateUserFromRepository(userFromRepository.isEmpty());
        return userFromRepository.get();
    }

    public User findUserByEmail(final String email) {
        Assert.notNull(email, userEmailCannotBeNull);
        Optional<User> userFromRepository = userDao.findByEmail(email);
        validateUserFromRepository(userFromRepository.isEmpty());
        return userFromRepository.get();
    }

    public User registerUser(final User user) {
        Assert.notNull(user, userCannotBeNull);
        Assert.isNull(user.getId(), userIdMustBeNull);
        validateEmailIsNotUsed(user.getEmail());
        return userDao.registerUser(User.createUserWithNewPassword(user, encoder.encode(user.getPassword())));
    }

    public User updateUser(final User user) {
        User userFromRepository = getValidatedUserFromRepository(user);
        validateEmailIsNotUsedByOtherUsers(user, userFromRepository);
        User userToUpdate = maskedPasswordValue.equals(user.getPassword()) ? user : User.createUserWithNewPassword(user, encoder.encode(user.getPassword()));
        return userDao.updateUser(userToUpdate);
    }

    private User getValidatedUserFromRepository(final User user) {
        Assert.notNull(user, userCannotBeNull);
        Assert.notNull(user.getId(), userIdCannotBeNul);
        Optional<User> userFromRepository = userDao.findById(user.getId());
        validateUserFromRepository(userFromRepository.isEmpty());
        return userFromRepository.get();
    }

    public User deleteUser(final User user) {
        getValidatedUserFromRepository(user);
        return userDao.deleteUser(user);
    }

    public String saveTokenIntoBlackList(final Long userId, final String invalidToken) {
        return blackListDao.saveToken(userId, invalidToken);
    }

    private void validateUserFromRepository(final boolean notFound) {
        if (notFound) {
            throw new UserCannotBeFoundException(userCannotBeFound);
        }
    }

    private void validateEmailIsNotUsed(final String email) {
        Optional<User> userFromRepository = userDao.findByEmail(email);
        if (userFromRepository.isPresent()) {
            throw new UserEmailIsReservedException(userEmailIsReserved, email);
        }
    }

    private void validateEmailIsNotUsedByOtherUsers(final User user, final User userFromRepository) {
        if (!user.getEmail().equals(userFromRepository.getEmail())) {
            Optional<User> userWithSameEmail = userDao.findByEmail(user.getEmail());
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().equals(userFromRepository)) {
                throw new UserEmailIsReservedException(userEmailIsReserved, user.getEmail());
            }
        }
    }

}
