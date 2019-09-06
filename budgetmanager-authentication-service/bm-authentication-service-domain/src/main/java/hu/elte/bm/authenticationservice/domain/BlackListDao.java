package hu.elte.bm.authenticationservice.domain;

import java.util.Optional;

public interface BlackListDao {

    Optional<String> saveToken(Long userId, String invalidToken);

    boolean isExpired(String token);

    void purgeExpiredTokens();

}
