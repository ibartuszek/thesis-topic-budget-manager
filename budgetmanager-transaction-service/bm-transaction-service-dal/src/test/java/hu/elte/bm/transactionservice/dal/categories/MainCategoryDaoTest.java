package hu.elte.bm.transactionservice.dal.categories;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class MainCategoryDaoTest {

    private static final long EXISTING_ID = 1L;
    private static final long NEW_ID = 2L;
    private static final String EXISTING_CATEGORY_NAME = "category name 1";
    private static final String NEW_CATEGORY_NAME = "category name";
    private final MainCategoryEntityTransformer mainCategoryEntityTransformer =
        new MainCategoryEntityTransformer(new SubCategoryEntityTransformer());

    private MainCategoryDao underTest;

    private IMocksControl control;
    private MainCategoryRepository mainCategoryRepository;
    private SubCategoryRepository subCategoryRepository;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createStrictControl();
        mainCategoryRepository = control.createMock(MainCategoryRepository.class);
        subCategoryRepository = control.createMock(SubCategoryRepository.class);
        underTest = new MainCategoryDao(mainCategoryRepository, subCategoryRepository, mainCategoryEntityTransformer);
    }

    @Test
    public void testFindAllWhenThereIsNoCategoryInTheRepository() {
        // GIVEN
        EasyMock.expect(mainCategoryRepository.findAllMainCategory(INCOME)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.findAll(INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void testFindAllWhenThereAreMoreCategoriesInTheRepository() {
        // GIVEN
        Iterable<MainCategoryEntity> mainCategoryEntities = createMainCategoryEntities();
        EasyMock.expect(mainCategoryRepository.findAllMainCategory(INCOME)).andReturn(mainCategoryEntities);
        List<MainCategory> expectedList = createMainCategoryList();
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.findAll(INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedList);
    }

    @Test
    public void testFindByIdWhenCategoryCannotBeFound() {
        // GIVEN
        EasyMock.expect(mainCategoryRepository.findById(NEW_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.findById(NEW_ID);
        // THEN
        control.verify();
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testFindByIdWhenCategoryCanBeFound() {
        // GIVEN
        MainCategoryEntity mainCategoryFromRepository = createMainCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME, createSubCategoryEntitySet());
        Optional<MainCategory> expectedMainCategory = Optional.of(createMainCategory(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME, createSubCategorySet()));
        EasyMock.expect(mainCategoryRepository.findById(EXISTING_ID)).andReturn(Optional.of(mainCategoryFromRepository));
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.findById(EXISTING_ID);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedMainCategory);
    }

    @Test
    public void testFindByNameWhenCategoryCannotBeFound() {
        // GIVEN
        EasyMock.expect(mainCategoryRepository.findByName(NEW_CATEGORY_NAME, INCOME)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.findByName(NEW_CATEGORY_NAME, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testFindByNameWhenCategoryCanBeFound() {
        // GIVEN
        MainCategoryEntity mainCategoryFromRepository = createMainCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME, createSubCategoryEntitySet());
        Optional<MainCategory> expectedSubCategory = Optional.of(createMainCategory(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME, createSubCategorySet()));
        EasyMock.expect(mainCategoryRepository.findByName(EXISTING_CATEGORY_NAME, INCOME)).andReturn(Optional.of(mainCategoryFromRepository));
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.findByName(EXISTING_CATEGORY_NAME, INCOME);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedSubCategory);
    }

    @Test
    public void testSaveMainCategory() {
        // GIVEN
        MainCategory mainCategoryToSave = createMainCategory(null, NEW_CATEGORY_NAME, INCOME, createSubCategorySet());
        MainCategoryEntity mainCategoryFromRepository = createMainCategoryEntity(NEW_ID, NEW_CATEGORY_NAME, INCOME, createSubCategoryEntitySet());
        Optional<MainCategory> expectedMainCategory = Optional.of(createMainCategory(NEW_ID, NEW_CATEGORY_NAME, INCOME, createSubCategorySet()));
        Capture<MainCategoryEntity> capture = Capture.newInstance();
        EasyMock.expect(subCategoryRepository.findById(EXISTING_ID)).andReturn(
            Optional.ofNullable(mainCategoryFromRepository.getSubCategoryEntitySet().iterator().next()));
        EasyMock.expect(mainCategoryRepository.save(EasyMock.capture(capture))).andReturn(mainCategoryFromRepository);
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.save(mainCategoryToSave);
        // THEN
        control.verify();
        Assert.assertEquals(result, expectedMainCategory);
        MainCategoryEntity capturedCategory = capture.getValue();
        Assert.assertEquals(capturedCategory.getId(), mainCategoryToSave.getId());
        Assert.assertEquals(capturedCategory.getName(), mainCategoryToSave.getName());
        Assert.assertEquals(capturedCategory.getTransactionType(), mainCategoryToSave.getTransactionType());
    }

    private Iterable<MainCategoryEntity> createMainCategoryEntities() {
        List<MainCategoryEntity> mainCategoryEntityList = new ArrayList<>();
        mainCategoryEntityList.add(createMainCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME, createSubCategoryEntitySet()));
        return mainCategoryEntityList;
    }

    private MainCategoryEntity createMainCategoryEntity(final Long id, final String categoryName, final TransactionType type,
        final Set<SubCategoryEntity> subCategoryEntitySet) {
        return new MainCategoryEntity(id, categoryName, type, subCategoryEntitySet);
    }

    private SubCategoryEntity createSubCategoryEntity(final Long id, final String categoryName, final TransactionType type) {
        return new SubCategoryEntity(id, categoryName, type);
    }

    private Set<SubCategoryEntity> createSubCategoryEntitySet() {
        Set<SubCategoryEntity> subCategoryEntitySet = new HashSet<>();
        subCategoryEntitySet.add(createSubCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME));
        return subCategoryEntitySet;
    }

    private MainCategory createMainCategory(final Long id, final String categoryName, final TransactionType type, final Set<SubCategory> subCategorySet) {
        return MainCategory.builder()
            .withId(id)
            .withName(categoryName)
            .withTransactionType(type)
            .withSubCategorySet(subCategorySet)
            .build();
    }

    private Set<SubCategory> createSubCategorySet() {
        Set<SubCategory> subCategorySet = new HashSet<>();
        subCategorySet.add(createSubCategory(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME));
        return subCategorySet;
    }

    private SubCategory createSubCategory(final Long id, final String categoryName, final TransactionType type) {
        return SubCategory.builder()
            .withId(id)
            .withName(categoryName)
            .withTransactionType(type)
            .build();
    }

    private List<MainCategory> createMainCategoryList() {
        List<MainCategory> mainCategoryList = new ArrayList<>();
        Set<SubCategory> subCategorySet = createSubCategorySet();
        mainCategoryList.add(createMainCategory(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME, subCategorySet));
        return mainCategoryList;
    }

}
