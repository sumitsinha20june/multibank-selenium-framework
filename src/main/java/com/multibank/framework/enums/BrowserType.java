package com.multibank.framework.enums;

public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE;

    public static BrowserType from(String value) {
        return BrowserType.valueOf(value.trim().toUpperCase());
    }
}
