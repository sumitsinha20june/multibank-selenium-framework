package com.multibank.framework.driver;

import com.multibank.framework.config.ConfigReader;
import com.multibank.framework.config.FrameworkConfig;
import com.multibank.framework.enums.PlatformType;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

public final class BrowserOptionsFactory {
    private BrowserOptionsFactory() {
    }

    public static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1440,900");
        if (FrameworkConfig.headless()) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
        }
        if (FrameworkConfig.platform() == PlatformType.MWEB) {
            ConfigReader config = ConfigReader.getInstance();
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("width", config.getInt("mobile.width"));
            metrics.put("height", config.getInt("mobile.height"));
            metrics.put("pixelRatio", 3.0);
            Map<String, Object> emulation = new HashMap<>();
            emulation.put("deviceMetrics", metrics);
            emulation.put("userAgent", "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1");
            options.setExperimentalOption("mobileEmulation", emulation);
        }
        return options;
    }

    public static FirefoxOptions firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1440");
        options.addArguments("--height=900");
        if (FrameworkConfig.headless()) {
            options.addArguments("--headless");
        }
        return options;
    }

    public static EdgeOptions edgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--window-size=1440,900");
        if (FrameworkConfig.headless()) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
        }
        return options;
    }

    public static MutableCapabilities forConfiguredBrowser() {
        return switch (FrameworkConfig.browser()) {
            case CHROME -> chromeOptions();
            case FIREFOX -> firefoxOptions();
            case EDGE -> edgeOptions();
        };
    }
}
