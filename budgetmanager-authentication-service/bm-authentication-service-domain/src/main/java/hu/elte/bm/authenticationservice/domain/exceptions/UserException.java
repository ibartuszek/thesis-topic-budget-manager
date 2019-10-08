package hu.elte.bm.authenticationservice.domain.exceptions;

import hu.elte.bm.authenticationservice.domain.User;

public interface UserException {

    String getMessage();

    StackTraceElement[] getStackTrace();

    void printStackTrace();

    User getUser();

}
