package com.multibank.framework.driver;

import com.multibank.framework.config.ConfigReader;
import com.multibank.framework.config.FrameworkConfig;
import com.multibank.framework.enums.BrowserType;
import com.multibank.framework.enums.ExecutionMode;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        WebDriver driver = FrameworkConfig.executionMode() == ExecutionMode.GRID
                ? createGridDriver()
                : createLocalDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(FrameworkConfig.pageLoadTimeout()));
        DriverManager.setDriver(driver);
        return driver;
    }

    private static WebDriver createLocalDriver() {
        BrowserType browser = FrameworkConfig.browser();
        return switch (browser) {
            case CHROME -> {
                WebDriverManager.chromedriver().setup();
                yield new ChromeDriver(BrowserOptionsFactory.chromeOptions());
            }
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                yield new FirefoxDriver(BrowserOptionsFactory.firefoxOptions());
            }
            case EDGE -> {
                WebDriverManager.edgedriver().setup();
                yield new EdgeDriver(BrowserOptionsFactory.edgeOptions());
            }
        };
    }

    private static WebDriver createGridDriver() {
        try {
            URL gridUrl = new URL(ConfigReader.getInstance().get("grid.url"));
            return new RemoteWebDriver(gridUrl, BrowserOptionsFactory.forConfiguredBrowser());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid grid.url", e);
        }
    }
}
