package hu.elte.bm.calculationservice.forexclient;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ForexRate.Builder.class)
public final class ForexRate {

    private final double rate;
    private final long timestamp;

    private ForexRate(final Builder builder) {
        this.rate = builder.rate;
        this.timestamp = builder.timestamp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public double getRate() {
        return rate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ForexRate rate1 = (ForexRate) o;
        return Double.compare(rate1.rate, rate) == 0
            && timestamp == rate1.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rate, timestamp);
    }

    @Override
    public String toString() {
        return "ForexRate{"
            + "rate=" + rate
            + ", timestamp=" + timestamp
            + '}';
    }

    public static final class Builder {

        private double rate;
        private long timestamp;

        private Builder() {
        }

        public Builder withRate(final double rate) {
            this.rate = rate;
            return this;
        }

        public Builder withTimestamp(final long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ForexRate build() {
            return new ForexRate(this);
        }

    }
}
