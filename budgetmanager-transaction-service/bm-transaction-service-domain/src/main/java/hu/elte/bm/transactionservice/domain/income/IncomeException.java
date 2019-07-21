package hu.elte.bm.transactionservice.domain.income;

public class IncomeException extends Exception {

    private final Income income;

    public IncomeException(final Income income, final String message) {
        super(message);
        this.income = income;
    }

    public IncomeException(final Income income, final String message,
        final Throwable throwable) {
        super(message, throwable);
        this.income = income;
    }

    public Income getIncome() {
        return income;
    }
}
