package com.multibank.tests.explore;

import com.multibank.framework.flows.ExploreFlow;
import com.multibank.framework.models.TestCaseData;
import com.multibank.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExplorePageTradingPairTests extends BaseTest {
    private final ExploreFlow exploreFlow = new ExploreFlow();

    @Test(dataProvider = "excelData")
    public void verifyTradingPairDisplaysExpectedFields(TestCaseData data) {
        exploreFlow.open();
        Assert.assertTrue(exploreFlow.tradingPairIsVisible(data.data("symbol"), data.data("name")), data.toString());
        Assert.assertTrue(exploreFlow.tradingPairHasPriceAndChange(data.data("symbol")), data.toString());
    }

    @Test(dataProvider = "excelData")
    public void verifyTradingPairCategoriesAreAvailable(TestCaseData data) {
        exploreFlow.open();
        for (String category : data.data("categories").split("\\|")) {
            Assert.assertTrue(exploreFlow.categoryExists(category.trim()), "Missing category: " + category);
        }
    }
}
