package com.multibank.framework.actions;

import com.multibank.framework.driver.DriverManager;
import org.openqa.selenium.JavascriptExecutor;

// Thin wrapper over JavascriptExecutor for one-off script execution.
public final class JavaScriptActions {
    private JavaScriptActions() {
    }

    public static Object execute(String script) {
        return ((JavascriptExecutor) DriverManager.getDriver()).executeScript(script);
    }

    public static void scrollToBottom() {
        execute("window.scrollTo(0, document.body.scrollHeight)");
    }
}
