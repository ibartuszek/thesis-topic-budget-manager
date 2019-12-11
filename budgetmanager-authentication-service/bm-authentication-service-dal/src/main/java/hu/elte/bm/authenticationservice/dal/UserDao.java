package hu.elte.bm.authenticationservice.dal;

import java.util.Optional;

import hu.elte.bm.authenticationservice.domain.User;

public interface UserDao {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailWithPassword(String email);

    User registerUser(User user);

    User updateUser(User user);

    User deleteUser(User user);

}
