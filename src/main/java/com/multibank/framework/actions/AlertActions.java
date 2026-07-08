package com.multibank.framework.actions;

import com.multibank.framework.config.FrameworkConfig;
import com.multibank.framework.driver.DriverManager;
import com.multibank.framework.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// JavaScript alert/dialog helpers: accept, dismiss, get text, send keys.
public final class AlertActions {
    private static final Logger log = LoggerUtil.getLogger(AlertActions.class);

    private AlertActions() {
    }

    public static void accept() {
        log.info("Accepting alert");
        waitForAlert().accept();
    }

    public static void dismiss() {
        log.info("Dismissing alert");
        waitForAlert().dismiss();
    }

    public static String getText() {
        String text = waitForAlert().getText();
        log.info("Alert text: {}", text);
        return text;
    }

    public static void sendKeys(String keys) {
        log.info("Sending keys to alert: {}", keys);
        waitForAlert().sendKeys(keys);
    }

    // Waits up to the configured explicit timeout for an alert to appear.
    private static org.openqa.selenium.Alert waitForAlert() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConfig.explicitWait()));
        return wait.until(ExpectedConditions.alertIsPresent());
    }
}
