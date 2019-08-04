package hu.elte.bm.transactionservice.web.common;

import java.text.MessageFormat;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ModelValidator {

    private static final Double ZERO = 0.0d;
    private static final String VALIDATOR_FIELD_CANNOT_BE_NUL = "Validated field cannot be null!";
    private static final String VALIDATOR_FIELD_VALUE_CANNOT_BE_NUL = "Validated field value cannot be null!";
    private static final String VALIDATOR_FIELD_NAME_CANNOT_BE_NUL = "Validated field name cannot be null!";
    private static final String VALIDATOR_FIELD_VALUE_CANNOT_BE_EMPTY = "{0} cannot be empty!";
    private static final String VALIDATOR_FIELD_VALUE_LONGER_THAN_MAXIMUM = "{0} cannot be longer than {1}!";
    private static final String VALIDATOR_FIELD_VALUE_NOT_MATCH = "{0} must be match with {1}!";
    private static final String VALIDATOR_FIELD_VALUE_NOT_ENUM = "{0} must be one of them: {1}!";
    private static final String VALIDATOR_FIELD_VALUE_POSITIVE = "{0} must be positive number!";
    private static final String VALIDATOR_FIELD_VALUE_POSITIVE_OR_ZERO = "{0} must be positive number or zero!";
    private static final String VALIDATOR_FIELD_VALUE_MUST_BE_AFTER = "{0} must be after {1}!";

    public boolean validate(final ModelStringValue modelStringValue, final String name) {
        commonValidation(modelStringValue, name);
        Assert.notNull(modelStringValue.getValue(), VALIDATOR_FIELD_VALUE_CANNOT_BE_NUL);
        boolean result = true;
        if ("".equals(modelStringValue.getValue())) {
            modelStringValue.setErrorMessage(MessageFormat.format(VALIDATOR_FIELD_VALUE_CANNOT_BE_EMPTY, name));
            result = false;
        } else if (modelStringValue.getMaximumLength() != null
            && modelStringValue.getValue().length() > modelStringValue.getMaximumLength()) {
            modelStringValue.setErrorMessage(MessageFormat.format(VALIDATOR_FIELD_VALUE_LONGER_THAN_MAXIMUM, name, modelStringValue.getMaximumLength()));
            result = false;
        } else if (modelStringValue.getRegexp() != null
            && !modelStringValue.getValue().matches(modelStringValue.getRegexp())) {
            modelStringValue.setErrorMessage(MessageFormat.format(VALIDATOR_FIELD_VALUE_NOT_MATCH, name, modelStringValue.getRegexp()));
            result = false;
        } else if (modelStringValue.getPossibleEnumValues() != null
            && !modelStringValue.getPossibleEnumValues().contains(modelStringValue.getValue())) {
            modelStringValue.setErrorMessage(MessageFormat.format(VALIDATOR_FIELD_VALUE_NOT_ENUM, name, modelStringValue.getPossibleEnumValues()));
            result = false;
        }
        return result;
    }

    public boolean validate(final ModelAmountValue modelAmountValue, final String name) {
        commonValidation(modelAmountValue, name);
        Assert.notNull(modelAmountValue.getValue(), VALIDATOR_FIELD_VALUE_CANNOT_BE_NUL);
        boolean result = true;
        if (modelAmountValue.getPositive() != null
            && modelAmountValue.getValue().compareTo(ZERO) < 1) {
            modelAmountValue.setErrorMessage(MessageFormat.format(VALIDATOR_FIELD_VALUE_POSITIVE, name));
            result = false;
        } else if (modelAmountValue.getPositiveOrZero() != null
            && modelAmountValue.getValue().compareTo(ZERO) < 0) {
            modelAmountValue.setErrorMessage(MessageFormat.format(VALIDATOR_FIELD_VALUE_POSITIVE_OR_ZERO, name));
            result = false;
        }
        return result;
    }

    public boolean validate(final ModelDateValue modelDateValue, final String name) {
        commonValidation(modelDateValue, name);
        Assert.notNull(modelDateValue.getValue(), VALIDATOR_FIELD_VALUE_CANNOT_BE_NUL);
        boolean result = true;
        if (modelDateValue.getPossibleFirstDay() != null && modelDateValue.getValue().isBefore(modelDateValue.getPossibleFirstDay())) {
            modelDateValue.setErrorMessage(MessageFormat.format(VALIDATOR_FIELD_VALUE_MUST_BE_AFTER, name, modelDateValue.getPossibleFirstDay()));
            result = false;
        }
        return result;
    }

    private void commonValidation(final Object modelValue, final String name) {
        Assert.notNull(modelValue, VALIDATOR_FIELD_CANNOT_BE_NUL);
        Assert.notNull(name, VALIDATOR_FIELD_NAME_CANNOT_BE_NUL);
    }

}
