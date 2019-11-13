package hu.elte.bm.calculationservice.forexclient;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;

@Component
public class ForexProxy {

    private static final String EXCEPTION_MESSAGE = "Forex client sent: {0} during: '{1}' call.";
    private static final String BASE_URL = "http://freeforexapi.com/api/live";
    private static final String PAIRS = "USDEUR,USDHUF";
    private final RestTemplate restTemplate;

    @Autowired
    ForexProxy(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    ForexResponse getForexResponse() {
        String url = createUrl();
        ResponseEntity<String> responseEntity = getResponseEntity(url);
        checkResponseStatus(responseEntity);
        return new Gson().fromJson(responseEntity.getBody(), ForexResponse.class);
    }

    private String createUrl() {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
            .queryParam("pairs", PAIRS)
            .toUriString();
    }

    private ResponseEntity<String> getResponseEntity(final String url) {
        return restTemplate.getForEntity(url, String.class);
    }

    private void checkResponseStatus(final ResponseEntity responseEntity) {
        if (!responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            throw new ForexClientException(MessageFormat.format(EXCEPTION_MESSAGE, responseEntity.getStatusCode(), BASE_URL));
        }
    }

}
