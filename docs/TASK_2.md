Task 2 – QA Strategy & Thinking

1. Where do you start?

Since this is a fintech trading application where real user funds are involved, my first priority would be understanding the product, identifying the highest-risk areas, and ensuring those are thoroughly validated before release. With only two weeks remaining and no existing QA process, trying to test everything isn't realistic, so I'd follow a risk-based approach.

On day one, I would work with the Product Manager, Developers, and Business Analysts to understand the application's architecture, business flows, and third-party integrations such as payment gateways, KYC providers, and trading/broker APIs. I'd also make sure I have access to all the environments, backend logs, databases, Jira, test management tools, and admin dashboards so I can investigate issues quickly without depending on others.

Rather than spending several days writing detailed test cases, I would prepare a comprehensive list of high-level test scenarios. These 1-line test cases would act as both a checklist and a progress tracker, allowing the team to see what has been tested, what has passed, and what is still pending.

At the same time, I'd create a simple **Risk Register** to prioritize testing. The register would capture each risk, its business impact, likelihood, mitigation plan, and owner. Since this is a trading platform, the highest priority would always be features that can directly affect customer money, such as deposits, withdrawals, order execution, portfolio calculations, and transaction history.

---

2. How would you approach testing this app?

With only ten working days available before release, I would divide testing into multiple phases while running activities in parallel wherever possible.
We will start testing on QA/Staging server , retest rasied bugs , then move to pre prod env after testing all fix and doing code freeze. below is the full plan.

Day 1 – Environment Setup, requirement gathering  & Smoke Testing (QA/Staging)

After the requiremnt gathering and basic one liner testcase done the nextt step is ensuring the build is stable enough for testing.

I would verify:

App installation
Login
Basic navigation
Dashboard loading
API connectivity
Market data loading
No major crashes

If any critical smoke scenario fails, I would reject the build immediately instead of continuing with detailed testing.

---
Day 2 & Day 3 – Core Functional Testing (QA/Staging)

These two days focus entirely on validating business-critical financial workflows.

This includes:

User Registration
KYC
Login & MFA
Deposits
Withdrawals
Buy Orders
Sell Orders
Order Cancellation
Portfolio Updates
Transaction History
Notifications

Alongside UI testing, I would validate API responses and verify database records wherever possible to ensure transaction integrity.

---

Day 4 – API, Data Integrity & Real-Time Market Data (QA/Staging)

Since a mobile application is heavily dependent on backend services, API validation is equally important.

I would verify:

API request/response validation
HTTP status codes
Error handling
Contract validation
Authorization
Data consistency
WebSocket connectivity
Live price updates
Recovery after connection loss

After every financial transaction, I would also validate that:

UI data
API response
Database records
Financial ledger

all remain consistent.

---

Day 5 – Negative Testing, Idempotency & Race Conditions (QA/Staging)

One of the biggest risks in fintech applications is duplicate financial transactions.

For example, if a user taps the **Buy** button multiple times because the network is slow, or retries after a timeout, the backend must not create duplicate orders.

I would specifically test:

Double tapping
Multiple retries
Network interruptions
App kill during transactions
Timeout recovery
Invalid inputs
Insufficient balance
Invalid OTP
Session expiry
Retry scenarios after timeout

A major focus would be idempotency testing, ensuring duplicate trades or duplicate fund movements are never created if a request is retried.

This ensures money is never deducted twice and duplicate trades are prevented.

---

Day 6 – Device Compatibility, Security & Performance (QA/Staging)

Validate the application across different environments.

Testing includes:

Multiple Android versions
Multiple iOS versions
Older supported OS versions
Different screen sizes
Low-memory devices
Network switching
SSL validation
Token expiry
MFA
API authorization

I'd also perform lightweight performance testing around critical APIs such as Login, Order Placement, Portfolio Loading, and Market Data during simulated peak usage.

---

Day 7 – Bug Verification & QA Regression (QA/Staging)

By this point, developers would have fixed the critical issues discovered during earlier testing.

This day is dedicated to:

Verifying bug fixes
Performing impact analysis
Executing regression on affected modules
Ensuring no new issues have been introduced

Only after all critical issues are resolved would I approve the build for promotion.
---

Day 8 – Deploy to Pre-Production & End-to-End Validation

Once QA sign-off is complete, the build is promoted to the Pre-Production environment, which closely mirrors Production.

The objective here is to identify issues that may not appear in QA, such as:

Environment-specific configurations
Database connectivity
Third-party integrations
Certificates
Network routing
Production-like infrastructure differences

I would execute complete end-to-end business flows again to ensure nothing changes after deployment.


---
Day 9 – Final Sanity, Regression & Release Readiness (Pre-Production)

