package hu.elte.bm.calculationservice.app.test.utils;

import java.time.LocalDate;

import hu.elte.bm.transactionservice.TransactionType;

public final class TransactionServiceContext {

    private final Long userId;
    private final TransactionType type;
    private final LocalDate start;
    private final LocalDate end;

    private TransactionServiceContext(final Builder builder) {
        this.userId = builder.userId;
        this.type = builder.type;
        this.start = builder.start;
        this.end = builder.end;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getUserId() {
        return userId;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public static final class Builder {
        private Long userId;
        private TransactionType type;
        private LocalDate start;
        private LocalDate end;

        private Builder() {
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withType(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder withStart(LocalDate start) {
            this.start = start;
            return this;
        }

        public Builder withEnd(LocalDate end) {
            this.end = end;
            return this;
        }

        public TransactionServiceContext build() {
            return new TransactionServiceContext(this);
        }
    }
}
