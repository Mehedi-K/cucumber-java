package com.automationframework.cucumber.stepdefinitions;

import com.automationframework.cucumber.context.TestContext;
import com.automationframework.cucumber.pages.LoginPage;
import com.automationframework.cucumber.pages.ProductsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {

    private final TestContext context;

    public LoginSteps(TestContext context) {
        this.context = context;
    }

    @Given("I am on the saucedemo login page")
    public void i_am_on_the_saucedemo_login_page() {
        context.setLoginPage(new LoginPage(context.getDriver()).open());
    }

    /** Reused as a Background step by sorting.feature, cart.feature and checkout.feature,
     *  which all need a logged-in shopper but don't care about the login flow itself. */
    @Given("I am logged in as a standard user")
    public void i_am_logged_in_as_a_standard_user() {
        LoginPage loginPage = new LoginPage(context.getDriver()).open();
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");
        Assert.assertTrue(productsPage.isLoaded(), "Expected to land on the Products page after login");
        context.setProductsPage(productsPage);
    }

    @When("I log in as {string} with password {string}")
    public void i_log_in_as_with_password(String username, String password) {
        context.getLoginPage().enterUsername(username).enterPassword(password).clickLoginButton();
    }

    @When("I attempt to log in without entering a username or password")
    public void i_attempt_to_log_in_without_credentials() {
        context.getLoginPage().clickLoginButton();
    }

    @Then("I should be redirected to the products page")
    public void i_should_be_redirected_to_the_products_page() {
        ProductsPage productsPage = new ProductsPage(context.getDriver());
        Assert.assertTrue(productsPage.isLoaded(), "Expected to land on the Products page");
        Assert.assertTrue(productsPage.getCurrentUrl().contains("inventory.html"),
                "Expected URL to contain inventory.html");
        context.setProductsPage(productsPage);
    }

    @Then("the login outcome should be {string}")
    public void the_login_outcome_should_be(String outcome) {
        switch (outcome) {
            case "success" -> {
                ProductsPage productsPage = new ProductsPage(context.getDriver());
                Assert.assertTrue(productsPage.isLoaded(), "Expected login to succeed");
                context.setProductsPage(productsPage);
            }
            case "locked_out" -> {
                Assert.assertTrue(context.getLoginPage().isErrorDisplayed(), "Expected a locked-out error");
                Assert.assertTrue(context.getLoginPage().getErrorMessage().contains("locked out"),
                        "Expected error message to mention the account is locked out");
            }
            case "invalid" -> {
                Assert.assertTrue(context.getLoginPage().isErrorDisplayed(), "Expected an invalid-credentials error");
                Assert.assertTrue(context.getLoginPage().getErrorMessage().contains("do not match"),
                        "Expected error message to mention the credentials do not match");
            }
            default -> throw new IllegalArgumentException("Unknown login outcome: " + outcome);
        }
    }
}
