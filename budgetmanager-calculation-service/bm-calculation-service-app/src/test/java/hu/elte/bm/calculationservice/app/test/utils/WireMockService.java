package hu.elte.bm.calculationservice.app.test.utils;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import hu.elte.bm.transactionservice.TransactionType;

@Component
public class WireMockService {

    private static final String HOST = "localhost";
    private static final int WIRE_MOCK_SERVER_PORT = 9999;
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String FIND_ALL_MAIN_CATEGORY_URL = "/bm/mainCategories/findAll?type={0}&userId={1}";
    private static final String FIND_ALL_SUB_CATEGORY_URL = "/bm/subCategories/findAll?type={0}&userId={1}";
    private static final String FIND_ALL_TRANSACTION_URL = "/bm/transactions/findAll?type={0}&userId={1}&start={2}&end={3}";
    private static final String GET_EXCHANGE_RATES_URL = "/freeforexapi.com/api/live?pairs=USDEUR,USDHUF";
    private static final String RESPONSE_FILE_FOLDER = "__files/";

    private WireMockServer wireMockServer;

    @PostConstruct
    public void init() {
        this.wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
            .port(WIRE_MOCK_SERVER_PORT));
        wireMockServer.start();
    }

    @PreDestroy
    public void destroy() {
        wireMockServer.stop();
    }

    public void resetServer() {
        WireMock.configureFor(HOST, WIRE_MOCK_SERVER_PORT);
        WireMock.reset();
    }

    public void setUpFindAllMainCategoriesResponse(final TransactionType type, final Long userId, final int responseStatus, final String responseFileName) {
        stubFor(get(MessageFormat.format(FIND_ALL_MAIN_CATEGORY_URL, type, userId))
            .willReturn(aResponse()
                .withStatus(responseStatus)
                .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON)
                .withBodyFile(responseFileName)));
    }

    public void setUpFindAllSubCategoriesResponse(final TransactionType type, final Long userId, final int responseStatus, final String responseFileName) {
        stubFor(get(MessageFormat.format(FIND_ALL_SUB_CATEGORY_URL, type, userId))
            .willReturn(aResponse()
                .withStatus(responseStatus)
                .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON)
                .withBodyFile(responseFileName)));
    }

    public void setUpFindAllTransactionsResponse(final TransactionServiceContext context, final int responseStatus, final String responseFileName) throws IOException {
        stubFor(get(MessageFormat.format(FIND_ALL_TRANSACTION_URL, context.getType(), context.getUserId(), context.getStart(), context.getEnd()))
            .willReturn(aResponse()
                .withStatus(responseStatus)
                .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON)
                .withBody(getResponseObject(responseFileName))));
    }

    public void setUpGetExchangeRates(final int responseStatus, final String responseFileName) {
        stubFor(get(GET_EXCHANGE_RATES_URL)
            .willReturn(aResponse()
                .withStatus(responseStatus)
                .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON)
                .withBodyFile(responseFileName)));
    }

    private String getResponseObject(final String responseFileName) throws IOException {
        Resource resource = new ClassPathResource(RESPONSE_FILE_FOLDER + responseFileName);
        JsonReader reader = new JsonReader(new FileReader(resource.getFile()));
        return new Gson().fromJson(reader, JsonObject.class).toString();
    }

}
