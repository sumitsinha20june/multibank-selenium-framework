package com.multibank.framework.flows;

import com.multibank.framework.pages.factory.PageObjectFactory;
import com.multibank.framework.pages.interfaces.IFooterComponent;

public class FooterFlow {
    private final IFooterComponent footer;

    public FooterFlow() {
        this.footer = PageObjectFactory.getFooterComponent();
    }

    public boolean linkDestinationMatches(String linkText, String expectedPath) {
        return footer.hrefFor(linkText).contains(expectedPath);
    }

    public String targetFor(String linkText) {
        return footer.targetFor(linkText);
    }
}
