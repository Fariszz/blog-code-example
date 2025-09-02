package org.example;

import org.logging.LazyLogger;

import java.util.concurrent.ThreadLocalRandom;

public class InventoryService {
    private final LazyLogger log = LazyLogger.get(InventoryService.class);

    public void reserve(String orderId) {
        log.info(() -> "▶ Reserve inventory for " + orderId);
        sleepRandom();
        log.info(() -> "✔ Reserve inventory for " + orderId);
    }

    private void sleepRandom() {
        try { Thread.sleep(ThreadLocalRandom.current().nextInt(30, 120)); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
