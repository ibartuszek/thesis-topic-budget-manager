package hu.elte.bm.transactionservice.web.common;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@PropertySource({ "classpath:common_constraints.properties", "classpath:messages.properties" })
public class ModelValidator {

    private static final Double ZERO = 0.0d;

    @Value("${validator.validated_field_cannot_be_null}")
    private String validatorFieldCannotBeNul;

    @Value("${validator.validated_field_value_cannot_be_null}")
    private String validatorFieldValueCannotBeNul;

    @Value("${validator.validated_field_name_cannot_be_null}")
    private String validatorFieldNameCannotBeNul;

    @Value("${validator.string_cannot_be_empty}")
    private String validatorFieldValueCannotBeEmpty;

    @Value("${validator.string_cannot_be_longer}")
    private String validatorFieldValueLongerThanMaximum;

    @Value("${validator.string_does_not_match}")
    private String validatorFieldValueNotMatch;

    @Value("${validator.string_must_be_enumerated}")
    private String validatorFieldValueNotEnum;

    @Value("${validator.amount_must_be_positive}")
    private String validatorFieldValuePositive;

    @Value("${validator.amount_must_be_positive_or_zero}")
    private String validatorFieldValuePositiveOrZero;

    @Value("${validator.date_cannot_be_before}")
    private String validatorFieldValueCannotBeBefore;

    public boolean validate(final ModelStringValue modelStringValue, final String name) {
        commonValidation(modelStringValue, name);
        Assert.notNull(modelStringValue.getValue(), validatorFieldValueCannotBeNul);
        boolean result = true;
        if ("".equals(modelStringValue.getValue())) {
            modelStringValue.setErrorMessage(MessageFormat.format(validatorFieldValueCannotBeEmpty, name));
            result = false;
        } else if (modelStringValue.getMaximumLength() != null
            && modelStringValue.getValue().length() > modelStringValue.getMaximumLength()) {
            modelStringValue.setErrorMessage(MessageFormat.format(validatorFieldValueLongerThanMaximum, name, modelStringValue.getMaximumLength()));
            result = false;
        } else if (modelStringValue.getRegexp() != null
            && !modelStringValue.getValue().matches(modelStringValue.getRegexp())) {
            modelStringValue.setErrorMessage(MessageFormat.format(validatorFieldValueNotMatch, name, modelStringValue.getRegexp()));
            result = false;
        } else if (modelStringValue.getPossibleEnumValues() != null
            && !modelStringValue.getPossibleEnumValues().contains(modelStringValue.getValue())) {
            modelStringValue.setErrorMessage(MessageFormat.format(validatorFieldValueNotEnum, name, modelStringValue.getPossibleEnumValues()));
            result = false;
        }
        return result;
    }

    public boolean validate(final ModelAmountValue modelAmountValue, final String name) {
        commonValidation(modelAmountValue, name);
        Assert.notNull(modelAmountValue.getValue(), validatorFieldValueCannotBeNul);
        boolean result = true;
        if (modelAmountValue.getPositive() != null
            && modelAmountValue.getValue().compareTo(ZERO) < 1) {
            modelAmountValue.setErrorMessage(MessageFormat.format(validatorFieldValuePositive, name));
            result = false;
        } else if (modelAmountValue.getPositiveOrZero() != null
            && modelAmountValue.getValue().compareTo(ZERO) < 0) {
            modelAmountValue.setErrorMessage(MessageFormat.format(validatorFieldValuePositiveOrZero, name));
            result = false;
        }
        return result;
    }

    public boolean validate(final ModelDateValue modelDateValue, final String name) {
        commonValidation(modelDateValue, name);
        Assert.notNull(modelDateValue.getValue(), validatorFieldValueCannotBeNul);
        boolean result = true;
        if (modelDateValue.getPossibleFirstDay() != null && modelDateValue.getValue().isBefore(modelDateValue.getPossibleFirstDay())) {
            modelDateValue.setErrorMessage(MessageFormat.format(validatorFieldValueCannotBeBefore, name, modelDateValue.getPossibleFirstDay()));
            result = false;
        }

        return result;
    }

    private void commonValidation(final Object modelValue, final String name) {
        Assert.notNull(modelValue, validatorFieldCannotBeNul);
        Assert.notNull(name, validatorFieldNameCannotBeNul);
    }

}
