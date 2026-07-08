package com.multibank.framework.flows;

import com.multibank.framework.pages.factory.PageObjectFactory;
import com.multibank.framework.pages.interfaces.IErrorPage;

public class ErrorFlow {
    private final IErrorPage errorPage;

    public ErrorFlow() {
        this.errorPage = PageObjectFactory.getErrorPage();
    }

    public void openInvalidPath(String invalidPath) {
        errorPage.openInvalidPath(invalidPath);
    }

    public boolean hasErrorContent(String heading, String text) {
        return errorPage.hasErrorContent(heading, text);
    }
}
