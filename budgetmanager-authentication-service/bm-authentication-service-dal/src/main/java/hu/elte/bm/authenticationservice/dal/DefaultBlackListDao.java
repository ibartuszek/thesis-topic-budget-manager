package hu.elte.bm.authenticationservice.dal;

import java.util.Calendar;
import java.util.Date;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("blackListDao")
public class DefaultBlackListDao implements BlackListDao {

    @Value("${security.expiration.limit.indays:1}")
    private Integer expirationLimit;

    private InvalidTokenRepository invalidTokenRepository;

    DefaultBlackListDao(final InvalidTokenRepository invalidTokenRepository) {
        this.invalidTokenRepository = invalidTokenRepository;
    }

    @Override
    public String saveToken(final Long userId, final String invalidToken) {
        return invalidTokenRepository.save(InvalidTokenEntity.create(invalidToken, userId, new Date())).getInvalidToken();
    }

    @Override
    public boolean isExpired(final String token) {
        Iterable<InvalidTokenEntity> invalidTokenEntityList = invalidTokenRepository.findAll();
        return StreamSupport.stream(invalidTokenEntityList.spliterator(), false)
                .anyMatch(invalidTokenEntity -> token.equals(invalidTokenEntity.getInvalidToken()));
    }

    @Override
    public void purgeExpiredTokens() {
        Iterable<InvalidTokenEntity> invalidTokenEntityList = invalidTokenRepository.findExpiredTokens(getExpirationDate());
        StreamSupport.stream(invalidTokenEntityList.spliterator(), false).forEach(invalidTokenRepository::delete);
    }

    private Date getExpirationDate() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, expirationLimit);
        return c.getTime();
    }

}
