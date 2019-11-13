package hu.elte.bm.calculationservice.web.errorhandling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import hu.elte.bm.calculationservice.exceptions.schema.IllegalStatisticsSchemaException;
import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaConflictException;
import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaNotFoundException;
import hu.elte.bm.calculationservice.forexclient.ForexClientException;
import hu.elte.bm.calculationservice.transactionserviceclient.exceptions.TransactionServiceException;
import hu.elte.bm.transactionservice.exceptions.maincategory.IllegalMainCategoryException;
import hu.elte.bm.transactionservice.exceptions.maincategory.MainCategoryNotFoundException;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ IllegalArgumentException.class, IllegalStatisticsSchemaException.class,
        IllegalMainCategoryException.class })
    protected ResponseEntity<Object> handleBadRequests(final RuntimeException e, final WebRequest request) {
        String bodyOfResponse = e.getMessage();
        return new ResponseEntity<>(bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        String bodyOfResponse = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StatisticsSchemaConflictException.class)
    protected ResponseEntity<Object> handleConflicts(final RuntimeException e, final WebRequest request) {
        String bodyOfResponse = e.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ StatisticsSchemaNotFoundException.class, MainCategoryNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(final RuntimeException e, final WebRequest request) {
        String bodyOfResponse = e.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ ForexClientException.class, TransactionServiceException.class })
    protected ResponseEntity<Object> handleServiceNotAvailable(final RuntimeException e, final WebRequest request) {
        String bodyOfResponse = e.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
