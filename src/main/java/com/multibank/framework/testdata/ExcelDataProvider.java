package com.multibank.framework.testdata;

import com.multibank.framework.config.ConfigReader;
import com.multibank.framework.constants.FrameworkConstants;
import com.multibank.framework.enums.PlatformType;
import com.multibank.framework.models.TestCaseData;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.List;

public final class ExcelDataProvider {
    private ExcelDataProvider() {
    }

    @DataProvider(name = "excelData", parallel = false)
    public static Object[][] excelData(Method method, ITestContext context) {
        String platform = context.getCurrentXmlTest().getParameter("platform");
        if (platform == null || platform.isBlank()) {
            platform = ConfigReader.getInstance().get("platform");
        }
        String sheet = PlatformType.from(platform) == PlatformType.MWEB
                ? FrameworkConstants.MWEB_SHEET
                : FrameworkConstants.DWEB_SHEET;
        String filePath = ConfigReader.getInstance().getPath("testdata.path").toString();
        String className = method.getDeclaringClass().getSimpleName();
        List<TestCaseData> cases = ExcelReader.read(filePath, sheet).stream()
                .filter(row -> "Y".equalsIgnoreCase(row.getRunMode()))
                .filter(row -> className.equals(row.getTestClass()))
                .filter(row -> method.getName().equals(row.getTestMethod()))
                .toList();
        Object[][] data = new Object[cases.size()][1];
        for (int i = 0; i < cases.size(); i++) {
            data[i][0] = cases.get(i);
        }
        return data;
    }
}
