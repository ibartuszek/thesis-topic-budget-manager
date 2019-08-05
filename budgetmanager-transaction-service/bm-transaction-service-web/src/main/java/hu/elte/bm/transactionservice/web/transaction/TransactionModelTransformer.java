package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelAmountValue;
import hu.elte.bm.transactionservice.web.common.ModelDateValue;
import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModel;
import hu.elte.bm.transactionservice.web.maincategory.MainCategoryModelTransformer;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModelTransformer;

@Component
public class TransactionModelTransformer {

    private static final Integer TRANSACTION_TITLE_MAXIMUM_LENGTH = 100;
    private static final Integer TRANSACTION_DESCRIPTION_MAXIMUM_LENGTH = 100;

    private final MainCategoryModelTransformer mainCategoryModelTransformer;
    private final SubCategoryModelTransformer subCategoryModelTransformer;

    TransactionModelTransformer(final MainCategoryModelTransformer mainCategoryModelTransformer,
        final SubCategoryModelTransformer subCategoryModelTransformer) {
        this.mainCategoryModelTransformer = mainCategoryModelTransformer;
        this.subCategoryModelTransformer = subCategoryModelTransformer;
    }

    TransactionModel transformToTransactionModel(final Transaction transaction, final LocalDate firstPossibleDay) {
        ModelStringValue title = ModelStringValue.builder()
            .withValue(transaction.getTitle())
            .build();
        ModelAmountValue amount = ModelAmountValue.builder()
            .withValue(transaction.getAmount())
            .build();
        ModelStringValue currency = ModelStringValue.builder()
            .withValue(transaction.getCurrency().name())
            .build();
        ModelStringValue type = ModelStringValue.builder()
            .withValue(transaction.getTransactionType().name())
            .build();
        MainCategoryModel mainCategory = mainCategoryModelTransformer.transformToMainCategoryModel(transaction.getMainCategory());
        SubCategoryModel subCategory = transaction.getSubCategory() == null ? null
            : subCategoryModelTransformer.transformToSubCategoryModel(transaction.getSubCategory());
        ModelDateValue date = ModelDateValue.builder()
            .withValue(transaction.getDate())
            .build();
        ModelDateValue endDate = transaction.getEndDate() == null ? null
            : ModelDateValue.builder()
            .withValue(transaction.getEndDate())
            .build();
        ModelStringValue description = transaction.getDescription() == null ? null
            : ModelStringValue.builder()
            .withValue(transaction.getDescription())
            .build();

        TransactionModel transactionModel = TransactionModel.builder()
            .withId(transaction.getId())
            .withTitle(title)
            .withAmount(amount)
            .withCurrency(currency)
            .withTransactionType(type)
            .withMainCategory(mainCategory)
            .withSubCategory(subCategory)
            .withDate(date)
            .withEndDate(endDate)
            .withDescription(description)
            .withMonthly(transaction.isMonthly())
            .withLocked(transaction.isLocked())
            .build();
        populateValidationFields(transactionModel, firstPossibleDay);
        return transactionModel;
    }

    Transaction transformToTransaction(final TransactionModel transactionModel) {
        return Transaction.builder()
            .withId(transactionModel.getId())
            .withTitle(transactionModel.getTitle().getValue())
            .withAmount(transactionModel.getAmount().getValue())
            .withCurrency(Currency.valueOf(transactionModel.getCurrency().getValue()))
            .withTransactionType(TransactionType.valueOf(transactionModel.getTransactionType().getValue()))
            .withMainCategory(mainCategoryModelTransformer.transformToMainCategory(transactionModel.getMainCategory()))
            .withSubCategory(transactionModel.getSubCategory() == null ? null
                : subCategoryModelTransformer.transformToSubCategory(transactionModel.getSubCategory()))
            .withDate(transactionModel.getDate().getValue())
            .withEndDate(transactionModel.getEndDate() == null ? null : transactionModel.getEndDate().getValue())
            .withDescription(transactionModel.getDescription() == null ? null : transactionModel.getDescription().getValue())
            .withMonthly(transactionModel.isMonthly())
            .withLocked(transactionModel.isLocked())
            .build();
    }

    void populateValidationFields(final TransactionModel transactionModel, final LocalDate firstPossibleDay) {
        transactionModel.getTitle().setMaximumLength(TRANSACTION_TITLE_MAXIMUM_LENGTH);
        transactionModel.getAmount().setPositive(true);
        transactionModel.getCurrency().setPossibleEnumValues(Arrays.stream(Currency.values()).map(Currency::name).collect(Collectors.toSet()));
        transactionModel.getTransactionType().setPossibleEnumValues(Arrays.stream(TransactionType.values()).map(TransactionType::name).collect(Collectors.toSet()));
        transactionModel.getDate().setPossibleFirstDay(firstPossibleDay);
        if (transactionModel.getDescription() != null) {
            transactionModel.getDescription().setMaximumLength(TRANSACTION_DESCRIPTION_MAXIMUM_LENGTH);
        }
    }

}