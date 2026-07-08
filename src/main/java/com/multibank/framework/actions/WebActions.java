package com.multibank.framework.actions;

import com.multibank.framework.driver.DriverManager;
import com.multibank.framework.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

public final class WebActions {
    private static final Logger log = LoggerUtil.getLogger(WebActions.class);

    private WebActions() {
    }

    public static void click(By locator) {
        log.info("Clicking element: {}", locator);
        try {
            WaitActions.clickable(locator).click();
        } catch (Exception e) {
            log.warn("Standard click failed, falling back to JS click for: {}", locator);
            WebElement element = WaitActions.visible(locator);
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", element);
        }
    }

    public static boolean isDisplayed(By locator) {
        try {
            WebElement element = WaitActions.visible(locator);
            highlight(element, "green");
            log.debug("Element displayed: {} = true", locator);
            return element.isDisplayed();
        } catch (Exception e) {
            log.debug("Element displayed: {} = false", locator);
            return false;
        }
    }

    public static String text(By locator) {
        WebElement element = WaitActions.visible(locator);
        highlight(element, "green");
        String value = element.getText().trim();
        log.debug("Text from {}: {}", locator, value);
        return value;
    }

    public static String attribute(By locator, String attribute) {
        WebElement element = WaitActions.visible(locator);
        highlight(element, "green");
        String value = element.getAttribute(attribute);
        log.debug("Attribute {} from {}: {}", attribute, locator, value);
        return value;
    }

    public static List<WebElement> elements(By locator) {
        log.debug("Finding elements: {}", locator);
        return DriverManager.getDriver().findElements(locator);
    }

    public static boolean pageContains(String value) {
        log.debug("Checking page contains: {}", value);
        boolean contains = DriverManager.getDriver().getPageSource().toLowerCase().contains(value.toLowerCase());
        if (contains) {
            highlightText(value);
        }
        return contains;
    }

    public static void highlightText(String value) {
        try {
            By locator = By.xpath("//*[contains(normalize-space(), \"" + value + "\")]");
            highlight(DriverManager.getDriver().findElement(locator), "green");
        } catch (Exception ignored) {
            // Visual highlight only — never affects pass/fail.
        }
    }

    // Draws a coloured border around the element for visual evidence in screenshots.
    public static void highlight(WebElement element, String color) {
        try {
            ((JavascriptExecutor) DriverManager.getDriver())
                    .executeScript("arguments[0].style.border='3px solid " + color + "'; arguments[0].style.boxShadow='0 0 0 2px rgba(0,128,0,0.25)';", element);
        } catch (Exception ignored) {
            // Visual highlight only — never affects pass/fail.
        }
    }
}
