package org.logging;

import org.slf4j.MDC;

import java.util.UUID;

public final class Trace {
    private Trace() {}

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    /** Scope MDC agar otomatis dikosongkan (try-with-resources). */
    public static AutoCloseable put(String traceId) {
        MDC.put("traceId", traceId);
        return () -> MDC.remove("traceId");
    }

    public static void runWith(String traceId, Runnable r) {
        try (var __ = put(traceId)) { r.run(); } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
