package org.example;

import java.math.BigDecimal;

public record Order(String id, BigDecimal amount) {
}
