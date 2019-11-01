package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;

import hu.elte.bm.transactionservice.web.common.ResponseModel;

public final class FirstPossibleDayResponse extends ResponseModel {

    private LocalDate firstPossibleDay;

    private FirstPossibleDayResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    static FirstPossibleDayResponse createSuccessfulFirstPossibleDayResponse(final LocalDate firstPossibleDay) {
        return createSuccessfulFirstPossibleDayResponse(firstPossibleDay, null);
    }

    static FirstPossibleDayResponse createSuccessfulFirstPossibleDayResponse(final LocalDate firstPossibleDay, final String message) {
        FirstPossibleDayResponse response = new FirstPossibleDayResponse(message, true);
        response.firstPossibleDay = firstPossibleDay;
        return response;
    }

    public LocalDate getFirstPossibleDay() {
        return firstPossibleDay;
    }

    public void setFirstPossibleDay(final LocalDate firstPossibleDay) {
        this.firstPossibleDay = firstPossibleDay;
    }

    @Override
    public String toString() {
        return "FirstPossibleDayResponse{"
            + "message='" + getMessage() + '\''
            + ", successful=" + isSuccessful()
            + ", firstPossibleDay=" + firstPossibleDay
            + '}';
    }

}
