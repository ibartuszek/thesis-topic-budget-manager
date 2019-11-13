package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.budgetdetails.BudgetDetailsElement;
import hu.elte.bm.calculationservice.budgetdetails.TransactionData;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

class StandardBudgetDetailsCalculatorTest extends AbstractBudgetCalculatorTest {

    private static final Double DEFAULT_MAIN_CATEGORY_SUM_AMOUNT = DEFAULT_TRANSACTION_AMOUNT + OTHER_TRANSACTION_AMOUNT;
    private static final Double EXPECTED_TOTAL_INCOMES_AMOUNT = EXPECTED_SUM_AMOUNT;
    private static final Double EXPECTED_TOTAL_EXPENSE_AMOUNT = DEFAULT_TRANSACTION_AMOUNT + OTHER_TRANSACTION_AMOUNT;
    private static final Double EXPECTED_SAVINGS_AMOUNT = EXPECTED_TOTAL_INCOMES_AMOUNT - EXPECTED_TOTAL_EXPENSE_AMOUNT;
    private StandardBudgetDetailsCalculator underTest = new StandardBudgetDetailsCalculator(new BudgetCalculatorUtils());

    @Test
    public void testCalculateStandardDetails() {
        // GIVEN
        List<Transaction> incomeList = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomeList = createExampleOutcomeList();
        List<TransactionData> expectedIncomes = createExpectedIncomesDataLis();
        List<TransactionData> expectedOutcomes = createExpectedOutcomesDataLis();
        BudgetDetailsElement expectedTotalIncomes = createBudgetDetailsElement(EXPECTED_TOTAL_INCOMES_AMOUNT, TOTAL_INCOMES_LABEL);
        BudgetDetailsElement expectedTotalExpenses = createBudgetDetailsElement(EXPECTED_TOTAL_EXPENSE_AMOUNT, TOTAL_EXPENSES_LABEL);
        BudgetDetailsElement expectedSavings = createBudgetDetailsElement(EXPECTED_SAVINGS_AMOUNT, SAVINGS_LABEL);
        BudgetDetails expected = BudgetDetails.builder()
                .withIncomes(expectedIncomes)
                .withOutcomes(expectedOutcomes)
                .withTotalIncomes(expectedTotalIncomes)
                .withTotalExpenses(expectedTotalExpenses)
                .withSavings(expectedSavings)
                .build();

        // WHEN
        var result = underTest.calculateStandardDetails(incomeList, outcomeList);

        // THEN
        Assertions.assertEquals(expected, result);
    }

    private List<TransactionData> createExpectedIncomesDataLis() {
        TransactionData first = TransactionData.builder()
                .withAmount(DEFAULT_MAIN_CATEGORY_SUM_AMOUNT)
                .withMainCategoryName(DEFAULT_MAIN_CATEGORY_NAME)
                .build();
        TransactionData second = TransactionData.builder()
                .withAmount(OTHER_TRANSACTION_AMOUNT)
                .withMainCategoryName(DEFAULT_MAIN_CATEGORY_NAME)
                .withSubCategoryName(DEFAULT_SUB_CATEGORY_NAME)
                .build();
        TransactionData third = TransactionData.builder()
                .withAmount(ANOTHER_TRANSACTION_AMOUNT)
                .withMainCategoryName(ANOTHER_MAIN_CATEGORY_NAME)
                .build();
        return List.of(first, second, third);
    }

    private List<TransactionData> createExpectedOutcomesDataLis() {
        TransactionData first = TransactionData.builder()
                .withAmount(DEFAULT_MAIN_CATEGORY_SUM_AMOUNT)
                .withMainCategoryName(DEFAULT_MAIN_CATEGORY_NAME)
                .build();
        TransactionData second = TransactionData.builder()
                .withAmount(OTHER_TRANSACTION_AMOUNT)
                .withMainCategoryName(DEFAULT_MAIN_CATEGORY_NAME)
                .withSubCategoryName(DEFAULT_SUB_CATEGORY_NAME)
                .build();
        return List.of(first, second);
    }

}
