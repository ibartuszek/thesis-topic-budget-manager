package hu.elte.bm.calculationservice.budgetdetails;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = BudgetDetailsElement.Builder.class)
public final class BudgetDetailsElement {

    private final double amount;
    private final String label;

    private BudgetDetailsElement(final Builder builder) {
        this.amount = builder.amount;
        this.label = builder.label;
    }

    public static Builder builder() {
        return new Builder();
    }

    public double getAmount() {
        return amount;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BudgetDetailsElement that = (BudgetDetailsElement) o;

        return new EqualsBuilder()
                .append(amount, that.amount)
                .append(label, that.label)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(amount)
                .append(label)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "BudgetDetailsElement{"
                + "amount=" + amount
                + ", label='" + label + '\''
                + '}';
    }

    public static final class Builder {

        private double amount;
        private String label;

        private Builder() {
        }

        public Builder withAmount(final double amount) {
            this.amount = amount;
            return this;
        }

        public Builder withLabel(final String label) {
            this.label = label;
            return this;
        }

        public BudgetDetailsElement build() {
            return new BudgetDetailsElement(this);
        }

    }
}
