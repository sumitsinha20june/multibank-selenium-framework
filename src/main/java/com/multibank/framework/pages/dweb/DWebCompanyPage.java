package com.multibank.framework.pages.dweb;

import com.multibank.framework.actions.WebActions;
import com.multibank.framework.pages.base.BasePage;
import com.multibank.framework.pages.interfaces.ICompanyPage;
import org.openqa.selenium.By;

public class DWebCompanyPage extends BasePage implements ICompanyPage {

    private final By pageBody = By.tagName("body");

    public void open() {
        openPath("company.path");
    }

    public boolean hasContent(String heading, String text) {
        return WebActions.isDisplayed(pageBody)
            && WebActions.pageContains(heading)
            && WebActions.pageContains(text);
    }
}
