package hu.elte.bm.calculationservice.service.statistics.transactionprovider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.forexclient.Rate;
import hu.elte.bm.transactionservice.Currency;
import hu.elte.bm.transactionservice.Transaction;

@Component
public class TransactionCurrencyExchanger {

    @Value("${statistics.currency_pair_is_not_supported:{0} and {1} currency pair are not supported!}")
    private String notSupportedCurrencyMessage;

    public Transaction exchangeCurrency(final Transaction transaction, final Currency newCurrency,
        final List<Rate> exchangeRates) {
        Rate rate = getRate(transaction.getCurrency(), newCurrency, exchangeRates)
            .orElseThrow(() -> new NotSupportedRateException(MessageFormat.format(notSupportedCurrencyMessage, transaction.getCurrency(), newCurrency)));
        Double newAmount = transaction.getAmount() * rate.getRate();
        return createNewTransaction(transaction, newCurrency, newAmount);
    }

    private Optional<Rate> getRate(final Currency originalCurrency, final Currency newCurrency, final List<Rate> exchangeRates) {
        return exchangeRates.stream()
            .filter(rate -> rate.getOriginalCurrency().equals(originalCurrency))
            .filter(rate -> rate.getNewCurrency().equals(newCurrency))
            .findAny();
    }

    private Transaction createNewTransaction(final Transaction transaction, final Currency newCurrency, final Double newAmount) {
        return Transaction.builder()
            .withId(transaction.getId())
            .withTitle(transaction.getTitle())
            .withTransactionType(transaction.getTransactionType())
            .withAmount(newAmount)
            .withCurrency(newCurrency)
            .withMainCategory(transaction.getMainCategory())
            .withSubCategory(transaction.getSubCategory())
            .withDate(transaction.getDate())
            .withMonthly(transaction.isMonthly())
            .withEndDate(transaction.getEndDate())
            .withLocked(transaction.isLocked())
            .withDescription(transaction.getDescription())
            .withCoordinate(transaction.getCoordinate())
            .withPictureId(transaction.getPictureId())
            .build();
    }
}
