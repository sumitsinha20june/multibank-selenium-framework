package com.multibank.tests.home;

import com.multibank.framework.flows.HomeFlow;
import com.multibank.framework.models.TestCaseData;
import com.multibank.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageBannerTests extends BaseTest {
    private final HomeFlow homeFlow = new HomeFlow();

    @Test(dataProvider = "excelData")
    public void verifyHomePageMarketingBannersRender(TestCaseData data) {
        homeFlow.open();
        Assert.assertTrue(homeFlow.isTextVisible(data.data("expectedText")), data.toString());
    }
}
