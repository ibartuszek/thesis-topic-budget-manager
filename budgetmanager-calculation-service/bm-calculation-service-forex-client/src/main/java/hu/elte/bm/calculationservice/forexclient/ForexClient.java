package hu.elte.bm.calculationservice.forexclient;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.Currency;

@Component
public class ForexClient {

    private final ForexProxy proxy;

    ForexClient(final ForexProxy proxy) {
        this.proxy = proxy;
    }

    public List<Rate> getExchangeRates() {
        List<Rate> exchangeRates = new ArrayList<>();
        ForexRates rates = proxy.getForexResponse().getRates();
        exchangeRates.add(createRate(Currency.USD, Currency.EUR, rates.getUSDEUR().getRate()));
        exchangeRates.add(createRate(Currency.EUR, Currency.USD, 1 / rates.getUSDEUR().getRate()));
        exchangeRates.add(createRate(Currency.USD, Currency.HUF, rates.getUSDHUF().getRate()));
        exchangeRates.add(createRate(Currency.HUF, Currency.USD, 1 / rates.getUSDHUF().getRate()));
        return exchangeRates;
    }

    private Rate createRate(final Currency originalCurrency, final Currency newCurrency, final double rate) {
        return Rate.builder()
            .withOriginalCurrency(originalCurrency)
            .withNewCurrency(newCurrency)
            .withRate(rate)
            .build();
    }

}
