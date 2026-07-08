package com.multibank.framework.pages.interfaces;

public interface IExplorePage {
    void open();
    boolean isSpotMarketVisible();
    boolean hasSpotMarketContent(String expectedText);
    boolean isTradingPairVisible(String symbol, String name);
    boolean hasCategory(String category);
    boolean hasMarketSentiment();
    boolean hasMarketSentimentLabels();
    boolean hasPriceAndChangeNearSymbol(String symbol);
    boolean containsText(String text);
}
