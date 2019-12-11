package hu.elte.bm.calculationservice.forexclient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ForexRates that = (ForexRates) o;

        return new EqualsBuilder()
                .append(USDEUR, that.USDEUR)
                .append(USDHUF, that.USDHUF)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(USDEUR)
                .append(USDHUF)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ForexResponse{"
                + "USDEUR=" + USDEUR
                + ", USDHUF=" + USDHUF
                + '}';
    }

}
