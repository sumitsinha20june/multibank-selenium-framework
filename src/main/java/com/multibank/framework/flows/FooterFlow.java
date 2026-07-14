package com.multibank.framework.flows;

import com.multibank.framework.config.ConfigReader;
import com.multibank.framework.pages.factory.PageObjectFactory;
import com.multibank.framework.pages.interfaces.IFooterComponent;
import com.multibank.framework.utilities.LinkChecker;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FooterFlow {
    private static final List<String> EXTERNAL_SKIP_DOMAINS = List.of(
            "hacken.io"
    );

    private final IFooterComponent footer;
    private final ConfigReader config = ConfigReader.getInstance();

    public FooterFlow() {
        this.footer = PageObjectFactory.getFooterComponent();
    }

    public Map<String, Integer> checkAllFooterLinkStatuses() {
        Map<String, String> footerLinks = footer.getAllFooterLinks();
        Map<String, Integer> results = new LinkedHashMap<>();
        String baseUrl = config.get("base.url");
        for (Map.Entry<String, String> entry : footerLinks.entrySet()) {
            String href = entry.getValue();
            String fullUrl = href.startsWith("http") ? href : baseUrl + href;
            if (isExternalBlocked(fullUrl)) {
                results.put(entry.getKey(), -1);
            } else {
                results.put(entry.getKey(), LinkChecker.statusCode(fullUrl));
            }
        }
        return results;
    }

    public Map<String, String> getAllFooterLinks() {
        return footer.getAllFooterLinks();
    }

    private boolean isExternalBlocked(String url) {
        return EXTERNAL_SKIP_DOMAINS.stream().anyMatch(url::contains);
    }
}
