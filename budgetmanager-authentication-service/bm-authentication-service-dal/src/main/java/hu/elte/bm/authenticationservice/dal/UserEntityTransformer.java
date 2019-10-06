package hu.elte.bm.authenticationservice.dal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.authenticationservice.domain.User;

@Component
public class UserEntityTransformer {

    @Value("${user.password.masked_value:********}")
    private String maskedPasswordValue;

    User transformToUserWithRawPassword(final UserEntity userEntity) {
        return User.builder()
            .withId(userEntity.getId())
            .withEmail(userEntity.getEmail())
            .withPassword(userEntity.getPassword())
            .withFirstName(userEntity.getFirstName())
            .withLastName(userEntity.getLastName())
            .withTracking(userEntity.isTracking())
            .build();
    }

    User transformToUserWithMaskedPassword(final UserEntity userEntity) {
        return User.builder()
            .withId(userEntity.getId())
            .withEmail(userEntity.getEmail())
            .withPassword(maskedPasswordValue)
            .withFirstName(userEntity.getFirstName())
            .withLastName(userEntity.getLastName())
            .withTracking(userEntity.isTracking())
            .build();
    }

    UserEntity transformToUserEntity(final User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setTracking(user.isTracking());
        return userEntity;
    }

}
