package hu.elte.bm.transactionservice.app.test.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.web.transaction.TransactionRequestContext;

public class UpdateTransactionTest extends AbstractTransactionTest {

    private static final String URL = "/bm/transactions/update";

    @Test(dataProvider = "dataForTransactionModelValidation")
    public void testUpdateWhenTransactionValidationFails(final Transaction.Builder builder, final String errorMessage) throws Exception {
        // GIVEN
        Transaction transaction = builder
                .withId(EXPECTED_ID)
                .build();
        TransactionRequestContext context = createContext(INCOME, transaction);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test(dataProvider = "dataForContextValidation")
    public void testUpdateCategoryWhenContextValidationFails(final TransactionRequestContext context, final String errorMessage) throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithDefaultValues(mainCategory).build();
        context.setTransaction(transactionToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test
    public void testDeleteWhenTransactionIsNull() throws Exception {
        // GIVEN
        TransactionRequestContext context = createContext(INCOME, null);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Transaction cannot be null!"));
    }

    @Test
    public void testUpdateWhenTransactionDoesNotHaveId() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategory)
                .withId(null)
                .build();
        TransactionRequestContext context = createContext(INCOME, transactionToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Transaction id cannot be null!"));
    }

    @Test
    public void testUpdateWhenTransactionCannotBeFound() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategory)
            .withId(INVALID_ID)
            .withTitle(EXPECTED_TITLE)
            .build();
        TransactionRequestContext context = createContext(INCOME, transactionToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Original transaction cannot be found in the repository!"));
    }

    @Test
    public void testUpdateWhenTransactionIsLocked() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategory)
            .withId(LOCKED_ID)
            .withTitle(EXPECTED_TITLE)
            .build();
        TransactionRequestContext context = createContext(INCOME, transactionToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Transaction is locked, cannot be changed!"));
    }

    @Test
    public void testUpdateWhenTransactionTypeChanged() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategory)
            .withTitle(EXPECTED_TITLE)
            .withTransactionType(OUTCOME)
            .build();
        TransactionRequestContext context = createContext(INCOME, transactionToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Transaction type cannot be changed!"));
    }

    @Test
    public void testUpdateWhenThereIsOneTransactionWithSameDateAndNameAndMainCategoryAndSubCategory() throws Exception {
        // GIVEN
        MainCategory mainCategoryModel = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategoryModel)
            .withTitle(RESERVED_TITLE_2)
            .withSubCategory(mainCategoryModel.getSubCategorySet().iterator().next())
            .withDate(RESERVED_DATE)
            .build();
        TransactionRequestContext context = createContext(INCOME, transactionToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("The transaction has been saved before!"));
    }

    @Test
    public void testUpdate() throws Exception {
        // GIVEN
        MainCategory mainCategoryModel = createDefaultMainCategory();
        Transaction transactionToUpdate = createTransactionBuilderWithValuesForUpdate(mainCategoryModel)
            .withTitle(EXPECTED_TITLE)
            .build();
        TransactionRequestContext context = createContext(INCOME, transactionToUpdate);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("The transaction has been updated.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.id", Matchers.is(RESERVED_ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.title", Matchers.is(EXPECTED_TITLE)));
    }

}
