package hu.elte.bm.calculationservice.service.statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class RounderUtil {

    private static final int PLACES = 2;

    public double round(final double amount) {
        return BigDecimal.valueOf(amount)
            .setScale(PLACES, RoundingMode.HALF_UP)
            .doubleValue();
    }

}
