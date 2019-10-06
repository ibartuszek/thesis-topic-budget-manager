package hu.elte.bm.transactionservice.dal.transaction.transactionEntityContext;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.time.LocalDate;
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

import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.MainCategoryRepository;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryRepository;
import hu.elte.bm.transactionservice.dal.picture.PictureEntity;
import hu.elte.bm.transactionservice.dal.picture.PictureRepository;
import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.exceptions.PictureNotFoundException;
import hu.elte.bm.transactionservice.domain.exceptions.maincategory.MainCategoryNotFoundException;
import hu.elte.bm.transactionservice.domain.exceptions.subcategory.SubCategoryNotFoundException;
import hu.elte.bm.transactionservice.domain.transaction.Picture;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;

public class TransactionEntityContextFactoryTest {

    private static final long ID = 1L;
    private static final String CATEGORY_NAME = "category";
    private static final byte[] PICTURE = new byte[1];
    private static final String TRANSACTION_TITLE = "title";
    private static final double TRANSACTION_AMOUNT = 1.0;
    private static final Currency TRANSACTION_CURRENCY = Currency.EUR;
    private static final LocalDate TRANSACTION_DATE = LocalDate.now();
    private static final Long USER_ID = 2L;
    private TransactionEntityContextFactory underTest;
    private IMocksControl control;
    private MainCategoryRepository mainCategoryRepository;
    private SubCategoryRepository subCategoryRepository;
    private PictureRepository pictureRepository;

