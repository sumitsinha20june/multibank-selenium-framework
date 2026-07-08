package com.multibank.framework.enums;

public enum PlatformType {
    DWEB,
    MWEB;

    public static PlatformType from(String value) {
        return PlatformType.valueOf(value.trim().toUpperCase());
    }
}
