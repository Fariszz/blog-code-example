package org.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;


public final class LazyLogger {
    private final Logger logger;
    private LazyLogger(Class<?> type) { this.logger = LoggerFactory.getLogger(type); }

    public static LazyLogger get(Class<?> type) { return new LazyLogger(type); }

    public void info(Supplier<String> msg) {
        if (logger.isInfoEnabled()) logger.info(msg.get());
    }
    public void debug(Supplier<String> msg) {
        if (logger.isDebugEnabled()) logger.debug(msg.get());
    }
    public void error(Throwable t, Supplier<String> msg) {
        if (logger.isErrorEnabled()) logger.error(msg.get(), t);
    }
}
