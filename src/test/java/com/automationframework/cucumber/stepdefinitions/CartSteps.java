package com.automationframework.cucumber.stepdefinitions;

import com.automationframework.cucumber.context.TestContext;
import com.automationframework.cucumber.pages.ProductsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CartSteps {

    private final TestContext context;

    public CartSteps(TestContext context) {
        this.context = context;
    }

    @When("I add the {string} to the cart")
    public void i_add_the_product_to_the_cart(String productName) {
        context.getProductsPage().addProductToCart(ProductsPage.Product.fromDisplayName(productName));
    }

    @Given("I have added the {string} to the cart")
    public void i_have_added_the_product_to_the_cart(String productName) {
        i_add_the_product_to_the_cart(productName);
    }

    @When("I remove the {string} from the cart")
    public void i_remove_the_product_from_the_cart(String productName) {
        context.getProductsPage().removeProductFromCart(ProductsPage.Product.fromDisplayName(productName));
    }

    @Then("the cart badge should show {string}")
    public void the_cart_badge_should_show(String expectedCount) {
        Assert.assertEquals(String.valueOf(context.getProductsPage().getCartBadgeCount()), expectedCount);
    }

    @Then("the cart badge should not be visible")
    public void the_cart_badge_should_not_be_visible() {
        Assert.assertFalse(context.getProductsPage().isCartBadgeVisible());
    }

    @When("I go to the cart")
    public void i_go_to_the_cart() {
        context.setCartPage(context.getProductsPage().goToCart());
    }

    @When("I remove {string} from the cart page")
    public void i_remove_product_from_the_cart_page(String productName) {
        context.getCartPage().removeProduct(ProductsPage.Product.fromDisplayName(productName));
    }

    @Then("the cart should contain {string}")
    public void the_cart_should_contain(String productName) {
        ensureOnCartPage();
        Assert.assertTrue(context.getCartPage().getCartItemNames().contains(productName));
    }

    @Then("the cart should not contain {string}")
    public void the_cart_should_not_contain(String productName) {
        ensureOnCartPage();
        Assert.assertFalse(context.getCartPage().getCartItemNames().contains(productName));
    }

    /** "the cart should contain" is sometimes asserted straight from the products page
     *  (badge/contents checked without ever visiting /cart.html), so lazily navigate there
     *  the first time it's needed instead of requiring every scenario to call "I go to the
     *  cart" first. */
    private void ensureOnCartPage() {
        if (context.getCartPage() == null) {
            context.setCartPage(context.getProductsPage().goToCart());
        }
    }
}
