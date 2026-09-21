# cucumber-java

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![Cucumber](https://img.shields.io/badge/BDD-Cucumber-23D96C?logo=cucumber&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4.x-43B02A?logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/Tests-TestNG-orange)
![CI](https://github.com/Mehedi-K/cucumber-java/actions/workflows/ci.yml/badge.svg)

A Cucumber BDD + Java + Selenium WebDriver test automation framework built
with the **Page Object Model**, driven by **Gherkin** feature files and run
through **TestNG** (`cucumber-testng`). It exercises
[saucedemo.com](https://www.saucedemo.com/), the well-known Selenium/QA
practice site, covering login, product sorting, the shopping cart, and the
full checkout flow.

This is a portfolio project meant to demonstrate a clean, maintainable BDD
automation setup — feature files that read like real acceptance criteria,
step definitions that stay thin and delegate to a proper POM layer, explicit
waits everywhere (no `Thread.sleep`), data-driven scenarios, failure
screenshots attached directly to the Cucumber report, and a working GitHub
Actions CI pipeline.

## Tech stack

- **Java 17**
- **Maven** for build/dependency management
- **Cucumber-JVM** (`cucumber-java`) for Gherkin parsing and step definitions
- **cucumber-testng** as the runner, via `AbstractTestNGCucumberTests` — each
  scenario surfaces as its own TestNG `@Test`, so one failing scenario never
  hides the pass/fail result of the others
- **cucumber-picocontainer** for constructor-based dependency injection, so
  step definition classes and hooks share one `TestContext` (WebDriver +
  current page object) per scenario without any static state
- **Selenium WebDriver 4.x** — driver binaries are resolved automatically by
  Selenium Manager (bundled since Selenium 4.6), no WebDriverManager needed
- **SLF4J + Logback** for lightweight logging
- **Chrome** as the primary browser, with a headless toggle for CI

## Prerequisites

- Java 17 (JDK)
- Maven 3.9+
- Google Chrome installed locally (Selenium Manager will fetch a matching
  chromedriver automatically)

## Running the tests

Run the full suite (visible browser window):

```bash
mvn test
```

Run headless (used in CI):

```bash
mvn test -Dheadless=true
```

You can also set the `HEADLESS=true` environment variable instead of the
system property. TestNG results land in `target/surefire-reports/`, the
Cucumber HTML/JSON/JUnit-XML reports land in `target/cucumber-reports/`, and
any screenshot taken on a scenario failure lands in `screenshots/`
(gitignored) as well as being attached directly to the Cucumber HTML report.

Locally, this suite runs as **21 scenarios / 60 steps, all passing**.

## How the BDD is organized

Gherkin `.feature` files under `src/test/resources/features/` describe
behavior from the user's point of view — `Given`/`When`/`Then`/`And` steps
that read like acceptance criteria, not implementation details:

```gherkin
Feature: Shopping Cart
  As a logged in shopper
  I want to add and remove products from my cart
  So that I only check out with the items I actually want

  Background:
    Given I am logged in as a standard user

  Scenario: Add multiple products to the cart
    When I add the "Sauce Labs Backpack" to the cart
    And I add the "Sauce Labs Bike Light" to the cart
    Then the cart badge should show "2"
    And the cart should contain "Sauce Labs Backpack"
    And the cart should contain "Sauce Labs Bike Light"
```

Each step maps to a method in `src/test/java/.../stepdefinitions/`, which in
turn calls into a page object under `src/main/java/.../pages/` — the step
definitions themselves never touch a `By` locator or call Selenium directly.
A `TestContext` (injected by `cucumber-picocontainer`) carries the WebDriver
and whichever page object the scenario currently sits on from one step
definition class to the next, so `LoginSteps`, `CartSteps` and
`CheckoutSteps` can hand off state without any global/static variables.

`login.feature` also uses a `Scenario Outline` + `Examples` table to sweep
several username/password combinations (valid, locked out, wrong password,
unknown user) through the same login logic, asserting a different expected
outcome per row.

## Project structure

```
cucumber-java/
  pom.xml                                             Maven build config (Cucumber, Selenium, TestNG, Logback)
  src/main/java/com/automationframework/cucumber/
    pages/                                             Page Object Model classes
      BasePage.java                                    Shared explicit-wait helpers
      LoginPage.java                                   Login form + error handling
      ProductsPage.java                                Inventory grid, sorting, add/remove to cart, cart badge
      CartPage.java                                     Cart contents, remove, checkout entry point
      CheckoutStepOnePage.java                         Shipping info form (name / zip)
      CheckoutStepTwoPage.java                          Order overview + totals
      CheckoutCompletePage.java                         Order confirmation
    utils/
      DriverFactory.java                               Creates/quits ChromeDriver, headless toggle, thread-safe
      ConfigReader.java                                 Reads config.properties (base URL, waits, etc.)
  src/test/java/com/automationframework/cucumber/
    stepdefinitions/
      LoginSteps.java                                  Login flow + shared "logged in as a standard user" step
      SortingSteps.java                                 Name/price ascending & descending sort verification
      CartSteps.java                                     Add/remove items, badge count, cart contents
      CheckoutSteps.java                                 Checkout flow, order totals
      CommonSteps.java                                   Shared error-message assertion (login + checkout)
    hooks/
      Hooks.java                                        @Before/@After — driver lifecycle, screenshot on failure
    context/
      TestContext.java                                  Per-scenario WebDriver + page-object state (DI via picocontainer)
    runners/
      TestRunner.java                                   @CucumberOptions: feature path, glue, report plugins
  src/test/resources/
    features/
      login.feature                                     Valid/invalid/locked-out login + data-driven sweep
      sorting.feature                                    Name/price sort scenarios
      cart.feature                                       Add/remove products, cart badge, cart contents
      checkout.feature                                   End-to-end checkout, validation errors, order totals
    testng.xml                                          Suite definition referencing TestRunner
    config.properties                                   base.url, browser, wait timeouts
  .github/workflows/ci.yml                              GitHub Actions: headless run on every push/PR to main
```

## Design notes

- **Explicit waits only.** Every page object interaction goes through
  `WebDriverWait` + `ExpectedConditions` in `BasePage`; there is no
  `Thread.sleep` anywhere in the framework.
- **Thin step definitions.** Step definition methods read almost like the
  Gherkin text itself and delegate all locator/interaction logic to the page
  objects — the same POM layer a plain Selenium+TestNG suite would use.
- **Shared scenario state via DI, not statics.** `cucumber-picocontainer`
  constructs one `TestContext` per scenario and injects it into every step
  definition/hook class, so `CartSteps` can hand off to `CheckoutSteps`
  cleanly.
- **Data-driven scenarios.** `login.feature`'s `Scenario Outline` sweeps
  multiple username/password combinations through the same steps, and
  `checkout.feature`'s totals scenario sweeps multiple customer details.
- **Failure diagnostics.** `Hooks#tearDown` captures a PNG screenshot on any
  failed scenario, attaches it directly to the Cucumber HTML/JSON report, and
  also saves it to `screenshots/`, which CI uploads as a build artifact
  alongside the Cucumber and Surefire reports.
- **CI.** `.github/workflows/ci.yml` runs on every push/PR to `main`: sets up
  JDK 17, caches Maven dependencies, runs `mvn -B test -Dheadless=true`, and
  uploads the Cucumber reports, Surefire reports, and any failure screenshots
  as artifacts.

## Target site

Tests run against [https://www.saucedemo.com/](https://www.saucedemo.com/),
a public site maintained by Sauce Labs specifically for practicing Selenium
and other UI automation tools. Known test logins (password is always
`secret_sauce`):

| Username                  | Behavior                              |
|----------------------------|----------------------------------------|
| `standard_user`            | Logs in normally                       |
| `locked_out_user`          | Login blocked with an error message    |
| `problem_user`             | Logs in but has broken UI behavior     |
| `performance_glitch_user`  | Logs in, but slowly                    |
