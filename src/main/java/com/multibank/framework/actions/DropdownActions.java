package com.multibank.framework.actions;

import com.multibank.framework.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

// Select (dropdown) helpers: by visible text, value, or index.  Retries once
// on StaleElementReferenceException.
public final class DropdownActions {
    private static final Logger log = LoggerUtil.getLogger(DropdownActions.class);

    private DropdownActions() {
    }

    public static void selectByText(WebElement element, String text) {
        log.info("Selecting dropdown option by text: {}", text);
        try {
            new Select(element).selectByVisibleText(text);
        } catch (StaleElementReferenceException e) {
            log.warn("Stale element, retrying select by text: {}", text);
            new Select(element).selectByVisibleText(text);
        }
    }

    public static void selectByValue(WebElement element, String value) {
        log.info("Selecting dropdown option by value: {}", value);
        try {
            new Select(element).selectByValue(value);
        } catch (StaleElementReferenceException e) {
            log.warn("Stale element, retrying select by value: {}", value);
            new Select(element).selectByValue(value);
        }
    }

    public static void selectByIndex(WebElement element, int index) {
        log.info("Selecting dropdown option by index: {}", index);
        try {
            new Select(element).selectByIndex(index);
        } catch (StaleElementReferenceException e) {
            log.warn("Stale element, retrying select by index: {}", index);
            new Select(element).selectByIndex(index);
        }
    }

    public static String getSelectedOption(WebElement element) {
        String selected = new Select(element).getFirstSelectedOption().getText();
        log.debug("Selected dropdown option: {}", selected);
        return selected;
    }
}
