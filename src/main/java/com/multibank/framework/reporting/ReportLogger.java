package com.multibank.framework.reporting;

import com.aventstack.extentreports.ExtentTest;

// Binds an ExtentTest to the current thread so listener callbacks
// (onSuccess, onFailure, etc.) can log against the right test entry
// without passing the test reference around.
public final class ReportLogger {
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    private ReportLogger() {
    }

    public static void setTest(ExtentTest test) {
        TEST.set(test);
    }

    public static ExtentTest getTest() {
        return TEST.get();
    }

    public static void remove() {
        TEST.remove();
    }
}
