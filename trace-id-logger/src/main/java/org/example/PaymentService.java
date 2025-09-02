package org.example;

import org.logging.LazyLogger;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public class PaymentService {
    private final LazyLogger log = LazyLogger.get(PaymentService.class);

    public void charge(String orderId, BigDecimal amount) {
        log.info(() -> "▶ Charge payment for " + orderId + " amount=" + amount);
        sleepRandom();

        // Simulasikan kemungkinan gagal
        if (ThreadLocalRandom.current().nextInt(0, 6) == 0) {
            var ex = new IllegalStateException("Payment gateway timeout");
            log.error(ex, () -> "✖ Charge failed for " + orderId);
            throw ex;
        }

        log.info(() -> "✔ Charge success for " + orderId);
    }

    private void sleepRandom() {
        try { Thread.sleep(ThreadLocalRandom.current().nextInt(50, 200)); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
