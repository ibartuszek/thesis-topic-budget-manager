package hu.elte.bm.transactionservice.domain.transaction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;

public final class Transaction {

    private final Long id;
    @NotBlank
    private final String title;
    @Positive
    private final double amount;
    @NotNull
    private final Currency currency;
    @NotNull
    private final TransactionType transactionType;
    @NotNull
    private final MainCategory mainCategory;
    private final SubCategory subCategory;
    private final boolean monthly;
    @NotNull
    @PastOrPresent
    private final LocalDate date;
    private final LocalDate endDate;
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
            && locked == that.locked
            && title.equals(that.title)
            && currency == that.currency
            && transactionType == that.transactionType
            && mainCategory.equals(that.mainCategory)
            && Objects.equals(subCategory, that.subCategory)
            && date.equals(that.date)
            && Objects.equals(endDate, that.endDate)
            && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, amount, currency, transactionType, mainCategory, subCategory, monthly, date, endDate, description, locked);
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
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Transaction transaction = new Transaction(this);
            Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
            if (!violations.isEmpty()) {
                ConstraintViolationException wrappedException = new ConstraintViolationException(
                    new HashSet<ConstraintViolation<?>>(violations));
                throw new TransactionException(transaction, "invalid transaction", wrappedException);
            }
            return transaction;
        }
    }
}
