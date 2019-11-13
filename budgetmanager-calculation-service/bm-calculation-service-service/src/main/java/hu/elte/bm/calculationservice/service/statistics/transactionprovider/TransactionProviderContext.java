package hu.elte.bm.calculationservice.service.statistics.transactionprovider;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import hu.elte.bm.calculationservice.forexclient.Rate;
import hu.elte.bm.transactionservice.Currency;

public final class TransactionProviderContext {

    private final Long userId;
    private final Currency currency;
    private final LocalDate start;
    private final LocalDate end;
    private final List<Rate> exchangeRates;

    private TransactionProviderContext(final Builder builder) {
        this.userId = builder.userId;
        this.currency = builder.currency;
        this.start = builder.start;
        this.end = builder.end;
        this.exchangeRates = builder.exchangeRates;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getUserId() {
        return userId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public List<Rate> getExchangeRates() {
        return exchangeRates;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionProviderContext context = (TransactionProviderContext) o;
        return userId.equals(context.userId)
            && currency == context.currency
            && start.equals(context.start)
            && end.equals(context.end)
            && exchangeRates.equals(context.exchangeRates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, currency, start, end, exchangeRates);
    }

    @Override
    public String toString() {
        return "TransactionProviderContext{"
            + "userId=" + userId
            + ", currency=" + currency
            + ", start=" + start
            + ", end=" + end
            + ", exchangeRates=" + exchangeRates
            + '}';
    }

    public static final class Builder {
        private Long userId;
        private Currency currency;
        private LocalDate start;
        private LocalDate end;
        private List<Rate> exchangeRates;

        private Builder() {
        }

        public Builder withUserId(final Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withCurrency(final Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder withStart(final LocalDate start) {
            this.start = start;
            return this;
        }

        public Builder withEnd(final LocalDate end) {
            this.end = end;
            return this;
        }

        public Builder withExchangeRates(final List<Rate> exchangeRates) {
            this.exchangeRates = exchangeRates;
            return this;
        }

        public TransactionProviderContext build() {
            return new TransactionProviderContext(this);
        }
    }
}
