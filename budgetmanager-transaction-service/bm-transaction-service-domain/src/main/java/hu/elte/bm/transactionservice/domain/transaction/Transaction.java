package hu.elte.bm.transactionservice.domain.transaction;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;

@JsonDeserialize(builder = Transaction.Builder.class)
public final class Transaction {

    private static final int MAXIMUM_TITLE_LENGTH = 50;
    private static final int MAXIMUM_DESCRIPTION_LENGTH = 100;

    private final Long id;
    @NotEmpty(message = "Title cannot be empty!")
    @Length(max = MAXIMUM_TITLE_LENGTH, message = "Title must be shorter than 50 characters!")
    private final String title;
    @Positive(message = "Amount must be positive!")
    private final double amount;
    @NotNull(message = "Currency cannot be null!")
    private final Currency currency;
    @NotNull(message = "Type cannot be null!")
    private final TransactionType transactionType;
    @NotNull(message = "Main category cannot be null!")
    @Valid
    private final MainCategory mainCategory;
    @Valid
    private final SubCategory subCategory;
    private final boolean monthly;
    @NotNull(message = "Date cannot be null!")
    private final LocalDate date;
    private final LocalDate endDate;
    @Length(min = 1, max = MAXIMUM_DESCRIPTION_LENGTH, message = "Description must be shorter than 100 characters!")
    private final String description;
    private final boolean locked;

    private Transaction(final Builder builder) {
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

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public boolean isMonthly() {
        return monthly;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0
            && monthly == that.monthly
            && title.equals(that.title)
            && currency == that.currency
            && transactionType == that.transactionType
            && mainCategory.equals(that.mainCategory)
            && (subCategory == null || subCategory.equals(that.subCategory))
            && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, amount, currency, transactionType, mainCategory, subCategory, monthly, date);
    }

    public static class Builder {
        private Long id;
        private String title;
        private double amount;
        private Currency currency;
        private TransactionType transactionType;
        private MainCategory mainCategory;
        private SubCategory subCategory;
        private boolean monthly;
        private LocalDate date;
        private LocalDate endDate;
        private String description;
        private boolean locked;

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

        public Builder withTransactionType(final TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder withMainCategory(final MainCategory mainCategory) {
            this.mainCategory = mainCategory;
            return this;
        }

        public Builder withSubCategory(final SubCategory subCategory) {
            this.subCategory = subCategory;
            return this;
        }

        public Builder withMonthly(final boolean monthly) {
            this.monthly = monthly;
            return this;
        }

        public Builder withDate(final LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder withEndDate(final LocalDate endDate) {
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

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
