package hu.elte.bm.transactionservice.app.test.maincategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.util.HashSet;

import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryRequestContext;

public class SaveMainCategoryTest extends AbstractMainCategoryTest {

    private static final String URL = "/bm/mainCategories/create";

    @Test(dataProvider = "dataForMainCategoryModelValidation")
    public void testSaveCategoryWhenMainCategoryModelValidationFails(final MainCategory mainCategory, final String errorMessage) throws Exception {
        // GIVEN
        MainCategoryRequestContext context = createContext(INCOME, mainCategory);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test(dataProvider = "dataForContextValidation")
    public void testSaveCategoryWhenContextValidationFails(final MainCategoryRequestContext context, final String errorMessage) throws Exception {
        // GIVEN
        MainCategory mainCategoryToSave = createMainCategoryBuilder(null, NEW_CATEGORY_NAME, INCOME, new HashSet<>()).build();
        context.setMainCategory(mainCategoryToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test
    public void testSaveCategoryWhenCategoryHasASubCategoryWithoutId() throws Exception {
        // GIVEN
        MainCategory mainCategoryToSave = createMainCategoryBuilder(null, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        mainCategoryToSave.getSubCategorySet().add(createSubCategory(null, "illegal supplementary category", INCOME));
        MainCategoryRequestContext context = createContext(INCOME, mainCategoryToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("A subCategory does not have id!"));
    }

    @Test
    public void testSaveCategoryWhenCategoryIdIsNotNull() throws Exception {
        // GIVEN
        MainCategory mainCategoryToSave = createMainCategoryBuilder(INVALID_ID, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(INCOME, mainCategoryToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("New mainCategory id must be null!"));
    }

    @Test
    public void testSaveCategoryWhenCategoryHasNewName() throws Exception {
        // GIVEN
        MainCategory mainCategoryToSave = createMainCategoryBuilder(null, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(INCOME, mainCategoryToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_CATEGORY_HAS_BEEN_SAVED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.id", Matchers.is(NEW_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.name", Matchers.is(NEW_CATEGORY_NAME)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.transactionType", Matchers.is(INCOME.name())));
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithDifferentCategory() throws Exception {
        // GIVEN
        MainCategory mainCategoryToSave = createMainCategoryBuilder(null, RESERVED_CATEGORY_NAME, OUTCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(OUTCOME, mainCategoryToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_CATEGORY_HAS_BEEN_SAVED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.id", Matchers.is(NEW_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.name", Matchers.is(RESERVED_CATEGORY_NAME)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.transactionType", Matchers.is(OUTCOME.name())));
    }

    @Test
    public void testSaveCategoryWhenThereIsOneWithSameNameWithSameCategory() throws Exception {
        // GIVEN
        MainCategory mainCategoryToSave = createMainCategoryBuilder(null, RESERVED_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(TransactionType.INCOME, mainCategoryToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(MockMvcResultMatchers.content().string("The category has been saved before!"));
    }

}
