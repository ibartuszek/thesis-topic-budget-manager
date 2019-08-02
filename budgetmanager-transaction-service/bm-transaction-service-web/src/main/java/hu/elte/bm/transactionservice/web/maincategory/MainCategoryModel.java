package hu.elte.bm.transactionservice.web.maincategory;

import java.util.Set;

import hu.elte.bm.transactionservice.web.common.ModelStringValue;
import hu.elte.bm.transactionservice.web.subcategory.SubCategoryModel;

public final class MainCategoryModel {

    private Long id;
    private ModelStringValue name;
    private ModelStringValue transactionType;
    private Set<SubCategoryModel> subCategoryModelSet;

    private MainCategoryModel() {
    }

    private MainCategoryModel(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.transactionType = builder.transactionType;
        this.subCategoryModelSet = builder.subCategoryModelSet;
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

    public Set<SubCategoryModel> getSubCategoryModelSet() {
        return subCategoryModelSet;
    }

    public void setSubCategoryModelSet(final Set<SubCategoryModel> subCategoryModelSet) {
        this.subCategoryModelSet = subCategoryModelSet;
    }

    public static final class Builder {

        private Long id;
        private ModelStringValue name;
        private ModelStringValue transactionType;
        private Set<SubCategoryModel> subCategoryModelSet;

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

        public Builder withSubCategoryModelSet(final Set<SubCategoryModel> subCategoryModelSet) {
            this.subCategoryModelSet = subCategoryModelSet;
            return this;
        }

        public MainCategoryModel build() {
            return new MainCategoryModel(this);
        }

    }

}
