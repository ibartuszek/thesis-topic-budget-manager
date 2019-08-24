package hu.elte.bm.transactionservice.dal.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.time.ZoneId;
import java.util.Date;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntityTransformer;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntityTransformer;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;

@Component
public class TransactionEntityTransformer {

    private final MainCategoryEntityTransformer mainCategoryEntityTransformer;
    private final SubCategoryEntityTransformer subCategoryEntityTransformer;
    private final DozerBeanMapper mapper;

    public TransactionEntityTransformer(final MainCategoryEntityTransformer mainCategoryEntityTransformer,
        final SubCategoryEntityTransformer subCategoryEntityTransformer, final DozerBeanMapper mapper) {
        this.mainCategoryEntityTransformer = mainCategoryEntityTransformer;
        this.subCategoryEntityTransformer = subCategoryEntityTransformer;
        this.mapper = mapper;
    }

    // MainCategory and Subcategory are value classes and managed entities.
    public IncomeEntity transformToIncomeEntity(final Transaction transaction, final MainCategoryEntity mainCategoryEntity,
        final SubCategoryEntity subCategoryEntity, final Long userId) {
        IncomeEntity incomeEntity = mapper.map(transformToTransactionEntity(transaction, userId), IncomeEntity.class);
        incomeEntity.setMainCategoryEntity(mainCategoryEntity);
        incomeEntity.setSubCategoryEntity(subCategoryEntity);
        return incomeEntity;
    }

    // MainCategory and Subcategory are value classes and managed entities.
    public OutcomeEntity transformToOutcomeEntity(final Transaction transaction, final MainCategoryEntity mainCategoryEntity,
        final SubCategoryEntity subCategoryEntity, final Long userId) {
        OutcomeEntity outcomeEntity = mapper.map(transformToTransactionEntity(transaction, userId), OutcomeEntity.class);
        outcomeEntity.setMainCategoryEntity(mainCategoryEntity);
        outcomeEntity.setSubCategoryEntity(subCategoryEntity);
        return outcomeEntity;
    }

    public Transaction transformToTransaction(final IncomeEntity incomeEntity) {
        TransactionEntity transactionEntity = mapper.map(incomeEntity, TransactionEntity.class);
        return transformToTransaction(transactionEntity)
            .withTransactionType(INCOME)
            .build();
    }

    public Transaction transformToTransaction(final OutcomeEntity outcomeEntity) {
        TransactionEntity transactionEntity = mapper.map(outcomeEntity, TransactionEntity.class);
        return transformToTransaction(transactionEntity)
            .withTransactionType(OUTCOME)
            .build();
    }

    private TransactionEntity transformToTransactionEntity(final Transaction transaction, final Long userId) {
        return TransactionEntity.builder()
            .withId(transaction.getId())
            .withTitle(transaction.getTitle())
            .withAmount(transaction.getAmount())
            .withCurrency(transaction.getCurrency())
            .withDate(Date.from(transaction.getDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
            .withEndDate(transaction.getEndDate() == null ? null
                : Date.from(transaction.getEndDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
            .withMonthly(transaction.isMonthly())
            .withDescription(transaction.getDescription())
            .withLocked(transaction.isLocked())
            .withUserId(userId)
            .build();
    }

    private Transaction.Builder transformToTransaction(final TransactionEntity transactionEntity) {
        return Transaction.builder()
            .withId(transactionEntity.getId())
            .withTitle(transactionEntity.getTitle())
            .withAmount(transactionEntity.getAmount())
            .withCurrency(transactionEntity.getCurrency())
            .withMainCategory(mainCategoryEntityTransformer.transformToMainCategory(transactionEntity.getMainCategoryEntity()))
            .withSubCategory(transactionEntity.getSubCategoryEntity() == null ? null
                : subCategoryEntityTransformer.transformToSubCategory(transactionEntity.getSubCategoryEntity()))
            .withDate(transactionEntity.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
            .withEndDate(transactionEntity.getEndDate() == null ? null
                : transactionEntity.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
            .withMonthly(transactionEntity.isMonthly())
            .withDescription(transactionEntity.getDescription())
            .withLocked(transactionEntity.isLocked());
    }

}
