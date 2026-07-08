package com.multibank.framework.utilities;

import org.testng.asserts.SoftAssert;

// Thread-local SoftAssert so parallel tests don't share assertion state.
// Call create() in the test, assertAll() at the end.
public final class SoftAssertManager {
    private static final ThreadLocal<SoftAssert> SOFT_ASSERT = new ThreadLocal<>();

    private SoftAssertManager() {
    }

    public static SoftAssert create() {
        SoftAssert sa = new SoftAssert();
        SOFT_ASSERT.set(sa);
        return sa;
    }

    public static void assertAll() {
        SoftAssert sa = SOFT_ASSERT.get();
        if (sa != null) {
            sa.assertAll();
            SOFT_ASSERT.remove();
        }
    }

    public static void remove() {
        SOFT_ASSERT.remove();
    }
}
