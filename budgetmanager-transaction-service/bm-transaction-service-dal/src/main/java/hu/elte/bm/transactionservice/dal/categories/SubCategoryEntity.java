package hu.elte.bm.transactionservice.dal.categories;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Entity
@Table(name = "sub_category",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "transaction_type" }) })
public final class SubCategoryEntity {

    private static final int MAXIMUM_NAME_LENGTH = 50;

    @Column(name = "name", length = MAXIMUM_NAME_LENGTH, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private SubCategoryEntity() {
    }

    private SubCategoryEntity(final Builder builder) {
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

        public SubCategoryEntity build() {
            return new SubCategoryEntity(this);
        }
    }
}
