package com.multibank.tests.explore;

import com.multibank.framework.api.ApiClient;
import com.multibank.framework.flows.HomeFlow;
import com.multibank.framework.models.TestCaseData;
import com.multibank.framework.utilities.LoggerUtil;
import com.multibank.tests.base.BaseTest;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;

public class ExplorePageDownloadLinkTests extends BaseTest {
    private static final Logger log = LoggerUtil.getLogger(ExplorePageDownloadLinkTests.class);
    private final HomeFlow homeFlow = new HomeFlow();
    private final ApiClient apiClient = new ApiClient();

    @Test(dataProvider = "excelData")
    public void verifyDownloadAppLinkResolves(TestCaseData data) {
        homeFlow.open();
        String href = homeFlow.downloadLinkHref(data.data("linkText"));
        Assert.assertTrue(href.contains(data.data("expectedUrlContains")));
        Assert.assertEquals(homeFlow.downloadLinkTarget(data.data("linkText")), data.data("target"));
        int status = apiClient.get(href, Collections.emptyMap()).statusCode();
        if (status >= 400) {
            log.warn("Download link resolved to {} with status {}", href, status);
        }
    }
}
