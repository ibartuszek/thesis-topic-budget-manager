package hu.elte.bm.transactionservice.web.subcategory;

import hu.elte.bm.commonpack.validator.ModelStringValue;

public final class SubCategoryModel {

    private Long id;
    private ModelStringValue name;
    private ModelStringValue transactionType;

    private SubCategoryModel() {
    }

    private SubCategoryModel(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.transactionType = builder.transactionType;
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

    public ModelStringValue getName() {
        return name;
    }

    public void setName(final ModelStringValue name) {
        this.name = name;
    }

    public ModelStringValue getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(final ModelStringValue transactionType) {
        this.transactionType = transactionType;
    }

    public static final class Builder {

        private Long id;
        private ModelStringValue name;
        private ModelStringValue transactionType;

        private Builder() {
        }

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(final ModelStringValue name) {
            this.name = name;
            return this;
        }

        public Builder withTransactionType(final ModelStringValue transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public SubCategoryModel build() {
            return new SubCategoryModel(this);
        }

    }

}
