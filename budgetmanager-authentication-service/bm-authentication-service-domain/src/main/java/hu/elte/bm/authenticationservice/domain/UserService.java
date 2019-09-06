package hu.elte.bm.authenticationservice.domain;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserService {

    private final UserDao userDao;
    private final BlackListDao blackListDao;

    @Value("${user.id_cannot_be_null:User's id cannot be null!}")
    private String userIdCannotBeNul;

    @Value("${user.email_cannot_be_null:User's email cannot be null!}")
    private String userEmailCannotBeNull;

    @Value("${user.user_cannot_be_null:User cannot be null!}")
    private String userCannotBeNull;

    @Value("${user.user_cannot_be_found:User cannot be found in the repository!}")
    private String userCannotBeFound;

    UserService(final UserDao userDao, final BlackListDao blackListDao) {
        this.userDao = userDao;
        this.blackListDao = blackListDao;
    }

    public Optional<User> findUserById(final Long id) {
        Assert.notNull(id, userIdCannotBeNul);
        return userDao.findById(id);
    }

    public Optional<User> findUserByEmail(final String email) {
        Assert.notNull(email, userEmailCannotBeNull);
        return userDao.findByEmail(email);
    }

    public Optional<User> registerUser(final User user) {
        Assert.notNull(user, userCannotBeNull);
        return findUserByEmail(user.getEmail()).isEmpty() ? userDao.registerUser(user) : Optional.empty();
    }

    public Optional<User> updateUser(final User user) {
        validateUserCanBeFound(user);
        return newEmailIsNotUsedByOtherUsers(user) ? userDao.updateUser(user) : Optional.empty();
    }

    public Optional<User> deleteUser(final User user) {
        validateUserCanBeFound(user);
        return userDao.deleteUser(user);
    }

    private void validateUserCanBeFound(final User user) {
        Assert.notNull(user, userCannotBeNull);
        if (findUserById(user.getId()).isEmpty()) {
            throw new UserException(userCannotBeFound, user);
        }
    }

    private boolean newEmailIsNotUsedByOtherUsers(final User user) {
        Optional<User> userWithSameEmail = userDao.findByEmail(user.getEmail());
        return userWithSameEmail.isEmpty() || userWithSameEmail.get().getId().equals(user.getId());
    }

    public Optional<String> saveTokenIntoBlackList(final Long userId, final String invalidToken) {
        return blackListDao.saveToken(userId, invalidToken);
    }

}
