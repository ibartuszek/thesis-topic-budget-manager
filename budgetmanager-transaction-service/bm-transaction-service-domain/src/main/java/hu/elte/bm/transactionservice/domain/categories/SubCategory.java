package hu.elte.bm.transactionservice.domain.categories;

import java.util.Objects;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

/**
 * It represents the secondary category of the transaction (can be income or outcome).
 */
public final class SubCategory {

    private final Long id;
    private final String name;
    private final TransactionType transactionType;

    private SubCategory(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.transactionType = builder.transactionType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubCategory that = (SubCategory) o;
        return Objects.equals(name, that.name)
            && transactionType == that.transactionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, transactionType);
    }

    @Override
    public String toString() {
        return "SubCategory{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", transactionType=" + transactionType
            + '}';
    }

    public static final class Builder {
        private Long id;
        private String name;
        private TransactionType transactionType;

        private Builder() {
        }

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withTransactionType(final TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public SubCategory build() {
            return new SubCategory(this);
        }
    }
}
