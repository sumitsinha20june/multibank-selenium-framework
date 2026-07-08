package com.multibank.framework.flows;

import com.multibank.framework.config.FrameworkConfig;
import com.multibank.framework.driver.DriverManager;
import com.multibank.framework.pages.factory.PageObjectFactory;
import com.multibank.framework.pages.interfaces.IExplorePage;
import com.multibank.framework.utilities.LoggerUtil;
import org.apache.logging.log4j.Logger;

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

    public void verifyTimeoutHandling(String expectedHeading) {
        Duration originalTimeout = Duration.ofSeconds(FrameworkConfig.pageLoadTimeout());
        DriverManager.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofMillis(1));
        try {
            explorePage.open();
            throw new AssertionError("Expected page load timeout but page loaded successfully");
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (!msg.contains("timeout")) {
                throw new AssertionError("Expected timeout error but got: " + e.getMessage());
            }
            log.info("Page load timeout correctly triggered: {}", e.getMessage());
        } finally {
            DriverManager.getDriver().manage().timeouts().pageLoadTimeout(originalTimeout);
            DriverManager.getDriver().get("about:blank");
        }
    }
}
