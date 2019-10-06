package hu.elte.bm.transactionservice.dal.picture;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "outcomePictures")
public class PictureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private byte[] picture;
    @Column(name = "user_id")
    private Long userId;

    PictureEntity() {
    }

    private PictureEntity(final Builder builder) {
        this.id = builder.id;
        this.picture = builder.picture;
        this.userId = builder.userId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(final byte[] picture) {
        this.picture = picture;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public static final class Builder {
        private Long id;
        private byte[] picture;
        private Long userId;

        private Builder() {
        }

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withPicture(final byte[] picture) {
            this.picture = picture;
            return this;
        }

        public Builder withUserId(final Long userId) {
            this.userId = userId;
            return this;
        }

        public PictureEntity build() {
            return new PictureEntity(this);
        }
    }
}
