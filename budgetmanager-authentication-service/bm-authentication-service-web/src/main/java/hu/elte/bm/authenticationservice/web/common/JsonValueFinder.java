package hu.elte.bm.authenticationservice.web.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class JsonValueFinder {

    private static final String JSON_FIELD_VALUE_SEPARATOR = ":";
    private static final String JSON_FIELD_SEPARATOR = ",";
    private static final String EMPTY_STRING = "";

    public String getFieldValueFromRequestBody(final InputStream inputStream, final String fieldName) throws IOException {
        Assert.notNull(inputStream, "Inputstream cannot be null!");
        Assert.notNull(fieldName, "FieldName cannot be null!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        String result = null;
        while (line != null && result == null) {
            if (line.contains(fieldName)) {
                String[] parts = line.split(JSON_FIELD_VALUE_SEPARATOR);
                result = parts[0].replaceAll("\t", "").replaceAll("\"", "").trim().equals(fieldName) ? parts[1].replaceAll(JSON_FIELD_SEPARATOR, EMPTY_STRING).trim() : null;
            }
            line = reader.readLine();
        }
        return result;
    }

}
