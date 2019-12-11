package hu.elte.bm.authenticationservice.dal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DefaultBlackListDaoTest {

    private static final Long USER_ID = 1L;
    private static final String INVALID_TOKEN = "invalid_token";
    private static final Date NOW = new Date();
    private static final String OTHER_INVALID_TOKEN = "other invalid token";
    private static final long USER_ID_2 = 2L;
    private static final Integer EXPIRATION_LIMIT = 1;

    private DefaultBlackListDao underTest;
    private IMocksControl control;
    private InvalidTokenRepository invalidTokenRepository;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createControl();
        invalidTokenRepository = control.createMock(InvalidTokenRepository.class);
        underTest = new DefaultBlackListDao(invalidTokenRepository);
        ReflectionTestUtils.setField(underTest, "expirationLimit", EXPIRATION_LIMIT);
    }

    @Test
    public void testSaveToken() {
        // GIVEN
        InvalidTokenEntity token = InvalidTokenEntity.create(INVALID_TOKEN, USER_ID, NOW);
        EasyMock.expect(invalidTokenRepository.save(EasyMock.isA(InvalidTokenEntity.class))).andReturn(token);
        control.replay();
        // WHEN
        String result = underTest.saveToken(USER_ID, INVALID_TOKEN);
        // THEN
        control.verify();
        Assert.assertEquals(result, INVALID_TOKEN);
    }

    @Test
    public void testIsExpiredWhenTokenIsNotExpired() {
        // GIVEN
        List<InvalidTokenEntity> expiredTokens = new ArrayList<>();
        expiredTokens.add(InvalidTokenEntity.create(OTHER_INVALID_TOKEN, USER_ID_2, NOW));
        EasyMock.expect(invalidTokenRepository.findAll()).andReturn(expiredTokens);
        control.replay();
        // WHEN
        boolean result = underTest.isExpired(INVALID_TOKEN);
        // THEN
        control.verify();
        Assert.assertFalse(result);
    }

    @Test
    public void testIsExpiredWhenTokenIsExpired() {
        // GIVEN
        List<InvalidTokenEntity> expiredTokens = new ArrayList<>();
        expiredTokens.add(InvalidTokenEntity.create(INVALID_TOKEN, USER_ID, NOW));
        expiredTokens.add(InvalidTokenEntity.create(OTHER_INVALID_TOKEN, USER_ID_2, NOW));
        EasyMock.expect(invalidTokenRepository.findAll()).andReturn(expiredTokens);
        control.replay();
        // WHEN
        boolean result = underTest.isExpired(INVALID_TOKEN);
        // THEN
        control.verify();
        Assert.assertTrue(result);
    }

    @Test
    public void testPurgeExpiredTokens() {
        // GIVEN
        List<InvalidTokenEntity> expiredTokens = new ArrayList<>();
        expiredTokens.add(InvalidTokenEntity.create(INVALID_TOKEN, USER_ID, NOW));
        expiredTokens.add(InvalidTokenEntity.create(OTHER_INVALID_TOKEN, USER_ID_2, NOW));
        Capture<Date> capture = Capture.newInstance();
        EasyMock.expect(invalidTokenRepository.findExpiredTokens(EasyMock.capture(capture))).andReturn(expiredTokens);
        invalidTokenRepository.delete(expiredTokens.get(0));
        invalidTokenRepository.delete(expiredTokens.get(1));
        control.replay();
        // WHEN
        underTest.purgeExpiredTokens();
        // THEN
        control.verify();
    }

}
