package org.example;

import org.logging.LazyLogger;

public class OrderService {
    private final InventoryService inventory;
    private final PaymentService payment;
    private final LazyLogger log = LazyLogger.get(OrderService.class);

    public OrderService(InventoryService inventory, PaymentService payment) {
        this.inventory = inventory;
        this.payment = payment;
    }

    public void process(Order order) {
        log.info(() -> "⟲ Start process " + order.id());
        inventory.reserve(order.id());
        payment.charge(order.id(), order.amount());
        log.info(() -> "⟲ Done process " + order.id());
    }
}
