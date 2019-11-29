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
    private static final String DATE = "date";
    private static final String END_DATE = "endDate";

    String getResponseAsStringWithDates(final String responseFileName) throws IOException {
        Resource resource = new ClassPathResource(RESPONSE_FILE_FOLDER + responseFileName);
        JsonReader reader = new JsonReader(new FileReader(resource.getFile()));
        JsonObject responseObject = new Gson().fromJson(reader, JsonObject.class);
        JsonArray transactionList = responseObject.get("transactionList").getAsJsonArray();
        for (JsonElement jsonElement : transactionList) {
            Optional<LocalDate> date = getNewDate(jsonElement, DATE);
            date.ifPresent(localDate -> ((JsonObject) jsonElement).addProperty(DATE, localDate.toString()));
            Optional<LocalDate> endDate = getNewDate(jsonElement, END_DATE);
            endDate.ifPresent(localDate -> ((JsonObject) jsonElement).addProperty(END_DATE, localDate.toString()));
        }
        return responseObject.toString();
    }

    private Optional<LocalDate> getNewDate(final JsonElement jsonElement, final String fieldName) {
        LocalDate result = null;
        JsonElement element = ((JsonObject) jsonElement).get(fieldName);
        if (!element.isJsonNull() && element.getAsString().contains("today")) {
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
