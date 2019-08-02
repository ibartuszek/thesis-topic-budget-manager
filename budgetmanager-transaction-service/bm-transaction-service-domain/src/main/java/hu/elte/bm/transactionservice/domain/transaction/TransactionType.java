package hu.elte.bm.transactionservice.domain.transaction;

import java.util.HashSet;
import java.util.Set;

public enum TransactionType {
    INCOME,
    OUTCOME;

    private static final Set<String> POSSIBLE_VALUES;

    static {
        POSSIBLE_VALUES = new HashSet<>();
        POSSIBLE_VALUES.add(INCOME.name());
        POSSIBLE_VALUES.add(OUTCOME.name());
    }

    public static Set<String> getPossibleValues() {
        return POSSIBLE_VALUES;
    }
}
