package com.multibank.framework.actions;

import com.multibank.framework.config.FrameworkConfig;
import com.multibank.framework.driver.DriverManager;
import com.multibank.framework.healing.AutoHealingLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Centralized wait logic used by all page objects and action helpers.
 *
 * <p>Each wait method first tries the original locator.  If that fails with a
 * {@link TimeoutException} or {@link NoSuchElementException}, and auto-healing
 * is enabled, the framework tries {@link AutoHealingLocator#tryHeal} before
 * rethrowing the original exception.
 */
public final class WaitActions {
    private WaitActions() {
    }

    public static WebElement visible(By locator) {
        return visible(locator, locator.toString());
    }

    // Waits for visibility, with auto-healing fallback if the locator fails.
    public static WebElement visible(By locator, String elementName) {
        try {
            return explicitWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException | NoSuchElementException e) {
            return AutoHealingLocator
                    .tryHeal(locator, elementName, "visible",
                            healedLocator -> explicitWait()
                                    .until(ExpectedConditions.visibilityOfElementLocated(healedLocator)))
                    .orElseThrow(() -> e);
        }
    }

    public static WebElement clickable(By locator) {
        return clickable(locator, locator.toString());
    }

    // Waits for clickability, with auto-healing fallback.
    public static WebElement clickable(By locator, String elementName) {
        try {
            return explicitWait().until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException | NoSuchElementException e) {
            return AutoHealingLocator
                    .tryHeal(locator, elementName, "clickable",
                            healedLocator -> explicitWait()
                                    .until(ExpectedConditions.elementToBeClickable(healedLocator)))
                    .orElseThrow(() -> e);
        }
    }

    public static boolean urlContains(String value) {
        return explicitWait().until(ExpectedConditions.urlContains(value));
    }

    public static WebDriverWait explicitWait() {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConfig.explicitWait()));
    }
}
