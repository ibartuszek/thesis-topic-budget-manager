package hu.elte.bm.transactionservice.dal.transaction;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;
import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.OUTCOME;

import java.time.ZoneId;
import java.util.Date;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntityTransformer;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntityTransformer;
import hu.elte.bm.transactionservice.dal.picture.PictureEntity;
import hu.elte.bm.transactionservice.dal.transaction.transactionEntityContext.TransactionEntityContext;
import hu.elte.bm.transactionservice.domain.transaction.Coordinate;
import hu.elte.bm.transactionservice.domain.transaction.Picture;
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
    IncomeEntity transformToIncomeEntity(final TransactionEntityContext transactionEntityContext) {
        Transaction transaction = transactionEntityContext.getTransaction();
        IncomeEntity incomeEntity = mapper.map(transformToTransactionEntity(transaction, transactionEntityContext.getUserId()), IncomeEntity.class);
        incomeEntity.setMainCategoryEntity(transactionEntityContext.getMainCategoryEntity());
        incomeEntity.setSubCategoryEntity(transactionEntityContext.getSubCategoryEntity());
        return incomeEntity;
    }

    // MainCategory and Subcategory are value classes and managed entities.
    OutcomeEntity transformToOutcomeEntity(final TransactionEntityContext transactionEntityContext) {
        Transaction transaction = transactionEntityContext.getTransaction();
        OutcomeEntity outcomeEntity = mapper.map(transformToTransactionEntity(transaction, transactionEntityContext.getUserId()), OutcomeEntity.class);
        if (transaction.getCoordinate() != null) {
            outcomeEntity.setLatitude(transaction.getCoordinate().getLatitude());
            outcomeEntity.setLongitude(transaction.getCoordinate().getLongitude());
        }
        outcomeEntity.setMainCategoryEntity(transactionEntityContext.getMainCategoryEntity());
        outcomeEntity.setSubCategoryEntity(transactionEntityContext.getSubCategoryEntity());
        outcomeEntity.setPictureEntity(transactionEntityContext.getPictureEntity());
        return outcomeEntity;
    }

    Transaction transformToTransaction(final IncomeEntity incomeEntity) {
        TransactionEntity transactionEntity = mapper.map(incomeEntity, TransactionEntity.class);
        return transformToTransaction(transactionEntity)
            .withTransactionType(INCOME)
            .build();
    }

    Transaction transformToTransaction(final OutcomeEntity outcomeEntity) {
        TransactionEntity transactionEntity = mapper.map(outcomeEntity, TransactionEntity.class);
        return transformToTransaction(transactionEntity)
            .withTransactionType(OUTCOME)
            .withCoordinate(getCoordinate(outcomeEntity))
            .withPicture(getPicture(outcomeEntity.getPictureEntity()))
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

    private Coordinate getCoordinate(final OutcomeEntity outcomeEntity) {
        Coordinate coordinate = null;
        if (outcomeEntity.getLatitude() != null && outcomeEntity.getLongitude() != null) {
            coordinate = Coordinate.builder()
                .withLatitude(outcomeEntity.getLatitude())
                .withLongitude(outcomeEntity.getLongitude())
                .build();
        }
        return coordinate;
    }

    private Picture getPicture(final PictureEntity pictureEntity) {
        Picture picture = null;
        if (pictureEntity != null) {
            picture = Picture.builder()
                .withId(pictureEntity.getId())
                .withPicture(pictureEntity.getPicture())
                .build();
        }
        return picture;
    }

}
