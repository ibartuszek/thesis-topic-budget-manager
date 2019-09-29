package hu.elte.bm.transactionservice.app.test.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.transaction.TransactionRequestContext;

public class SaveTransactionTest extends AbstractTransactionTest {

    private static final String URL = "/bm/transactions/create";

    @Test(dataProvider = "dataForTransactionModelValidation")
    public void testSaveWhenTransactionValidationFails(final Transaction.Builder builder, final String errorMessage) throws Exception {
        // GIVEN
        Transaction transaction = builder
            .withId(null)
            .build();
        TransactionRequestContext context = createContext(INCOME, transaction);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test(dataProvider = "dataForContextValidation")
    public void testSaveCategoryWhenContextValidationFails(final TransactionRequestContext context, final String errorMessage) throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToSave = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(null)
            .build();
        context.setTransaction(transactionToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test
    public void testSaveWhenIdIsNotNull() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToSave = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(RESERVED_ID)
            .build();
        TransactionRequestContext context = createContext(INCOME, transactionToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("New transaction id must be null!"));
    }

    @Test
    public void testSaveWhenThereIsOneWithSameDateAndType() throws Exception {
        // GIVEN
        MainCategory mainCategory = createMainCategory(EXISTING_MAIN_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        Transaction transactionToSave = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(null)
            .withDate(RESERVED_DATE)
            .build();
        TransactionRequestContext context = createContext(INCOME, transactionToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        // TODO:
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    public void testSaveWhenThereIsOneWithSameDateAndTypeAndTitle() throws Exception {
        // GIVEN
        MainCategory mainCategory = createMainCategory(EXISTING_MAIN_CATEGORY_ID_2, EXISTING_MAIN_CATEGORY_NAME_2, INCOME);
        Transaction transactionToSave = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(null)
            .withTitle(RESERVED_TITLE)
            .withDate(RESERVED_DATE)
            .build();
        TransactionRequestContext context = createContext(INCOME, transactionToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        // TODO:
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(""));

        //        Assert.assertTrue(response.isSuccessful());
        //        Assert.assertEquals(response.getMessage(), THE_TRANSACTION_HAS_BEEN_SAVED);
        //        Assert.assertEquals(response.getTransaction().getId(), EXPECTED_ID);
    }

    @Test
    public void testSaveWhenThereIsOneWithSameDateAndTypeAndTitleAndMainCategory() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToSave = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(null)
            .withTitle(RESERVED_TITLE)
            .withDate(RESERVED_DATE)
            .build();
        TransactionRequestContext context = createContext(TransactionType.INCOME, transactionToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(""));

        //        Assert.assertFalse(response.isSuccessful());
        //        Assert.assertEquals(response.getMessage(), "The transaction has been saved before.");
    }

    @Test
    public void testSaveWhenOptionalFieldsPresent() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategory();
        Transaction transactionToSave = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(null)
            .withSubCategory(mainCategory.getSubCategorySet().iterator().next())
            .withEndDate(EXPECTED_END_DATE)
            .withDescription(EXPECTED_DESCRIPTION)
            .withMonthly(true)
            .withLocked(true)
            .build();
        TransactionRequestContext context = createContext(TransactionType.INCOME, transactionToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("End of the periodical transaction cannot be before its start!"));

        //        Assert.assertTrue(response.isSuccessful());
        //        Assert.assertEquals(response.getMessage(), THE_TRANSACTION_HAS_BEEN_SAVED);
        //        Assert.assertEquals(response.getTransaction().getId(), EXPECTED_ID);
    }

}
