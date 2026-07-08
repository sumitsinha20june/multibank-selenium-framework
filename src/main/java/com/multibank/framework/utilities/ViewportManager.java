package com.multibank.framework.utilities;

import com.multibank.framework.driver.DriverManager;
import org.openqa.selenium.Dimension;

// Resizes the browser window by parsing "WIDTHxHEIGHT" strings (e.g. "1440x900").
public final class ViewportManager {
    private ViewportManager() {
    }

    public static void setViewport(String viewport) {
        String[] parts = viewport.split("x");
        DriverManager.getDriver().manage().window().setSize(new Dimension(
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim())
        ));
    }
}
