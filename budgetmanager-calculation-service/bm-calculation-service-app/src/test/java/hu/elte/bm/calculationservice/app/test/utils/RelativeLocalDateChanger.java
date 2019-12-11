package hu.elte.bm.calculationservice.app.test.utils;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

@Component
public class RelativeLocalDateChanger {

    private static final String RESPONSE_FILE_FOLDER = "__files/";
    private static final String STATISTICS_RESPONSE_FILE_FOLDER = "expected_responses/";
    private static final String DATE = "date";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";

    String getResponseAsStringWithDates(final String responseFileName) throws IOException {
        JsonObject responseObject = getResponseObject(RESPONSE_FILE_FOLDER + responseFileName);
        JsonArray transactionList = responseObject.get("transactionList").getAsJsonArray();
        for (JsonElement jsonElement : transactionList) {
            getNewDate(jsonElement, DATE)
                    .ifPresent(localDate -> ((JsonObject) jsonElement).addProperty(DATE, localDate.toString()));
            getNewDate(jsonElement, END_DATE)
                    .ifPresent(localDate -> ((JsonObject) jsonElement).addProperty(END_DATE, localDate.toString()));
        }
        return responseObject.toString();
    }

    public String getExpectedResponseAsStringWithDates(final String responseFileName) throws IOException {
        JsonObject responseObject = getResponseObject(STATISTICS_RESPONSE_FILE_FOLDER + responseFileName);
        JsonObject statistics = (JsonObject) responseObject.get("statistics");
        getNewDate(statistics, START_DATE)
                .ifPresent(localDate -> statistics.addProperty(START_DATE, localDate.toString()));
        getNewDate(statistics, END_DATE)
                .ifPresent(localDate -> statistics.addProperty(END_DATE, localDate.toString()));
        JsonArray dataPoints = statistics.getAsJsonObject("chartData").get("dataPoints").getAsJsonArray();
        for (JsonElement jsonElement : dataPoints) {
            getNewDate(jsonElement, DATE)
                    .ifPresent(localDate -> ((JsonObject) jsonElement).addProperty(DATE, localDate.toString()));
        }
        return responseObject.toString();
    }

    private JsonObject getResponseObject(final String responseFileName) throws IOException {
        Resource resource = new ClassPathResource(responseFileName);
        JsonReader reader = new JsonReader(new FileReader(resource.getFile()));
        return new Gson().fromJson(reader, JsonObject.class);
    }

    private Optional<LocalDate> getNewDate(final JsonElement jsonElement, final String fieldName) {
        return getNewDate((JsonObject) jsonElement, fieldName);
    }

    private Optional<LocalDate> getNewDate(final JsonObject jsonObject, final String fieldName) {
        LocalDate result = null;
        JsonElement element = jsonObject.get(fieldName);
        if (element != null && !element.isJsonNull() && element.getAsString().contains("today")) {
            String[] modifierArray = getDateModifier(element.getAsString());
            result = calculateNewDate(modifierArray[0], Integer.parseInt(modifierArray[1]), modifierArray[2]);
        }
        return Optional.ofNullable(result);
    }

    private String[] getDateModifier(final String elementAsString) {
        String base = elementAsString.replace("${today", "")
                .replace("}", "");
        int lastCharacterPosition = base.length();
        return new String[]{base.substring(0, 1), base.substring(1, lastCharacterPosition - 1), base.substring(lastCharacterPosition - 1, lastCharacterPosition)};
    }

    private LocalDate calculateNewDate(final String signOfDateElement, final int numberOfDateElement, final String dateElementType) {
        LocalDate result = null;
        if ("d".equals(dateElementType)) {
            result = "+".equals(signOfDateElement) ? LocalDate.now().plusDays(numberOfDateElement) : LocalDate.now().minusDays(numberOfDateElement);
        } else if ("m".equals(dateElementType)) {
            result = "+".equals(signOfDateElement) ? LocalDate.now().plusMonths(numberOfDateElement) : LocalDate.now().minusMonths(numberOfDateElement);
        } else if ("y".equals(dateElementType)) {
            result = "+".equals(signOfDateElement) ? LocalDate.now().plusYears(numberOfDateElement) : LocalDate.now().minusYears(numberOfDateElement);
        }
        return result;
    }

}
