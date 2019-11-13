package hu.elte.bm.calculationservice.forexclient;

public class ForexRates {

    private ForexRate USDEUR;
    private ForexRate USDHUF;

    public ForexRate getUSDEUR() {
        return USDEUR;
    }

    public void setUSDEUR(final ForexRate USDEUR) {
        this.USDEUR = USDEUR;
    }

    public ForexRate getUSDHUF() {
        return USDHUF;
    }

    public void setUSDHUF(final ForexRate USDHUF) {
        this.USDHUF = USDHUF;
    }

    @Override
    public String toString() {
        return "ForexResponse{"
            + "USDEUR=" + USDEUR
            + ", USDHUF=" + USDHUF
            + '}';
    }

}
