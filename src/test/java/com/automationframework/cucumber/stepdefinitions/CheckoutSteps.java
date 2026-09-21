package com.automationframework.cucumber.stepdefinitions;

import com.automationframework.cucumber.context.TestContext;
import com.automationframework.cucumber.pages.CheckoutStepTwoPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CheckoutSteps {

    private final TestContext context;

    public CheckoutSteps(TestContext context) {
        this.context = context;
    }

    @When("I proceed to checkout")
    public void i_proceed_to_checkout() {
        context.setCheckoutStepOnePage(context.getCartPage().clickCheckout());
    }

    @When("I fill in the checkout information with first name {string}, last name {string}, postal code {string}")
    public void i_fill_in_the_checkout_information(String firstName, String lastName, String postalCode) {
        context.getCheckoutStepOnePage()
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterPostalCode(postalCode);
    }

    @When("I complete the checkout")
    public void i_complete_the_checkout() {
        CheckoutStepTwoPage stepTwoPage = context.getCheckoutStepOnePage().clickContinue();
        context.setCheckoutStepTwoPage(stepTwoPage);
        context.setCheckoutCompletePage(stepTwoPage.clickFinish());
    }

    @When("I continue to the order overview")
    public void i_continue_to_the_order_overview() {
        context.setCheckoutStepTwoPage(context.getCheckoutStepOnePage().clickContinue());
    }

    @When("I attempt to continue checkout")
    public void i_attempt_to_continue_checkout() {
        context.getCheckoutStepOnePage().clickContinueExpectingFailure();
    }

    @Then("I should see the order confirmation {string}")
    public void i_should_see_the_order_confirmation(String expectedHeader) {
        Assert.assertEquals(context.getCheckoutCompletePage().getCompleteHeader(), expectedHeader);
        Assert.assertTrue(context.getCheckoutCompletePage().isOrderComplete());
    }

    @Then("the order total should equal the subtotal plus tax")
    public void the_order_total_should_equal_the_subtotal_plus_tax() {
        CheckoutStepTwoPage stepTwoPage = context.getCheckoutStepTwoPage();
        double expectedTotal = Math.round((stepTwoPage.getSubtotal() + stepTwoPage.getTax()) * 100.0) / 100.0;
        Assert.assertEquals(stepTwoPage.getTotal(), expectedTotal, 0.01);
    }
}
