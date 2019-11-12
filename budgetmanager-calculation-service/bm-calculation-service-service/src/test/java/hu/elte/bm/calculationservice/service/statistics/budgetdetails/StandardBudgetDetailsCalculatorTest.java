package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@ExtendWith(MockitoExtension.class)
class StandardBudgetDetailsCalculatorTest extends AbstractBudgetCalculatorTest {

    public static final double FIRST_MAIN_CATEGORY_SUM = 30.0d;
    public static final double FIRST_SUB_CATEGORY_SUM = 20.0d;
    @InjectMocks
    private StandardBudgetDetailsCalculator underTest;

    @Mock
    private BudgetCalculatorUtils utils;

    @Test
    public void testCalculateStandardDetails() {
        // GIVEN
        List<Transaction> incomeList = createExampleTransactionList(TransactionType.INCOME, DEFAULT_CURRENCY);
        List<Transaction> outcomeList = Collections.emptyList();
        // List<Transaction> outcomeList = createExampleOutcomeList();
        List<Transaction> filteredIncomeListOnFirstMainCategory = List.of(incomeList.get(0), incomeList.get(1));
        List<Transaction> filteredIncomeListOnFirstSubCategory = List.of(incomeList.get(1));
        List<Transaction> filteredIncomeListOnSecondMainCategory = List.of(incomeList.get(2));

        Mockito.when(utils.filterTransactionListOnMainCategory(incomeList.get(0).getMainCategory(), incomeList))
            .thenReturn(filteredIncomeListOnFirstMainCategory);
        Mockito.when(utils.getAmountFromTransactionList(filteredIncomeListOnFirstMainCategory)).thenReturn(FIRST_MAIN_CATEGORY_SUM);
        Mockito.when(utils.filterTransactionListOnSubCategory(incomeList.get(1).getSubCategory(), filteredIncomeListOnFirstMainCategory))
            .thenReturn(filteredIncomeListOnFirstSubCategory);
        Mockito.when(utils.getAmountFromTransactionList(filteredIncomeListOnFirstSubCategory)).thenReturn(FIRST_SUB_CATEGORY_SUM);

        Mockito.when(utils.filterTransactionListOnMainCategory(incomeList.get(2).getMainCategory(), incomeList))
            .thenReturn(filteredIncomeListOnSecondMainCategory);
        Mockito.when(utils.getZero()).thenReturn(ZERO);
        // WHEN
        var result = underTest.calculateStandardDetails(incomeList, outcomeList);
        // THEN

    }

    /*
    private List<TransactionData> createTransactionDataList(final List<Transaction> transactionList) {
        List<TransactionData> transactionDataList = new ArrayList<>();
        Set<MainCategory> mainCategorySet = transactionList.stream()
            .map(Transaction::getMainCategory)
            .collect(Collectors.toSet());
        for (MainCategory mainCategory : mainCategorySet) {
            List<Transaction> filteredTransactions = utils.filterTransactionListOnMainCategory(mainCategory, transactionList);
            double amount = utils.getAmountFromTransactionList(filteredTransactions);
            getTransactionData(mainCategory.getName(), amount).map(transactionDataList::add);
            addTransactionDataOfSubCategories(mainCategory, filteredTransactions, transactionDataList);
        }
        return transactionDataList;
    }
     */

}
