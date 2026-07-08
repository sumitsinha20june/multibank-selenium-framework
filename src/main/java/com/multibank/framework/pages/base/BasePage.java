package com.multibank.framework.pages.base;

import com.multibank.framework.actions.BrowserActions;
import com.multibank.framework.actions.WebActions;
import com.multibank.framework.config.ConfigReader;
import org.openqa.selenium.By;

// Common page-object base: resolves config-driven URLs and provides
// text-based By helpers shared by all page objects.
public abstract class BasePage {
    protected final ConfigReader config = ConfigReader.getInstance();

    public void openPath(String pathKey) {
        BrowserActions.open(config.resolveUrl(pathKey));
    }

    public boolean containsText(String text) {
        return WebActions.pageContains(text);
    }

    // Matches anchor elements by visible text (normalize-space ignores extra whitespace).
    protected By linkByText(String text) {
        return By.xpath("//a[normalize-space()='" + text + "' or .//*[normalize-space()='" + text + "']]");
    }

    // Matches any element containing the given text (substring match).
    protected By elementByText(String text) {
        return By.xpath("//*[contains(normalize-space(), \"" + text + "\")]");
    }
}
