package hu.elte.bm.calculationservice.app.test;

import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import hu.elte.bm.calculationservice.app.CalculationServiceApplication;
import hu.elte.bm.calculationservice.dal.schema.StatisticsSchemaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CalculationServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource({"classpath:messages.properties", "classpath:database-h2.properties"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public abstract class AbstractCalculationServiceApplicationTest {

    protected static final Long USER_ID = 1L;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WireMockService wireMockService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private StatisticsSchemaRepository repository;

    public MockMvc getMvc() {
        return mvc;
    }

    public WireMockService getWireMockService() {
        return wireMockService;
    }

    public StatisticsSchemaRepository getRepository() {
        return repository;
    }

    @Before
    public void beforeMethodOfAbstractWireMockTestClass() {
        wireMockService.resetServer();
    }

    protected String getExpectedResponseJsonFromFile(final String fileName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:expected_responses/" + fileName);
        JsonReader reader = new JsonReader(new FileReader(resource.getFile()));
        return new Gson().toJsonTree(new Gson().fromJson(reader, Object.class)).toString();
    }

    protected String getActualResponseFromResult(final MockHttpServletResponse result) throws UnsupportedEncodingException {
        return new Gson().toJsonTree(new Gson().fromJson(result.getContentAsString(), Object.class)).toString();
    }

}
