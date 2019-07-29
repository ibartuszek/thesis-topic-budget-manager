package hu.elte.bm.transactionservice.app.test;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.categories.DefaultMainCategoryService;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class MainCategoryTest extends AbstractTransactionServiceApplicationTest {

    private static final String NEW_CATEGORY_NAME = "new main category";
    private static final long EXISTING_INCOME_ID = 1L;
    private static final long EXISTING_OUTCOME_ID = 4L;
    private static final String RESERVED_CATEGORY_NAME = "main category 1";
    private static final String OTHER_RESERVED_CATEGORY_NAME = "main category 2";

    @Autowired
    private DefaultMainCategoryService mainCategoryService;

    @Test
    public void testSaveCategoryWhenCategoryHasNewName() {
        // GIVEN
        MainCategory newMainCategory = MainCategory.builder()
            .withName(NEW_CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(new HashSet<>())
            .build();
        // WHEN
        Optional<MainCategory> result = mainCategoryService.save(newMainCategory);
        // THEN
        Assert.assertEquals(newMainCategory, result.get());
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithDifferentCategory() {
        // GIVEN
        MainCategory newMainCategory = MainCategory.builder()
            .withName(RESERVED_CATEGORY_NAME)
            .withTransactionType(OUTCOME)
            .withSubCategorySet(new HashSet<>())
            .build();
        // WHEN
        Optional<MainCategory> result = mainCategoryService.save(newMainCategory);
        // THEN
        Assert.assertEquals(newMainCategory, result.get());
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithSameCategory() {
        // GIVEN
        MainCategory newMainCategory = MainCategory.builder()
            .withName(RESERVED_CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(createSubCategorySet())
            .build();
        // WHEN
        Optional<MainCategory> result = mainCategoryService.save(newMainCategory);
        // THEN
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSaveCategoryWhenCategoryHasNewNameButThereIsOneSubCategoryWithoutId() {
        // GIVEN
        Set<SubCategory> subCategorySet = createSubCategorySet();
        subCategorySet.add(createSubCategory(null, "illegal supplementary category", INCOME));
        MainCategory newMainCategory = MainCategory.builder()
            .withName(NEW_CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(subCategorySet)
            .build();
        // WHEN
        Optional<MainCategory> result = mainCategoryService.save(newMainCategory);
        // THEN
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewName() {
        // GIVEN
        MainCategory newMainCategory = MainCategory.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(NEW_CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(createSubCategorySet())
            .build();
        // WHEN
        Optional<MainCategory> result = mainCategoryService.update(newMainCategory);
        // THEN
        Assert.assertEquals(newMainCategory, result.get());
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndOtherTypeHasThisName() {
        // GIVEN
        MainCategory newMainCategory = MainCategory.builder()
            .withId(EXISTING_OUTCOME_ID)
            .withName(RESERVED_CATEGORY_NAME)
            .withTransactionType(OUTCOME)
            .withSubCategorySet(new HashSet<>())
            .build();
        // WHEN
        Optional<MainCategory> result = mainCategoryService.update(newMainCategory);
        // THEN
        Assert.assertEquals(newMainCategory, result.get());
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndItIsReserved() {
        // GIVEN
        MainCategory newMainCategory = MainCategory.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(OTHER_RESERVED_CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(createSubCategorySet())
            .build();
        // WHEN
        Optional<MainCategory> result = mainCategoryService.update(newMainCategory);
        // THEN
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewNameButThereIsOneSubCategoryWithoutId() {
        // GIVEN
        Set<SubCategory> subCategorySet = createSubCategorySet();
        subCategorySet.add(createSubCategory(null, "illegal supplementary category", INCOME));
        MainCategory newMainCategory = MainCategory.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(NEW_CATEGORY_NAME)
            .withTransactionType(INCOME)
            .withSubCategorySet(subCategorySet)
            .build();
        // WHEN
        Optional<MainCategory> result = mainCategoryService.update(newMainCategory);
        // THEN
        Assert.assertEquals(Optional.empty(), result);
    }

    private Set<SubCategory> createSubCategorySet() {
        Set<SubCategory> subCategorySet = new HashSet<>();
        subCategorySet.add(createSubCategory(1L, "supplementary category 1", INCOME));
        subCategorySet.add(createSubCategory(2L, "supplementary category 2", INCOME));
        return subCategorySet;
    }

    private SubCategory createSubCategory(final Long id, final String name, final TransactionType type) {
        return SubCategory.builder()
            .withId(id)
            .withName(name)
            .withTransactionType(type)
            .build();
    }
}
