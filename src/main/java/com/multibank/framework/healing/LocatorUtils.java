package com.multibank.framework.healing;

import org.openqa.selenium.By;

/**
 * Small locator helpers used by the auto-healing layer.
 *
 * Validates and sanitizes XPath strings returned by the healer
 * before they are re-applied by the framework.
 */
public final class LocatorUtils {

    private LocatorUtils() {
    }

    /**
     * Checks whether a given Selenium {@code By} locator is an XPath locator.
     */
    public static boolean isXPath(By locator) {
        return locator != null && locator.toString().startsWith("By.xpath:");
    }

    /**
     * Extracts the raw XPath string from a Selenium {@code By.xpath} locator.
     */
    public static String extractXPath(By locator) {
        if (!isXPath(locator)) {
            throw new IllegalArgumentException("Locator is not XPath: " + locator);
        }
        return locator.toString().replace("By.xpath: ", "").trim();
    }

    /**
     * Sanitizes a raw XPath returned by the healer.
     *
     * Strips markdown code fences, leading "xpath=" labels, and surrounding
     * quotes.  Throws if the result is empty or does not look like a valid
     * XPath (must start with / or ().
     */
    public static String sanitizeXPath(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Healer returned an empty XPath");
        }

        String xpath = rawValue.trim()
                .replace("```xpath", "")
                .replace("```xml", "")
                .replace("```", "")
                .replaceFirst("(?i)^xpath\\s*=", "")
                .replaceFirst("^\"|\"$", "")
                .trim();

        if (!xpath.startsWith("/") && !xpath.startsWith("(")) {
            throw new IllegalArgumentException("Healer returned invalid XPath: " + xpath);
        }

        return xpath;
    }

    /**
     * Converts a raw healer response into a Selenium {@code By.xpath} locator.
     * Sanitizes the input first.
     */
    public static By toByXPath(String rawValue) {
        return By.xpath(sanitizeXPath(rawValue));
    }
}
