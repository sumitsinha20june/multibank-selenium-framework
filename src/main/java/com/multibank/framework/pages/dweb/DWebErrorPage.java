package com.multibank.framework.pages.dweb;

import com.multibank.framework.actions.BrowserActions;
import com.multibank.framework.actions.WebActions;
import com.multibank.framework.config.ConfigReader;
import com.multibank.framework.pages.base.BasePage;
import com.multibank.framework.pages.interfaces.IErrorPage;
import org.openqa.selenium.By;

public class DWebErrorPage extends BasePage implements IErrorPage {

    private final By pageBody = By.tagName("body");

    public void openInvalidPath(String invalidPath) {
        BrowserActions.open(ConfigReader.getInstance().get("base.url") + invalidPath);
    }

    public boolean hasErrorContent(String heading, String text) {
        return WebActions.isDisplayed(pageBody)
            && WebActions.pageContains(heading)
            && WebActions.pageContains(text);
    }
}
