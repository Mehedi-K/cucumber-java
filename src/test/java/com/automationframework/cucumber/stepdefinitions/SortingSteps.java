package com.automationframework.cucumber.stepdefinitions;

import com.automationframework.cucumber.context.TestContext;
import com.automationframework.cucumber.pages.ProductsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingSteps {

    private final TestContext context;

    public SortingSteps(TestContext context) {
        this.context = context;
    }

    @When("I sort products by {string}")
    public void i_sort_products_by(String sortOptionLabel) {
        context.getProductsPage().sortBy(mapSortOption(sortOptionLabel));
    }

    @Then("the products should be listed in ascending alphabetical order")
    public void the_products_should_be_listed_in_ascending_alphabetical_order() {
        List<String> displayedNames = context.getProductsPage().getDisplayedProductNames();
        List<String> expectedOrder = new ArrayList<>(displayedNames);
        Collections.sort(expectedOrder);
        Assert.assertEquals(displayedNames, expectedOrder);
    }

    @Then("the products should be listed in descending alphabetical order")
    public void the_products_should_be_listed_in_descending_alphabetical_order() {
        List<String> displayedNames = context.getProductsPage().getDisplayedProductNames();
        List<String> expectedOrder = new ArrayList<>(displayedNames);
        expectedOrder.sort(Collections.reverseOrder());
        Assert.assertEquals(displayedNames, expectedOrder);
    }

    @Then("the product prices should be listed in ascending order")
    public void the_product_prices_should_be_listed_in_ascending_order() {
        List<Double> displayedPrices = context.getProductsPage().getDisplayedProductPrices();
        List<Double> expectedOrder = new ArrayList<>(displayedPrices);
        Collections.sort(expectedOrder);
        Assert.assertEquals(displayedPrices, expectedOrder);
    }

    @Then("the product prices should be listed in descending order")
    public void the_product_prices_should_be_listed_in_descending_order() {
        List<Double> displayedPrices = context.getProductsPage().getDisplayedProductPrices();
        List<Double> expectedOrder = new ArrayList<>(displayedPrices);
        expectedOrder.sort(Collections.reverseOrder());
        Assert.assertEquals(displayedPrices, expectedOrder);
    }

    private ProductsPage.SortOption mapSortOption(String label) {
        return switch (label) {
            case "Name (A to Z)" -> ProductsPage.SortOption.NAME_A_TO_Z;
            case "Name (Z to A)" -> ProductsPage.SortOption.NAME_Z_TO_A;
            case "Price (low to high)" -> ProductsPage.SortOption.PRICE_LOW_TO_HIGH;
            case "Price (high to low)" -> ProductsPage.SortOption.PRICE_HIGH_TO_LOW;
            default -> throw new IllegalArgumentException("Unknown sort option: " + label);
        };
    }
}
