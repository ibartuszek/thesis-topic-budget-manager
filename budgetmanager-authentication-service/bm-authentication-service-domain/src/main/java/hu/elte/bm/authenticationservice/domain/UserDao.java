package hu.elte.bm.authenticationservice.domain;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> registerUser(User user);

    Optional<User> updateUser(User user);

    Optional<User> deleteUser(User user);

}
