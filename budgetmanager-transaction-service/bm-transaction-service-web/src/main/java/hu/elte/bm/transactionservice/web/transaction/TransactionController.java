package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@RestController
public class TransactionController {

    private static final String APPLICATION_JSON = "application/json";

    private final TransactionModelService transactionModelService;

    public TransactionController(final TransactionModelService transactionModelService) {
        this.transactionModelService = transactionModelService;
    }

    @RequestMapping(value = "/bm/transactions/findAll", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public ResponseEntity<Object> getTransactions(@RequestParam(value = "type") final TransactionType type,
            @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate end) {
        TransactionModelRequestContext context = createContext(type, start, end);
        List<TransactionModel> transactionModelList = transactionModelService.findAll(context);
        return new ResponseEntity<>(transactionModelList, HttpStatus.OK);
    }

    @RequestMapping(value = "/bm/transactions/create", method = RequestMethod.POST, produces = APPLICATION_JSON)
    public ResponseEntity<Object> createTransaction(@RequestBody final TransactionModelRequestContext context) {
        TransactionModelResponse response;
        try {
            response = transactionModelService.saveTransaction(context);
        } catch (Exception e) {
            response = createErrorResponse(context, e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/bm/transactions/update", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    public ResponseEntity<Object> updateTransaction(@RequestBody final TransactionModelRequestContext context) {
        TransactionModelResponse response;
        try {
            response = transactionModelService.updateTransaction(context);
        } catch (Exception e) {
            response = createErrorResponse(context, e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/bm/transactions/delete", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public ResponseEntity<Object> deleteTransaction(@RequestBody final TransactionModelRequestContext context) {
        TransactionModelResponse response;
        try {
            response = transactionModelService.deleteTransaction(context);
        } catch (Exception e) {
            response = createErrorResponse(context, e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    private TransactionModelRequestContext createContext(final TransactionType type, final LocalDate start, final LocalDate end) {
        TransactionModelRequestContext context = new TransactionModelRequestContext();
        context.setTransactionType(type);
        context.setStart(start);
        context.setEnd(end);
        return context;
    }

    private TransactionModelResponse createErrorResponse(final TransactionModelRequestContext context, final Exception e) {
        TransactionModelResponse response = new TransactionModelResponse();
        response.setTransactionModel(context.getTransactionModel());
        response.setSuccessful(false);
        response.setMessage(e.getMessage());
        return response;
    }

}
