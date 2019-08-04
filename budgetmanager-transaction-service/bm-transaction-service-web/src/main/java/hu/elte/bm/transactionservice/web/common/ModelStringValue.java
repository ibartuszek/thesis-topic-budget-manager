package hu.elte.bm.transactionservice.web.common;

import java.util.Set;

public final class ModelStringValue {

    private String value;
    private String errorMessage;
    private Integer maximumLength;
    private String regexp;
    private Set<String> possibleEnumValues;

    private ModelStringValue() {
    }

    private ModelStringValue(final Builder builder) {
        this.value = builder.value;
        this.maximumLength = builder.maximumLength;
        this.regexp = builder.regexp;
        this.possibleEnumValues = builder.possibleEnumValues;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(final Integer maximumLength) {
        this.maximumLength = maximumLength;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(final String regexp) {
        this.regexp = regexp;
    }

    public Set<String> getPossibleEnumValues() {
        return possibleEnumValues;
    }

    public void setPossibleEnumValues(final Set<String> possibleEnumValues) {
        this.possibleEnumValues = possibleEnumValues;
    }

    public static final class Builder {
        private String value;
        private Integer maximumLength;
        private String regexp;
        private Set<String> possibleEnumValues;

        private Builder() {
        }

        public Builder withValue(final String value) {
            this.value = value;
            return this;
        }

        public Builder withMaximumLength(final Integer maximumLength) {
            this.maximumLength = maximumLength;
            return this;
        }

        public Builder withRegexp(final String regexp) {
            this.regexp = regexp;
            return this;
        }

        public Builder withPossibleEnumValues(final Set<String> possibleEnumValues) {
            this.possibleEnumValues = possibleEnumValues;
            return this;
        }

        public ModelStringValue build() {
            return new ModelStringValue(this);
        }

    }
}
