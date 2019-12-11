package hu.elte.bm.calculationservice.service.statistics.transactionprovider;

public class NotSupportedRateException extends RuntimeException {

    public NotSupportedRateException(final String message) {
        super(message);
    }

}
