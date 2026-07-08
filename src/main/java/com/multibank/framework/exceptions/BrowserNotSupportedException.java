package com.multibank.framework.exceptions;

public class BrowserNotSupportedException extends FrameworkException {
    public BrowserNotSupportedException(String browser) {
        super("Browser is not supported: " + browser);
    }
}
