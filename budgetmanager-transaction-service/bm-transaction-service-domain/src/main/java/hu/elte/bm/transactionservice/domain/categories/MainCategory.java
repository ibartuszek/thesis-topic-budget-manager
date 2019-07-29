package hu.elte.bm.transactionservice.domain.categories;

import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

/**
 * MainCategory represents the main category of the transaction (income or outcome).
 */
public final class MainCategory {

    private final Long id;
    @NotBlank
    private final String name;
    @NotNull
    private final TransactionType transactionType;
    @NotNull
    private final Set<SubCategory> subCategorySet;

    private MainCategory(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.transactionType = builder.transactionType;
        this.subCategorySet = builder.subCategorySet;
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

    public Set<SubCategory> getSubCategorySet() {
        return subCategorySet;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MainCategory that = (MainCategory) o;
        return Objects.equals(name, that.name)
            && transactionType == that.transactionType
            && subCategorySet.size() == that.subCategorySet.size()
            && subCategorySet.containsAll(that.subCategorySet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, transactionType, subCategorySet);
    }

    @Override
    public String toString() {
        return "MainCategory{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", transactionType=" + transactionType
            + ", subCategorySet=" + subCategorySet
            + '}';
    }

    public static final class Builder {
        private Long id;
        private String name;
        private TransactionType transactionType;
        private Set<SubCategory> subCategorySet;

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

        public Builder withSubCategorySet(final Set<SubCategory> subCategorySet) {
            this.subCategorySet = subCategorySet;
            return this;
        }

        public MainCategory build() {
            return new MainCategory(this);
        }
    }
}
