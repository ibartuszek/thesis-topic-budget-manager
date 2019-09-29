package hu.elte.bm.transactionservice.app;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.GsonBuilder;

import hu.elte.bm.transactionservice.app.test.utils.LocalDateAdapter;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@SpringBootTest
@TestPropertySource({
    "classpath:common_constraints.properties",
    "classpath:messages.properties",
    "classpath:database-h2.properties" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public abstract class AbstractTransactionServiceApplicationTest extends AbstractTestNGSpringContextTests {

    protected static final Long USER_ID = 1L;

    @Autowired
    private MockMvc mvc;

    public MockMvc getMvc() {
        return mvc;
    }

    protected MainCategory.Builder createMainCategoryBuilder(final Long id, final String categoryName, final TransactionType type,
        final Set<SubCategory> subCategorySet) {
        return MainCategory.builder()
            .withId(id)
            .withName(categoryName)
            .withTransactionType(type)
            .withSubCategorySet(subCategorySet);
    }

    protected MainCategory createMainCategory(final Long id, final String name, final TransactionType type) {
        return createMainCategoryBuilder(id, name, type, new HashSet<>()).build();
    }

    protected SubCategory.Builder createSubCategoryBuilder(final Long id, final String name, final TransactionType type) {
        return SubCategory.builder()
            .withId(id)
            .withName(name)
            .withTransactionType(type);
    }

    protected SubCategory createSubCategory(final Long id, final String name, final TransactionType type) {
        return createSubCategoryBuilder(id, name, type).build();
    }

    protected String createRequestBody(final Object object) {
        return new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create()
            .toJson(object);
    }

}