    @BeforeClass
    public void setup() {
        control = EasyMock.createControl();
        mainCategoryRepository = control.createMock(MainCategoryRepository.class);
        subCategoryRepository = control.createMock(SubCategoryRepository.class);
        pictureRepository = control.createMock(PictureRepository.class);
        underTest = new TransactionEntityContextFactory(mainCategoryRepository, subCategoryRepository, pictureRepository);
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
    public void testCreateWhenPictureAndSubCategoryIsNull() {
        // GIVEN
        MainCategory mainCategory = createMainCategory(Set.of());
        Transaction transaction = createTransaction(null, mainCategory, null);
        MainCategoryEntity mainCategoryEntity = createMainCategoryEntity(Set.of());
        EasyMock.expect(mainCategoryRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.of(mainCategoryEntity));
        control.replay();
        // WHEN
        TransactionEntityContext result = underTest.create(transaction, USER_ID);
        // THEN
        Assert.assertEquals(result.getTransaction(), transaction);
        Assert.assertEquals(result.getUserId(), USER_ID);
        Assert.assertEquals(result.getMainCategoryEntity(), mainCategoryEntity);
        Assert.assertNull(result.getSubCategoryEntity());
        Assert.assertNull(result.getPictureEntity());
    }

    @Test(expectedExceptions = MainCategoryNotFoundException.class)
    public void testCreateWhenMainCategoryCannotBeFound() {
        // GIVEN
        MainCategory mainCategory = createMainCategory(Set.of());
        Transaction transaction = createTransaction(null, mainCategory, null);
        EasyMock.expect(mainCategoryRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        underTest.create(transaction, USER_ID);
        // THEN
    }

    @Test(expectedExceptions = SubCategoryNotFoundException.class)
    public void testCreateWhenSubCategoryCannotBeFound() {
        // GIVEN
        SubCategory subCategory = createSubCategory();
        MainCategory mainCategory = createMainCategory(Set.of(subCategory));
        Transaction transaction = createTransaction(subCategory, mainCategory, null);
        MainCategoryEntity mainCategoryEntity = createMainCategoryEntity(Set.of());
        EasyMock.expect(mainCategoryRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.of(mainCategoryEntity));
        EasyMock.expect(subCategoryRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        underTest.create(transaction, USER_ID);
        // THEN
    }

    @Test(expectedExceptions = PictureNotFoundException.class)
    public void testCreateWhenPictureCannotBeFound() {
        // GIVEN
        SubCategory subCategory = createSubCategory();
        MainCategory mainCategory = createMainCategory(Set.of(subCategory));
        Picture picture = createPicture().build();
        Transaction transaction = createTransaction(subCategory, mainCategory, picture);
        SubCategoryEntity subCategoryEntity = createSubCategoryEntity();
        MainCategoryEntity mainCategoryEntity = createMainCategoryEntity(Set.of(subCategoryEntity));
        EasyMock.expect(mainCategoryRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.of(mainCategoryEntity));
        EasyMock.expect(subCategoryRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.of(subCategoryEntity));
        EasyMock.expect(pictureRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.empty());
        control.replay();
        // WHEN
        underTest.create(transaction, USER_ID);
        // THEN
    }

    @Test
    public void testCreateWhenThereIsAnExistingParts() {
        // GIVEN
        SubCategory subCategory = createSubCategory();
        MainCategory mainCategory = createMainCategory(Set.of(subCategory));
        Picture picture = createPicture().build();
        Transaction transaction = createTransaction(subCategory, mainCategory, picture);
        SubCategoryEntity subCategoryEntity = createSubCategoryEntity();
        MainCategoryEntity mainCategoryEntity = createMainCategoryEntity(Set.of(subCategoryEntity));
        PictureEntity pictureEntity = createPictureEntity();
        EasyMock.expect(mainCategoryRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.of(mainCategoryEntity));
        EasyMock.expect(subCategoryRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.of(subCategoryEntity));
        EasyMock.expect(pictureRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.of(pictureEntity));
        control.replay();
        // WHEN
        TransactionEntityContext result = underTest.create(transaction, USER_ID);
        // THEN
        Assert.assertEquals(result.getTransaction(), transaction);
        Assert.assertEquals(result.getUserId(), USER_ID);
        Assert.assertEquals(result.getMainCategoryEntity(), mainCategoryEntity);
        Assert.assertEquals(result.getSubCategoryEntity(), subCategoryEntity);
        Assert.assertEquals(result.getPictureEntity(), pictureEntity);
    }

    @Test
    public void testCreateWhenThereIsNewPicture() {
        // GIVEN
        SubCategory subCategory = createSubCategory();
        MainCategory mainCategory = createMainCategory(Set.of(subCategory));
        Picture picture = createPicture()
            .withId(null)
            .build();
        Transaction transaction = createTransaction(subCategory, mainCategory, picture);
        SubCategoryEntity subCategoryEntity = createSubCategoryEntity();
        MainCategoryEntity mainCategoryEntity = createMainCategoryEntity(Set.of(subCategoryEntity));
        PictureEntity pictureEntity = createPictureEntity();
        Capture<PictureEntity> capture = Capture.newInstance();
        EasyMock.expect(mainCategoryRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.of(mainCategoryEntity));
        EasyMock.expect(subCategoryRepository.findByIdAndUserId(ID, USER_ID)).andReturn(Optional.of(subCategoryEntity));
        EasyMock.expect(pictureRepository.save(EasyMock.capture(capture))).andReturn(pictureEntity);
        control.replay();
        // WHEN
        TransactionEntityContext result = underTest.create(transaction, USER_ID);
        // THEN
        Assert.assertEquals(result.getTransaction(), transaction);
        Assert.assertEquals(result.getUserId(), USER_ID);
        Assert.assertEquals(result.getMainCategoryEntity(), mainCategoryEntity);
        Assert.assertEquals(result.getSubCategoryEntity(), subCategoryEntity);
        Assert.assertEquals(result.getPictureEntity(), pictureEntity);
        PictureEntity captured = capture.getValue();
        Assert.assertNull(captured.getId());
        Assert.assertEquals(captured.getPicture(), PICTURE);
        Assert.assertEquals(captured.getUserId(), USER_ID);
    }

    private Transaction createTransaction(final SubCategory subCategory, final MainCategory mainCategory, final Picture picture) {
        return Transaction.builder()
            .withTitle(TRANSACTION_TITLE)
            .withAmount(TRANSACTION_AMOUNT)
            .withCurrency(TRANSACTION_CURRENCY)
            .withDate(TRANSACTION_DATE)
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory)
            .withPicture(picture)
            .build();
    }

    private MainCategory createMainCategory(final Set<SubCategory> subCategorySet) {
        return MainCategory.builder()
            .withId(ID)
            .withName(CATEGORY_NAME)
            .withSubCategorySet(subCategorySet)
            .build();
    }

    private SubCategory createSubCategory() {
        return SubCategory.builder()
            .withId(ID)
            .withName(CATEGORY_NAME)
            .build();
    }

    private Picture.Builder createPicture() {
        return Picture.builder()
            .withId(ID)
            .withPicture(PICTURE);
    }

    private MainCategoryEntity createMainCategoryEntity(final Set<SubCategoryEntity> subCategoryEntitySet) {
        return MainCategoryEntity.builder()
            .withId(ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategoryEntitySet(subCategoryEntitySet)
            .withUserId(USER_ID)
            .build();
    }

    private SubCategoryEntity createSubCategoryEntity() {
        return SubCategoryEntity.builder()
            .withId(ID)
            .withName(CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withUserId(USER_ID)
            .build();
    }

    private PictureEntity createPictureEntity() {
        return PictureEntity.builder()
            .withId(ID)
            .withPicture(PICTURE)
            .withUserId(USER_ID)
            .build();
    }

}
