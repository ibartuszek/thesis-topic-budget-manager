package hu.elte.bm.authenticationservice.dal;

public interface BlackListDao {

    String saveToken(Long userId, String invalidToken);

    boolean isExpired(String token);

    void purgeExpiredTokens();

}
