package com.multibank.tests.explore;

import com.multibank.framework.flows.ExploreFlow;
import com.multibank.framework.models.TestCaseData;
import com.multibank.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExplorePageMarketSentimentTests extends BaseTest {
    private final ExploreFlow exploreFlow = new ExploreFlow();

    @Test(dataProvider = "excelData")
    public void verifyMarketSentimentWidgetRenders(TestCaseData data) {
        exploreFlow.open();
        Assert.assertTrue(exploreFlow.hasMarketSentiment(), "Market sentiment heading missing");
        if ("true".equalsIgnoreCase(data.data("validateScoreNumeric"))) {
            Assert.assertTrue(exploreFlow.hasMarketSentimentLabels(), "Expected sentiment labels not found");
        }
    }
}
