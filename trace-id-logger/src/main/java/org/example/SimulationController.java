package org.example;

import org.logging.LazyLogger;
import org.logging.Trace;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class SimulationController {
    private final LazyLogger log = LazyLogger.get(SimulationController.class);

    private final OrderService orderService;

    public SimulationController() {
        var inventory = new InventoryService();
        var payment = new PaymentService();
        this.orderService = new OrderService(inventory, payment);
    }

    // ========== Versi 1: pakai virtual thread default (Java 21 built-in) ==========
    public void runNoTrace(List<Order> orders) {
        log.info(() -> "===== SIMULASI: TANPA TRACE ID =====");
        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var o : orders) {
                exec.submit(() -> processSafe(o)); // tidak set traceId
            }
        }
    }

    public void runWithTrace(List<Order> orders) {
        log.info(() -> "===== SIMULASI: DENGAN TRACE ID =====");
        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var o : orders) {
                final String traceId = Trace.newId();
                exec.submit(() -> Trace.runWith(traceId, () -> processSafe(o)));
            }
        }
    }

    // ========== Versi 2: pakai nama thread custom ==========
    private ExecutorService newNamedVirtualThreadExecutor(String prefix) {
        ThreadFactory factory = Thread.ofVirtual().name(prefix, 1).factory();
        return Executors.newThreadPerTaskExecutor(factory);
    }

    public void runNoTraceNamed(List<Order> orders) {
        log.info(() -> "===== SIMULASI (NAMED): TANPA TRACE ID =====");
        try (ExecutorService exec = newNamedVirtualThreadExecutor("no-trace-")) {
            for (var o : orders) {
                exec.submit(() -> processSafe(o));
            }
        }
    }

    public void runWithTraceNamed(List<Order> orders) {
        log.info(() -> "===== SIMULASI (NAMED): DENGAN TRACE ID =====");
        try (ExecutorService exec = newNamedVirtualThreadExecutor("with-trace-")) {
            for (var o : orders) {
                final String traceId = Trace.newId();
                exec.submit(() -> Trace.runWith(traceId, () -> processSafe(o)));
            }
        }
    }

    // ========== Helper ==========
    private void processSafe(Order o) {
        try {
            orderService.process(o);
        } catch (Exception e) {
            log.error(e, () -> "Process failed for " + o.id());
        }
    }

    public static List<Order> sampleOrders() {
        return List.of(
                new Order("ORD-001", new BigDecimal("300000")),
                new Order("ORD-002", new BigDecimal("450000")),
                new Order("ORD-003", new BigDecimal("120000"))
        );
    }
}
