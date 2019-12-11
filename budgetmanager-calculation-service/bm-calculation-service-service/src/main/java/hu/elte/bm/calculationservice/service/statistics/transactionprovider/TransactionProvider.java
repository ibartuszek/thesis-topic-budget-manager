package hu.elte.bm.calculationservice.service.statistics.transactionprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final MonthlyTransactionProvider monthlyTransactionProvider;

    public TransactionProvider(final TransactionServiceFacade transactionServiceFacade, final ForexClient forexClient,
            final TransactionCurrencyExchanger exchanger, final MonthlyTransactionProvider monthlyTransactionProvider) {
        this.transactionServiceFacade = transactionServiceFacade;
        this.forexClient = forexClient;
        this.exchanger = exchanger;
        this.monthlyTransactionProvider = monthlyTransactionProvider;
    }

    public List<Transaction> getTransactions(final TransactionType type, final TransactionProviderContext context) {
        List<Transaction> result;
        List<Transaction> transactionListWithNewCurrency = new ArrayList<>();
        List<Transaction> transactionList = transactionServiceFacade.getTransactions(type, context.getUserId(), context.getStart(), context.getEnd());
        List<Rate> exchangeRates = forexClient.getExchangeRates();
        transactionList.forEach(transaction -> transactionListWithNewCurrency
            .add(exchanger.exchangeCurrency(transaction, context.getCurrency(), exchangeRates)));
        result = transactionListWithNewCurrency.stream()
            .filter(transaction -> !transaction.isMonthly())
            .collect(Collectors.toList());
        List<Transaction> monthlyTransactionList = monthlyTransactionProvider.provide(transactionListWithNewCurrency, context.getStart(), context.getEnd());
        result.addAll(monthlyTransactionList);
        return result;
    }

}
