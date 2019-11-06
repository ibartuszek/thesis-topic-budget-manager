package hu.elte.bm.calculationservice.app.test.utils;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import hu.elte.bm.transactionservice.TransactionType;

@Component
public class WireMockService {

    private static final String HOST = "localhost";
    private static final int WIRE_MOCK_SERVER_PORT = 9999;
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String FIND_ALL_MAIN_CATEGORY_URL = "/bm/mainCategories/findAll?type={0}&userId={1}";
    private static final String FIND_ALL_SUB_CATEGORY_URL = "/bm/subCategories/findAll?type={0}&userId={1}";

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

}
