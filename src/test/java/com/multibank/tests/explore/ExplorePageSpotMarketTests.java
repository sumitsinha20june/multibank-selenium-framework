package com.multibank.tests.explore;

import com.multibank.framework.flows.ExploreFlow;
import com.multibank.framework.models.TestCaseData;
import com.multibank.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExplorePageSpotMarketTests extends BaseTest {
    private final ExploreFlow exploreFlow = new ExploreFlow();

    //currently not blocking API request on page
    @Test(dataProvider = "excelData")
    public void verifySpotMarketSectionRenders(TestCaseData data) {
        exploreFlow.open();
        Assert.assertTrue(exploreFlow.isSpotMarketVisible(), data.toString());
        Assert.assertTrue(exploreFlow.hasSpotMarketContent(data.data("expectedText")), data.toString());
    }
}
