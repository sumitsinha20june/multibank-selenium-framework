package com.multibank.framework.pages.dweb;

import com.multibank.framework.actions.WebActions;
import com.multibank.framework.pages.base.BasePage;
import com.multibank.framework.pages.interfaces.IHomePage;

public class DWebHomePage extends BasePage implements IHomePage {

    public void open() {
        openPath("home.path");
    }

    public boolean isTextVisible(String text) {
        return WebActions.isDisplayed(elementByText(text));
    }

    public String downloadLinkHref(String linkText) {
        return WebActions.attribute(linkByText(linkText), "href");
    }

    public String downloadLinkTarget(String linkText) {
        String target = WebActions.attribute(linkByText(linkText), "target");
        return target == null || target.isBlank() ? "_self" : target;
    }
}
