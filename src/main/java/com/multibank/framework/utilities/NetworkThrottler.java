package com.multibank.framework.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Map;

public final class NetworkThrottler {
    private NetworkThrottler() {
    }

    public static void enable2G(WebDriver driver) {
        ChromeDriver chromeDriver = (ChromeDriver) driver;
        chromeDriver.executeCdpCommand("Network.enable", Map.of());
        chromeDriver.executeCdpCommand("Network.emulateNetworkConditions", Map.of(
                "offline", false,
                "latency", 300,
                "downloadThroughput", 50 * 1024,
                "uploadThroughput", 20 * 1024,
                "connectionType", "cellular2g"
        ));
    }

    public static void disable(WebDriver driver) {
        ChromeDriver chromeDriver = (ChromeDriver) driver;
        chromeDriver.executeCdpCommand("Network.emulateNetworkConditions", Map.of(
                "offline", false, "latency", 0,
                "downloadThroughput", 0, "uploadThroughput", 0
        ));
    }
}
