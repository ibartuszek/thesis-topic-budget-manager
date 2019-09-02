package hu.elte.bm.authenticationservice.web.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonValueFinderTest {

    private static final String FIELD_NAME = "targetField";
    private static final String FIELD_VALUE = "targetValue";

    private JsonValueFinder underTest = new JsonValueFinder();

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWhenInputReaderIsNull() throws IOException {
        // GIVEN
        // WHEN
        underTest.getFieldValueFromRequestBody(null, FIELD_NAME);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWhenFieldNameIsNull() throws IOException {
        // GIVEN
        InputStream input = new ByteArrayInputStream("test".getBytes());
        // WHEN
        underTest.getFieldValueFromRequestBody(input, null);
        // THEN
    }

    @Test
    public void testWhenInputReaderHasEmptyString() throws IOException {
        // GIVEN
        String test = "";
        InputStream input = new ByteArrayInputStream(test.getBytes());
        // WHEN
        String result = underTest.getFieldValueFromRequestBody(input, FIELD_NAME);
        // THEN
        Assert.assertNull(result);
    }

    @Test
    public void testWhenFieldNameNotMatches() throws IOException {
        // GIVEN
        String test = "{\n\ttest: {\n\t\ttest: value\n\t}\n}";
        InputStream input = new ByteArrayInputStream(test.getBytes());
        // WHEN
        String result = underTest.getFieldValueFromRequestBody(input, FIELD_NAME);
        // THEN
        Assert.assertNull(result);
    }

    @Test(expectedExceptions = ArrayIndexOutOfBoundsException.class)
    public void testWhenFieldNameMatchesButLineNotContainsSeparator() throws IOException {
        // GIVEN
        String test = "{\n"
                + "\ttest: {\n"
                + "\t\ttest: value,\n"
                + "\t\t" + FIELD_NAME + "\n"
                + "\t}\n"
                + "}";
        InputStream input = new ByteArrayInputStream(test.getBytes());
        // WHEN
        underTest.getFieldValueFromRequestBody(input, FIELD_NAME);
        // THEN
    }

    @Test
    public void testWhenFieldNameMatchesButNotTheSame() throws IOException {
        // GIVEN
        String test = "{\n"
                + "\ttest: {\n"
                + "\t\ttest: value,\n"
                + "\t\tinvalid" + FIELD_NAME + ": " + FIELD_VALUE + "\n"
                + "\t}\n"
                + "}";
        InputStream input = new ByteArrayInputStream(test.getBytes());
        // WHEN
        String result = underTest.getFieldValueFromRequestBody(input, FIELD_NAME);
        // THEN
        Assert.assertNull(result);
    }

    @Test
    public void testWhenFieldNameEqualsTarget() throws IOException {
        // GIVEN
        String test = "{\n"
                + "\ttest: {\n"
                + "\t\ttest: value,\n"
                + "\t\t \"" + FIELD_NAME + "\": " + FIELD_VALUE + "\n"
                + "\t}\n"
                + "}";
        InputStream input = new ByteArrayInputStream(test.getBytes());
        // WHEN
        String result = underTest.getFieldValueFromRequestBody(input, FIELD_NAME);
        // THEN
        Assert.assertEquals(result, FIELD_VALUE);
    }

}
