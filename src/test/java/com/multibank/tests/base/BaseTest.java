package com.multibank.tests.base;

import com.multibank.framework.actions.BrowserActions;
import com.multibank.framework.config.ConfigReader;
import com.multibank.framework.driver.DriverFactory;
import com.multibank.framework.driver.DriverManager;
import com.multibank.framework.testdata.ExcelDataProvider;
import com.multibank.framework.utilities.LoggerUtil;
import com.multibank.framework.utilities.SoftAssertManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

// Base class for all multi-browser test classes.  Sets browser from the
// XML suite parameter before each method, then opens the base URL.
public abstract class BaseTest {
    private static final Logger log = LoggerUtil.getLogger(BaseTest.class);
    protected final ConfigReader config = ConfigReader.getInstance();

    // Data provider delegates to ExcelDataProvider which filters by RunMode=Y.
    @DataProvider(name = "excelData", parallel = false)
    public Object[][] excelData(Method method, ITestContext context) {
        return ExcelDataProvider.excelData(method, context);
    }

    // Reads browser/platform from the <test> block in dweb.xml, propagates
    // them to System properties so ConfigReader picks them up.  Falls back
    // to sensible defaults when the XML value is blank or missing.
    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestContext context) {
        String platform = context.getCurrentXmlTest().getParameter("platform");
        System.setProperty("platform", (platform != null && !platform.isBlank()) ? platform : "dweb");

        String browser = context.getCurrentXmlTest().getParameter("browser");
        System.setProperty("browser", (browser != null && !browser.isBlank()) ? browser : "chrome");

        String url = config.get("base.url");
        log.info("=== Test Setup ===");
        log.info("Platform: {}, URL: {}, Browser: {}, Headless: {}",
                config.getOptional("platform", "dweb"), url,
                config.getOptional("browser", "chrome"), config.getOptional("headless", "false"));
        DriverFactory.createDriver();
        BrowserActions.open(url);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("=== Test Teardown ===");
        SoftAssertManager.remove();
        DriverManager.quitDriver();
    }
}
