package com.multibank.framework.pages.dweb;

import com.multibank.framework.actions.WebActions;
import com.multibank.framework.pages.base.BasePage;
import com.multibank.framework.pages.interfaces.IHeaderComponent;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

public class DWebHeaderComponent extends BasePage implements IHeaderComponent {

    private final By menuButton = By.xpath("//button[contains(@aria-label,'menu') or contains(.,'Menu') or contains(@class,'menu')]");

    public boolean isNavigationItemVisible(String item) {
        return WebActions.isDisplayed(linkByText(item));
    }

    public boolean areNavigationItemsVisible(String items) {
        return missingNavigationItems(items).isEmpty();
    }

    public List<String> missingNavigationItems(String items) {
        return Arrays.stream(items.split("\\|"))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .filter(item -> !isNavigationItemVisible(item))
                .toList();
    }

    public String hrefFor(String item) {
        return WebActions.attribute(linkByText(item), "href");
    }

    public String targetFor(String item) {
        String target = WebActions.attribute(linkByText(item), "target");
        return target == null || target.isBlank() ? "_self" : target;
    }

    public boolean isMenuAvailable() {
        return WebActions.isDisplayed(menuButton);
    }

    public void openMenuIfPresent() {
        if (isMenuAvailable()) {
            WebActions.click(menuButton);
        }
    }
}
