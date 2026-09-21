package com.automationframework.cucumber.context;

import com.automationframework.cucumber.pages.CartPage;
import com.automationframework.cucumber.pages.CheckoutCompletePage;
import com.automationframework.cucumber.pages.CheckoutStepOnePage;
import com.automationframework.cucumber.pages.CheckoutStepTwoPage;
import com.automationframework.cucumber.pages.LoginPage;
import com.automationframework.cucumber.pages.ProductsPage;
import com.automationframework.cucumber.utils.DriverFactory;
import org.openqa.selenium.WebDriver;

/**
 * Per-scenario state shared across step definition / hook classes.
 *
 * Cucumber-Picocontainer creates exactly one TestContext per scenario and injects
 * it into every glue class whose constructor asks for it, so step definitions in
 * different classes (LoginSteps, CartSteps, CheckoutSteps, ...) can hand the
 * "current page" off to one another without any static or global state.
 */
public class TestContext {

    private final WebDriver driver;

    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutStepOnePage checkoutStepOnePage;
    private CheckoutStepTwoPage checkoutStepTwoPage;
    private CheckoutCompletePage checkoutCompletePage;

    public TestContext() {
        this.driver = DriverFactory.getDriver();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public ProductsPage getProductsPage() {
        return productsPage;
    }

    public void setProductsPage(ProductsPage productsPage) {
        this.productsPage = productsPage;
    }

    public CartPage getCartPage() {
        return cartPage;
    }

    public void setCartPage(CartPage cartPage) {
        this.cartPage = cartPage;
    }

    public CheckoutStepOnePage getCheckoutStepOnePage() {
        return checkoutStepOnePage;
    }

    public void setCheckoutStepOnePage(CheckoutStepOnePage checkoutStepOnePage) {
        this.checkoutStepOnePage = checkoutStepOnePage;
    }

    public CheckoutStepTwoPage getCheckoutStepTwoPage() {
        return checkoutStepTwoPage;
    }

    public void setCheckoutStepTwoPage(CheckoutStepTwoPage checkoutStepTwoPage) {
        this.checkoutStepTwoPage = checkoutStepTwoPage;
    }

    public CheckoutCompletePage getCheckoutCompletePage() {
        return checkoutCompletePage;
    }

    public void setCheckoutCompletePage(CheckoutCompletePage checkoutCompletePage) {
        this.checkoutCompletePage = checkoutCompletePage;
    }
}
