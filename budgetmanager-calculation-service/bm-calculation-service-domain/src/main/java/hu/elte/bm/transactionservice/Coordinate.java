package hu.elte.bm.transactionservice;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = Coordinate.Builder.class)
public final class Coordinate {

    private final double latitude;
    private final double longitude;

    private Coordinate(final Builder builder) {
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public static Builder builder() {
        return new Builder();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinate that = (Coordinate) o;
        return Double.compare(that.latitude, latitude) == 0
            && Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "Coordinate{"
            + "latitude=" + latitude
            + ", longitude=" + longitude
            + '}';
    }

    public static final class Builder {
        private double latitude;
        private double longitude;

        private Builder() {
        }

        public Builder withLatitude(final double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(final double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Coordinate build() {
            return new Coordinate(this);
        }

    }
}
