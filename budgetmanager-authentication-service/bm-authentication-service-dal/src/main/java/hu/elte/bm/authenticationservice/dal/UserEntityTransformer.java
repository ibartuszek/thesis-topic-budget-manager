package hu.elte.bm.authenticationservice.dal;

import org.springframework.stereotype.Component;

import hu.elte.bm.authenticationservice.domain.User;

@Component
public class UserEntityTransformer {

    User transformToUser(final UserEntity userEntity) {
        return User.builder()
            .withId(userEntity.getId())
            .withEmail(userEntity.getEmail())
            .withPassword(userEntity.getPassword())
            .withFirstName(userEntity.getFirstName())
            .withLastName(userEntity.getLastName())
            .build();
    }

    UserEntity transformToUserEntity(final User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        return userEntity;
    }

}
