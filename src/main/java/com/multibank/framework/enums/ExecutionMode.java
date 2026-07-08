package com.multibank.framework.enums;

public enum ExecutionMode {
    LOCAL,
    GRID;

    public static ExecutionMode from(String value) {
        return ExecutionMode.valueOf(value.trim().toUpperCase());
    }
}
