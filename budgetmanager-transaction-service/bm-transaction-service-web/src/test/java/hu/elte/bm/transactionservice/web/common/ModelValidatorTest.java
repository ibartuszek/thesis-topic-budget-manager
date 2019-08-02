package hu.elte.bm.transactionservice.web.common;

import java.text.MessageFormat;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class ModelValidatorTest {

    private static final String VALIDATOR_FIELD_CANNOT_BE_NUL_MESSAGE = "Validated field cannot be null!";
    private static final String VALIDATOR_FIELD_VALUE_CANNOT_BE_NUL_MESSAGE = "Validated field value cannot be null!";
    private static final String VALIDATOR_FIELD_NAME_CANNOT_BE_NUL_MESSAGE = "Validated field name cannot be null!";
    private static final String FIELD_NAME = "Name";
    private static final String VALIDATOR_FIELD_VALUE_CANNOT_BE_EMPTY_MESSAGE = "Name cannot be empty!";
    private static final String EMPTY_VALUE = "";
    private static final String MAXIMUM_LENGTH_VALUE = "aaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbb";
    private static final String LONGER_VALUE = "aaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbb1";
    private static final Integer MAXIMUM_LENGTH = 50;
    private static final String VALIDATOR_FIELD_VALUE_LONGER_THAN_MAXIMUM = MessageFormat.format("Name cannot be longer than {0}!", MAXIMUM_LENGTH);
    private static final String REGEX_MATCHING_VALUE = "example@email.com";
    private static final String REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private static final String VALIDATOR_FIELD_VALUE_NOT_MATCH = MessageFormat.format("Name must be match with {0}!", REGEX);
    private static final Set<String> POSSIBLE_VALUES = TransactionType.getPossibleValues();
    private static final String VALIDATOR_FIELD_VALUE_NOT_ENUM = MessageFormat.format("Name must be one of them: {0}!", POSSIBLE_VALUES);
    private static final String ENUMERATED_VALUE = "INCOME";

    private final ModelValidator underTest = new ModelValidator();

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateWhenModelStringValueIsNull() {
        // GIVEN
        // WHEN
        try {
            underTest.validateModelStringValue(null, FIELD_NAME);
        } catch (Exception e) {
            // THEN
            Assert.assertEquals(VALIDATOR_FIELD_CANNOT_BE_NUL_MESSAGE, e.getMessage());
            throw e;
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateWhenValueIsNull() {
        // GIVEN
        ModelStringValue value = ModelStringValue.builder().build();
        // WHEN
        try {
            underTest.validateModelStringValue(value, FIELD_NAME);
        } catch (Exception e) {
            // THEN
            Assert.assertEquals(VALIDATOR_FIELD_VALUE_CANNOT_BE_NUL_MESSAGE, e.getMessage());
            throw e;
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateWhenNameIsNull() {
        // GIVEN
        ModelStringValue value = ModelStringValue.builder()
            .withValue(REGEX_MATCHING_VALUE)
            .build();
        // WHEN
        try {
            underTest.validateModelStringValue(value, null);
        } catch (Exception e) {
            // THEN
            Assert.assertEquals(VALIDATOR_FIELD_NAME_CANNOT_BE_NUL_MESSAGE, e.getMessage());
            throw e;
        }
    }

    @Test
    public void testValidateWhenValueIsEmptyString() {
        // GIVEN
        ModelStringValue value = ModelStringValue.builder()
            .withValue(EMPTY_VALUE)
            .build();
        // WHEN
        boolean result = underTest.validateModelStringValue(value, FIELD_NAME);
        // THEN
        Assert.assertFalse(result);
        Assert.assertEquals(VALIDATOR_FIELD_VALUE_CANNOT_BE_EMPTY_MESSAGE, value.getErrorMessage());
    }

    @Test
    public void testValidateWhenValueHasMaximumLength() {
        // GIVEN
        ModelStringValue value = ModelStringValue.builder()
            .withValue(MAXIMUM_LENGTH_VALUE)
            .withMaximumLength(MAXIMUM_LENGTH)
            .build();
        // WHEN
        boolean result = underTest.validateModelStringValue(value, FIELD_NAME);
        // THEN
        Assert.assertTrue(result);
        Assert.assertNull(value.getErrorMessage());
    }

    @Test
    public void testValidateWhenValueLongerThanMaximumLength() {
        // GIVEN
        ModelStringValue value = ModelStringValue.builder()
            .withValue(LONGER_VALUE)
            .withMaximumLength(MAXIMUM_LENGTH)
            .build();
        // WHEN
        boolean result = underTest.validateModelStringValue(value, FIELD_NAME);
        // THEN
        Assert.assertFalse(result);
        Assert.assertEquals(VALIDATOR_FIELD_VALUE_LONGER_THAN_MAXIMUM, value.getErrorMessage());
    }

    @Test
    public void testValidateWhenValueNotMatchWithRegex() {
        // GIVEN
        ModelStringValue value = ModelStringValue.builder()
            .withValue(MAXIMUM_LENGTH_VALUE)
            .withRegexp(REGEX)
            .build();
        // WHEN
        boolean result = underTest.validateModelStringValue(value, FIELD_NAME);
        // THEN
        Assert.assertFalse(result);
        Assert.assertEquals(VALIDATOR_FIELD_VALUE_NOT_MATCH, value.getErrorMessage());
    }

    @Test
    public void testValidateWhenValueMatchesWithRegex() {
        // GIVEN
        ModelStringValue value = ModelStringValue.builder()
            .withValue(REGEX_MATCHING_VALUE)
            .withRegexp(REGEX)
            .build();
        // WHEN
        boolean result = underTest.validateModelStringValue(value, FIELD_NAME);
        // THEN
        Assert.assertTrue(result);
        Assert.assertNull(value.getErrorMessage());
    }

    @Test
    void testValidateWhenValueIsNotEnumerated() {
        // GIVEN
        ModelStringValue value = ModelStringValue.builder()
            .withValue(FIELD_NAME)
            .withPossibleEnumValues(POSSIBLE_VALUES)
            .build();
        // WHEN
        boolean result = underTest.validateModelStringValue(value, FIELD_NAME);
        // THEN
        Assert.assertFalse(result);
        Assert.assertEquals(VALIDATOR_FIELD_VALUE_NOT_ENUM, value.getErrorMessage());
    }

    @Test
    void testValidateWhenValueIsEnumerated() {
        // GIVEN
        ModelStringValue value = ModelStringValue.builder()
            .withValue(ENUMERATED_VALUE)
            .withPossibleEnumValues(POSSIBLE_VALUES)
            .build();
        // WHEN
        boolean result = underTest.validateModelStringValue(value, FIELD_NAME);
        // THEN
        Assert.assertTrue(result);
        Assert.assertNull(value.getErrorMessage());
    }
}
