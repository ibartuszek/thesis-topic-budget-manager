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

public class UpdateSubCategoryTest extends AbstractSubCategoryTest {

    private static final String URL = "/bm/subCategories/update";

    @Test(dataProvider = "dataForSubCategoryValidation")
    public void testUpdateCategoryWhenCategoryValidationFails(final SubCategory subCategoryFromData, final String errorMessage) throws Exception {
        // GIVEN
        SubCategory subCategory = createSubCategoryWithExistingId(subCategoryFromData);
        SubCategoryRequestContext context = createContext(INCOME, subCategory);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(errorMessage));

    }

    @Test(dataProvider = "dataForContextValidation")
    public void testUpdateCategoryWhenContextValidationFails(final SubCategoryRequestContext context, final String errorMessage) throws Exception {
        // GIVEN
        SubCategory subCategoryToUpdate = createSubCategoryBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME).build();
        context.setSubCategory(subCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test
    public void testUpdateCategoryWhenCategoryIdIsNull() throws Exception {
        // GIVEN
        SubCategory subCategoryToUpdate = createSubCategoryBuilder(null, NEW_CATEGORY_NAME, INCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("SubCategory id cannot be null!"));
    }

    @Test
    public void testUpdateCategoryWhenCategoryCannotBeFoundInRepository() throws Exception {
        // GIVEN
        SubCategory subCategoryToUpdate = createSubCategoryBuilder(INVALID_ID, NEW_CATEGORY_NAME, INCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Original subCategory cannot be found in the repository!"));
    }

    @Test
    public void testUpdateCategoryWhenCategoryTypeHasChanged() throws Exception {
        // GIVEN
        SubCategory subCategoryToUpdate = createSubCategoryBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, OUTCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Transaction type cannot be changed!"));
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewName() throws Exception {
        // GIVEN
        SubCategory subCategoryToUpdate = createSubCategoryBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_CATEGORY_HAS_BEEN_UPDATED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory.id", Matchers.is(EXISTING_INCOME_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory.name", Matchers.is(NEW_CATEGORY_NAME)));
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewNameAndOtherTypeHasThisName() throws Exception {
        // GIVEN
        SubCategory subCategoryToUpdate = createSubCategoryBuilder(EXISTING_OUTCOME_ID, RESERVED_CATEGORY_NAME, OUTCOME).build();
        SubCategoryRequestContext context = createContext(OUTCOME, subCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_CATEGORY_HAS_BEEN_UPDATED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory.id", Matchers.is(EXISTING_OUTCOME_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory.name", Matchers.is(RESERVED_CATEGORY_NAME)));
    }

    @Test
    public void testUpdateCategorySaveWhenCategoryHasNewNameAndItIsReserved() throws Exception {
        // GIVEN
        SubCategory subCategoryToUpdate = createSubCategoryBuilder(EXISTING_INCOME_ID, OTHER_RESERVED_CATEGORY_NAME, INCOME).build();
        SubCategoryRequestContext context = createContext(INCOME, subCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(MockMvcResultMatchers.content().string("The category has been saved before!"));
    }

    private SubCategory createSubCategoryWithExistingId(final SubCategory subCategory) {
        return SubCategory.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(subCategory.getName())
            .withTransactionType(subCategory.getTransactionType())
            .build();
    }

}
