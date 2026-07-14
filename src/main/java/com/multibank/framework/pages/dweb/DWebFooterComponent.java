package com.multibank.framework.pages.dweb;

import com.multibank.framework.actions.JavaScriptActions;
import com.multibank.framework.driver.DriverManager;
import com.multibank.framework.pages.base.BasePage;
import com.multibank.framework.pages.interfaces.IFooterComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DWebFooterComponent extends BasePage implements IFooterComponent {

    private static final List<String> SKIP_PREFIXES = List.of("#", "javascript:", "mailto:", "tel:");

    public Map<String, String> getAllFooterLinks() {
        JavaScriptActions.scrollToBottom();
        WebElement footer = DriverManager.getDriver().findElement(By.tagName("footer"));
        List<WebElement> anchors = footer.findElements(By.tagName("a"));
        Map<String, String> links = new LinkedHashMap<>();
        for (WebElement a : anchors) {
            String href = a.getAttribute("href");
            if (href == null || href.isBlank()) {
                continue;
            }
            String trimmed = href.trim();
            if (SKIP_PREFIXES.stream().anyMatch(trimmed::startsWith)) {
                continue;
            }
            String text = a.getText().trim();
            String label = text.isBlank() ? trimmed : text;
            links.put(label, trimmed);
        }
        return links;
    }
}
