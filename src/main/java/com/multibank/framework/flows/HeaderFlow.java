package com.multibank.framework.flows;

import com.multibank.framework.actions.BrowserActions;
import com.multibank.framework.actions.WaitActions;
import com.multibank.framework.pages.factory.PageObjectFactory;
import com.multibank.framework.pages.interfaces.IHeaderComponent;
import com.multibank.framework.utilities.ViewportManager;

import java.util.List;

public class HeaderFlow {
    private final IHeaderComponent header;

    public HeaderFlow() {
        this.header = PageObjectFactory.getHeaderComponent();
    }

    public List<String> missingNavigationItems(String items) {
        return header.missingNavigationItems(items);
    }

    public boolean isNavigationItemVisible(String item) {
        return header.isNavigationItemVisible(item);
    }

    public boolean isMenuAvailable() {
        return header.isMenuAvailable();
    }

    public void openMenuIfPresent() {
        header.openMenuIfPresent();
    }

    public boolean itemsAreAllVisible(String items) {
        return header.areNavigationItemsVisible(items);
    }

    public String hrefFor(String item) {
        return header.hrefFor(item);
    }

    public String targetFor(String item) {
        return header.targetFor(item);
    }

    public boolean linkDestinationMatches(String item, String expectedPath) {
        String href = header.hrefFor(item);
        return href.contains(expectedPath);
    }

    public void navigateToHref(String href, String expectedPath) {
        BrowserActions.open(href);
        WaitActions.urlContains(expectedPath);
    }

    public void setViewport(String viewport) {
        ViewportManager.setViewport(viewport);
    }
}
