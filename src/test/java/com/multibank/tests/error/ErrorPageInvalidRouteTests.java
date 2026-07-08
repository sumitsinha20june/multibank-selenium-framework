package com.multibank.tests.error;

import com.multibank.framework.flows.ErrorFlow;
import com.multibank.framework.models.TestCaseData;
import com.multibank.framework.utilities.LinkChecker;
import com.multibank.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ErrorPageInvalidRouteTests extends BaseTest {
    private final ErrorFlow errorFlow = new ErrorFlow();

    @Test(dataProvider = "excelData")
    public void verifyInvalidRouteDisplaysPageNotFound(TestCaseData data) {
        String invalidUrl = config.get("base.url") + data.data("invalidPath");
        int statusCode = LinkChecker.statusCode(invalidUrl);
        Assert.assertEquals(statusCode, 404, "Server did not return 404 for: " + invalidUrl);
        errorFlow.openInvalidPath(data.data("invalidPath"));
        Assert.assertTrue(errorFlow.hasErrorContent(data.data("expectedHeading"), data.data("expectedText")), data.toString());
    }
}