This is the final validation before release.

Activities include:

Smoke Testing
Critical Regression (P0)
End-to-End Validation
API Health Checks
Monitoring Verification
Crash Analytics Validation
Release Readiness Review

At the end of the day, I'd prepare the QA Sign-off Report, clearly documenting:

Tested features
Open defects
Known risks
Risk acceptance (if applicable)
Release recommendation

Day 10 – Buffer Day

I always prefer keeping the final day free rather than planning testing until the last minute.

This buffer allows the team to handle:

Last-minute production blockers
High-priority bug fixes
Environment issues
Emergency hotfix validation
Final release verification

If no major issues arise, the day can be used for an additional sanity check before the application is released to the public.

---

3. What does QA look like inside a sprint?

For me, QA starts long before development is completed. The earlier QA gets involved, the cheaper and easier defects are to fix.

Story Grooming

QA participates in story grooming to:

Review requirements
Clarify acceptance criteria
Identify missing scenarios
Raise potential risks
Estimate testing effort

This reduces ambiguity before development even begins.

---

During Development

While developers are building the feature, QA prepares:

Test scenarios
Test data
API test cases
Automation candidates

Developers should also execute a small set of agreed P0 test cases before handing over the build to QA.

This prevents obviously broken builds from reaching the QA team.

---

Build Ready for QA

Once development is complete, QA performs:

Smoke testing
Functional testing
API validation
Negative testing
Device testing

If a P0 scenario fails during smoke testing, I would reject the build immediately and return it to development.

---

Defect Reporting

Every bug should contain:

Steps to reproduce
Expected result
Actual result
Logs
Screenshots or videos
Environment details
Severity
Priority

Clear defect reports reduce unnecessary back-and-forth between QA and developers.

---

Bug Verification

Once defects are fixed, QA verifies the fix and performs impact analysis to ensure related functionality hasn't broken.

---

Regression

Before every release, QA executes the regression suite, performs sanity testing on the release build, and shares a release readiness report with any remaining known risks.

---

Production Readiness

Before approving the release, I also like to ensure that production monitoring is ready.

This includes:

* Crash analytics
* API monitoring
* Application logs
* Payment monitoring
* Alerts
* Rollback plan

Even if an issue reaches production, the team should be able to detect and respond to it quickly.

---

4. What does your ideal regression suite look like?

My regression suite is built around business risk rather than feature count. The goal is to ensure that critical user journeys never break, while repetitive scenarios are automated for every release.

P0 – Critical Financial Flows (Automated)

These are the highest priority and must pass before every release.

Registration
Login
MFA
Deposit
Withdrawal
Buy Order
Sell Order
Order Cancellation
Wallet Balance
Portfolio Update
Transaction History

These flows directly affect customer money and business reputation.

---

P1 – Business Features

KYC
User Profile
Notifications
Search
Watchlist
Market Data
Price Alerts
Settings

---

P2 – UI & Compatibility

Navigation
Device compatibility
Orientation
Screen resolutions
Dark mode
Accessibility
Localization (if applicable)

---

Security Regression

Every release should also verify:

Session timeout
Token expiry
API authorization
Role-based access
Data masking
SSL enforcement

---
API Regression

Automated API regression should verify:

Request validation
Response validation
Error handling
Contract validation
Idempotency
Backend data consistency

Over time, the majority of repetitive P0 and API scenarios would be automated and integrated into the CI/CD pipeline, while exploratory and usability testing would remain manual.

---

5. What would keep you up at night about this app and releasing it to the public?

The biggest concern isn't simply finding bugs—it's the unknown issues that only appear when thousands of real users begin trading with real money.

Some of the risks I'd be most concerned about are:

Incorrect trade execution
Duplicate orders due to retries or network failures
Deposits or withdrawals completing incorrectly
Portfolio balances not matching backend records
Third-party payment or broker API failures
Delayed market data causing users to trade using stale prices
Performance degradation during market opening
Security vulnerabilities exposing financial information

To minimize these risks, I would focus heavily on validating transaction integrity, API behaviour, idempotency, and data reconciliation between the UI, backend services, database, and financial ledger.

However, no matter how much testing is done, production can still expose scenarios that were impossible to reproduce in a test environment. That's why I believe releasing safely is not just about testing—it's also about having strong monitoring, alerting, and rollback mechanisms in place.

If an issue does occur in production, the priority is to detect it quickly, resolve it immediately, perform a thorough root cause analysis, and then convert that incident into a permanent regression test so the same issue never escapes again.

For a fintech application, customer trust is everything. My objective is to reduce risk as much as possible before release, be transparent about any known risks, and continuously strengthen the QA process with every production learning.
