package com.multibank.framework.pages.dweb;

import com.multibank.framework.actions.WebActions;
import com.multibank.framework.pages.base.BasePage;
import com.multibank.framework.pages.interfaces.IExplorePage;
import org.openqa.selenium.By;

public class DWebExplorePage extends BasePage implements IExplorePage {

    private final By spotMarketHeading = By.xpath("//h2[contains(text(), '" + config.get("explore.spot.market.heading") + "')]");
    private final By marketSentimentHeading = By.xpath("//*[contains(normalize-space(), '" + config.get("explore.market.sentiment.heading") + "')]");

    public void open() {
        openPath("explore.path");
    }

    public boolean isSpotMarketVisible() {
        return WebActions.isDisplayed(spotMarketHeading);
    }

    public boolean hasSpotMarketContent(String expectedText) {
        return WebActions.isDisplayed(elementByText(expectedText));
    }

    public boolean isTradingPairVisible(String symbol, String name) {
        return WebActions.pageContains(symbol) && WebActions.pageContains(name);
    }

    public boolean hasCategory(String category) {
        return WebActions.isDisplayed(elementByText(category));
    }

    public boolean hasMarketSentiment() {
        return WebActions.isDisplayed(marketSentimentHeading);
    }

    public boolean hasMarketSentimentLabels() {
        String labels = config.get("explore.sentiment.labels");
        for (String label : labels.split("\\|")) {
            if (WebActions.pageContains(label.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPriceAndChangeNearSymbol(String symbol) {
        String page = WebActions.text(By.tagName("body"));
        int index = page.toLowerCase().indexOf(symbol.toLowerCase());
        if (index < 0) {
            return false;
        }
        int start = Math.max(0, index - 50);
        int end = Math.min(page.length(), index + 250);
        String context = page.substring(start, end);
        boolean hasPrice = context.matches("(?s).*\\$?\\d+[\\.\\,]\\d{2}.*");
        boolean hasChange = context.matches("(?s).*[+-]?\\d+\\.?\\d*\\s*%.*");
        return hasPrice && hasChange;
    }
}
