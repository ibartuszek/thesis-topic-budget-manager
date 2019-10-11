package hu.elte.bm.transactionservice;

public enum Currency {
    HUF("Huf"),
    USD("Usd"),
    EUR("Eur");

    private final String currencyName;

    Currency(String currencyName) {
        this.currencyName = currencyName;
    }

}
