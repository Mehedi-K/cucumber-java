Feature: Checkout
  As a logged in shopper with items in my cart
  I want to complete the checkout flow
  So that I can place my order

  Background:
    Given I am logged in as a standard user
    And I have added the "Sauce Labs Backpack" to the cart
    And I go to the cart

  Scenario: Complete checkout with valid information
    When I proceed to checkout
    And I fill in the checkout information with first name "John", last name "Doe", postal code "12345"
    And I complete the checkout
    Then I should see the order confirmation "Thank you for your order!"

  Scenario: Checkout is blocked when the first name is missing
    When I proceed to checkout
    And I fill in the checkout information with first name "", last name "Doe", postal code "12345"
    And I attempt to continue checkout
    Then I should see the error message "Error: First Name is required"

  Scenario: Checkout is blocked when the postal code is missing
    When I proceed to checkout
    And I fill in the checkout information with first name "John", last name "Doe", postal code ""
    And I attempt to continue checkout
    Then I should see the error message "Error: Postal Code is required"

  Scenario Outline: Order total equals the item subtotal plus tax
    When I proceed to checkout
    And I fill in the checkout information with first name "<first name>", last name "<last name>", postal code "<postal code>"
    And I continue to the order overview
    Then the order total should equal the subtotal plus tax

    Examples:
      | first name | last name | postal code |
      | Jane        | Smith     | 94016       |
      | John        | Doe       | 12345       |
