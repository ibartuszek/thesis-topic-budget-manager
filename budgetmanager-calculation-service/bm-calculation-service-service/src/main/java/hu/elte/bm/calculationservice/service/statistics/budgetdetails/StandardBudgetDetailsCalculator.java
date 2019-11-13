package hu.elte.bm.calculationservice.service.statistics.budgetdetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.budgetdetails.BudgetDetailsElement;
import hu.elte.bm.calculationservice.budgetdetails.TransactionData;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.Transaction;

@Component
public class StandardBudgetDetailsCalculator {

    private final BudgetCalculatorUtils utils;

    public StandardBudgetDetailsCalculator(final BudgetCalculatorUtils utils) {
        this.utils = utils;
    }

    public BudgetDetails calculateStandardDetails(final List<Transaction> incomeList, final List<Transaction> outcomeList) {
        List<TransactionData> incomes = createTransactionDataList(incomeList);
        List<TransactionData> outcomes = createTransactionDataList(outcomeList);
        BudgetDetailsElement totalIncomes = utils.createBudgetDetailsElement(getAmountFromTransactionDataList(incomes), utils.getTotalIncomesLabel());
        BudgetDetailsElement totalExpenses = utils.createBudgetDetailsElement(getAmountFromTransactionDataList(outcomes), utils.getTotalExpensesLabel());
        return BudgetDetails.builder()
            .withIncomes(incomes)
            .withOutcomes(outcomes)
            .withTotalIncomes(totalIncomes)
            .withTotalExpenses(totalExpenses)
            .withSavings(utils.createSavings(totalIncomes, totalExpenses))
            .build();
    }

    private List<TransactionData> createTransactionDataList(final List<Transaction> transactionList) {
        List<TransactionData> transactionDataList = new ArrayList<>();
        List<MainCategory> mainCategorySet = getMainCategoryList(transactionList);
        for (MainCategory mainCategory : mainCategorySet) {
            List<Transaction> filteredTransactions = utils.filterTransactionListOnMainCategory(mainCategory, transactionList);
            double amount = utils.getAmountFromTransactionList(filteredTransactions);
            getTransactionData(mainCategory.getName(), null, amount)
                .map(transactionDataList::add);
            addTransactionDataOfSubCategories(mainCategory, filteredTransactions, transactionDataList);
        }
        return transactionDataList;
    }

    private List<MainCategory> getMainCategoryList(final List<Transaction> transactionList) {
        List<MainCategory> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (!result.contains(transaction.getMainCategory())) {
                result.add(transaction.getMainCategory());
            }
        }
        return result;
    }

    private void addTransactionDataOfSubCategories(final MainCategory mainCategory, final List<Transaction> transactionList,
        final List<TransactionData> transactionDataList) {
        for (SubCategory subCategory :  mainCategory.getSubCategorySet()) {
            List<Transaction> filteredTransactions = utils.filterTransactionListOnSubCategory(subCategory, transactionList);
            double amount = utils.getAmountFromTransactionList(filteredTransactions);
            getTransactionData(mainCategory.getName(), subCategory.getName(), amount)
                .map(transactionDataList::add);
        }
    }

    private Optional<TransactionData> getTransactionData(final String mainCategoryName, final String subCategoryName, final double amount) {
        return amount == utils.getZero() ? Optional.empty()
            : Optional.of(TransactionData.builder()
            .withAmount(amount)
            .withMainCategoryName(mainCategoryName)
            .withSubCategoryName(subCategoryName)
            .build());
    }

    private Double getAmountFromTransactionDataList(final List<TransactionData> transactionDataList) {
        return transactionDataList.stream()
            .filter(transactionData -> transactionData.getSubCategoryName() == null)
            .map(TransactionData::getAmount)
            .reduce(Double::sum)
            .orElse(utils.getZero());
    }

}
