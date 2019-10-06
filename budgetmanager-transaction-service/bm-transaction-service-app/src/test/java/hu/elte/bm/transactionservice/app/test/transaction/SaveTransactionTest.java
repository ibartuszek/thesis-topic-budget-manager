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
import hu.elte.bm.transactionservice.domain.transaction.Picture;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
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
    public void testDeleteWhenTransactionIsNull() throws Exception {
        // GIVEN
        TransactionRequestContext context = createContext(INCOME, null);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Transaction cannot be null!"));
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
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_TRANSACTION_HAS_BEEN_SAVED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.id", Matchers.is(EXPECTED_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.title", Matchers.is(EXPECTED_TITLE)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.amount", Matchers.is(EXPECTED_AMOUNT)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.currency", Matchers.is(EXPECTED_CURRENCY.name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.transactionType", Matchers.is(INCOME.name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.mainCategory.id", Matchers.is(EXISTING_MAIN_CATEGORY_ID_2.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.date", Matchers.is(RESERVED_DATE.toString())));
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
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_TRANSACTION_HAS_BEEN_SAVED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.id", Matchers.is(EXPECTED_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.title", Matchers.is(RESERVED_TITLE)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.amount", Matchers.is(EXPECTED_AMOUNT)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.currency", Matchers.is(EXPECTED_CURRENCY.name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.transactionType", Matchers.is(INCOME.name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.mainCategory.id", Matchers.is(EXISTING_MAIN_CATEGORY_ID_2.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.date", Matchers.is(RESERVED_DATE.toString())));
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
        TransactionRequestContext context = createContext(INCOME, transactionToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(MockMvcResultMatchers.content().string("The transaction has been saved before!"));
    }

    @Test
    public void testSaveIncomeWhenOptionalFieldsPresent() throws Exception {
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
        TransactionRequestContext context = createContext(INCOME, transactionToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_TRANSACTION_HAS_BEEN_SAVED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.id", Matchers.is(EXPECTED_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.title", Matchers.is(EXPECTED_TITLE)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.amount", Matchers.is(EXPECTED_AMOUNT)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.currency", Matchers.is(EXPECTED_CURRENCY.name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.transactionType", Matchers.is(INCOME.name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.mainCategory.id", Matchers.is(EXISTING_MAIN_CATEGORY_ID_1.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.date", Matchers.is(EXPECTED_DATE.toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.monthly", Matchers.is(true)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.endDate", Matchers.is(EXPECTED_END_DATE.toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.description", Matchers.is(EXPECTED_DESCRIPTION)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.locked", Matchers.is(true)));
    }

    @Test
    public void testSaveOutcomeWhenOptionalFieldsPresent() throws Exception {
        // GIVEN
        MainCategory mainCategory = createDefaultMainCategoryForOutcome();
        Picture newPicture = Picture.builder()
            .withPicture(PICTURE_BYTES)
            .build();
        Transaction transactionToSave = createTransactionBuilderWithDefaultValues(mainCategory)
            .withId(null)
            .withTransactionType(OUTCOME)
            .withSubCategory(mainCategory.getSubCategorySet().iterator().next())
            .withEndDate(EXPECTED_END_DATE)
            .withDescription(EXPECTED_DESCRIPTION)
            .withMonthly(true)
            .withLocked(true)
            .withCoordinate(COORDINATE)
            .withPicture(newPicture)
            .build();
        TransactionRequestContext context = createContext(OUTCOME, transactionToSave);

        // WHEN
        ResultActions result = getMvc().perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestBody(context)));

        // THEN
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(THE_TRANSACTION_HAS_BEEN_SAVED)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.id", Matchers.is(EXPECTED_OUTCOME_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.title", Matchers.is(EXPECTED_TITLE)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.amount", Matchers.is(EXPECTED_AMOUNT)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.currency", Matchers.is(EXPECTED_CURRENCY.name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.transactionType", Matchers.is(OUTCOME.name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.mainCategory.id", Matchers.is(EXISTING_OUTCOME_MAIN_CATEGORY_ID_1.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.date", Matchers.is(EXPECTED_DATE.toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.monthly", Matchers.is(true)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.endDate", Matchers.is(EXPECTED_END_DATE.toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.description", Matchers.is(EXPECTED_DESCRIPTION)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.locked", Matchers.is(true)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.coordinate.latitude", Matchers.is(LATITUDE)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.coordinate.longitude", Matchers.is(LONGITUDE)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.picture.id", Matchers.is(PICTURE_ID.intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transaction.picture.picture", Matchers.is(EXPECTED_PICTURE_BYTES)));
    }

}
