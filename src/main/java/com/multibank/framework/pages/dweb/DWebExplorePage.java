package com.multibank.framework.pages.dweb;

import com.multibank.framework.actions.WaitActions;
import com.multibank.framework.actions.WebActions;
import com.multibank.framework.pages.base.BasePage;
import com.multibank.framework.pages.interfaces.IExplorePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Pattern;

public class DWebExplorePage extends BasePage implements IExplorePage {

    private static final Pattern PRICE_PATTERN = Pattern.compile("\\$?\\d{1,3}(,\\d{3})*(\\.\\d{2})?");
    private static final Pattern CHANGE_PATTERN = Pattern.compile("[+-]?\\d+\\.?\\d*\\s*%");

    private final By spotMarketHeading = By.xpath("//h2[contains(text(), '" + config.get("explore.spot.market.heading") + "')]");
    private final By marketSentimentHeading = By.xpath("//*[contains(normalize-space(), '" + config.get("explore.market.sentiment.heading") + "')]");

    private static final By TABLE_LOCATOR = By.xpath("//section[contains(@class,'bg-dark')]//table");

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
        try {
            WebElement table = findTradingPairTable();
            WebElement row = table.findElement(
                    By.xpath(".//tr[.//span[text()='" + symbol + "'] and .//span[text()='" + name + "']]"));
            return true;
        } catch (Exception e) {
            return false;
        }
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
        try {
            WebElement table = findTradingPairTable();
            WebElement row = table.findElement(
                    By.xpath(".//tr[.//span[text()='" + symbol + "']]"));
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() < 3) return false;
            String priceText = cells.get(1).getText().trim();
            String changeText = cells.get(2).getText().trim();
            return PRICE_PATTERN.matcher(priceText).find()
                    && CHANGE_PATTERN.matcher(changeText).find();
        } catch (Exception e) {
            return false;
        }
    }

    private WebElement findTradingPairTable() {
        WaitActions.visible(By.xpath("//h3[contains(text(),\"Today's top crypto prices\")]"),
                "Trading pair prices heading");
        return WaitActions.visible(TABLE_LOCATOR, "Trading pair prices table");
    }
}
