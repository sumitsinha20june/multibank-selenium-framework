package com.multibank.framework.config;

import com.multibank.framework.enums.BrowserType;
import com.multibank.framework.enums.ExecutionMode;
import com.multibank.framework.enums.PlatformType;

// Typed config accessors — thin wrappers over ConfigReader that save
// callers from dealing with string keys and parsing.
public final class FrameworkConfig {
    private static final ConfigReader CONFIG = ConfigReader.getInstance();

    private FrameworkConfig() {
    }

    public static PlatformType platform() {
        return PlatformType.from(CONFIG.get("platform"));
    }

    public static BrowserType browser() {
        return BrowserType.from(CONFIG.get("browser"));
    }

    public static ExecutionMode executionMode() {
        return ExecutionMode.from(CONFIG.get("execution.mode"));
    }

    public static boolean headless() {
        return CONFIG.getBoolean("headless");
    }

    public static int explicitWait() {
        return CONFIG.getInt("explicit.wait");
    }

    public static int pageLoadTimeout() {
        return CONFIG.getInt("page.load.timeout");
    }

    public static String baseUrl() {
        return CONFIG.get("base.url");
    }
}
