package hu.elte.bm.transactionservice.domain;

import java.util.HashSet;
import java.util.Set;

public enum Currency {
    HUF("Huf"),
    USD("Usd"),
    EUR("Eur");

    private static final Set<String> POSSIBLE_VALUES;

    static {
        POSSIBLE_VALUES = new HashSet<>();
        POSSIBLE_VALUES.add(HUF.name());
        POSSIBLE_VALUES.add(USD.name());
        POSSIBLE_VALUES.add(EUR.name());
    }

    private final String currencyName;

    Currency(String currencyName) {
        this.currencyName = currencyName;
    }

    public static Set<String> getPossibleValues() {
        return POSSIBLE_VALUES;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

}
