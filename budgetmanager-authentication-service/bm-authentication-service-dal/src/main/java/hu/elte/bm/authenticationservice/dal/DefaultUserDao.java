package hu.elte.bm.authenticationservice.dal;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import hu.elte.bm.authenticationservice.domain.User;

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
                .map(userEntityTransformer::transformToUserWithMaskedPassword);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email)
            .map(userEntityTransformer::transformToUserWithMaskedPassword);
    }

    @Override
    public Optional<User> findByEmailWithPassword(final String email) {
        return userRepository.findByEmail(email)
                .map(userEntityTransformer::transformToUserWithRawPassword);
    }

    @Override
    @Transactional
    public User registerUser(final User user) {
        UserEntity userEntity = userRepository.save(userEntityTransformer.transformToUserEntity(user));
        return userEntityTransformer.transformToUserWithMaskedPassword(userEntity);
    }

    @Override
    @Transactional
    public User updateUser(final User user) {
        return registerUser(user);
    }

    @Override
    @Transactional
    public User deleteUser(final User user) {
        UserEntity userEntity = userEntityTransformer.transformToUserEntity(user);
        userRepository.delete(userEntity);
        return userEntityTransformer.transformToUserWithMaskedPassword(userEntity);
    }

}
