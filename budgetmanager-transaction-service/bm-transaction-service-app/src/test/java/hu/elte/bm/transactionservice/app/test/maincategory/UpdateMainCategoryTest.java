package hu.elte.bm.transactionservice.app.test.maincategory;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryRequestContext;

public class UpdateMainCategoryTest extends AbstractMainCategoryTest {

    private static final String URL = "/bm/mainCategories/update";
    private static final Long NEW_SUBCATEGORY_ID = 3L;
    private static final String NEW_SUBCATEGORY_NAME = "supplementary category 3";

    @Test(dataProvider = "dataForMainCategoryModelValidation")
    public void testUpdateCategoryWhenMainCategoryModelValidationFails(final MainCategory mainCategoryModelFromData, final String errorMessage)
        throws Exception {
        // GIVEN
        MainCategory mainCategoryModel = createMainCategoryWithExistingId(mainCategoryModelFromData);
        MainCategoryRequestContext context = createContext(INCOME, mainCategoryModel);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test(dataProvider = "dataForContextValidation")
    public void testUpdateCategoryWhenContextValidationFails(final MainCategoryRequestContext context, final String errorMessage) throws Exception {
        // GIVEN
        MainCategory mainCategoryToSave = createMainCategoryBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME, new HashSet<>()).build();
        context.setMainCategory(mainCategoryToSave);

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
        MainCategory mainCategoryToUpdate = createMainCategoryBuilder(null, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(INCOME, mainCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("MainCategory id cannot be null!"));
    }

    @Test
    public void testUpdateCategoryWhenCategoryTypeIsChanged() throws Exception {
        // GIVEN
        MainCategory mainCategoryToUpdate =
            createMainCategoryBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, OUTCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Transaction type cannot be changed!"));
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasOneSubCategoryWithoutId() throws Exception {
        // GIVEN
        MainCategory mainCategoryToUpdate =
            createMainCategoryBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        mainCategoryToUpdate.getSubCategorySet().add(createSubCategory(null, "illegal supplementary category", INCOME));
        MainCategoryRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("A subCategory does not have id!"));
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasLessSubCategory() throws Exception {
        // GIVEN
        Set<SubCategory> modifiedSet = Set.of(createSubCategory(1L, "supplementary category 1", INCOME));
        MainCategory mainCategoryToUpdate = createMainCategoryBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME, modifiedSet).build();
        MainCategoryRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("New mainCategory does not contain all original subCategory!"));
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewSubCategory() throws Exception {
        // GIVEN
        MainCategory mainCategoryToUpdate =
            createMainCategoryBuilder(EXISTING_INCOME_ID, RESERVED_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        mainCategoryToUpdate.getSubCategorySet().add(createSubCategory(NEW_SUBCATEGORY_ID, NEW_SUBCATEGORY_NAME, INCOME));
        MainCategoryRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_CATEGORY_HAS_BEEN_SAVED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.id", Matchers.is(EXISTING_INCOME_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.name", Matchers.is(RESERVED_CATEGORY_NAME)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.transactionType", Matchers.is(INCOME.name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.subCategorySet[*].id", Matchers.hasItem(NEW_SUBCATEGORY_ID.intValue())));
    }

    @Test
    public void testUpdateCategoryWhenCategoryCannotBeFoundInTheRepository() throws Exception {
        // GIVEN
        MainCategory mainCategoryToUpdate = createMainCategoryBuilder(INVALID_ID, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Original mainCategory cannot be found in the repository!"));
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewName() throws Exception {
        // GIVEN
        MainCategory mainCategoryToUpdate =
            createMainCategoryBuilder(EXISTING_INCOME_ID, NEW_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_CATEGORY_HAS_BEEN_SAVED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.id", Matchers.is(EXISTING_INCOME_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.name", Matchers.is(NEW_CATEGORY_NAME)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.transactionType", Matchers.is(INCOME.name())));
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewNameAndOtherTypeHasThisName() throws Exception {
        // GIVEN
        MainCategory mainCategoryToUpdate =
            createMainCategoryBuilder(EXISTING_OUTCOME_ID, RESERVED_CATEGORY_NAME, OUTCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(OUTCOME, mainCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_CATEGORY_HAS_BEEN_SAVED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.id", Matchers.is(EXISTING_OUTCOME_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.name", Matchers.is(RESERVED_CATEGORY_NAME)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory.transactionType", Matchers.is(OUTCOME.name())));
    }

    @Test
    public void testUpdateCategoryWhenCategoryHasNewNameAndItIsReserved() throws Exception {
        // GIVEN
        MainCategory mainCategoryToUpdate =
            createMainCategoryBuilder(EXISTING_INCOME_ID, OTHER_RESERVED_CATEGORY_NAME, INCOME, createSubCategoryModelSet()).build();
        MainCategoryRequestContext context = createContext(TransactionType.INCOME, mainCategoryToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(MockMvcResultMatchers.content().string("The category has been saved before!"));
    }

    private MainCategory createMainCategoryWithExistingId(final MainCategory mainCategoryModelFromData) {
        return MainCategory.builder()
            .withId(EXISTING_INCOME_ID)
            .withName(mainCategoryModelFromData.getName())
            .withTransactionType(mainCategoryModelFromData.getTransactionType())
            .withSubCategorySet(mainCategoryModelFromData.getSubCategorySet())
            .build();
    }

}
