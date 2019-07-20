package hu.elte.bm.transactionservice.domain.transaction;

import java.time.LocalDate;

import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;

public class Transaction {

    private final Long id;
    private final String title;
    private final double amount;
    private final Currency currency;
    private final MainCategory mainCategory;
    private final SubCategory subCategory;
    private final boolean monthly;
    private final LocalDate date;
    private final LocalDate endDate;
    private final String description;

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

    public static class TransactionBuilder {
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

        public TransactionBuilder withId(final Long id) {
            this.id = id;
            return this;
        }

        public TransactionBuilder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public TransactionBuilder withAmount(final double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder withCurrency(final Currency currency) {
            this.currency = currency;
            return this;
        }

        public TransactionBuilder withMainCategory(final MainCategory mainCategory) {
            this.mainCategory = mainCategory;
            return this;
        }

        public TransactionBuilder withSubCategory(final SubCategory subCategory) {
            this.subCategory = subCategory;
            return this;
        }

        public TransactionBuilder withMonthly(final boolean monthly) {
            this.monthly = monthly;
            return this;
        }

        public TransactionBuilder withDate(final LocalDate date) {
            this.date = date;
            return this;
        }

        public TransactionBuilder withEndDate(final LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public TransactionBuilder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
