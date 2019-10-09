package hu.elte.bm.transactionservice.dal.transaction.transactionEntityContext;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntity;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;

public final class TransactionEntityContext {

    private final Transaction transaction;
    private final Long userId;
    private final MainCategoryEntity mainCategoryEntity;
    private final SubCategoryEntity subCategoryEntity;

    private TransactionEntityContext(final Builder builder) {
        this.transaction = builder.transaction;
        this.userId = builder.userId;
        this.mainCategoryEntity = builder.mainCategoryEntity;
        this.subCategoryEntity = builder.subCategoryEntity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Long getUserId() {
        return userId;
    }

    public MainCategoryEntity getMainCategoryEntity() {
        return mainCategoryEntity;
    }

    public SubCategoryEntity getSubCategoryEntity() {
        return subCategoryEntity;
    }


    public static final class Builder {
        private Transaction transaction;
        private Long userId;
        private MainCategoryEntity mainCategoryEntity;
        private SubCategoryEntity subCategoryEntity;

        private Builder() {
        }

        public Builder withTransaction(final Transaction transaction) {
            this.transaction = transaction;
            return this;
        }

        public Builder withUserId(final Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withMainCategoryEntity(final MainCategoryEntity mainCategoryEntity) {
            this.mainCategoryEntity = mainCategoryEntity;
            return this;
        }

        public Builder withSubCategoryEntity(final SubCategoryEntity subCategoryEntity) {
            this.subCategoryEntity = subCategoryEntity;
            return this;
        }

        public TransactionEntityContext build() {
            return new TransactionEntityContext(this);
        }
    }

}
