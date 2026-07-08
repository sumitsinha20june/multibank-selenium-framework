package com.multibank.framework.healing;

import com.multibank.framework.config.ConfigReader;
import com.multibank.framework.driver.DriverManager;
import com.multibank.framework.reporting.ReportLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Optional;
import java.util.function.Function;

// Called by WaitActions after the original locator fails.  Supports MCP
// and OpenAI backends; the original locator is always the truth.
public final class AutoHealingLocator {

    private static final Logger logger = LogManager.getLogger(AutoHealingLocator.class);
    private static final OpenAILocatorHealer OPEN_AI_HEALER = new OpenAILocatorHealer();

    private AutoHealingLocator() {
    }

    // Tries AI/MCP healing when enabled.  Logs the attempt to ExtentReport
    // for debuggability.  Returns empty if healing is off or unsuccessful.
    public static Optional<WebElement> tryHeal(
            By originalLocator,
            String elementName,
            String waitState,
            Function<By, WebElement> healedLookup) {

        ConfigReader config = ConfigReader.getInstance();
        if (!config.isAutoHealingEnabled()) {
            return Optional.empty();
        }

        logger.warn("Locator failed for '{}' using '{}'. Auto-healing is enabled.",
                elementName, originalLocator);
        if (ReportLogger.getTest() != null) {
            ReportLogger.getTest().warning("Locator failed: " + originalLocator + " (\"" + elementName + "\") → attempting healing...");
        }

        if (!config.isAutoHealingAIEnabled()) {
            logger.warn("AI healing is disabled in config for '{}'", elementName);
            return Optional.empty();
        }

        // Try MCP server first, fall back to direct OpenAI
        Optional<By> healedLocator = OPEN_AI_HEALER.heal(
                originalLocator,
                elementName,
                DriverManager.getDriver().getPageSource());

        if (healedLocator.isEmpty()) {
            logger.warn("No healed locator was available for '{}'", elementName);
            return Optional.empty();
        }

        try {
            WebElement healedElement = healedLookup.apply(healedLocator.get());
            logger.info("Auto-healing recovered '{}' using '{}' for {}",
                    elementName, healedLocator.get(), waitState);
            return Optional.of(healedElement);
        } catch (Exception e) {
            logger.warn("Healed locator also failed for '{}': {}", elementName, e.getMessage());
            return Optional.empty();
        }
    }
}
