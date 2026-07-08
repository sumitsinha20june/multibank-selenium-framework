package com.multibank.tests.common;

import com.multibank.framework.actions.BrowserActions;
import com.multibank.framework.flows.HeaderFlow;
import com.multibank.framework.models.TestCaseData;
import com.multibank.framework.utilities.SoftAssertManager;
import com.multibank.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.Test;

import java.util.List;

public class HeaderNavigationTests extends BaseTest {
    private final HeaderFlow headerFlow = new HeaderFlow();

    @Test(dataProvider = "excelData")
    public void verifyHeaderNavigationItemsAreVisible(TestCaseData data) {
        List<String> missingItems = headerFlow.missingNavigationItems(data.data("items"));
        SoftAssert softAssert = SoftAssertManager.create();
        softAssert.assertTrue(missingItems.isEmpty(), "Missing header items: " + missingItems + " for " + data);
        for (String item : data.data("items").split("\\|")) {
            softAssert.assertTrue(headerFlow.isNavigationItemVisible(item.trim()), "Header item not visible: " + item);
        }
        SoftAssertManager.assertAll();
    }

    @Test(dataProvider = "excelData")
    public void verifyHeaderNavigationLinkDestination(TestCaseData data) {
        String item = data.data("navItem");
        String expectedPath = data.data("expectedPath");
        String expectedUrlContains = data.data("expectedUrlContains");
        String expectedTarget = data.data("target");
        Assert.assertTrue(headerFlow.linkDestinationMatches(item, expectedPath != null ? expectedPath : expectedUrlContains));
        if (expectedPath != null) {
            headerFlow.navigateToHref(headerFlow.hrefFor(item), expectedPath);
        }
        Assert.assertEquals(headerFlow.targetFor(item), expectedTarget);
    }

    @Test(dataProvider = "excelData")
    public void verifyHeaderNavigationAtDesktopViewport(TestCaseData data) {
        headerFlow.setViewport(data.data("viewport"));
        BrowserActions.open(config.get("base.url"));
        List<String> missingItems = headerFlow.missingNavigationItems(data.data("expectedVisible"));
        Assert.assertTrue(missingItems.isEmpty(), "Missing desktop header items: " + missingItems + " for " + data);
    }
}
