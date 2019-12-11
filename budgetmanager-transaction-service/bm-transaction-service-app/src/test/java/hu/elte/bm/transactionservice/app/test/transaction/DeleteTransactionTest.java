package hu.elte.bm.transactionservice.app.test.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.web.transaction.TransactionRequestContext;

public class DeleteTransactionTest extends AbstractTransactionTest {

    private static final String URL = "/bm/transactions/delete";

    @Test(dataProvider = "dataForContextValidation")
    public void testDeleteCategoryWhenContextValidationFails(final TransactionRequestContext context, final String errorMessage) throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToDelete = createTransactionBuilderWithDefaultValues(mainCategory).build();
        context.setTransaction(transactionToDelete);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.delete(URL)
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
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.delete(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Transaction cannot be null!"));
    }

    @Test
    public void testDeleteWhenTransactionDoesNotHaveId() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToDelete = createTransactionBuilderWithValuesForUpdate(mainCategory).withId(null).build();
        TransactionRequestContext context = createContext(INCOME, transactionToDelete);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.delete(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Transaction id cannot be null!"));
    }

    @Test
    public void testDeleteWhenTransactionCannotBeFound() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToDelete = createTransactionBuilderWithValuesForUpdate(mainCategory)
            .withId(INVALID_ID)
            .withTitle(EXPECTED_TITLE)
            .build();
        TransactionRequestContext context = createContext(INCOME, transactionToDelete);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.delete(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Original transaction cannot be found in the repository!"));
    }

    @Test
    public void testDeleteWhenTransactionIsLocked() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToDelete = createTransactionBuilderWithValuesForUpdate(mainCategory)
            .withId(LOCKED_ID)
            .withTitle(EXPECTED_TITLE)
            .build();
        TransactionRequestContext context = createContext(INCOME, transactionToDelete);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.delete(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Transaction is locked, cannot be deleted!"));
    }

    @Test
    public void testDelete() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToDelete = createTransactionBuilderWithValuesForUpdate(mainCategory).build();
        TransactionRequestContext context = createContext(INCOME, transactionToDelete);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.delete(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("The transaction has been deleted.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.id", Matchers.is(RESERVED_ID.intValue())));
    }

}
