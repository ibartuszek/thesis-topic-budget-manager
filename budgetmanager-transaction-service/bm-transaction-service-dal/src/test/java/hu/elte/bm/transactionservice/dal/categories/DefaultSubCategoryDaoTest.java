package hu.elte.bm.transactionservice.dal.categories;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

public class DefaultSubCategoryDaoTest {

    private static final long EXISTING_ID = 1L;
    private static final long NEW_ID = 2L;
    private static final String EXISTING_CATEGORY_NAME = "category name 1";
    private static final String NEW_CATEGORY_NAME = "category name";
    private static final Long USER_ID = 1L;
    private final SubCategoryEntityTransformer subCategoryEntityTransformer = new SubCategoryEntityTransformer();

    private DefaultSubCategoryDao underTest;

    private IMocksControl control;
    private SubCategoryRepository subCategoryRepository;

    @BeforeClass
    public void setup() {
        control = EasyMock.createStrictControl();
        subCategoryRepository = control.createMock(SubCategoryRepository.class);
        underTest = new DefaultSubCategoryDao(subCategoryRepository, subCategoryEntityTransformer);
    }

    @BeforeMethod
    public void beforeMethod() {
        control.reset();
    }

    @AfterMethod
    public void afterMethod() {
        control.verify();
    }

    @Test
    public void testFindAllWhenThereIsNoCategoryInTheRepository() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        EasyMock.expect(subCategoryRepository.findAllSubcategory(INCOME, USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.findAll(context);
        // THEN
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void testFindAllWhenThereAreMoreCategoriesInTheRepository() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        Iterable<SubCategoryEntity> subCategoryEntities = createSubCategoryEntities();
        EasyMock.expect(subCategoryRepository.findAllSubcategory(INCOME, USER_ID)).andReturn(subCategoryEntities);
        List<SubCategory> expectedList = createSubCategoryList();
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.findAll(context);
        // THEN
        Assert.assertEquals(result, expectedList);
    }

    @Test
    public void testFindByIdWhenCategoryCannotBeFound() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        EasyMock.expect(subCategoryRepository.findByIdAndUserId(NEW_ID, USER_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.findById(NEW_ID, context);
        // THEN
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testFindByIdWhenCategoryCanBeFound() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        SubCategoryEntity subCategoryFromRepository = createSubCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME);
        Optional<SubCategory> expectedSubCategory = Optional.of(createSubCategory(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME));
        EasyMock.expect(subCategoryRepository.findByIdAndUserId(EXISTING_ID, USER_ID)).andReturn(Optional.of(subCategoryFromRepository));
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.findById(EXISTING_ID, context);
        // THEN
        Assert.assertEquals(result, expectedSubCategory);
    }

    @Test
    public void testFindByNameWhenCategoryCannotBeFound() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        EasyMock.expect(subCategoryRepository.findByName(NEW_CATEGORY_NAME, INCOME, USER_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.findByName(NEW_CATEGORY_NAME, context);
        // THEN
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testFindByNameWhenCategoryCanBeFound() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        SubCategoryEntity subCategoryFromRepository = createSubCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME);
        Optional<SubCategory> expectedSubCategory = Optional.of(createSubCategory(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME));
        EasyMock.expect(subCategoryRepository.findByName(EXISTING_CATEGORY_NAME, INCOME, USER_ID)).andReturn(Optional.of(subCategoryFromRepository));
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.findByName(EXISTING_CATEGORY_NAME, context);
        // THEN
        Assert.assertEquals(result, expectedSubCategory);
    }

    @Test
    public void testSaveSubCategory() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        SubCategory subCategoryToSave = createSubCategory(null, NEW_CATEGORY_NAME, INCOME);
        SubCategoryEntity subCategoryFromRepository = createSubCategoryEntity(NEW_ID, NEW_CATEGORY_NAME, INCOME);
        SubCategory expectedSubCategory = createSubCategory(NEW_ID, NEW_CATEGORY_NAME, INCOME);
        Capture<SubCategoryEntity> capture = Capture.newInstance();
        EasyMock.expect(subCategoryRepository.save(EasyMock.capture(capture))).andReturn(subCategoryFromRepository);
        control.replay();
        // WHEN
        SubCategory result = underTest.save(subCategoryToSave, context);
        // THEN
        Assert.assertEquals(result, expectedSubCategory);
        SubCategoryEntity capturedCategory = capture.getValue();
        Assert.assertEquals(capturedCategory.getId(), subCategoryToSave.getId());
        Assert.assertEquals(capturedCategory.getName(), subCategoryToSave.getName());
        Assert.assertEquals(capturedCategory.getTransactionType(), subCategoryToSave.getTransactionType());
    }

    private Iterable<SubCategoryEntity> createSubCategoryEntities() {
        List<SubCategoryEntity> subCategoryEntityList = new ArrayList<>();
        subCategoryEntityList.add(createSubCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME));
        return subCategoryEntityList;
    }

    private SubCategoryEntity createSubCategoryEntity(final Long id, final String categoryName, final TransactionType type) {
        return SubCategoryEntity.builder()
            .withId(id)
            .withName(categoryName)
            .withTransactionType(type)
            .withUserId(USER_ID)
            .build();
    }

    private List<SubCategory> createSubCategoryList() {
        List<SubCategory> subCategoryList = new ArrayList<>();
        subCategoryList.add(createSubCategory(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME));
        return subCategoryList;
    }

    private SubCategory createSubCategory(final Long id, final String categoryName, final TransactionType type) {
        return SubCategory.builder()
            .withId(id)
            .withName(categoryName)
            .withTransactionType(type)
            .build();
    }

    private TransactionContext createTransactionContext(final TransactionType income) {
        return TransactionContext.builder()
            .withUserId(USER_ID)
            .withTransactionType(income)
            .build();
    }

}
