package hu.elte.bm.transactionservice.domain.transaction;

import java.util.Objects;

public final class TransactionContext {

    private final TransactionType transactionType;
    private final Long userId;

    private TransactionContext(final Builder builder) {
        this.transactionType = builder.transactionType;
        this.userId = builder.userId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionContext that = (TransactionContext) o;
        return transactionType == that.transactionType
            && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionType, userId);
    }

    public static final class Builder {

        private TransactionType transactionType;
        private Long userId;

        private Builder() {
        }

        public Builder withTransactionType(final TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder withUserId(final Long userId) {
            this.userId = userId;
            return this;
        }

        public TransactionContext build() {
            return new TransactionContext(this);
        }

    }

}
