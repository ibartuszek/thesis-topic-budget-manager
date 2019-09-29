package hu.elte.bm.transactionservice.app.test.subcategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryRequestContext;

public class SaveSubCategoryTest extends AbstractSubCategoryTest {

    private static final String URL = "/bm/subCategories/create";
    private static final int EXPECTED_ID = 8;

    @Test(dataProvider = "dataForSubCategoryValidation")
    public void testSaveCategoryWhenCategoryValidationFails(final SubCategory subCategory, final String errorMessage) throws Exception {
        // GIVEN
        SubCategoryRequestContext context = createContext(INCOME, subCategory);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test(dataProvider = "dataForContextValidation")
    public void testSaveCategoryWhenContextValidationFails(final SubCategoryRequestContext context, final String errorMessage) throws Exception {
        // GIVEN
        SubCategory subCategoryToSave = createSubCategoryBuilder(null, NEW_CATEGORY_NAME, INCOME).build();
        context.setSubCategory(subCategoryToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test
    public void testSaveCategoryWhenIdIsNotNull() throws Exception {
        // GIVEN
        SubCategory subCategoryToSave = createSubCategoryBuilder(NEW_ID, NEW_CATEGORY_NAME, INCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("New subCategory id must be null!"));
    }

    @Test
    public void testSaveCategoryWhenCategoryHasNewName() throws Exception {
        // GIVEN
        SubCategory subCategoryModelToSave = createSubCategoryBuilder(null, NEW_CATEGORY_NAME, INCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryModelToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_CATEGORY_HAS_BEEN_SAVED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory.id", Matchers.is(EXPECTED_ID)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory.name", Matchers.is(NEW_CATEGORY_NAME)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory.transactionType", Matchers.is(INCOME.name())));
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithDifferentCategory() throws Exception {
        // GIVEN
        SubCategory subCategoryModelToSave = createSubCategoryBuilder(null, RESERVED_CATEGORY_NAME, OUTCOME).build();
        SubCategoryRequestContext context = createContext(OUTCOME, subCategoryModelToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_CATEGORY_HAS_BEEN_SAVED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory.id", Matchers.is(EXPECTED_ID)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory.name", Matchers.is(RESERVED_CATEGORY_NAME)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory.transactionType", Matchers.is(OUTCOME.name())));
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithSameCategory() throws Exception {
        // GIVEN
        SubCategory subCategoryModelToSave = createSubCategoryBuilder(null, RESERVED_CATEGORY_NAME, INCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryModelToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(MockMvcResultMatchers.content().string("The category has been saved before."));
    }

}
