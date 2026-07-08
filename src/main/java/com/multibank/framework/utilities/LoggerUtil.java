package com.multibank.framework.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Shorthand for LogManager.getLogger() so callers don't need the import.
public final class LoggerUtil {
    private LoggerUtil() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
