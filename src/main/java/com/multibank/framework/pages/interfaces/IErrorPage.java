package com.multibank.framework.pages.interfaces;

public interface IErrorPage {
    void openInvalidPath(String invalidPath);
    boolean hasErrorContent(String heading, String text);
}
