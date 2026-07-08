package com.multibank.framework.actions;

import com.multibank.framework.config.FrameworkConfig;
import com.multibank.framework.driver.DriverManager;
import com.multibank.framework.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Frame switching helpers: by index, name/id, parent, or default content.
public final class FrameActions {
    private static final Logger log = LoggerUtil.getLogger(FrameActions.class);

    private FrameActions() {
    }

    public static void switchTo(int index) {
        log.info("Switching to frame at index: {}", index);
        waitForFrameAndSwitch(index);
    }

    public static void switchTo(String nameOrId) {
        log.info("Switching to frame: {}", nameOrId);
        waitForFrameAndSwitch(nameOrId);
    }

    public static void switchToParent() {
        log.info("Switching to parent frame");
        DriverManager.getDriver().switchTo().parentFrame();
    }

    public static void switchToDefault() {
        log.info("Switching to default content");
        DriverManager.getDriver().switchTo().defaultContent();
    }

    private static void waitForFrameAndSwitch(Object frameIdentifier) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConfig.explicitWait()));
        if (frameIdentifier instanceof Integer) {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((Integer) frameIdentifier));
        } else if (frameIdentifier instanceof String) {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((String) frameIdentifier));
        }
    }
}
