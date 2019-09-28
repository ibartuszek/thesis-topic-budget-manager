package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;
import hu.elte.bm.transactionservice.service.transaction.TransactionService;
import hu.elte.bm.transactionservice.web.common.ContextTransformer;

@RestController
public class TransactionController {

    private static final String APPLICATION_JSON = "application/json";

    private final TransactionService transactionService;
    private final ContextTransformer transformer;

    @Value("${transaction.transaction_has_been_saved}")
    private String transactionHasBeenSaved;

    @Value("${transaction.transaction_has_been_updated}")
    private String transactionHasBeenUpdated;

    @Value("${transaction.transaction_has_been_deleted}")
    private String transactionHasBeenDeleted;

    public TransactionController(final TransactionService transactionService, final ContextTransformer transformer) {
        this.transactionService = transactionService;
        this.transformer = transformer;
    }

    @RequestMapping(value = "/bm/transactions/findAll", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public TransactionListResponse getTransactions(
        @RequestParam(value = "type") final TransactionType type,
        @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate start,
        @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate end,
        @RequestParam(value = "userId") final Long userId) {
        List<Transaction> transactionList = transactionService.getTransactionList(start, end, transformer.transform(type, userId));
        return TransactionListResponse.createSuccessfulSubCategoryResponse(transactionList);
    }

    @RequestMapping(value = "/bm/transactions/create", method = RequestMethod.POST, produces = APPLICATION_JSON)
    public TransactionResponse createTransaction(@RequestBody final TransactionRequestContext context) {
        TransactionContext transactionContext = transformer.transform(context);
        Transaction transaction = transactionService.save(context.getTransaction(), transactionContext);
        LocalDate firstPossibleDay = transactionService.getTheFirstDateOfTheNewPeriod(transactionContext);
        return TransactionResponse.createSuccessfulTransactionResponse(transaction, firstPossibleDay, transactionHasBeenSaved);
    }

    @RequestMapping(value = "/bm/transactions/update", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    public TransactionResponse updateTransaction(@RequestBody final TransactionRequestContext context) {
        TransactionContext transactionContext = transformer.transform(context);
        Transaction transaction = transactionService.update(context.getTransaction(), transactionContext);
        LocalDate firstPossibleDay = transactionService.getTheFirstDateOfTheNewPeriod(transactionContext);
        return TransactionResponse.createSuccessfulTransactionResponse(transaction, firstPossibleDay, transactionHasBeenUpdated);
    }

    @RequestMapping(value = "/bm/transactions/delete", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public TransactionResponse deleteTransaction(@RequestBody final TransactionRequestContext context) {
        TransactionContext transactionContext = transformer.transform(context);
        Transaction transaction = transactionService.delete(context.getTransaction(), transactionContext);
        LocalDate firstPossibleDay = transactionService.getTheFirstDateOfTheNewPeriod(transactionContext);
        return TransactionResponse.createSuccessfulTransactionResponse(transaction, firstPossibleDay, transactionHasBeenDeleted);
    }

}
