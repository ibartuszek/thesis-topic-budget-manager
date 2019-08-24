package hu.elte.bm.transactionservice.app;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;

@SpringBootTest
@TestPropertySource({
    "classpath:common_constraints.properties",
    "classpath:messages.properties",
    "classpath:database-h2.properties" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractTransactionServiceApplicationTest extends AbstractTestNGSpringContextTests {

    protected static final Long USER_ID = 1L;

    protected MainCategoryModel.Builder createMainCategoryModelBuilder(final Long id, final String categoryName, final TransactionType type,
        final Set<SubCategoryModel> subCategoryModelSet) {
        return MainCategoryModel.builder()
            .withId(id)
            .withName(ModelStringValue.builder().withValue(categoryName).build())
            .withTransactionType(ModelStringValue.builder().withValue(type.name()).build())
            .withSubCategoryModelSet(subCategoryModelSet);
    }

    protected MainCategoryModel createMainCategoryModel(final Long id, final String name, final TransactionType type) {
        return createMainCategoryModelBuilder(id, name, type, new HashSet<>()).build();
    }

    protected SubCategoryModel.Builder createSubCategoryModelBuilder(final Long id, final String name, final TransactionType type) {
        return SubCategoryModel.builder()
            .withId(id)
            .withName(ModelStringValue.builder().withValue(name).build())
            .withTransactionType(ModelStringValue.builder().withValue(type.name()).build());
    }

    protected SubCategoryModel createSubCategoryModel(final Long id, final String name, final TransactionType type) {
        return createSubCategoryModelBuilder(id, name, type).build();
    }

}
