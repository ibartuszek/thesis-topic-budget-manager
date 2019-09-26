package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.commonpack.validator.ModelAmountValue;
import hu.elte.bm.commonpack.validator.ModelDateValue;
import hu.elte.bm.commonpack.validator.ModelStringValue;
import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Component
public class TransactionModelTransformer {

    private final MainCategoryModelTransformer mainCategoryModelTransformer;
    private final SubCategoryModelTransformer subCategoryModelTransformer;

    @Value("${transaction.endDate.minimum_extra_days_to_date}")
    private Integer minimumDaysToAddToStartDate;

    @Value("${transaction.title.maximum_length}")
    private Integer transactionTitleMaximumLength;

    @Value("${transaction.description.maximum_length}")
    private Integer transactionDescriptionMaximumLength;

    TransactionModelTransformer(final MainCategoryModelTransformer mainCategoryModelTransformer,
        final SubCategoryModelTransformer subCategoryModelTransformer) {
        this.mainCategoryModelTransformer = mainCategoryModelTransformer;
        this.subCategoryModelTransformer = subCategoryModelTransformer;
    }

    Transaction transformToTransactionModel(final hu.elte.bm.transactionservice.domain.transaction.Transaction transaction, final LocalDate firstPossibleDay) {
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

        Transaction transactionModel = Transaction.builder()
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

    hu.elte.bm.transactionservice.domain.transaction.Transaction transformToTransaction(final Transaction transaction) {
        return hu.elte.bm.transactionservice.domain.transaction.Transaction.builder()
            .withId(transaction.getId())
            .withTitle(transaction.getTitle().getValue())
            .withAmount(transaction.getAmount().getValue())
            .withCurrency(Currency.valueOf(transaction.getCurrency().getValue()))
            .withTransactionType(TransactionType.valueOf(transaction.getTransactionType().getValue()))
            .withMainCategory(mainCategoryModelTransformer.transformToMainCategory(transaction.getMainCategory()))
            .withSubCategory(transaction.getSubCategory() == null ? null
                : subCategoryModelTransformer.transformToSubCategory(transaction.getSubCategory()))
            .withDate(transaction.getDate().getValue())
            .withEndDate(transaction.getEndDate() == null ? null : transaction.getEndDate().getValue())
            .withDescription(transaction.getDescription() == null ? null : transaction.getDescription().getValue())
            .withMonthly(transaction.isMonthly())
            .withLocked(transaction.isLocked())
            .build();
    }

    void populateValidationFields(final Transaction transaction, final LocalDate firstPossibleDay) {
        transaction.getTitle().setMaximumLength(transactionTitleMaximumLength);
        transaction.getAmount().setPositive(true);
        transaction.getCurrency().setPossibleEnumValues(Arrays.stream(Currency.values()).map(Currency::name).collect(Collectors.toSet()));
        transaction.getTransactionType()
            .setPossibleEnumValues(Arrays.stream(TransactionType.values()).map(TransactionType::name).collect(Collectors.toSet()));
        transaction.getDate().setPossibleFirstDay(firstPossibleDay);
        if (transaction.getDescription() != null) {
            transaction.getDescription().setMaximumLength(transactionDescriptionMaximumLength);
        }
        if (transaction.getEndDate() != null) {
            transaction.getEndDate().setPossibleFirstDay(transaction.getDate().getValue().plusDays(minimumDaysToAddToStartDate));
        }
    }

}
