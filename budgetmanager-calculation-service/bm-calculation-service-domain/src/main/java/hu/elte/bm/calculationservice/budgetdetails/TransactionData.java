package hu.elte.bm.calculationservice.budgetdetails;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = TransactionData.Builder.class)
public final class TransactionData {

    private final String mainCategoryName;
    private final String subCategoryName;
    private final double amount;

    private TransactionData(final Builder builder) {
        this.mainCategoryName = builder.mainCategoryName;
        this.subCategoryName = builder.subCategoryName;
        this.amount = builder.amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMainCategoryName() {
        return mainCategoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionData that = (TransactionData) o;

        return new EqualsBuilder()
                .append(amount, that.amount)
                .append(mainCategoryName, that.mainCategoryName)
                .append(subCategoryName, that.subCategoryName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(mainCategoryName)
                .append(subCategoryName)
                .append(amount)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "TransactionData{"
                + "mainCategoryName='" + mainCategoryName + '\''
                + ", subCategoryName='" + subCategoryName + '\''
                + ", amount=" + amount
                + '}';
    }

    public static final class Builder {

        private String mainCategoryName;
        private String subCategoryName;
        private double amount;

        private Builder() {
        }

        public Builder withMainCategoryName(final String mainCategoryName) {
            this.mainCategoryName = mainCategoryName;
            return this;
        }

        public Builder withSubCategoryName(final String subCategoryName) {
            this.subCategoryName = subCategoryName;
            return this;
        }

        public Builder withAmount(final double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionData build() {
            return new TransactionData(this);
        }
    }
}
