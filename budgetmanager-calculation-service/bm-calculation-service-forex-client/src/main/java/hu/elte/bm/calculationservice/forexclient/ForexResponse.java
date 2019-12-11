package hu.elte.bm.calculationservice.forexclient;

public final class ForexResponse {

    private ForexRates rates;
    private Integer code;

    public ForexRates getRates() {
        return rates;
    }

    public void setRates(final ForexRates rates) {
        this.rates = rates;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }
}
