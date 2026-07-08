package com.multibank.framework.flows;

import com.multibank.framework.pages.factory.PageObjectFactory;
import com.multibank.framework.pages.interfaces.IHomePage;

public class HomeFlow {
    private final IHomePage homePage;

    public HomeFlow() {
        this.homePage = PageObjectFactory.getHomePage();
    }

    public void open() {
        homePage.open();
    }

    public boolean isTextVisible(String text) {
        return homePage.isTextVisible(text);
    }

    public boolean downloadLinkValidates(String linkText, String expectedUrlContains, String expectedTarget) {
        String href = homePage.downloadLinkHref(linkText);
        String target = homePage.downloadLinkTarget(linkText);
        return href.contains(expectedUrlContains) && target.equals(expectedTarget);
    }

    public String downloadLinkHref(String linkText) {
        return homePage.downloadLinkHref(linkText);
    }

    public String downloadLinkTarget(String linkText) {
        return homePage.downloadLinkTarget(linkText);
    }
}
