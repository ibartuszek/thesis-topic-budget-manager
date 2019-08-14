package hu.elte.bm.authenticationservice.domain;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserService {

    private static final String USER_ID_CANNOT_BE_NULL = "User's id cannot be null!";
    private static final String USER_EMAIL_CANNOT_BE_NULL = "User's email cannot be null!";
    private static final String USER_CANNOT_BE_NULL = "User cannot be null!";
    private static final String USER_CANNOT_BE_FOUND = "User cannot be found in the repository!";

    private final UserDao userDao;

    UserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> findUserById(final Long id) {
        Assert.notNull(id, USER_ID_CANNOT_BE_NULL);
        return userDao.findById(id);
    }

    public Optional<User> findUserByEmail(final String email) {
        Assert.notNull(email, USER_EMAIL_CANNOT_BE_NULL);
        return userDao.findByEmail(email);
    }

    public Optional<User> registerUser(final User user) {
        Assert.notNull(user, USER_CANNOT_BE_NULL);
        return findUserByEmail(user.getEmail()).isEmpty() ? userDao.registerUser(user) : Optional.empty();
    }

    public Optional<User> updateUser(final User user) {
        validateUserCanBeFound(user);
        return findUserByEmail(user.getEmail()).isEmpty() ? userDao.updateUser(user) : Optional.empty();
    }

    public Optional<User> deleteUser(final User user) {
        validateUserCanBeFound(user);
        return userDao.deleteUser(user);
    }

    private void validateUserCanBeFound(final User user) {
        Assert.notNull(user, USER_CANNOT_BE_NULL);
        if (findUserById(user.getId()).isEmpty()) {
            throw new UserException(USER_CANNOT_BE_FOUND, user);
        }
    }

}
