package hu.elte.bm.calculationservice.app.test;

import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Set;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import hu.elte.bm.calculationservice.app.CalculationServiceApplication;
import hu.elte.bm.calculationservice.app.test.utils.LocalDateAdapter;
import hu.elte.bm.calculationservice.app.test.utils.WireMockService;
import hu.elte.bm.calculationservice.dal.schema.StatisticsSchemaEntity;
import hu.elte.bm.calculationservice.dal.schema.StatisticsSchemaRepository;
import hu.elte.bm.calculationservice.schema.ChartType;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.transactionservice.Currency;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CalculationServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource({ "classpath:messages.properties", "classpath:database-h2.properties" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public abstract class AbstractCalculationServiceApplicationTest {

    protected static final Long USER_ID = 1L;
    protected static final TransactionType TRANSACTION_TYPE = TransactionType.OUTCOME;
    protected static final String FIND_ALL_OUTCOME_MAIN_CATEGORIES = "findAllOutcomeMainCategoryWithReponseOk.json";
    protected static final String FIND_ALL_SUB_CATEGORIES_RESULT_BODY = "findAllOutcomeSubCategoryWithReponseOk.json";
    protected static final String FIND_ALL_MAIN_CATEGORIES_WITH_EMPTY_BODY = "findAllOutcomeMainCategoryWithEmptyList.json";
    protected static final String FIND_ALL_INCOME_MAIN_CATEGORIES = "findAllIncomeMainCategoryWithResponseOk.json";
    protected static final LocalDate START = LocalDate.now().minusDays(30);
    protected static final LocalDate END = LocalDate.now();
    protected static final Long NEW_SCHEMA_ID = 7L;
    protected static final long INVALID_MAIN_CATEGORY_ID = 13L;

    private static final Long DEFAULT_SCHEMA_ID = 2L;
    private static final String DEFAULT_SCHEMA_TITLE = "Schema";
    private static final StatisticsType DEFAULT_SCHEMA_TYPE = StatisticsType.SUM;
    private static final Currency DEFAULT_CURRENCY = Currency.EUR;
    private static final ChartType DEFAULT_CHART_TYPE = ChartType.BAR;
    private static final Long DEFAULT_MAIN_CATEGORY_ID = 10L;
    private static final String DEFAULT_MAIN_CATEGORY_NAME = "Entertainment";
    private static final Long DEFAULT_SUB_CATEGORY_ID = 12L;
    private static final Long OTHER_SUB_CATEGORY_ID = 13L;
    private static final Long ANOTHER_SUB_CATEGORY_ID = 14L;
    private static final String DEFAULT_SUB_CATEGORY_NAME = "Cinema";
    private static final String OTHER_SUB_CATEGORY_NAME = "Computer";
    private static final String ANOTHER_SUB_CATEGORY_NAME = "Hobby";
    private static final String INVALID_SCHEMA = "Invalid schema";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WireMockService wireMockService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private StatisticsSchemaRepository repository;

    protected static StatisticsSchema.Builder createSchemaBuilderWithDefaultValues() {
        return StatisticsSchema.builder()
            .withId(DEFAULT_SCHEMA_ID)
            .withTitle(DEFAULT_SCHEMA_TITLE)
            .withType(DEFAULT_SCHEMA_TYPE)
            .withCurrency(DEFAULT_CURRENCY)
            .withChartType(DEFAULT_CHART_TYPE);
    }

    protected static MainCategory.Builder createDefaultMainCategoryBuilder() {
        return MainCategory.builder()
            .withId(DEFAULT_MAIN_CATEGORY_ID)
            .withName(DEFAULT_MAIN_CATEGORY_NAME)
            .withTransactionType(TRANSACTION_TYPE)
            .withSubCategorySet(createDefaultSubCategorySet());
    }

    protected static Set<SubCategory> createDefaultSubCategorySet() {
        SubCategory other = createSubCategory(OTHER_SUB_CATEGORY_ID, OTHER_SUB_CATEGORY_NAME);
        SubCategory another = createSubCategory(ANOTHER_SUB_CATEGORY_ID, ANOTHER_SUB_CATEGORY_NAME);
        return Set.of(createDefaultSubCategoryBuilder().build(), other, another);
    }

    protected static SubCategory createSubCategory(final Long id, final String name) {
        return SubCategory.builder()
            .withId(id)
            .withName(name)
            .withTransactionType(TRANSACTION_TYPE)
            .build();
    }

    protected static SubCategory.Builder createDefaultSubCategoryBuilder() {
        return SubCategory.builder()
            .withId(DEFAULT_SUB_CATEGORY_ID)
            .withName(DEFAULT_SUB_CATEGORY_NAME)
            .withTransactionType(TRANSACTION_TYPE);
    }

    public MockMvc getMvc() {
        return mvc;
    }

    public WireMockService getWireMockService() {
        return wireMockService;
    }

    public StatisticsSchemaRepository getRepository() {
        return repository;
    }

    @BeforeEach
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

    protected String createRequestBody(final Object object) {
        return new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create()
            .toJson(object);
    }

    protected void insertSchemaIntoDb(final StatisticsType type, final Long mainCategoryId) {
        StatisticsSchemaEntity schema = StatisticsSchemaEntity.builder()
            .withTitle(INVALID_SCHEMA)
            .withType(type)
            .withChartType(DEFAULT_CHART_TYPE)
            .withCurrency(DEFAULT_CURRENCY)
            .withMainCategoryId(mainCategoryId)
            .withUserId(USER_ID)
            .build();
        getRepository().save(schema);
    }

    protected void assertExpectedJsonFileWithDates(final String expectedBodyFileName, final MockHttpServletResponse result)
            throws IOException, JSONException {
        JsonObject jsonObject = new Gson().toJsonTree(new Gson().fromJson(result.getContentAsString(), Object.class)).getAsJsonObject();
        JSONAssert.assertEquals(getExpectedResponseJsonFromFile(expectedBodyFileName),
                jsonObject.toString(), new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("statistics.startDate", (o1, o2) -> true),
                        new Customization("statistics.endDate", (o1, o2) -> true)));
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        LocalDate startDate = gson.fromJson(jsonObject.getAsJsonObject("statistics").get("startDate"), LocalDate.class);
        LocalDate endDate = gson.fromJson(jsonObject.getAsJsonObject("statistics").get("endDate"), LocalDate.class);
        Assertions.assertEquals(START, startDate);
        Assertions.assertEquals(END, endDate);
    }

}
