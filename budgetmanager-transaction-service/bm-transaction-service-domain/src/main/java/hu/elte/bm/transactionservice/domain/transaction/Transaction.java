package hu.elte.bm.transactionservice.domain.transaction;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;

public abstract class Transaction {

    private final Long id;
    @NotBlank
    private final String title;
    @Positive
    private final double amount;
    @NotNull
    private final Currency currency;
    @NotNull
    private final MainCategory mainCategory;
    private final SubCategory subCategory;
    private final boolean monthly;
    @NotNull
    private final LocalDate date;
    private final LocalDate endDate;
    private final String description;
    private final boolean locked;

    protected Transaction(final TransactionBuilder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.mainCategory = builder.mainCategory;
        this.subCategory = builder.subCategory;
        this.monthly = builder.monthly;
        this.date = builder.date;
        this.endDate = builder.endDate;
        this.description = builder.description;
        this.locked = builder.locked;
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
            && mainCategory.equals(that.mainCategory)
            && Objects.equals(subCategory, that.subCategory)
            && date.equals(that.date)
            && Objects.equals(endDate, that.endDate)
            && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, amount, currency, mainCategory, subCategory, monthly, date, endDate, description, locked);
    }

    public abstract static class TransactionBuilder<B extends TransactionBuilder> {
        private Long id;
        private String title;
        private double amount;
        private Currency currency;
        private MainCategory mainCategory;
        private SubCategory subCategory;
        private boolean monthly;
        private LocalDate date;
        private LocalDate endDate;
        private String description;
        private boolean locked;

        protected TransactionBuilder() {
        }

        protected TransactionBuilder(final Transaction transaction) {
            this.id = transaction.id;
            this.title = transaction.title;
            this.amount = transaction.amount;
            this.currency = transaction.currency;
            this.mainCategory = transaction.mainCategory;
            this.subCategory = transaction.subCategory;
            this.monthly = transaction.monthly;
            this.date = transaction.date;
            this.endDate = transaction.endDate;
            this.description = transaction.description;
            this.locked = transaction.locked;
        }

        @SuppressWarnings("unchecked")
        final B self() {
            return (B) this;
        }

        public B withId(final Long id) {
            this.id = id;
            return self();
        }

        public B withTitle(final String title) {
            this.title = title;
            return self();
        }

        public B withAmount(final double amount) {
            this.amount = amount;
            return self();
        }

        public B withCurrency(final Currency currency) {
            this.currency = currency;
            return self();
        }

        public B withMainCategory(final MainCategory mainCategory) {
            this.mainCategory = mainCategory;
            return self();
        }

        public B withSubCategory(final SubCategory subCategory) {
            this.subCategory = subCategory;
            return self();
        }

        public B withMonthly(final boolean monthly) {
            this.monthly = monthly;
            return self();
        }

        public B withDate(final LocalDate date) {
            this.date = date;
            return self();
        }

        public B withEndDate(final LocalDate endDate) {
            this.endDate = endDate;
            return self();
        }

        public B withDescription(final String description) {
            this.description = description;
            return self();
        }

        public B withLocked(final boolean locked) {
            this.locked = locked;
            return self();
        }

        public abstract Transaction build();
    }
}
