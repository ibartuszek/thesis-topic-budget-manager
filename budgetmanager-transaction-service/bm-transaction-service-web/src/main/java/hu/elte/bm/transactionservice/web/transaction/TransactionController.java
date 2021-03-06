package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import hu.elte.bm.transactionservice.web.common.ResponseModel;

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

    @Value("${transaction.transactions_has_been_locked}")
    private String transactionsLocked;

    public TransactionController(final TransactionService transactionService, final ContextTransformer transformer) {
        this.transactionService = transactionService;
        this.transformer = transformer;
    }

    @RequestMapping(value = "/bm/transactions/findAll", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public TransactionListResponse getTransactions(
        @NotNull @RequestParam(value = "type") final TransactionType type,
        @NotNull @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate start,
        @NotNull @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate end,
        @NotNull @RequestParam(value = "userId") final Long userId) {
        List<Transaction> transactionList = transactionService.getTransactionList(start, end, transformer.transform(type, userId));
        return TransactionListResponse.createSuccessfulSubCategoryResponse(transactionList);
    }

    @RequestMapping(value = "/bm/transactions/create", method = RequestMethod.POST, produces = APPLICATION_JSON)
    public TransactionResponse createTransaction(@Valid @RequestBody final TransactionRequestContext context) {
        TransactionContext transactionContext = transformer.transform(context);
        Transaction transaction = transactionService.save(context.getTransaction(), transactionContext);
        return TransactionResponse.createSuccessfulTransactionResponse(transaction, transactionHasBeenSaved);
    }

    @RequestMapping(value = "/bm/transactions/update", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    public TransactionResponse updateTransaction(@Valid @RequestBody final TransactionRequestContext context) {
        TransactionContext transactionContext = transformer.transform(context);
        Transaction transaction = transactionService.update(context.getTransaction(), transactionContext);
        return TransactionResponse.createSuccessfulTransactionResponse(transaction, transactionHasBeenUpdated);
    }

    @RequestMapping(value = "/bm/transactions/delete", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public TransactionResponse deleteTransaction(@Valid @RequestBody final TransactionRequestContext context) {
        TransactionContext transactionContext = transformer.transform(context);
        Transaction transaction = transactionService.delete(context.getTransaction(), transactionContext);
        return TransactionResponse.createSuccessfulTransactionResponse(transaction, transactionHasBeenDeleted);
    }

    @RequestMapping(value = "/bm/transactions/getFirstPossibleDay")
    public FirstPossibleDayResponse getFirstPossibleDay(@NotNull @RequestParam(value = "userId") final Long userId) {
        return FirstPossibleDayResponse.createSuccessfulFirstPossibleDayResponse(transactionService.getTheFirstDateOfTheNewPeriod(userId));
    }

    @RequestMapping(value = "/bm/transactions/lockTransactions")
    public ResponseModel lockTransactions(
        @NotNull @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate end,
        @NotNull @RequestParam(value = "userId") final Long userId) {
        transactionService.lockTransactions(end, transformer.transform(TransactionType.INCOME, userId));
        transactionService.lockTransactions(end, transformer.transform(TransactionType.OUTCOME, userId));
        return ResponseModel.createSuccessfulResponse(transactionsLocked);
    }
}
