package hu.elte.bm.transactionservice.app.test;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.app.AbstractTransactionServiceApplicationTest;
import hu.elte.bm.transactionservice.domain.categories.DefaultSubCategoryService;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public class SubCategoryTest extends AbstractTransactionServiceApplicationTest {

    private static final String NEW_CATEGORY_NAME = "new supplementary category";
    private static final Long EXISTING_INCOME_ID = 1L;
    private static final Long EXISTING_OUTCOME_ID = 4L;
    private static final String RESERVED_CATEGORY_NAME = "supplementary category 1";
    private static final String OTHER_RESERVED_CATEGORY_NAME = "supplementary category 2";

    @Autowired
    private DefaultSubCategoryService subCategoryService;

    @Test
    public void testSaveCategoryWhenCategoryHasNewName() {
        // GIVEN
        SubCategory newSubCategory = SubCategory.builder()
            .withName(NEW_CATEGORY_NAME)
            .withTransactionType(INCOME)
            .build();
        // WHEN
        Optional<SubCategory> result = subCategoryService.save(newSubCategory);
        // THEN
        Assert.assertEquals(newSubCategory, result.get());
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithDifferentCategory() {
        // GIVEN
        SubCategory newSubCategory = SubCategory.builder()
            .withName(RESERVED_CATEGORY_NAME)
            .withTransactionType(TransactionType.OUTCOME)
            .build();
        // WHEN
        Optional<SubCategory> result = subCategoryService.save(newSubCategory);
        // THEN
        Assert.assertEquals(newSubCategory, result.get());
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithSameCategory() {
        // GIVEN
        SubCategory newSubCategory = SubCategory.builder()
            .withName(RESERVED_CATEGORY_NAME)
            .withTransactionType(INCOME)
            .build();
        // WHEN
        Optional<SubCategory> result = subCategoryService.save(newSubCategory);
        // THEN
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewName() {
        // GIVEN
        SubCategory newSubCategory = SubCategory.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(NEW_CATEGORY_NAME)
            .withTransactionType(INCOME)
            .build();
        // WHEN
        Optional<SubCategory> result = subCategoryService.update(newSubCategory);
        // THEN
        Assert.assertEquals(newSubCategory, result.get());
        Assert.assertEquals(EXISTING_INCOME_ID, result.get().getId());
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndOtherTypeHasThisName() {
        // GIVEN
        SubCategory newSubCategory = SubCategory.builder()
            .withId(EXISTING_OUTCOME_ID)
            .withName(RESERVED_CATEGORY_NAME)
            .withTransactionType(TransactionType.OUTCOME)
            .build();
        // WHEN
        Optional<SubCategory> result = subCategoryService.update(newSubCategory);
        // THEN
        Assert.assertEquals(newSubCategory, result.get());
        Assert.assertEquals(EXISTING_OUTCOME_ID, result.get().getId());
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndItIsReserved() {
        // GIVEN
        SubCategory newSubCategory = SubCategory.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(OTHER_RESERVED_CATEGORY_NAME)
            .withTransactionType(INCOME)
            .build();
        // WHEN
        Optional<SubCategory> result = subCategoryService.update(newSubCategory);
        // THEN
        Assert.assertEquals(Optional.empty(), result);
    }

}
