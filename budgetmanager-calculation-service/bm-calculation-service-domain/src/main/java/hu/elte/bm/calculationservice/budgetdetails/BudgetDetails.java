package hu.elte.bm.calculationservice.budgetdetails;

import java.util.Arrays;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = BudgetDetails.Builder.class)
public final class BudgetDetails {

    private final BudgetDetailsElement totalIncomes;
    private final BudgetDetailsElement totalExpenses;
    private final BudgetDetailsElement savings;
    private final TransactionData[] incomes;
    private final TransactionData[] outcomes;

    private BudgetDetails(final Builder builder) {
        this.totalIncomes = builder.totalIncomes;
        this.totalExpenses = builder.totalExpenses;
        this.savings = builder.savings;
        this.incomes = builder.incomes;
        this.outcomes = builder.outcomes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public BudgetDetailsElement getTotalIncomes() {
        return totalIncomes;
    }

    public BudgetDetailsElement getTotalExpenses() {
        return totalExpenses;
    }

    public BudgetDetailsElement getSavings() {
        return savings;
    }

    public TransactionData[] getIncomes() {
        return incomes;
    }

    public TransactionData[] getOutcomes() {
        return outcomes;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BudgetDetails that = (BudgetDetails) o;

        return new EqualsBuilder()
                .append(totalIncomes, that.totalIncomes)
                .append(totalExpenses, that.totalExpenses)
                .append(savings, that.savings)
                .append(incomes, that.incomes)
                .append(outcomes, that.outcomes)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(totalIncomes)
                .append(totalExpenses)
                .append(savings)
                .append(incomes)
                .append(outcomes)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "BudgetDetails{"
                + "totalIncomes=" + totalIncomes
                + ", totalExpenses=" + totalExpenses
                + ", savings=" + savings
                + ", incomes=" + Arrays.toString(incomes)
                + ", outcomes=" + Arrays.toString(outcomes)
                + '}';
    }

    public static final class Builder {

        private BudgetDetailsElement totalIncomes;
        private BudgetDetailsElement totalExpenses;
        private BudgetDetailsElement savings;
        private TransactionData[] incomes;
        private TransactionData[] outcomes;

        private Builder() {
        }

        public Builder withTotalIncomes(final BudgetDetailsElement totalIncomes) {
            this.totalIncomes = totalIncomes;
            return this;
        }

        public Builder withTotalExpenses(final BudgetDetailsElement totalExpenses) {
            this.totalExpenses = totalExpenses;
            return this;
        }

        public Builder withSavings(final BudgetDetailsElement savings) {
            this.savings = savings;
            return this;
        }

        public Builder withIncomes(final TransactionData[] incomes) {
            this.incomes = incomes;
            return this;
        }

        public Builder withOutcomes(final  TransactionData[] outcomes) {
            this.outcomes = outcomes;
            return this;
        }

        public BudgetDetails build() {
            return new BudgetDetails(this);
        }

    }
}
