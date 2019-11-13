package hu.elte.bm.calculationservice.forexclient;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import hu.elte.bm.transactionservice.Currency;

@JsonDeserialize(builder = Rate.Builder.class)
public final class Rate {

    private final Currency originalCurrency;
    private final Currency newCurrency;
    private final double rate;

    private Rate(final Builder builder) {
        this.originalCurrency = builder.originalCurrency;
        this.newCurrency = builder.newCurrency;
        this.rate = builder.rate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Currency getOriginalCurrency() {
        return originalCurrency;
    }

    public Currency getNewCurrency() {
        return newCurrency;
    }

    public double getRate() {
        return rate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rate rate1 = (Rate) o;
        return Double.compare(rate1.rate, rate) == 0
            && originalCurrency == rate1.originalCurrency
            && newCurrency == rate1.newCurrency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalCurrency, newCurrency, rate);
    }

    @Override
    public String toString() {
        return "Rate{"
            + "originalCurrency=" + originalCurrency
            + ", newCurrency=" + newCurrency
            + ", rate=" + rate
            + '}';
    }

    public static final class Builder {
        private Currency originalCurrency;
        private Currency newCurrency;
        private double rate;

        private Builder() {
        }

        public Builder withOriginalCurrency(Currency originalCurrency) {
            this.originalCurrency = originalCurrency;
            return this;
        }

        public Builder withNewCurrency(Currency newCurrency) {
            this.newCurrency = newCurrency;
            return this;
        }

        public Builder withRate(double rate) {
            this.rate = rate;
            return this;
        }

        public Rate build() {
            return new Rate(this);
        }
    }
}
