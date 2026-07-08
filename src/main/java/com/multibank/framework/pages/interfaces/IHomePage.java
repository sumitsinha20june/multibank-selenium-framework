package com.multibank.framework.pages.interfaces;

public interface IHomePage {
    void open();
    boolean isTextVisible(String text);
    String downloadLinkHref(String linkText);
    String downloadLinkTarget(String linkText);
}
