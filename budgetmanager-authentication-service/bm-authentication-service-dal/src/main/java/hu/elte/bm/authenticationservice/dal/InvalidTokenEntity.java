package hu.elte.bm.authenticationservice.dal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "invalid_tokens")
public final class InvalidTokenEntity {

    private static final int TOKEN_LENGTH = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = TOKEN_LENGTH, nullable = false, unique = true)
    private String invalidToken;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Date invalidationDate;

    InvalidTokenEntity() {
    }

    public static InvalidTokenEntity create(final String invalidToken, final Long userId, final Date date) {
        InvalidTokenEntity invalidTokenEntity = new InvalidTokenEntity();
        invalidTokenEntity.setInvalidToken(invalidToken);
        invalidTokenEntity.setUserId(userId);
        invalidTokenEntity.setInvalidationDate(date);
        return invalidTokenEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getInvalidToken() {
        return invalidToken;
    }

    public void setInvalidToken(final String invalidToken) {
        this.invalidToken = invalidToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Date getInvalidationDate() {
        return invalidationDate;
    }

    public void setInvalidationDate(final Date invalidationDate) {
        this.invalidationDate = invalidationDate;
    }

}
