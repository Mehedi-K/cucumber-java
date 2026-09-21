Feature: Shopping Cart
  As a logged in shopper
  I want to add and remove products from my cart
  So that I only check out with the items I actually want

  Background:
    Given I am logged in as a standard user

  Scenario: Add a single product to the cart
    When I add the "Sauce Labs Backpack" to the cart
    Then the cart badge should show "1"

  Scenario: Add multiple products to the cart
    When I add the "Sauce Labs Backpack" to the cart
    And I add the "Sauce Labs Bike Light" to the cart
    Then the cart badge should show "2"
    And the cart should contain "Sauce Labs Backpack"
    And the cart should contain "Sauce Labs Bike Light"

  Scenario: Remove a product from the cart on the products page
    Given I have added the "Sauce Labs Backpack" to the cart
    When I remove the "Sauce Labs Backpack" from the cart
    Then the cart badge should not be visible

  Scenario: Remove a product from the cart page
    Given I have added the "Sauce Labs Backpack" to the cart
    And I have added the "Sauce Labs Bike Light" to the cart
    When I go to the cart
    And I remove "Sauce Labs Backpack" from the cart page
    Then the cart should contain "Sauce Labs Bike Light"
    But the cart should not contain "Sauce Labs Backpack"
