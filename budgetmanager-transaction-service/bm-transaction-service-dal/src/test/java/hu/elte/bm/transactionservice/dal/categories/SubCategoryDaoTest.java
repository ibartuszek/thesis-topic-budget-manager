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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class SubCategoryDaoTest {

    private static final long EXISTING_ID = 1L;
    private static final long NEW_ID = 2L;
    private static final String EXISTING_CATEGORY_NAME = "category name 1";
    private static final String NEW_CATEGORY_NAME = "category name";
    private final SubCategoryEntityTransformer subCategoryEntityTransformer = new SubCategoryEntityTransformer();

    private SubCategoryDao underTest;

    private IMocksControl control;
    private SubCategoryRepository subCategoryRepository;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createStrictControl();
        subCategoryRepository = control.createMock(SubCategoryRepository.class);
        underTest = new SubCategoryDao(subCategoryRepository, subCategoryEntityTransformer);
    }

    @Test
    public void testFindAllWhenThereIsNoCategoryInTheRepository() {
        // GIVEN
        EasyMock.expect(subCategoryRepository.findAllSubcategory(INCOME)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.findAll(INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFindAllWhenThereAreMoreCategoriesInTheRepository() {
        // GIVEN
        Iterable<SubCategoryEntity> subCategoryEntities = createSubCategoryEntities();
        EasyMock.expect(subCategoryRepository.findAllSubcategory(INCOME)).andReturn(subCategoryEntities);
        List<SubCategory> expectedList = createSubCategoryList();
        control.replay();
        // WHEN
        List<SubCategory> result = underTest.findAll(INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testFindByIdWhenCategoryCannotBeFound() {
        // GIVEN
        EasyMock.expect(subCategoryRepository.findById(NEW_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.findById(NEW_ID);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testFindByIdWhenCategoryCanBeFound() {
        // GIVEN
        SubCategoryEntity subCategoryFromRepository = createSubCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME);
        Optional<SubCategory> expectedSubCategory = Optional.of(createSubCategory(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME));
        EasyMock.expect(subCategoryRepository.findById(EXISTING_ID)).andReturn(Optional.of(subCategoryFromRepository));
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.findById(EXISTING_ID);
        // THEN
        control.verify();
        Assert.assertEquals(expectedSubCategory, result);
    }

    @Test
    public void testFindByNameWhenCategoryCannotBeFound() {
        // GIVEN

        EasyMock.expect(subCategoryRepository.findByName(NEW_CATEGORY_NAME, INCOME)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.findByName(NEW_CATEGORY_NAME, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testFindByNameWhenCategoryCanBeFound() {
        // GIVEN
        SubCategoryEntity subCategoryFromRepository = createSubCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME);
        Optional<SubCategory> expectedSubCategory = Optional.of(createSubCategory(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME));
        EasyMock.expect(subCategoryRepository.findByName(EXISTING_CATEGORY_NAME, INCOME)).andReturn(Optional.of(subCategoryFromRepository));
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.findByName(EXISTING_CATEGORY_NAME, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(expectedSubCategory, result);
    }

    @Test
    public void testSaveSubCategory() {
        // GIVEN
        SubCategory subCategoryToSave = createSubCategory(null, NEW_CATEGORY_NAME, INCOME);
        SubCategoryEntity subCategoryFromRepository = createSubCategoryEntity(NEW_ID, NEW_CATEGORY_NAME, INCOME);
        Optional<SubCategory> expecTedSubCategory = Optional.of(createSubCategory(NEW_ID, NEW_CATEGORY_NAME, INCOME));
        Capture<SubCategoryEntity> capture = Capture.newInstance();
        EasyMock.expect(subCategoryRepository.save(EasyMock.capture(capture))).andReturn(subCategoryFromRepository);
        control.replay();
        // WHEN
        Optional<SubCategory> result = underTest.save(subCategoryToSave);
        // THEN
        control.verify();
        Assert.assertEquals(expecTedSubCategory, result);
        SubCategoryEntity capturedCategory = capture.getValue();
        Assert.assertEquals(subCategoryToSave.getId(), capturedCategory.getId());
        Assert.assertEquals(subCategoryToSave.getName(), capturedCategory.getName());
        Assert.assertEquals(subCategoryToSave.getTransactionType(), capturedCategory.getTransactionType());
    }

    private Iterable<SubCategoryEntity> createSubCategoryEntities() {
        List<SubCategoryEntity> subCategoryEntityList = new ArrayList<>();
        subCategoryEntityList.add(createSubCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME));
        return subCategoryEntityList;
    }

    private SubCategoryEntity createSubCategoryEntity(final Long id, final String categoryName, final TransactionType type) {
        return new SubCategoryEntity(id, categoryName, type);
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

}
