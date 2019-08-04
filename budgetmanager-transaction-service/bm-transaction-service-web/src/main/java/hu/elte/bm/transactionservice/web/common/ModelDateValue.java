package hu.elte.bm.transactionservice.web.common;

import java.time.LocalDate;

public final class ModelDateValue {

    private LocalDate value;
    private String errorMessage;
    private LocalDate possibleFirstDay;

    private ModelDateValue() {
    }

    private ModelDateValue(final Builder builder) {
        this.value = builder.value;
        this.possibleFirstDay = builder.possibleFirstDay;
    }

    public static Builder builder() {
        return new Builder();
    }

    public LocalDate getValue() {
        return value;
    }

    public void setValue(final LocalDate value) {
        this.value = value;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDate getPossibleFirstDay() {
        return possibleFirstDay;
    }

    public void setPossibleFirstDay(final LocalDate possibleFirstDay) {
        this.possibleFirstDay = possibleFirstDay;
    }

    public static final class Builder {
        private LocalDate value;
        private LocalDate possibleFirstDay;

        public Builder withValue(final LocalDate value) {
            this.value = value;
            return this;
        }

        public Builder withAfter(final LocalDate possibleFirstDay) {
            this.possibleFirstDay = possibleFirstDay;
            return this;
        }

        public ModelDateValue build() {
            return new ModelDateValue(this);
        }
    }

}
