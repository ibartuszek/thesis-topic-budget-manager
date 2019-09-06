package hu.elte.bm.authenticationservice.dal;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import hu.elte.bm.authenticationservice.domain.BlackListDao;

@Component("blackListDao")
public class DefaultBlackListDao implements BlackListDao {

    private static final int EXPIRATION_LIMIT_IN_DAYS = 1;

    private InvalidTokenRepository invalidTokenRepository;

    public DefaultBlackListDao(final InvalidTokenRepository invalidTokenRepository) {
        this.invalidTokenRepository = invalidTokenRepository;
    }

    @Override
    public Optional<String> saveToken(final Long userId, final String invalidToken) {
        invalidTokenRepository.save(InvalidTokenEntity.create(invalidToken, userId, new Date()));
        return Optional.of(invalidToken);
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
        c.add(Calendar.DATE, EXPIRATION_LIMIT_IN_DAYS);
        return c.getTime();
    }

}
