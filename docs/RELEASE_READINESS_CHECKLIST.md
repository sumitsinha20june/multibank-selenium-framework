# Release Readiness Checklist

This checklist acts as the final Quality Gate before a release is promoted to Production.

### Phase 1: Automated Validation
- [ ] **Smoke Suite (P0):** 100% pass rate in the Staging environment.
- [ ] **Regression Suite (P1/P2):** >98% pass rate across all automated suites (DWeb and MWeb).
- [ ] **Cross-Browser Check:** Automated execution confirmed passing on Chrome and Firefox.
- [ ] **Test Report Generated:** Extent Report is generated, reviewed for false positives, and attached to the release ticket.

### Phase 2: Manual & UAT Validation
- [ ] **UAT Sign-Off:** Business stakeholders/Product Owners have signed off on the new feature workflows in the UAT environment.
- [ ] **Exploratory Testing:** A 2-hour exploratory session completed on the release branch to catch edge cases not covered by automation.
- [ ] **Defect Triage:** ZERO open Blocker or Critical defects. Any open Minor defects have documented risk acceptance from the Product Manager.

### Phase 3: Infrastructure & Security
- [ ] **Performance Check:** Page load times for the Spot Market dashboard are within acceptable SLAs under standard load.
- [ ] **Monitoring Active:** Production crash analytics (e.g., Sentry/Datadog) and API health checks are active and listening.
- [ ] **Rollback Plan:** DevOps has confirmed the deployment rollback procedure is documented and ready.
