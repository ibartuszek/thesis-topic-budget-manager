package hu.elte.bm.transactionservice.web.common;

import java.text.MessageFormat;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ModelValidator {

    private static final String VALIDATOR_FIELD_CANNOT_BE_NUL_MESSAGE = "Validated field cannot be null!";
    private static final String VALIDATOR_FIELD_VALUE_CANNOT_BE_NUL_MESSAGE = "Validated field value cannot be null!";
    private static final String VALIDATOR_FIELD_NAME_CANNOT_BE_NUL_MESSAGE = "Validated field name cannot be null!";
    private static final String VALIDATOR_FIELD_VALUE_CANNOT_BE_EMPTY_MESSAGE = "{0} cannot be empty!";
    private static final String VALIDATOR_FIELD_VALUE_LONGER_THAN_MAXIMUM = "{0} cannot be longer than {1}!";
    private static final String VALIDATOR_FIELD_VALUE_NOT_MATCH = "{0} must be match with {1}!";
    private static final String VALIDATOR_FIELD_VALUE_NOT_ENUM = "{0} must be one of them: {1}!";

    public boolean validateModelStringValue(final ModelStringValue modelStringValue, final String name) {
        Assert.notNull(modelStringValue, VALIDATOR_FIELD_CANNOT_BE_NUL_MESSAGE);
        Assert.notNull(modelStringValue.getValue(), VALIDATOR_FIELD_VALUE_CANNOT_BE_NUL_MESSAGE);
        Assert.notNull(name, VALIDATOR_FIELD_NAME_CANNOT_BE_NUL_MESSAGE);
        boolean result = true;
        if ("".equals(modelStringValue.getValue())) {
            modelStringValue.setErrorMessage(MessageFormat.format(VALIDATOR_FIELD_VALUE_CANNOT_BE_EMPTY_MESSAGE, name));
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

}
