package hu.elte.bm.transactionservice.web.common;

public final class ModelAmountValue {

    private Double value;
    private String errorMessage;
    private Boolean positive;
    private Boolean positiveOrZero;

    private ModelAmountValue() {
    }

    private ModelAmountValue(final Builder builder) {
        this.value = builder.value;
        this.positive = builder.positive;
        this.positiveOrZero = builder.positiveOrZero;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Double getValue() {
        return value;
    }

    public void setValue(final Double value) {
        this.value = value;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getPositive() {
        return positive;
    }

    public void setPositive(final Boolean positive) {
        this.positive = positive;
    }

    public Boolean getPositiveOrZero() {
        return positiveOrZero;
    }

    public void setPositiveOrZero(final Boolean positiveOrZero) {
        this.positiveOrZero = positiveOrZero;
    }

    public static final class Builder {
        private Double value;
        private Boolean positive;
        private Boolean positiveOrZero;

        public Builder withValue(final Double value) {
            this.value = value;
            return this;
        }

        public Builder withPositive(final Boolean positive) {
            this.positive = positive;
            return this;
        }

        public Builder withPositiveOrZero(final Boolean positiveOrZero) {
            this.positiveOrZero = positiveOrZero;
            return this;
        }

        public ModelAmountValue build() {
            return new ModelAmountValue(this);
        }
    }
}
