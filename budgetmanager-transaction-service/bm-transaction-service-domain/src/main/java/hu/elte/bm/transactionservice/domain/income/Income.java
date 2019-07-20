package hu.elte.bm.transactionservice.domain.income;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;

public final class Income extends Transaction {

    private Income(final IncomeBuilder builder) {
        super(builder);
    }

    public static final class IncomeBuilder extends TransactionBuilder {

        private IncomeBuilder() {
        }

        public static IncomeBuilder newInstance() {
            return new IncomeBuilder();
        }

        @Override
        public Income build() {
            return new Income(this);
        }
    }
}
