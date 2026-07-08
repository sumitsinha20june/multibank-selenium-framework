# Master Test Plan

## 1. Objective
To deliver a high-confidence quality gate for the MultiBank public-facing platform, ensuring that core trading data, navigation, and marketing content render accurately across all devices before any production release.

## 2. Scope
**In-Scope:**
*   **Navigation:** Top header routing, broken link detection (404/500), and mobile menu responsiveness.
*   **Trading Data:** Spot market tables, category filtering (Hot/Gainers), and currency format validations.
*   **Content:** Hero banners, static company pages, and App Store CTA links.
*   **Negative Scenarios:** Graceful handling of network timeouts and invalid URL routing.

**Out-of-Scope:**
*   User authentication (Registration/Login/MFA).
*   Live financial order execution (Buy/Sell/Withdrawals).
*   Backend database and API-level payload validation.

## 3. Execution Strategy & Environments
1.  **QA / Staging:** 
    *   *Goal:* Feature validation and bug hunting.
    *   *Action:* Manual exploratory testing, automated API checks (future scope), and execution of the Automated Smoke Suite (`smoke.xml`) on every code commit.
2.  **UAT (User Acceptance Testing):** 
    *   *Goal:* Business validation.
    *   *Action:* Product Managers and stakeholders run through high-level workflows. Automation executes the full Regression Suite (`regression.xml`) overnight.
3.  **Pre-Production:**
    *   *Goal:* Final infrastructure validation.
    *   *Action:* Execute the Edge Case Suite (timeouts, load balancing checks) to ensure production-parity environments handle stress smoothly.

## 4. Automation Approach
*   **Data-Driven:** All test inputs and expected outputs are decoupled into Excel.
*   **Fail-Fast Mechanism:** The framework uses Global Retry Analyzers for flaky tests, but immediately halts execution if core URL routing fails.
