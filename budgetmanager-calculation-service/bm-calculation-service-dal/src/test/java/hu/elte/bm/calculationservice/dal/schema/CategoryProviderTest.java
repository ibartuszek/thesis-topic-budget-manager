package hu.elte.bm.calculationservice.dal.schema;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import hu.elte.bm.calculationservice.transactionserviceclient.TransactionServiceFacade;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;
import hu.elte.bm.transactionservice.exceptions.maincategory.IllegalMainCategoryException;
import hu.elte.bm.transactionservice.exceptions.maincategory.MainCategoryNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CategoryProviderTest {

    private static final TransactionType TYPE = TransactionType.OUTCOME;
    private static final long USER_ID = 1L;
    private static final long EXPECTED_MAIN_CATEGORY_ID = 1L;
    private static final Long OTHER_MAIN_CATEGORY_ID = 2L;
    private static final Long INVALID_MAIN_CATEGORY_ID = 3L;
    private static final Long ANOTHER_MAIN_CATEGORY_ID = 4L;
    private static final String MAIN_CATEGORY_NAME = "Main category name";
    private static final String OTHER_MAIN_CATEGORY_NAME = "Other main category name";
    private static final String ANOTHER_MAIN_CATEGORY_NAME = "Another main category name";
    private static final long EXPECTED_SUB_CATEGORY_ID = 1L;
    private static final long OTHER_SUB_CATEGORY_ID = 2L;
    private static final Long INVALID_SUB_CATEGORY_ID = 3L;
    private static final long ANOTHER_SUB_CATEGORY_ID = 4L;
    private static final String SUB_CATEGORY_NAME = "Subcategory name";
    private static final String OTHER_SUB_CATEGORY_NAME = "Other subcategory name";
    private static final String ANOTHER_SUB_CATEGORY_NAME = "Another subcategory name";

    @InjectMocks
    private CategoryProvider underTest;

    @Mock
    private TransactionServiceFacade transactionServiceFacade;

    @Test
    public void testProvideMainCategoryListWhenMainCategoryIdListIsEmpty() {
        // GIVEN
        // WHEN
        var result = underTest.provideMainCategoryList(Collections.emptySet(), USER_ID, TYPE);
        // THEN
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = MainCategoryNotFoundException.class)
    public void testProvideMainCategoryListWhenOneMainCategoryCannotBeFound() {
        // GIVEN
        Set<Long> mainCategoryIdSet = Set.of(EXPECTED_MAIN_CATEGORY_ID, INVALID_MAIN_CATEGORY_ID);
        MainCategory expectedMainCategory = createMainCategoryBuilder().build();
        setTransactionServiceFacadeResponse(expectedMainCategory);
        // WHEN
        underTest.provideMainCategoryList(mainCategoryIdSet, USER_ID, TYPE);
        // THEN
    }

    @Test
    public void testProvideMainCategoryList() {
        // GIVEN
        Set<Long> mainCategoryIdSet = Set.of(EXPECTED_MAIN_CATEGORY_ID);
        MainCategory expectedMainCategory = createMainCategoryBuilder().build();
        setTransactionServiceFacadeResponse(expectedMainCategory);
        // WHEN
        var result = underTest.provideMainCategoryList(mainCategoryIdSet, USER_ID, TYPE);
        // THEN
        Mockito.verify(transactionServiceFacade).getMainCategories(TYPE, USER_ID);
        Assert.assertEquals(List.of(expectedMainCategory), result);
    }

    @Test(expected = MainCategoryNotFoundException.class)
    public void testProvideMainCategoryWhenMainCategoryCannotBeFound() {
        // GIVEN
        MainCategory expectedMainCategory = createMainCategoryBuilder().build();
        setTransactionServiceFacadeResponse(expectedMainCategory);
        // WHEN
        underTest.provideMainCategory(INVALID_MAIN_CATEGORY_ID, USER_ID, TYPE);
        // THEN
    }

    @Test
    public void testProvideMainCategory() {
        // GIVEN
        MainCategory expectedMainCategory = createMainCategoryBuilder().build();
        setTransactionServiceFacadeResponse(expectedMainCategory);
        // WHEN
        var result = underTest.provideMainCategory(EXPECTED_MAIN_CATEGORY_ID, USER_ID, TYPE);
        // THEN
        Mockito.verify(transactionServiceFacade).getMainCategories(TYPE, USER_ID);
        Assert.assertEquals(expectedMainCategory, result);
    }

    @Test(expected = IllegalMainCategoryException.class)
    public void testProvideSubCategoryWhenMainCategoryNotContainsSubCategory() {
        // GIVEN
        SubCategory subCategory = createSubCategoryBuilder().build();
        MainCategory mainCategory = createMainCategoryBuilder()
            .withSubCategorySet(createSubCategorySet(subCategory))
            .build();
        // WHEN
        underTest.provideSubCategory(INVALID_SUB_CATEGORY_ID, mainCategory);
        // THEN
    }

    @Test
    public void testProvideSubCategory() {
        // GIVEN
        SubCategory expected = createSubCategoryBuilder().build();
        MainCategory mainCategory = createMainCategoryBuilder()
            .withSubCategorySet(createSubCategorySet(expected))
            .build();
        // WHEN
        var result = underTest.provideSubCategory(EXPECTED_SUB_CATEGORY_ID, mainCategory);
        // THEN
        Assert.assertEquals(expected, result);
    }

    private void setTransactionServiceFacadeResponse(final MainCategory expectedMainCategory) {
        MainCategory other = MainCategory.builder()
            .withId(OTHER_MAIN_CATEGORY_ID)
            .withName(OTHER_MAIN_CATEGORY_NAME)
            .withTransactionType(TYPE)
            .build();
        MainCategory another = MainCategory.builder()
            .withId(ANOTHER_MAIN_CATEGORY_ID)
            .withName(ANOTHER_MAIN_CATEGORY_NAME)
            .withTransactionType(TYPE)
            .build();
        List<MainCategory> mainCategoryList = List.of(other, expectedMainCategory, another);
        Mockito.when(transactionServiceFacade.getMainCategories(TYPE, USER_ID)).thenReturn(mainCategoryList);
    }

    private Set<SubCategory> createSubCategorySet(final SubCategory subCategory) {
        SubCategory other = SubCategory.builder()
            .withId(OTHER_SUB_CATEGORY_ID)
            .withName(OTHER_SUB_CATEGORY_NAME)
            .withTransactionType(TYPE)
            .build();
        SubCategory another = SubCategory.builder()
            .withId(ANOTHER_SUB_CATEGORY_ID)
            .withName(ANOTHER_SUB_CATEGORY_NAME)
            .withTransactionType(TYPE)
            .build();
        return Set.of(other, subCategory, another);
    }

    private MainCategory.Builder createMainCategoryBuilder() {
        return MainCategory.builder()
            .withId(EXPECTED_MAIN_CATEGORY_ID)
            .withName(MAIN_CATEGORY_NAME)
            .withTransactionType(TYPE);
    }

    private SubCategory.Builder createSubCategoryBuilder() {
        return SubCategory.builder()
            .withId(EXPECTED_SUB_CATEGORY_ID)
            .withName(SUB_CATEGORY_NAME)
            .withTransactionType(TYPE);
    }

}
