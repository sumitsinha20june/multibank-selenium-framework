package com.multibank.framework.actions;

import com.multibank.framework.driver.DriverManager;
import com.multibank.framework.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;

// Browser-level actions: navigate, get URL/title.
public final class BrowserActions {
    private static final Logger log = LoggerUtil.getLogger(BrowserActions.class);

    private BrowserActions() {
    }

    public static void open(String url) {
        log.info("Navigating to: {}", url);
        DriverManager.getDriver().get(url);
    }

    public static String currentUrl() {
        String url = DriverManager.getDriver().getCurrentUrl();
        log.debug("Current URL: {}", url);
        return url;
    }

    public static String title() {
        String title = DriverManager.getDriver().getTitle();
        log.debug("Page title: {}", title);
        return title;
    }
}
