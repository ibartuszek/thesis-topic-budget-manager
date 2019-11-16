package hu.elte.bm.calculationservice.service.statistics.transactionprovider;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import hu.elte.bm.calculationservice.forexclient.ForexClient;
import hu.elte.bm.calculationservice.forexclient.Rate;
import hu.elte.bm.calculationservice.transactionserviceclient.TransactionServiceFacade;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class TransactionProvider {

    private final TransactionServiceFacade transactionServiceFacade;
    private final ForexClient forexClient;
    private final TransactionCurrencyExchanger exchanger;

    public TransactionProvider(final TransactionServiceFacade transactionServiceFacade, final ForexClient forexClient,
        final TransactionCurrencyExchanger exchanger) {
        this.transactionServiceFacade = transactionServiceFacade;
        this.forexClient = forexClient;
        this.exchanger = exchanger;
    }

    public List<Transaction> getTransactions(final TransactionType type, final TransactionProviderContext context) {
        List<Transaction> result = new ArrayList<>();
        List<Transaction> transactionList = transactionServiceFacade.getTransactions(type, context.getUserId(), context.getStart(), context.getEnd());
        List<Rate> exchangeRates = forexClient.getExchangeRates();
        transactionList.forEach(transaction -> result.add(exchanger.exchangeCurrency(transaction, context.getCurrency(), exchangeRates)));
        return result;
    }
}
