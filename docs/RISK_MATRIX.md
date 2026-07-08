# Risk Matrix

*This document identifies potential risks to the project, categorizes them by Likelihood and Business Impact, and provides actionable Mitigation Strategies.*

| Risk Scenario | Likelihood | Impact | Mitigation Strategy |
| :--- | :---: | :---: | :--- |
| **Market Data Volatility Breaking Tests** <br> *(Tests fail because Bitcoin price changes mid-test)* | High | High | **Mitigation:** We do not assert hardcoded numbers. We assert regex patterns (e.g., `^\$?\d+(,\d{3})*(\.\d{2})?$`) to ensure the *format* is correct regardless of market fluctuations. |
| **Flaky Locators Due to UI Reskins** <br> *(Marketing team changes button text or CSS classes)* | Medium | High | **Mitigation:** Strict adherence to the Page Object Model. We isolate locators to single classes. Furthermore, the `healing` package is stubbed out to eventually support AI-fallback locators. |
| **False-Positive Broken Links** <br> *(Security firewalls block our automated scraper with 403 errors)* | Medium | Low | **Mitigation:** The `LinkChecker` uses HTTP HEAD requests to save bandwidth, but intelligently falls back to standard GET requests if the server blocks bot traffic. |
| **Network Latency Causing Timeouts** <br> *(Staging environment runs slower than local dev machines)* | High | Medium | **Mitigation:** Raw Selenium calls are forbidden. All clicks and text retrievals go through `WebActions` which enforce fluent `WebDriverWait` loops to dynamically wait for elements to render. |
| **UAT Discovers Misaligned Requirements** <br> *(Business wanted X, developers built Y)* | Low | Critical | **Mitigation:** Enforcing Shift-Left QA. QA participates in story grooming and integrates UAT stakeholders early in the sprint to review the Excel Test Data sheet before automation even begins. |
