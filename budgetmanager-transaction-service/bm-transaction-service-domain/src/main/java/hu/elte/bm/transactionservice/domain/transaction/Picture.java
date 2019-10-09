package hu.elte.bm.transactionservice.domain.transaction;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = Picture.Builder.class)
public final class Picture {

    private final Long id;
    private final String picture;

    private Picture(final Builder builder) {
        this.id = builder.id;
        this.picture = builder.picture;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }

    @Override
    public String toString() {
        return "Picture{"
            + "id=" + id
            + '}';
    }

    public static final class Builder {
        private Long id;
        private String picture;

        private Builder() {
        }

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withPicture(final String picture) {
            this.picture = picture;
            return this;
        }

        public Picture build() {
            return new Picture(this);
        }
    }

}
