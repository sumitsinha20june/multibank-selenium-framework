package com.multibank.framework.flows;

import com.multibank.framework.config.FrameworkConfig;
import com.multibank.framework.driver.DriverManager;
import com.multibank.framework.pages.factory.PageObjectFactory;
import com.multibank.framework.pages.interfaces.IExplorePage;
import com.multibank.framework.utilities.LoggerUtil;
import com.multibank.framework.utilities.NetworkThrottler;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class ExploreFlow {
    private static final Logger log = LoggerUtil.getLogger(ExploreFlow.class);
    private final IExplorePage explorePage;

    public ExploreFlow() {
        this.explorePage = PageObjectFactory.getExplorePage();
    }

    public void open() {
        explorePage.open();
    }

    public boolean isSpotMarketVisible() {
        return explorePage.isSpotMarketVisible();
    }

    public boolean hasSpotMarketContent(String text) {
        return explorePage.hasSpotMarketContent(text);
    }

    public boolean tradingPairIsVisible(String symbol, String name) {
        return explorePage.isTradingPairVisible(symbol, name);
    }

    public boolean tradingPairHasPriceAndChange(String symbol) {
        return explorePage.hasPriceAndChangeNearSymbol(symbol);
    }

    public boolean categoryExists(String category) {
        return explorePage.hasCategory(category);
    }

    public boolean hasMarketSentiment() {
        return explorePage.hasMarketSentiment();
    }

    public boolean hasMarketSentimentLabels() {
        return explorePage.hasMarketSentimentLabels();
    }

    public void verifyTimeoutHandling() {
        WebDriver driver = DriverManager.getDriver();
        Duration originalTimeout = Duration.ofSeconds(FrameworkConfig.pageLoadTimeout());
        try {
            NetworkThrottler.enable2G(driver);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
            explorePage.open();
            throw new AssertionError("Expected page load timeout but page loaded successfully");
        } catch (TimeoutException e) {
            log.info("Page load timeout correctly triggered under 2G throttling: {}", e.getMessage());
        } finally {
            NetworkThrottler.disable(driver);
            driver.manage().timeouts().pageLoadTimeout(originalTimeout);
            driver.get("about:blank");
        }
    }
}
