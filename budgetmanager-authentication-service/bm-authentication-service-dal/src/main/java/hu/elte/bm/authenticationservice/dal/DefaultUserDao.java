package hu.elte.bm.authenticationservice.dal;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.domain.UserDao;

@Service("userDao")
public class DefaultUserDao implements UserDao {

    private final UserRepository userRepository;
    private final UserEntityTransformer userEntityTransformer;

    DefaultUserDao(final UserEntityTransformer userEntityTransformer, final UserRepository userRepository) {
        this.userEntityTransformer = userEntityTransformer;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(final Long id) {
        return userRepository.findById(id)
            .map(userEntityTransformer::transformToUser);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email)
            .map(userEntityTransformer::transformToUser);
    }

    @Override
    @Transactional
    public Optional<User> registerUser(final User user) {
        UserEntity userEntity = userRepository.save(userEntityTransformer.transformToUserEntity(user));
        return Optional.of(userEntityTransformer.transformToUser(userEntity));
    }

    @Override
    @Transactional
    public Optional<User> updateUser(final User user) {
        return registerUser(user);
    }

    @Override
    @Transactional
    public Optional<User> deleteUser(final User user) {
        userRepository.delete(userEntityTransformer.transformToUserEntity(user));
        return Optional.of(user);
    }
}
