package hu.elte.bm.transactionservice.domain.income;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;

public final class Income extends Transaction {

    private Income(final IncomeBuilder builder) {
        super(builder);
    }

    public static IncomeBuilder builder() {
        return new IncomeBuilder();
    }

    public static IncomeBuilder builder(final Income income) {
        return new IncomeBuilder(income);
    }

    public static final class IncomeBuilder extends TransactionBuilder<IncomeBuilder> {

        private IncomeBuilder() {
            super();
        }

        private IncomeBuilder(final Income income) {
            super(income);
        }

        @Override
        public Income build() {
            return new Income(this);
        }
    }
}
