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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

public class DefaultMainCategoryDaoTest {

    private static final long EXISTING_ID = 1L;
    private static final long NEW_ID = 2L;
    private static final String EXISTING_CATEGORY_NAME = "category name 1";
    private static final String NEW_CATEGORY_NAME = "category name";
    private static final Long USER_ID = 1L;

    private DefaultMainCategoryDao underTest;

    private IMocksControl control;
    private MainCategoryRepository mainCategoryRepository;
    private SubCategoryRepository subCategoryRepository;

    @BeforeClass
    public void setup() {
        control = EasyMock.createStrictControl();
        mainCategoryRepository = control.createMock(MainCategoryRepository.class);
        subCategoryRepository = control.createMock(SubCategoryRepository.class);
        MainCategoryEntityTransformer mainCategoryEntityTransformer =
            new MainCategoryEntityTransformer(new SubCategoryEntityTransformer());
        underTest = new DefaultMainCategoryDao(mainCategoryRepository, subCategoryRepository, mainCategoryEntityTransformer);
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
        EasyMock.expect(mainCategoryRepository.findAllMainCategory(INCOME, USER_ID)).andReturn(Collections.emptyList());
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.findAll(context);
        // THEN
        Assert.assertEquals(result, Collections.emptyList());
    }

    @Test
    public void testFindAllWhenThereAreMoreCategoriesInTheRepository() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        Iterable<MainCategoryEntity> mainCategoryEntities = createMainCategoryEntities();
        EasyMock.expect(mainCategoryRepository.findAllMainCategory(INCOME, USER_ID)).andReturn(mainCategoryEntities);
        List<MainCategory> expectedList = createMainCategoryList();
        control.replay();
        // WHEN
        List<MainCategory> result = underTest.findAll(context);
        // THEN
        Assert.assertEquals(result, expectedList);
    }

    @Test
    public void testFindByIdWhenCategoryCannotBeFound() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        EasyMock.expect(mainCategoryRepository.findByIdAndUserId(NEW_ID, USER_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.findById(NEW_ID, context);
        // THEN
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testFindByIdWhenCategoryCanBeFound() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        MainCategoryEntity mainCategoryFromRepository = createMainCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME, createSubCategoryEntitySet());
        Optional<MainCategory> expectedMainCategory = Optional.of(createMainCategory(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME, createSubCategorySet()));
        EasyMock.expect(mainCategoryRepository.findByIdAndUserId(EXISTING_ID, USER_ID)).andReturn(Optional.of(mainCategoryFromRepository));
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.findById(EXISTING_ID, context);
        // THEN
        Assert.assertEquals(result, expectedMainCategory);
    }

    @Test
    public void testFindByNameWhenCategoryCannotBeFound() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        EasyMock.expect(mainCategoryRepository.findByName(NEW_CATEGORY_NAME, INCOME, USER_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.findByName(NEW_CATEGORY_NAME, context);
        // THEN
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void testFindByNameWhenCategoryCanBeFound() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        MainCategoryEntity mainCategoryFromRepository = createMainCategoryEntity(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME, createSubCategoryEntitySet());
        Optional<MainCategory> expectedSubCategory = Optional.of(createMainCategory(EXISTING_ID, EXISTING_CATEGORY_NAME, INCOME, createSubCategorySet()));
        EasyMock.expect(mainCategoryRepository.findByName(EXISTING_CATEGORY_NAME, INCOME, USER_ID)).andReturn(Optional.of(mainCategoryFromRepository));
        control.replay();
        // WHEN
        Optional<MainCategory> result = underTest.findByName(EXISTING_CATEGORY_NAME, context);
        // THEN
        Assert.assertEquals(result, expectedSubCategory);
    }

    @Test
    public void testSaveMainCategory() {
        // GIVEN
        TransactionContext context = createTransactionContext(INCOME);
        MainCategory mainCategoryToSave = createMainCategory(null, NEW_CATEGORY_NAME, INCOME, createSubCategorySet());
        MainCategoryEntity mainCategoryFromRepository = createMainCategoryEntity(NEW_ID, NEW_CATEGORY_NAME, INCOME, createSubCategoryEntitySet());
        MainCategory expectedMainCategory = createMainCategory(NEW_ID, NEW_CATEGORY_NAME, INCOME, createSubCategorySet());
        Capture<MainCategoryEntity> capture = Capture.newInstance();
        EasyMock.expect(subCategoryRepository.findByIdAndUserId(EXISTING_ID, USER_ID)).andReturn(
            Optional.ofNullable(mainCategoryFromRepository.getSubCategoryEntitySet().iterator().next()));
        EasyMock.expect(mainCategoryRepository.save(EasyMock.capture(capture))).andReturn(mainCategoryFromRepository);
        control.replay();
        // WHEN
        MainCategory result = underTest.save(mainCategoryToSave, context);
        // THEN
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
        return MainCategoryEntity.builder()
            .withId(id)
            .withName(categoryName)
            .withTransactionType(type)
            .withSubCategoryEntitySet(subCategoryEntitySet)
            .withUserId(USER_ID)
            .build();
    }

    private SubCategoryEntity createSubCategoryEntity(final Long id, final String categoryName, final TransactionType type) {
        return SubCategoryEntity.builder()
            .withId(id)
            .withName(categoryName)
            .withTransactionType(type)
            .withUserId(USER_ID)
            .build();
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

    private TransactionContext createTransactionContext(final TransactionType income) {
        return TransactionContext.builder()
            .withUserId(USER_ID)
            .withTransactionType(income)
            .build();
    }

}
