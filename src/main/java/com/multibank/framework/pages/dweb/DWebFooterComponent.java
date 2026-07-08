package com.multibank.framework.pages.dweb;

import com.multibank.framework.actions.JavaScriptActions;
import com.multibank.framework.actions.WebActions;
import com.multibank.framework.pages.base.BasePage;
import com.multibank.framework.pages.interfaces.IFooterComponent;

public class DWebFooterComponent extends BasePage implements IFooterComponent {

    public String hrefFor(String linkText) {
        JavaScriptActions.scrollToBottom();
        return WebActions.attribute(linkByText(linkText), "href");
    }

    public String targetFor(String linkText) {
        JavaScriptActions.scrollToBottom();
        String target = WebActions.attribute(linkByText(linkText), "target");
        return target == null || target.isBlank() ? "_self" : target;
    }
}
