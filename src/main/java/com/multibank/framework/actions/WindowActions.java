package com.multibank.framework.actions;

import com.multibank.framework.driver.DriverManager;
import com.multibank.framework.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.Set;

// Window/tab management: switch by title/URL, close current, count handles.
public final class WindowActions {
    private static final Logger log = LoggerUtil.getLogger(WindowActions.class);

    private WindowActions() {
    }

    public static void switchToWindow(String titleOrUrl) {
        log.info("Switching to window containing: {}", titleOrUrl);
        String currentHandle = DriverManager.getDriver().getWindowHandle();
        for (String handle : DriverManager.getDriver().getWindowHandles()) {
            WebDriver window = DriverManager.getDriver().switchTo().window(handle);
            if (window.getTitle().contains(titleOrUrl) || window.getCurrentUrl().contains(titleOrUrl)) {
                log.info("Switched to window: {}", window.getTitle());
                return;
            }
        }
        DriverManager.getDriver().switchTo().window(currentHandle);
        log.warn("No window found matching: {}", titleOrUrl);
    }

    public static void closeCurrentWindow() {
        String title = DriverManager.getDriver().getTitle();
        log.info("Closing current window: {}", title);
        DriverManager.getDriver().close();
        switchToRemainingWindow();
    }

    public static Set<String> getWindowHandles() {
        Set<String> handles = DriverManager.getDriver().getWindowHandles();
        log.debug("Window handles count: {}", handles.size());
        return handles;
    }

    public static int count() {
        int count = DriverManager.getDriver().getWindowHandles().size();
        log.debug("Open window count: {}", count);
        return count;
    }

    private static void switchToRemainingWindow() {
        Set<String> handles = DriverManager.getDriver().getWindowHandles();
        if (!handles.isEmpty()) {
            DriverManager.getDriver().switchTo().window(handles.iterator().next());
            log.info("Switched to remaining window: {}", DriverManager.getDriver().getTitle());
        }
    }
}
