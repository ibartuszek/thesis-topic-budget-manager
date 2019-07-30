package hu.elte.bm.transactionservice.dal.transaction;

import java.util.Date;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntity;
import hu.elte.bm.transactionservice.domain.Currency;

public class TransactionEntity {

    private Long id;
    private String title;
    private double amount;
    private Currency currency;
    private MainCategoryEntity mainCategoryEntity;
    private SubCategoryEntity subCategoryEntity;
    private boolean monthly;
    private Date date;
    private Date endDate;
    private String description;
    private boolean locked;

    public TransactionEntity() {
    }

    private TransactionEntity(final Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.mainCategoryEntity = builder.mainCategoryEntity;
        this.subCategoryEntity = builder.subCategoryEntity;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public MainCategoryEntity getMainCategoryEntity() {
        return mainCategoryEntity;
    }

    public void setMainCategoryEntity(final MainCategoryEntity mainCategoryEntity) {
        this.mainCategoryEntity = mainCategoryEntity;
    }

    public SubCategoryEntity getSubCategoryEntity() {
        return subCategoryEntity;
    }

    public void setSubCategoryEntity(final SubCategoryEntity subCategoryEntity) {
        this.subCategoryEntity = subCategoryEntity;
    }

    public boolean isMonthly() {
        return monthly;
    }

    public void setMonthly(final boolean monthly) {
        this.monthly = monthly;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
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
        private String title;
        private double amount;
        private Currency currency;
        private MainCategoryEntity mainCategoryEntity;
        private SubCategoryEntity subCategoryEntity;
        private boolean monthly;
        private Date date;
        private Date endDate;
        private String description;
        private boolean locked;

        private Builder() {
        }

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder withAmount(final double amount) {
            this.amount = amount;
            return this;
        }

        public Builder withCurrency(final Currency currency) {
            this.currency = currency;
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

        public Builder withMonthly(final boolean monthly) {
            this.monthly = monthly;
            return this;
        }

        public Builder withDate(final Date date) {
            this.date = date;
            return this;
        }

        public Builder withEndDate(final Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public Builder withLocked(final boolean locked) {
            this.locked = locked;
            return this;
        }

        public TransactionEntity build() {
            return new TransactionEntity(this);
        }
    }
}
