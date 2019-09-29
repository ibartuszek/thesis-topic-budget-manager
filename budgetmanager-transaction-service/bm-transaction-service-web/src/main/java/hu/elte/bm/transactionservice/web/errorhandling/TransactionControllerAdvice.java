package hu.elte.bm.transactionservice.web.errorhandling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import hu.elte.bm.transactionservice.domain.categories.MainCategoryConflictException;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryNotFoundException;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryConflictException;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryNotFoundException;
import hu.elte.bm.transactionservice.domain.transaction.TransactionConflictException;
import hu.elte.bm.transactionservice.domain.transaction.TransactionNotFoundException;

@RestControllerAdvice
public class TransactionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
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

    @ExceptionHandler({ SubCategoryConflictException.class, MainCategoryConflictException.class, TransactionConflictException.class })
    protected ResponseEntity<Object> handleConflicts(final RuntimeException e, final WebRequest request) {
        String bodyOfResponse = e.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ SubCategoryNotFoundException.class, MainCategoryNotFoundException.class, TransactionNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(final RuntimeException e, final WebRequest request) {
        String bodyOfResponse = e.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

}
