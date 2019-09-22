package hu.elte.bm.authenticationservice.web.errorhandling;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import hu.elte.bm.authenticationservice.domain.UserEmailIsReservedException;

@RestControllerAdvice
public class UserControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ IllegalArgumentException.class })
    protected ResponseEntity<Object> handleBadRequests(final RuntimeException e, final WebRequest request) {
        String bodyOfResponse = e.getMessage();
        return new ResponseEntity<>(bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String bodyOfResponse = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        // return this.handleExceptionInternal(ex, (Object)null, headers, status, request);
    }

    @ExceptionHandler(UserEmailIsReservedException.class)
    protected ResponseEntity<Object> handleConflicts(final RuntimeException e, final WebRequest request) {
        String bodyOfResponse = e.getMessage();
        return new ResponseEntity<Object>(bodyOfResponse, HttpStatus.CONFLICT);
    }

}
