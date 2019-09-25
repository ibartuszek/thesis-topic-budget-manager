package hu.elte.bm.transactionservice.web.transaction;

import hu.elte.bm.commonpack.validator.ModelAmountValue;
import hu.elte.bm.commonpack.validator.ModelDateValue;
import hu.elte.bm.commonpack.validator.ModelStringValue;

public final class TransactionModel {

    private Long id;
    private ModelStringValue title;
    private ModelAmountValue amount;
    private ModelStringValue currency;
    private ModelStringValue transactionType;
    private MainCategoryModel mainCategory;
    private SubCategoryModel subCategory;
    private boolean monthly;
    private ModelDateValue date;
    private ModelDateValue endDate;
    private ModelStringValue description;
    private boolean locked;

    private TransactionModel() {
    }

    private TransactionModel(final Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.transactionType = builder.transactionType;
        this.mainCategory = builder.mainCategory;
        this.subCategory = builder.subCategory;
        this.monthly = builder.monthly;
        this.date = builder.date;
        this.endDate = builder.endDate;
        this.description = builder.description;
        this.locked = builder.locked;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public ModelStringValue getTitle() {
        return title;
    }

    public void setTitle(final ModelStringValue title) {
        this.title = title;
    }

    public ModelAmountValue getAmount() {
        return amount;
    }

    public void setAmount(final ModelAmountValue amount) {
        this.amount = amount;
    }

    public ModelStringValue getCurrency() {
        return currency;
    }

    public void setCurrency(final ModelStringValue currency) {
        this.currency = currency;
    }

    public ModelStringValue getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(final ModelStringValue transactionType) {
        this.transactionType = transactionType;
    }

    public MainCategoryModel getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(final MainCategoryModel mainCategory) {
        this.mainCategory = mainCategory;
    }

    public SubCategoryModel getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(final SubCategoryModel subCategory) {
        this.subCategory = subCategory;
    }

    public boolean isMonthly() {
        return monthly;
    }

    public void setMonthly(final boolean monthly) {
        this.monthly = monthly;
    }

    public ModelDateValue getDate() {
        return date;
    }

    public void setDate(final ModelDateValue date) {
        this.date = date;
    }

    public ModelDateValue getEndDate() {
        return endDate;
    }

    public void setEndDate(final ModelDateValue endDate) {
        this.endDate = endDate;
    }

    public ModelStringValue getDescription() {
        return description;
    }

    public void setDescription(final ModelStringValue description) {
        this.description = description;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(final boolean locked) {
        this.locked = locked;
    }

    public static final class Builder {

        private Long id;
        private ModelStringValue title;
        private ModelAmountValue amount;
        private ModelStringValue currency;
        private ModelStringValue transactionType;
        private MainCategoryModel mainCategory;
        private SubCategoryModel subCategory;
        private boolean monthly;
        private ModelDateValue date;
        private ModelDateValue endDate;
        private ModelStringValue description;
        private boolean locked;

        private Builder() {
        }

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(final ModelStringValue title) {
            this.title = title;
            return this;
        }

        public Builder withAmount(final ModelAmountValue amount) {
            this.amount = amount;
            return this;
        }

        public Builder withCurrency(final ModelStringValue currency) {
            this.currency = currency;
            return this;
        }

        public Builder withTransactionType(final ModelStringValue transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder withMainCategory(final MainCategoryModel mainCategory) {
            this.mainCategory = mainCategory;
            return this;
        }

        public Builder withSubCategory(final SubCategoryModel subCategory) {
            this.subCategory = subCategory;
            return this;
        }

        public Builder withMonthly(final boolean monthly) {
            this.monthly = monthly;
            return this;
        }

        public Builder withDate(final ModelDateValue date) {
            this.date = date;
            return this;
        }

        public Builder withEndDate(final ModelDateValue endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder withDescription(final ModelStringValue description) {
            this.description = description;
            return this;
        }

        public Builder withLocked(final boolean locked) {
            this.locked = locked;
            return this;
        }

        public TransactionModel build() {
            return new TransactionModel(this);
        }

    }

}
